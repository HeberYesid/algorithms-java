# Aplicación del Framework de Selección Estructural y Algorítmica

Este documento presenta la decisión de diseño para un entorno de contenedor restringido (512 MB de RAM) manejando colecciones de eventos, aplicando el framework de 5 pasos evaluando las heurísticas asintóticas frente a las observaciones empíricas.

## Paso 1: Caracterizar la entrada
* **Volumen de datos ($n$):** Típico de $50,000$, máximo aproximado de $200,000$.
* **Tipos de valores:** Objetos transaccionales o eventos discretos (referencias / *boxed*).
* **Operaciones requeridas:**
  * Inserción frecuente al final de la colección (`add-tail`).
  * Iteración completa y frecuente (1 vez por segundo) a través de todos los eventos del sistema.
  * Acceso mediante el índice de la lista de forma **ocasional** (para reportar, e.g., un priorizado *top-10*).

## Paso 2: Restricciones de entorno
* **Memoria:** Límite estricto de **512 MB en el contenedor**. Cada objeto y byte *allocado* en la memoria es crítico.
* **Componente de Latencia:** Iterar con una holgura inferior a 1 segundo, no estricta, pero tolerante siempre y cuando no agote los recursos brutos o atragante la recolección de basuras.
* **Recolección de Basura (GC):** Se debe eludir una alta presión por alojamiento de objetos latentes. La masiva destrucción/creación rápida de *wrappers* provocaría paradas de *Garbage Collector* impactando la latencia del contenedor.

## Paso 3: Candidatos asintóticos viables
Si nos guiamos puramente por la notación formal teórica de complejidad **Big-O**:

* **`ArrayList` (Arreglo Dinámico):**
  * `add(tail)`: $\mathcal{O}(1)$ *amortizado* (hace falta un redimensionado de bloque eventual que impone un $\mathcal{O}(n)$ temporal).
  * `get(i)`: $\mathcal{O}(1)$ puro, mediante artimética directa de *offset* e índices.
  * `forEach`: $\mathcal{O}(n)$.
* **`LinkedList` (Lista Doblemente Enlazada):**
  * `add(tail)`: $\mathcal{O}(1)$ (disponiendo de un apuntador a la cola).
  * `get(i)`: $\mathcal{O}(n)$, hay que cruzar desde los extremos secuencialmente las referencias para encontrarlo.
  * `forEach`: $\mathcal{O}(n)$.
* **`ArrayDeque`:**
  * Aunque ofrece $\mathcal{O}(1)$ en arreglos, típicamente no provee la semántica de la API `List` para el acceso nativo general indexado (`get(i)`) requerido para el factor top-10.

## Paso 4: Análisis Constantes y Caché (Hechos Empíricos)
La notación $\mathcal{O}()$ teórica no refleja el hardware subyacente. Al revisar los datos empíricos aportados en los ciclos de **`MatrixBenchmark`** y **`ListBenchmark`**, vemos diferencias de arquitectura dramáticas:

1. **La "Ceguera" asintótica frente a la Iteración Constante:** Tanto el `ArrayList` como el `LinkedList` iteran asintóticamente con orden $\mathcal{O}(n)$. Sin embargo, en el perfilado de `ListBenchmark`, los tiempos indicaron que `LinkedList` para $n=50,000$ tiene penalizaciones masivas al transitar elementos. Este efecto está directamente ligado a la **Localidad Espacial de la Caché** (`cache-friendly`) que corroboramos en el experimento en 2D (`MatrixBenchmark`). El array es contiguo (parecido a `rowMajor`), enlazando el procesador con pre-alojamiento al L1/L2, reduciendo el desgaste de ciclos. El `LinkedList`, por su naturaleza _cache-hostile_, genera severos *cache misses* dispersando nodos por todo el *Heap* .
2. **Elevada Presión de Asignación ($alloc.rate.norm$):** Los *benchmarks* perfilaron que utilizar `LinkedList` incurría en en un costo implícito de **$\sim 48 \text{ bytes}$ extras por elemento** meramente para inyectar su estructura en un envoltorio `Node` (datos base + punteros adelante/atrás).
   - En un escenario con $200,000$ inserciones/limpiezas los desperdicios solo por formato de nodos rebasarían los megabytes, creando altísima saturación a nivel GC y asfixiando los estrechos $512 \text{ MB}$ de la cuota del contenedor.

## Paso 5: Decisión Justificada

**Selección de Estructura Final:** **`ArrayList`**

**Justificación Estratégica:**
Se decreta la utilización de `ArrayList`. Este análisis permite asimilar por qué una abstracción de igual categoría asintótica (`LinkedList` con iteración $\mathcal{O}(n)$ y anexos de cola en $\mathcal{O}(1)$ constante) llega a comportarse **drásticamente peor en un entorno productivo**. 

* **Resolución del Trade-off:** En la academia, se aboga constantemente por "evitar el coste de redimensionalidad $\mathcal{O}(n)$ " recomendando listas referenciadas mutables para eventos impredecibles. No obstante, al analizar con datos reales de la JVM, el esfuerzo de crecer un arreglo continuo mediante la función nativa veloz de `System.arraycopy()` sucede espaciadamente (solamente 1 redimensionamiento por enésima fracción de registros). Con nuestro promedio de llegada para $n=50,000$, ese costo resulta ser matemáticamente ínfimo y **totalmente negligente**.
* **Gestión Garantizada:** El entorno es limitado (512 MB). Suplir $n = 50,000$ entradas costará una fracción exacta del número de eventos si no hay objetos `Node` de por medio. La velocidad para extraer el _top-10_ requerida será $\mathcal{O}(1)$ contundente, y la iteración transaccional transitará fluidamente sobre caché caliente acortando drásticamente el uso de CPU a favor de las tareas del contenedor. 
* **Conclusión Concreta:** Ceder el modelo de listas enlazadas mitiga el impacto de fragmentación, probando que un diseño formal correcto puede corromper un entorno de producción si se ignora el comportamiento real de iteración y *allocation profiling* latente en lenguajes como Java.