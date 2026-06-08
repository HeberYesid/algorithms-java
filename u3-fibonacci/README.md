# daza-post1-u3

Proyecto Maven inicial para el post1-u3.

## Instrucciones de Build y Ejecución

Para compilar, ejecutar las pruebas y correr los benchmarks del proyecto, asegúrate de tener instalado Java 17 y Maven. Luego, puedes usar los siguientes comandos desde la raíz del proyecto:

1. **Compilar el proyecto:**
   ```bash
   mvn clean compile
   ```
2. **Ejecutar Pruebas Unitarias:**
   ```bash
   mvn test
   ```
3. **Empaquetar y ejecutar Benchmarks (JMH):**
   ```bash
   mvn clean package
   java -jar target/benchmarks.jar
   ```

## Notas sobre Fibonacci

Para las pruebas unitarias de las implementaciones de Fibonacci, la variante recursiva ingenua (`fibRecursivo`) solo se prueba con valores de `n ≤ 30`. No se prueba con valores de `n` grandes (ej. 40 o más) porque tiene una complejidad temporal exponencial de `O(2^n)`, por lo tanto los tiempos de ejecución crecen dramáticamente y las pruebas tomarían un tiempo prohibitivo.

## Resultados JMH - Fibonacci

A continuación, se presentan los resultados obtenidos al ejecutar los benchmarks de las tres implementaciones con la herramienta **JMH**:

| \(n\) | Recursivo (μs) | Memoización (μs) | Tabulación (μs) |
|-------|----------------|------------------|-----------------|
| 10    | 0.276          | 0.036            | 0.003           |
| 20    | 33.794         | 0.073            | 0.004           |
| 30    | 4155.243       | 0.109            | 0.006           |
| 35    | 45748.972      | 0.133            | 0.006           |

### Análisis de Complejidad

Los resultados arrojados por el benchmark confirman de manera empírica las complejidades temporales teóricas analizadas para cada variante del algoritmo. 

Por un lado, la aproximación recursiva ingenua muestra un claro comportamiento de tiempo exponencial de crecimiento desorbitado, correspondiente a una cota temporal de $O(2^n)$. Al aumentar el valor de `n` (de 10 hasta 35), el tiempo de ejecución se dispara de forma vertiginosa pasando de la fracción de microsegundo (0.276 μs) a sobrepasar los ~45 milisegundos (45748 μs). Este fenómeno ocurre debido a la explosión del árbol de llamados recursivos que recalculan repetidamente los mismos subproblemas superpuestos repetidas veces.

Por el contrario, las aproximaciones por programación dinámica logran optimizar el algoritmo para llegar a un tiempo lineal $O(n)$. Al revisar la variante top-down (memoización), vemos que sus tiempos aumentan de forma proporcionalmente controlada (de 0.036 μs a 0.133 μs) gracias a la memorización de las secuencias ya calculadas, evitando ramificar excesivamente. No obstante, existe un pequeñísimo costo de sobrecarga adicional por motivo del uso de memorias para almacenar la pila de llamadas (stack size auxiliar), y un espacio extra $O(n)$.

Finalmente, se constata que la variante bottom-up o por tabulación es por mucho la implementación de mejor desempeño, con una complejidad de tiempo $O(n)$ en tan óptimos pasos iterativos que crece marginalmente (0.003 μs a 0.006 μs). Además, como evita recursión de por medio, su complejidad espacial decanta en $O(1)$, haciéndola insuperable en balance temporal y de uso de memoria frente al resto de opciones.

