package edu.trees;

/**
 * AVL Tree genérico con auto-balanceo por rotaciones.
 *
 * <p>La estructura mantiene la altura balanceada mediante rotaciones para preservar
 * operaciones de búsqueda e inserción eficientes.</p>
 *
 * @param <T> tipo de las claves almacenadas
 */
public class AVLTree<T extends Comparable<T>> {
    private static class Node<T> {
        T key;
        int height;
        Node<T> left;
        Node<T> right;
        Node(T key) { this.key = key; this.height = 1; }
    }

    private Node<T> root;

    private int height(Node<T> n) { return n == null ? 0 : n.height; }
    
    private int bf(Node<T> n) { return n == null ? 0 : height(n.left) - height(n.right); }
    
    private void updateHeight(Node<T> n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private Node<T> rotateRight(Node<T> z) {
        Node<T> y = z.left; 
        z.left = y.right; 
        y.right = z;
        updateHeight(z); 
        updateHeight(y); 
        return y;
    }

    private Node<T> rotateLeft(Node<T> z) {
        Node<T> y = z.right; 
        z.right = y.left; 
        y.left = z;
        updateHeight(z); 
        updateHeight(y); 
        return y;
    }

    private Node<T> balance(Node<T> n) {
        updateHeight(n);
        int b = bf(n);
        if (b > 1) { // Left-heavy
            if (bf(n.left) < 0) n.left = rotateLeft(n.left); // LR
            return rotateRight(n);
        }
        if (b < -1) { // Right-heavy
            if (bf(n.right) > 0) n.right = rotateRight(n.right); // RL
            return rotateLeft(n);
        }
        return n;
    }

    private Node<T> insert(Node<T> n, T key) {
        if (n == null) return new Node<>(key);
        int cmp = key.compareTo(n.key);
        if (cmp < 0) n.left = insert(n.left, key);
        else if (cmp > 0) n.right = insert(n.right, key);
        else return n; // Omitir duplicados
        return balance(n);
    }

    /**
     * Inserta una clave en el árbol si todavía no existe.
     *
     * @param key clave a insertar
     */
    public void insert(T key) { 
        root = insert(root, key); 
    }

    /**
     * Verifica si el árbol contiene la clave indicada.
     *
     * @param key clave a buscar
     * @return {@code true} si la clave existe en el árbol
     */
    public boolean contains(T key) {
        Node<T> cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return true;
            cur = cmp < 0 ? cur.left : cur.right;
        }
        return false;
    }

    /**
     * Devuelve la altura actual del árbol.
     *
     * @return altura del árbol, o 0 si está vacío
     */
    public int height() { 
        return height(root); 
    }
}
