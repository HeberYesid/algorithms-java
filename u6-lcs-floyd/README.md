# U6 PostContenido 2 - DP en cadenas y grafos

## Implementado
- `StringDP`: LCS completo, reconstruccion de LCS, version O(min(n,m)), Edit Distance, alineamiento y version de memoria optimizada.
- `FloydWarshall`: APSP, deteccion de ciclo negativo y reconstruccion de camino minimo.
- `StringDPBench`: benchmark de `lcsLength` para n=100, 500 y 1000.

## Resultados de referencia (LCS)

| Longitud | lcsLength (us/op) |
|----------|-------------------|
| 100      | 32.6              |
| 500      | 762.4             |
| 1000     | 3088.1            |

## Analisis
La evolucion del tiempo al incrementar la longitud de las cadenas es consistente con comportamiento cuadratico. Entre 100 y 500 caracteres el costo se multiplica varias veces, y al llegar a 1000 el crecimiento sigue la misma tendencia porque la tabla DP aumenta en area n*m. Esto confirma que la recurrencia de LCS depende del total de celdas procesadas y no solo de la longitud lineal. En pruebas de exactitud, `lcsMemOpt` y `editDistanceMemOpt` producen los mismos valores que las versiones completas para multiples pares aleatorios, por lo que la optimizacion de memoria mantiene correctitud. En grafos, Floyd-Warshall detecta correctamente ciclos negativos y mantiene `INF` cuando no existe camino. La reconstruccion de ruta retorna una trayectoria valida con costo igual a la distancia minima calculada.

## Ejecucion
- Compilar: `mvn compile`
- Probar: `mvn test`
- Benchmark: `mvn exec:java -Dexec.mainClass=org.openjdk.jmh.Main`
