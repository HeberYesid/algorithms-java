# Huffman Encoder Project

## Ejemplo de Huffman

Cadena de entrada: `aabbbcccc`

Frecuencias:

- `a`: 2
- `b`: 3
- `c`: 4

Árbol resultante:

```
        (\0, 9)
        /     \
    (c, 4)   (\0, 5)
             /      \
          (a, 2)   (b, 3)
```

Códigos generados:

- `c`: `0`
- `a`: `10`
- `b`: `11`

## Análisis de N-Queens (Naive vs Bitmask)

| N  | Solutions | Naive Nodes | Bitmask Nodes | Ratio  |
|----|-----------|-------------|---------------|--------|
| 6  | 4         | 153         | 153           | 1.0000 |
| 7  | 40        | 552         | 552           | 1.0000 |
| 8  | 92        | 2057        | 2057          | 1.0000 |
| 9  | 352       | 8394        | 8394          | 1.0000 |
| 10 | 724       | 35539       | 35539         | 1.0000 |
| 11 | 2680      | 166926      | 166926        | 1.0000 |
| 12 | 14200     | 856189      | 856189        | 1.0000 |
| 13 | 73712     | 4674890     | 4674890       | 1.0000 |
| 14 | 365596    | 27358553    | 27358553      | 1.0000 |

### Interpretación de la poda

La tabla muestra que ambas variantes recorren el mismo árbol de búsqueda y, por eso, el número de nodos explorados coincide en esta instrumentación. La diferencia entre ellas no está en qué estados se visitan, sino en el costo de validar cada estado. La variante naive revisa conflictos recorriendo las reinas colocadas previamente, mientras que la variante bitmask codifica columnas y diagonales ocupadas para hacer esa comprobación con operaciones de bits.

Esto significa que la poda lógica es la misma en ambas soluciones: las ramas inválidas se descartan en el mismo punto de la recursión. Lo que mejora con bitmask es el tiempo por nodo explorado, no necesariamente el total de nodos. Conforme crece `N`, el espacio de búsqueda aumenta con rapidez y la densidad de soluciones válidas disminuye, así que la poda se vuelve cada vez más importante para mantener el problema tractable. En términos prácticos, una poda eficiente reduce el trabajo inútil y hace posible resolver valores más altos de `N` con menos costo computacional.

## Instrucciones de ejecución

Desde la raíz del proyecto puedes usar estos comandos:

```bash
mvn test
```

Ejecuta la suite de pruebas de Huffman y N-Queens.

```bash
mvn package
```

Compila el proyecto y genera el artefacto `.jar` en `target/`.

```bash
mvn compile "exec:java" "-Dexec.mainClass=com.u3.lab2.huffman.Main"
```

Ejecuta la aplicación y muestra en consola la tabla de análisis de N-Queens para `n = 6..14`.

