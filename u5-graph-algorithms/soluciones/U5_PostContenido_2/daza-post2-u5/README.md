# U5 PostContenido 2 - Caminos minimos, MST, toposort y SCC

## Implementado
- `WeightedDiGraph`: grafo dirigido ponderado con `transpose()`.
- `Dijkstra`: distancias minimas con lazy deletion y reconstruccion de camino con early termination.
- `BellmanFord`: soporte para pesos negativos y deteccion de ciclo negativo.
- `UnionFind`: path splitting + union by size (reutilizable para Kruskal).
- `MST`: `kruskal` y `prim` para costo total de arbol de expansion minima.
- `TopSort`: ordenamiento topologico por Kahn con excepcion en presencia de ciclo.
- `SCC`: Kosaraju para componentes fuertemente conexas.
- `ShortestPathBenchmark`: comparacion JMH de BFS vs Dijkstra en grafos no ponderados.

## Checkpoints cubiertos
- `Dijkstra.run` produce distancias esperadas en un caso de 5 vertices.
- `Dijkstra.path` retorna lista vacia para destino no alcanzable.
- `BellmanFord.hasNegCycle` detecta correctamente ciclo negativo.
- En pesos no negativos, Bellman-Ford y Dijkstra generan el mismo `dist[]`.
- `MST.kruskal` y `MST.prim` retornan igual costo en el mismo grafo.
- `TopSort.kahn` da orden valido en DAG y falla con `IllegalArgumentException` si hay ciclo.
- `SCC.kosaraju` identifica 3 SCCs en el caso de prueba definido.

## Resultados de referencia benchmark (ops/s)

| V | dijkstraBenchmark | bfsBenchmark |
|---|-------------------|--------------|
| 1000 | 13,900 | 27,600 |
| 5000 | 2,600  | 5,000  |

## Observaciones
Cuando todos los pesos son 1, BFS evita la sobrecarga del heap y por eso supera a Dijkstra en throughput. La diferencia se mantiene al escalar `V`, alineada con la complejidad teorica esperada.

## Ejecucion
- Compilar: `mvn compile`
- Probar: `mvn test`
- Benchmark en PowerShell: `mvn "-Dexec.mainClass=org.openjdk.jmh.Main" "-Dexec.args=-f 0" exec:java`
- <img width="921" height="442" alt="image" src="https://github.com/user-attachments/assets/ff519856-3477-4361-8467-f6d7c9713719" />

