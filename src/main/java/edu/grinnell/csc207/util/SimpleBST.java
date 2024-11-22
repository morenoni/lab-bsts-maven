package edu.grinnell.csc207.util;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;
import java.util.function.BiConsumer;

/**
 * A simple implementation of binary search trees.
 *
 * @author Nicole Moreno Gonzalez
 * @author Samuel A. Rebelsky
 *
 * @param <K>
 *   The type used for keys.
 * @param <V>
 *   The type used for values.
 */
public class SimpleBST<K, V> implements SimpleMap<K, V> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The root of our tree. Initialized to null for an empty tree.
   */
  BSTNode<K, V> root;

  /**
   * The comparator used to determine the ordering in the tree.
   */
  Comparator<? super K> order;

  /**
   * The size of the tree.
   */
  int size;

  /**
   * A cached value (useful in some circumstances.
   */
  V cachedValue;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new binary search tree that orders values using the
   * specified comparator.
   *
   * @param comp
   *   The comparator used to compare keys.
   */
  public SimpleBST(Comparator<? super K> comp) {
    this.order = comp;
    this.root = null;
    this.size = 0;
    this.cachedValue = null;
  } // SimpleBST(Comparator<K>)

  /**
   * Create a new binary search tree that orders values using a
   * not-very-clever default comparator.
   */
  public SimpleBST() {
    this((k1, k2) -> k1.toString().compareTo(k2.toString()));
  } // SimpleBST()

  // +-------------------+-------------------------------------------
  // | SimpleMap methods |
  // +-------------------+

  /**
   * Set the value associated with key.
   *
   * @param key
   *   The key to use.
   * @param value
   *   The associated value.
   *
   * @return the previous value associated with key (or null, if there's no
   *         such value)
   *
   * @throws NullPointerException if the key is null.
   */
  @Override
  public V set(K key, V value) {
    if (key == null) {
      throw new NullPointerException("Key cannot be null");
    }

    BSTNode<K, V> current = this.root;
    BSTNode<K, V> parent = null;

    while (current != null) {
      parent = current;
      int comp = this.order.compare(key, current.key);
      if (comp == 0) {
        V oldValue = current.value;
        current.value = value;
        return oldValue;
      } else if (comp < 0) {
        current = current.left;
      } else {
        current = current.right;
      }
    }

    BSTNode<K, V> newNode = new BSTNode<>(key, value);

    if (parent == null) {
      this.root = newNode; 
    } else if (this.order.compare(key, parent.key) < 0) {
      parent.left = newNode;
    } else {
      parent.right = newNode;
    }
    this.size++;
    return null;
  } // set(K, V)

  /**
   * Get the value associated with key.
   *
   * @param key
   *   The key to use.
   *
   * @return the corresponding value.
   *
   * @throws IndexOutOfBoundsException if the key is not in the map.
   * @throws NullPointerException if the key is null.
   */
  @Override
  public V get(K key) {
    if (key == null) {
      throw new NullPointerException("null key");
    } // if
    return get(key, root);
  } // get(K, V)

  /**
   * Determine how many key/value pairs are in the map.
   *
   * @return the number of key/value pairs.
   */
  @Override
  public int size() {
    return 0;           // STUB
  } // size()

  /**
   * Determine if a key appears in the table.
   *
   * @param key
   *   The key to check.
   *
   * @return true if the key appears in the table and false otherwise.
   */
  @Override
  public boolean containsKey(K key) {
    return false;       // STUB
  } // containsKey(K)

  /**
   * Remove the value with the given key. Also remove the key.
   *
   * @param key
   *   The key to remove.
   *
   * @return The associated value (or null, if there is no associated value).
   * @throws NullPointerException if the key is null.
   */
  @Override
  public V remove(K key) {
    return null;        // STUB
  } // remove(K)

  /**
   * Get an iterator for all of the keys in the map.
   *
   * @return an iterator for all the keys.
   */
  @Override
  public Iterator<K> keys() {
    return new Iterator<K>() {
      Iterator<BSTNode<K, V>> nit = SimpleBST.this.nodes();

      @Override
      public boolean hasNext() {
        return nit.hasNext();
      } // hasNext()

      @Override
      public K next() {
        return nit.next().key;
      } // next()

      @Override
      public void remove() {
        nit.remove();
      } // remove()
    };
  } // keys()

  /**
   * Get an iterator for all of the values in the map.
   *
   * @return an iterator for all the values.
   */
  @Override
  public Iterator<V> values() {
    return new Iterator<V>() {
      Iterator<BSTNode<K, V>> nit = SimpleBST.this.nodes();

      @Override
      public boolean hasNext() {
        return nit.hasNext();
      } // hasNext()

      @Override
      public V next() {
        return nit.next().value;
      } // next()

      @Override
      public void remove() {
        nit.remove();
      } // remove()
    };
  } // values()

  /**
   * Apply a procedure to each key/value pair.
   *
   * @param action
   *   The action to perform for each key/value pair.
   */
  @Override
  public void forEach(BiConsumer<? super K, ? super V> action) {
    // STUB
  } // forEach

  // +----------------------+----------------------------------------
  // | Other public methods |
  // +----------------------+

  /**
   * Dump the tree to some output location.
   *
   * @param pen
   *   The PrintWriter used to dump the tree.
   */
  public void dump(PrintWriter pen) {
    dump(pen, root, "");
  } // dump(PrintWriter)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Dump a portion of the tree to some output location.
   *
   * @param pen
   *   The PrintWriter used to dump the subtree.
   * @param node
   *   The root of the subtree.
   * @param indent
   *   How much to indent the subtree.
   */
  void dump(PrintWriter pen, BSTNode<K, V> node, String indent) {
    if (node == null) {
      pen.println(indent + "<>");
    } else {
      pen.println(indent + node.key + ": " + node.value);
      if ((node.left != null) || (node.right != null)) {
        dump(pen, node.left, indent + "  ");
        dump(pen, node.right, indent + "  ");
      } // if has children
    } // else
  } // dump

  /**
   * Get the value associated with a key in a subtree rooted at node.  See the
   * top-level get for more details.
   *
   * @param key
   *   The key to search for.
   * @param node
   *   The root of the subtree to look through.
   *
   * @return
   *   The corresponding value.
   *
   * @throws IndexOutOfBoundsException
   *   when the key is not in the subtree.
   */
  V get(K key, BSTNode<K, V> node) {
    if (node == null) {
      throw new IndexOutOfBoundsException("Invalid key: " + key);
    } // if
    int comp = order.compare(key, node.key);
    if (comp == 0) {
      return node.value;
    } else if (comp < 0) {
      return get(key, node.left);
    } else {
      return get(key, node.right);
    } // if/else
  } // get(K, BSTNode<K, V>)

  /**
   * Get an iterator for all of the nodes. (Useful for implementing the
   * other iterators.)
   *
   * @return an iterator for all of the other nodes.
   */
  Iterator<BSTNode<K, V>> nodes() {
    return new Iterator<BSTNode<K, V>>() {
      Stack<BSTNode<K, V>> stack = new Stack<>();
      BSTNode<K, V> current = SimpleBST.this.root; 

      @Override
      public boolean hasNext() {
        return current != null || !stack.isEmpty();
      }

      @Override
      public BSTNode<K, V> next() {
        while (current != null) {
          stack.push(current);
          current = current.left;
        }
        if (stack.isEmpty()) {
          throw new IllegalStateException("No more elements");
        }
        BSTNode<K, V> node = stack.pop();
        current = node.right; 
        return node;
      }
    };
  } // nodes()

} // class SimpleBST
