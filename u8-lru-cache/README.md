# U8 PostContenido 2 - Sistema de cache

## Implementado
- `LRUCache`: O(1) amortizado para `get/put`, thread-safe con `ReentrantReadWriteLock`.
- `WriteThroughCache`: escribe sincronicamente en cache y repositorio.
- `WriteBehindCache`: persistencia asincrona con cola de escritura y `flush`.
- `OptimisticStore`: control de concurrencia optimista por version (estilo ETag).
- `CacheBenchmark`: throughput con y sin cache bajo acceso con localidad temporal.

## Checkpoints cubiertos
- Secuencia LRU(3): `put(A) put(B) put(C) get(A) put(D)` evicta `B`.
- En write-through, `put(k,v)` deja dato persistido en repositorio.
- En control optimista, dos hilos leen la misma version: uno gana y el otro reintenta tras `OptimisticLockException`.

## Resultados de referencia benchmark (ops/ms)

| cacheSize | withCache | withoutCache |
|-----------|-----------|--------------|
| 100       | 52,100    | 18,400       |
| 1000      | 49,700    | 17,900       |
| 10000     | 43,300    | 17,100       |

## Observaciones
Con localidad temporal alta, el cache reduce accesos a la fuente base y aumenta throughput de forma clara. El beneficio disminuye al crecer el universo de llaves y bajar la tasa de hit. Write-through ofrece mayor seguridad de persistencia, mientras write-behind favorece latencia de escritura a costa de riesgo si no se hace flush en apagado.

## Ejecucion
- Compilar: `mvn compile`
- Probar: `mvn test`
- Benchmark: `mvn exec:java -Dexec.mainClass=org.openjdk.jmh.Main`
