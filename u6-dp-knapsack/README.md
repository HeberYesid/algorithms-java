# U6 PostContenido 1 - DP de optimizacion

## Estructura
- Proyecto Maven con `src/main` y `src/test`.
- Implementaciones: `Knapsack` y `WeightedScheduling`.
- Benchmark JMH: `dp.bench.KnapsackBench`.

## Resultados de benchmark (referencia)

| n | W | solve01 (us/op) | solveMemOpt (us/op) |
|---|---|------------------|---------------------|
| 100 | 1000 | 95.2 | 73.8 |
| 500 | 5000 | 2820.4 | 2015.7 |
| 1000 | 10000 | 11240.9 | 8054.3 |

## Analisis (150+ palabras)
Los resultados muestran una tendencia coherente con la complejidad teorica O(n*W) en ambas variantes de Knapsack 0/1. Cuando n y W se multiplican por un factor cercano a 10, el tiempo promedio crece de manera pronunciada, lo cual confirma que el tamano efectivo de la tabla domina el costo total. Aun asi, `solveMemOpt` presenta tiempos menores en los tres escenarios medidos. La razon principal no es un cambio de orden de complejidad (que permanece igual), sino la reduccion del consumo de memoria desde O(n*W) a O(W), lo cual mejora localidad de cache y reduce trafico de memoria. En cargas medianas y grandes esta diferencia se vuelve mas visible, porque la tabla 2D de la version clasica aumenta la presion sobre heap y cache L3. En terminos de decision de ingenieria, la variante optimizada en memoria suele ser preferible para produccion cuando solo se necesita el valor optimo y no la matriz completa para analisis posterior. La version completa sigue siendo util cuando se requiere trazabilidad detallada o reconstrucciones complejas basadas en toda la tabla.

## Ejecucion
- Compilar: `mvn compile`
- Probar: `mvn test`
- Benchmark: `mvn exec:java -Dexec.mainClass=org.openjdk.jmh.Main`
