# Análisis de Benchmarks de Algoritmos de Ordenamiento

## Descripción del Experimento
Este proyecto evalúa el rendimiento práctico y empírico de diferentes algoritmos de ordenamiento (InsertionSort, MergeSort y HeapSort) utilizando el framework JMH (Java Microbenchmark Harness). El propósito del experimento es analizar cómo crece el tiempo de ejecución a medida que se duplica el tamaño de la entrada ($n$), calculando empíricamente la razón $T(2n)/T(n)$ para compararla con las predicciones asintóticas teóricas ($O(n^2)$ y $O(n \log n)$). Además, se comparan las constantes ocultas a nivel de hardware y recolección de basura entre MergeSort y HeapSort.

## Instrucciones de Build y Ejecución

Para compilar y empaquetar de forma correcta el proyecto JMH, es necesario usar Maven:

1. **Construir el proyecto:**
   ```bash
   mvn clean package
   ```
2. **Ejecutar el benchmark:**
   El proceso de build generará un ejecutable en el directorio `target/`. Para iniciar la prueba, ejecuta:
   ```bash
   java -jar target/benchmarks.jar
   ```
   *Nota:* Para guardar los resultados en un archivo, puedes agregar banderas de JMH como en este ejemplo:
   ```bash
   java -jar target/benchmarks.jar -rf csv -rff results/resultados.csv
   ```

## Paso 6: Análisis de la Razón de Crecimiento $T(2n)/T(n)$

La siguiente tabla refleja los resultados empíricos extraídos de las pruebas JMH.

| $n$ | $T_{insertion}$ ($\mu$s) | Razón IS | $T_{merge}$ ($\mu$s) | Razón MS | $T_{heap}$ ($\mu$s) | Razón HS | Predicción $O(n^2)$ | Predicción $O(n \log n)$ |
|--------|----------------------|----------|----------------|----------|----------------|----------|---------------------|--------------------------|
| **500** | 18.96 | — | 15.40 | — | 8.65 | — | — | — |
| **1000** | 71.89 | **3.79** | 41.45 | **2.69** | 28.36 | **3.28** | 4.0 | $\approx 2.1$ |
| **2000** | 263.42 | **3.66** | 136.40 | **3.29** | 105.23 | **3.71** | 4.0 | $\approx 2.1$ |
| **4000** | 1113.44 | **4.23** | 312.90 | **2.29** | 246.76 | **2.34** | 4.0 | $\approx 2.05$ |
| **8000** | 4209.62 | **3.78** | 650.97 | **2.08** | 519.21 | **2.10** | 4.0 | $\approx 2.03$ |
| **16000**| 16497.58 | **3.92** | 1411.22 | **2.17** | 1130.75 | **2.18** | 4.0 | $\approx 2.02$ |

### Análisis por Algoritmo

#### 1. InsertionSort (Ordenamiento por Inserción)
La razón de crecimiento de InsertionSort converge rápidamente a valores muy próximos a la predicción teórica de $4.0$, oscilando típicamente entre $3.66$ y $4.23$, lo cual confirma su naturaleza asintótica polinómica $O(n^2)$. Las pequeñas desviaciones respecto al exacto $4.0$, particularmente en entradas muy pequeñas ($n \le 1000$), se deben a la altísima localidad de caché que tiene este algoritmo, en donde los datos pueden encajar directamente en la caché L1/L2. Una vez que el array excede ciertas cuotas de espacio en registros inmediatos o debido a los costos asociados a saltos de bucle y ramificaciones más frecuentes, la relación se rebalancea promediando un crecimiento cuadrático puro al rebasar $n \ge 2000$.

#### 2. MergeSort (Ordenamiento por Mezcla)
MergeSort presenta una predicción base $O(n \log n)$, sin embargo, en la tabla se aprecian razones elevadas para brincos pequeños ($2.69$ y $3.29$ para $n=1000$ y $2000$ respectivamente) y posteriormente una amortiguación que estabiliza la razón cerca de su ideal en $2.08$ a $2.29$ en $n \ge 4000$. Este comportamiento puede explicarse debido al calentamiento asimétrico (warm-up JIT) y, crucialmente, al uso de espacio adicional $O(n)$. Al instanciar un arreglo auxiliar en cada etapa para tamaños reducidos entra en juego una significativa penalización relacionada con la reserva de memoria en la heap de la JVM y su paulatina limpieza por parte del recolector de basura (GC). Solo cuando $n$ crece lo suficiente, el peso asintótico sobrepasa las interrupciones del GC.

