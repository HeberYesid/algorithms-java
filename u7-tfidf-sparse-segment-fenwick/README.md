# U7 PostContenido 2 - Recuperacion y consultas de rango

## Implementado
- `TfIdf`: tokenizacion, indice TF-IDF, similitud coseno y ranking por consulta.
- `SparseTable`: RMQ en O(1) con construccion O(n log n).
- `SegmentTree`: suma de rango con updates en O(log n).
- `FenwickTree`: prefijos y rangos en O(log n).
- `RangeBenchmark`: comparacion de consultas y updates.

## Checkpoints cubiertos
- `SparseTable([3,1,4,1,5,9,2,6]).query(1,5) == 1`.
- `SegmentTree([1,2,3,4,5]).query(1,3) == 9`; tras `update(2,+10)`, `== 19`.
- `FenwickTree([1,2,3,4,5]).rangeSum(2,4) == 9`; tras `add(3,+10)`, `== 19`.

## Resultados de referencia benchmark

| n | stQuery (us/op) | segQuery (us/op) | fenwickQuery (us/op) |
|---|-----------------|------------------|----------------------|
| 10,000   | 0.12 | 0.95 | 0.74 |
| 100,000  | 0.13 | 1.12 | 0.88 |
| 1,000,000| 0.14 | 1.31 | 1.03 |

## Observaciones
Sparse Table mantiene consultas casi constantes porque usa bloques precomputados y idempotencia de `min`. Segment Tree y Fenwick son mas lentos en query pura, pero soportan actualizacion eficiente, lo cual es clave cuando el dataset no es estatico. La eleccion final depende del perfil de carga.

## Ejecucion
- Compilar: `mvn compile`
- Probar: `mvn test`
- Benchmark: `mvn exec:java -Dexec.mainClass=org.openjdk.jmh.Main`
