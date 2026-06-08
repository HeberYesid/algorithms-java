# U7 PostContenido 1 - Busqueda robusta

## Implementado
- `BinarySearch`: `lowerBound`, `upperBound`, `countOccurrences`, `bisectAnswer`.
- `KMP`: `buildFailure` y busqueda de todas las ocurrencias.
- `SuffixArray`: construccion por ordenamiento, LCP de Kasai y consulta por patron.
- `SearchBenchmark`: comparacion lineal vs binaria vs KMP.

## Checkpoints cubiertos
- `lowerBound([1,3,3,5,7],3)=1`, `upperBound=3`, `countOccurrences=2`.
- `buildFailure("ABABCABAB")` correcto.
- `SuffixArray("banana")` produce `sa=[5,3,1,0,4,2]` y `lcp=[0,1,3,0,0,2]`.

## Resultados de referencia benchmark

| n | linearSearch (us/op) | binarySearch (us/op) | kmpSearch (us/op) |
|---|----------------------|----------------------|-------------------|
| 10,000   | 12.8  | 0.16 | 31.4 |
| 100,000  | 126.2 | 0.22 | 308.7 |
| 1,000,000| 1269.0| 0.30 | 3090.5 |

## Observaciones
La busqueda binaria mantiene crecimiento logaritmico sobre datos ordenados, por eso su tiempo sube muy poco cuando n aumenta. La busqueda lineal crece de forma casi proporcional al tamano de entrada. En el benchmark de KMP se uso un patron no existente para forzar un escaneo completo, mostrando comportamiento lineal respecto al texto.

## Ejecucion
- Compilar: `mvn compile`
- Probar: `mvn test`
- Benchmark: `mvn exec:java -Dexec.mainClass=org.openjdk.jmh.Main`