#### 3. HeapSort (Ordenamiento por Montículo)
Al igual que MergeSort, el HeapSort está acotado por un nivel de crecimiento $O(n \log n)$, pero con la ventaja de ser "in-place" (complejidad espacial $O(1)$). No obstante, para tamaños iniciales exhibe igual un salto anómalo cerca de $3.71$ al pasar a $n=2000$. Esto ocurre porque HeapSort padece de una localidad de caché muy deficiente; procesa los elementos realizando accesos sumamente dispersos en la memoria una vez que el tamaño del árbol supera los límites de la caché L1/L2, produciendo "cache misses" (fallos de caché) que castigan temporalmente su desempeño hasta llegar a la estabilización macroscópica. Se aprecia claramente que para $n \ge 4000$ sus razones comienzan a tender a la convergencia esperada entre $2.10$ y $2.34$.

## Paso 7: Análisis de constantes ocultas (MergeSort vs HeapSort)

Tanto **MergeSort** como **HeapSort** poseen una misma complejidad algorítmica de peor caso equivalente a $\Theta(n \log n)$. Sin embargo, la notación asintótica "Big-O / Theta" descarta sistemáticamente lo que llamamos _constantes multiplicativas ocultas_ y términos de Vorden inferior.

Teóricamente, se espera que para arreglos grandes (ej. $n \ge 8000$) y en arquitecturas de hardware modernas, MergeSort sea entre **1.5 a 3 veces más rápido** que HeapSort. Esto se deriva principalmente del efecto de **Localidad de Caché**:
*   **MergeSort** procesa y fusiona subarreglos accediendo a elementos de forma estrictamente secuencial y altamente predecible (alta localidad espacial), permitiendo al procesador introducir grandes bloques de arreglos a la caché L1/L2 y aprovechar las instrucciones de pre-fetching implícitas del procesador.
*   **HeapSort**, durante las fases de pre-condicionamiento de montículo y _sift-down_, está en contra del hardware saltando a posiciones dispersas calculadas mediante $2i+1$ y $2i+2$. Para tamaños grandes $n$, estos saltos provocan colisiones y caen recurrentemente fuera de la línea de caché transcurriendo en inevitables *"cache misses"* que obligan a acceder a la lenta memoria RAM principal.

**Comparación absoluta en $n = 16000$ (Nuestras mediciones vs Teoría):**
En nuestros datos registrados (ver `results/resultados.csv`):
*   $T_{heap}(16000) \approx 1130.75\ \mu s$
*   $T_{merge}(16000) \approx 1411.22\ \mu s$

Podemos observar y cuantificar que **en nuestra implementación subyacente HeapSort le ganó a MergeSort en tiempo absoluto por un factor de 1.25x (~20% más rápido)**. ¿A qué se debe esta aparente contradicción asintótica? 
El culpable es el pesado costo de gestión de memoria del lenguaje y JVM en la implementación utilizada para el MergeSort, ya que nuestra clase `MergeSort` actual instancia un arreglo primitivo nuevo (`new int[hi - lo + 1]`) en cada nodo del árbol recursivo _durante la fase de mezcla_. Para $n=16000$, la sobrecarga constante de reserva de espacio en heap local y la recolección de basura empañaron por completo su ventaja de caché de L1. HeapSort, al realizar swaps puramente _in-place_, operó sin esa pesada carga espacial. 

Si bien asintóticamente ambos algoritmos crecen a razón de $\approx 2.0$, la diferencia de velocidades se decanta en factores de _hardware (memoria)_ y de _infraestructura del lenguaje (GC)_ que Big-Theta jamás registrará en el papel.

> **Nota:** Se ha adjuntado el archivo de salida con estas métricas en [results/resultados.csv](results/resultados.csv).

## Resumen Final: Selección de algoritmo según $n$

Con base en los datos empíricos obtenidos de este experimento y en los comportamientos de hardware/software observados, se concluyen las siguientes recomendaciones para la selección del algoritmo según el tamaño de la entrada ($n$):

1. **Para arreglos pequeños ($n \le 1000$):**
   El algoritmo a elegir de nuestro conjunto es **InsertionSort**. Aunque su complejidad asintótica es $O(n^2)$, las constantes operativas son extremadamente reducidas y presenta una localidad de caché impecable. El procesador logra mantener los datos calientes en L1/L2, lo que lo vuelve imbatible en velocidad pura para conjuntos de elementos diminutos. No asigna memoria adicional y aprovecha las estructuras que ya están "casi" ordenadas.

