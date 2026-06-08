# Daza Algoritmos - Post 2 U4

Proyecto Java con dos estructuras de datos principales:

- `AVLTree` para inserciones y busquedas balanceadas.
- `DSU` para conjuntos disjuntos y componentes conexas.

Tambien incluye checkpoints ejecutables y benchmarks con JMH para comparar implementaciones.

## Requisitos

- JDK 17
- Apache Maven 3.8 o superior

## Compilacion

Desde la raiz del proyecto:

```bash
mvn clean package
```

El proyecto usa `src` como directorio principal de fuentes, segun la configuracion del `pom.xml`.

## Ejecucion de checkpoints

### Checkpoint 1: AVL Tree

```bash
java -cp target/classes edu.trees.Checkpoint1
```

### Checkpoint 2: DSU + GraphComponents

```bash
java -cp target/classes edu.graphs.Checkpoint2
```

## Benchmarks

El `pom.xml` genera un JAR sombreado llamado `target/benchmarks.jar` con JMH.

Para ejecutar todos los benchmarks:

```bash
java -jar target/benchmarks.jar
```

Para ejecutar un benchmark especifico:

```bash
java -jar target/benchmarks.jar TreeBenchmark
java -jar target/benchmarks.jar UnionFindBenchmark
```

## Estructura del proyecto

- `src/edu/trees/` contiene el arbol AVL y su checkpoint.
- `src/edu/dsu/` contiene la implementacion de DSU.
- `src/edu/graphs/` contiene utilidades de componentes conexas y su checkpoint.
- `src/edu/benchmarks/` contiene los benchmarks JMH.

## Notas

- Los checkpoints usan salida por consola y lanzan excepciones si detectan un error funcional.
- Los benchmarks requieren compilar antes el proyecto para que exista `target/benchmarks.jar`.