2. **Para arreglos medianos y grandes ($n > 1000$):**
   A medida que las distancias y repeticiones escalan drásticamente, $O(n^2)$ se vuelve intolerable, por lo que es preferible utilizar algoritmos de estrategia divide-y-vencerás / estructuras jerárquicas:
   * **¿Memoria y GC no son un problema de peso?** En arquitecturas ideales ($O(n)$ de espacio tolerable, evitando un `new` constante de arreglos en el subárbol), **MergeSort** es muy rápido gracias a su acceso casi perfectamente lineal por memoria, explotando las capacidades del branch-prediction del hardware.
   * **¿Tengo baja memoria RAM o altas penalizaciones por recolección de basura?** Como pudimos verificar para el caso $n=16000$ (~20% superioridad en nuestra implementación), cuando la instanciación de memoria impacta, **HeapSort** sobresale dada su característica de procesado espacial $O(1)$. Opera totalmente "in-place", evadiendo de tajo al recolector de basura, salvaguardando latencias en aplicaciones críticas.

En la práctica real (como en el método `Arrays.sort()` de la API de Java), **se usan implementaciones híbridas**. Para tipos primitivos se prefieren derivados estabilizados como **Dual-Pivot QuickSort**, alternando con **InsertionSort** para los pequeños umbrales a nivel hoja; mientras que para objetos, el **TimSort** provee las ventajas de MergeSort pero de forma optimizada.

## Laboratorio: Costos Ocultos y Selección de Algoritmo

### Experimento 1: Localidad de Caché (MatrixBenchmark)
Se ejecutaron pruebas de rendimiento para suma de matrices cuadradas comparando recorridos 
owMajor (cache-friendly) y columnMajor (cache-hostile).

Para una matriz de tamaño 
 = 2048, se observó que el tiempo de ejecución del abordaje por columnas es considerablemente mayor debido a los fallos de caché (cache misses).

* Rendimiento **rowMajor (n=2048)**: ~1.313 ms/op
* Rendimiento **columnMajor (n=2048)**: ~16.114 ms/op

El factor de degradación observado al acceder a la matriz por columnas en lugar de por filas para 
=2048 es de **aproximadamente 12.2x**. Este resultado empírico está dentro del rango esperado (3x - 15x) y demuestra de manera fehaciente cómo las penalizaciones por falta de localidad espacial destruyen el rendimiento a pesar de que ambos fragmentos de código poseen una complejidad teórica $\Theta(n^2)$.

### Experimento 2: Impacto de Memoria y Acceso en Estructuras de Datos (ArrayList vs LinkedList)
Se evaluó el tiempo de acceso y uso de memoria entre ArrayList y LinkedList iterando y accediendo aleatoriamente bajo diferentes volúmenes de elementos ( = 1000, 10000, 100000$).

**Acceso Aleatorio:**
- **ArrayList** mantiene un tiempo de respuesta **$\mathcal{O}(1)$ constante** ($\sim 0.002$ us/op independientemente del volumen de datos), al aprovechar aritmética de punteros simple sobre arreglos continuos.
- **LinkedList** posee acceso aleatorio que escala de forma **$\mathcal{O}(n)$ lineal**, ya que en el peor de los casos (la mitad de la lista) requiere realizar saltos de referencias secuenciales, elevando el tiempo hasta $\sim 69.99$ us/op para =100000$.

**Iteración y Costos Ocultos (Memoria):**
Durante la iteración secuencial (=100000$), ArrayList tarda aproximadamente la mitad del tiempo que LinkedList dado que sus referencias son consecutivamente predecibles y la localidad de la caché principal es amigable. Adicionalmente, analizándolo en conjunto con el perfilador GC (-prof gc), resalta la altísima presión al \*garbage collector\* latente de las listas enlazadas.

* Cada entrada en ArrayList requiere en esencia su espacio de referencia al objeto dentro de un arreglo.
* A la inversa, LinkedList envuelve **cada** elemento insertado bajo un objeto Node interno, el cual contiene el dato y los dos apuntadores (prev y 
ext). En una arquitectura de 64-bit que utilice *Compressed Oops*, esto acarrea **$\sim 48$ bytes adicionales** de carga de nodo (objeto base + referencias + padding interno). Esto genera un valor global elevadísimo de gc.alloc.rate.norm (bytes por operación) que se traduce en dispersión en el _heap_ y masivos fallos de caché recurrentes cuando se recorre.
