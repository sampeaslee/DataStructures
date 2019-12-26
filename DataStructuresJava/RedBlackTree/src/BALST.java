import java.util.ArrayList;
import java.util.List;

/**
 * Name: Sam Peaslee Email: speaslee@wisc.edu
 * project: p2 Balanced Search Tree - BALST.java
 * Lecture: 002
 * Description: This class implements a Red Black Tree with the BALSTADT interface. Each node of
 * the tree stores a key, node, color, reference to a left, right and parent node. Inserting nodes
 * will maintain a balanced tree, once an item is removed the tree made not be balanced anymore.
 * Remove method does removes key and restructure tree, but does not repair Red Black properties to
 * maintain a balanced tree
 * 
 * @param <K> is the generic type of key
 * 
 * @param <V> is the generic type of value
 */
public class BALST<K extends Comparable<K>, V> implements BALSTADT<K, V> {

    enum Color {
        // Enum to keep track of the color of a node.
        RED, BLACK;
    }
    /**
     * Node class that stores a node with a key, value,
     * reference to a left, right and parent node
     * @param <K>  is the generic type of key
     * @param <V> is the generic type of value
     */
    class BSTNode<K, V> {

        // Fields of a BSTNode
        K key;
        V value;
        BSTNode<K, V> left;
        BSTNode<K, V> right;
        BSTNode<K, V> parent;
        Color color;

        /*
         * Constructor to create a new BSTNode
         */
        BSTNode(K key, V value, Color color, BSTNode<K, V> leftChild, BSTNode<K, V> rightChild,
            BSTNode<K, V> parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.left = leftChild;
            this.right = rightChild;
            this.parent = parent;
        }

        /**
         * Left rotate around a BSTNode
         * @param node- rotated around
         * @return reference to updated node 
         */
        BSTNode<K, V> leftRotate() {
            BSTNode<K, V> temp = this.right;
            this.right = temp.left;
            temp.left = this;
            // Structure of the node changes so need to update the parent references
            temp.parent = this.parent;
            this.parent = temp;
            if (this.right != null) {
                this.right.parent = this;
            }
            return temp;
        }

        /**
         * Right rotate around a BSTNode
         * @param node- rotated around
         * @return reference to updated node 
         */
        BSTNode<K, V> rightRotate() {
            BSTNode<K, V> temp = this.left;
            this.left = temp.right;
            temp.right = this;
            // Structure of the node changes so need to update the parent references
            temp.parent = this.parent;
            this.parent = temp;
            if (this.right != null) {
                this.right.parent = this;
            }
            return temp;
        }

        K getParent() {
            return this.parent.key;
        }

    }// End of inner class

    // Private Field of BALST class
    private BSTNode<K, V> root;// Reference to root of tree
    private int numKeys;// Number of keys in tree

    /*
     * Constructor to create a new empty BALST
     */
    public BALST() {}

    /**
     * If tree is empty returns null, otherwise returns the key stored at the root 
     * @return root key or null
     */
    @Override
    public K getKeyAtRoot() {
        if (root == null) {
            return null;
        }
        return root.key;
    }

    /**
     * Tries to find a node with a key that matches the specified key.
     * If a matching node is found, it returns the key that is in the left child.
     * If the left child of the found node is null, returns null.
     * 
     * @param key A key to search for
     * @return The key that is in the left child of the found key
     * 
     * @throws IllegalNullKeyException if key argument is null
     * @throws KeyNotFoundException if key is not found in this BST
     */

    @Override
    public K getKeyOfLeftChildOf(K key) throws IllegalNullKeyException, KeyNotFoundException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        // Left child of node with key, if key is found, otherwise throws KeyNotFoundException
        BSTNode<K, V> LeftChild = lookup(root, key).left;
        if (LeftChild == null) {// If left child is null returns null
            return null;
        } else {
            return LeftChild.key;// returns key of left child
        }
    }

    /**
     * Tries to find a node with a key that matches the specified key.
     * If a matching node is found, it returns the key that is in the right child.
     * If the right child of the found node is null, returns null.
     * 
     * @param key A key to search for
     * @return The key that is in the right child of the found key
     * 
     * @throws IllegalNullKeyException if key is null
     * @throws KeyNotFoundException if key is not found in this BST
     */

    @Override
    public K getKeyOfRightChildOf(K key) throws IllegalNullKeyException, KeyNotFoundException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        BSTNode<K, V> RightChild = lookup(root, key).right;
        // Right child of node with key, if key is found, otherwise throws key not
        // found Exception
        if (RightChild == null) {// If right child is null returns null
            return null;
        } else {
            return RightChild.key;// returns key of right child
        }
    }

    /**
     * Helper method 
     * Recursively looks through the tree to see if the key is present. If key not present throws KeyNotFoundExcepton 
     * If key is present returns the node that stores the key 
     * @param node - node to see if key is present in
     * @param key - key to look for in node 
     * @return node that stores key 
     * @throws KeyNotFoundException if lookup reaches end of tree without finding key then exception is thrown 
     */

    private BSTNode<K, V> lookup(BSTNode<K, V> node, K key) throws KeyNotFoundException {
        if (node == null) {
            // Key not found throw Exception
            throw new KeyNotFoundException();
        }
        if (key.compareTo(node.key) == 0) {
            // If key is found return node that stores that key
            return node;
        }
        // If key is less than the key at the current node search left
        if (key.compareTo(node.key) < 0) {
            return lookup(node.left, key);

        } else {// Otherwise search right
            return lookup(node.right, key);
        }

    }

    /**
     * Gets the node that stores the specified key
     * If key is is not found throws KeyNotFoundException
     * @param Key
     * @return BSTNode that stores the key
     * @throws KeyNotFoundException
     */
    private BSTNode<K, V> lookup(K Key) throws KeyNotFoundException {
        // Starts at the root then recursively looks through tree to see if key is present
        return lookup(root, Key);
    }


    /**
     * Returns the height of this BST.
     * H is defined as the number of levels in the tree.
     * 
     * If root is null, return 0
     * If root is a leaf, return 1
     * Else return 1 + max( height(root.left), height(root.right) )
     * 
     * Examples:
     * A BST with no keys, has a height of zero (0).
     * A BST with one key, has a height of one (1).
     * A BST with two keys, has a height of two (2).
     * A BST with three keys, can be balanced with a height of two(2)
     *                        or it may be linear with a height of three (3)
     * ... and so on for tree with other heights
     * 
     * @return the number of levels that contain keys in this BINARY SEARCH TREE
     */

    @Override
    public int getHeight() {
        if (root == null) {// If tree is empty return 0
            return 0;
        }
        return height(root);// Returns height of tree
    }

    /**
     * Helper method for getHeight that recursively goes through a tree adding max heights of its subtrees to calculate total height of tree 
     * @param node
     * @return the height of the tree 
     */
    private int height(BSTNode<K, V> node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null & node.right == null) {
            return 1;
        }
        int heightLeft = height(node.left);// Recursively get the height of the nodes left subtree
        int heightRight = height(node.right);// Recursively get the height of the nodes right
                                             // subtree
        int maxHeight = 0;
        // maxHeight is the height of the taller subtree
        if (heightLeft >= heightRight) {
            maxHeight = heightLeft;
        } else {
            maxHeight = heightRight;
        }
        return 1 + maxHeight;// returns maxHeight plus 1, to include the node in its height
    }

    /**
    * Returns the keys of the data structure in sorted order.
    * In the case of binary search trees, the visit order is: L V R
    * 
    * If the SearchTree is empty, an empty list is returned.
    * 
    * @return List of Keys in-order
    */

    @Override
    public List<K> getInOrderTraversal() {
        ArrayList<K> inOrderList = new ArrayList<>();
        inOrder(root, inOrderList);
        return inOrderList;
    }

    /**
     * Helper function to recursively go through a tree and add the keys to list 
     * Stores left,root, then right
     * @param node
     * @param list 
     */
    private void inOrder(BSTNode<K, V> node, ArrayList<K> list) {
        if (node == null) {
            return;
        }
        inOrder(node.left, list);
        list.add(node.key);
        inOrder(node.right, list);
    }

    /**
     * Returns the keys of the data structure in pre-order traversal order.
     * In the case of binary search trees, the order is: V L R
     * 
     * If the SearchTree is empty, an empty list is returned.
     * 
     * @return List of Keys in pre-order
     */
    @Override
    public List<K> getPreOrderTraversal() {
        ArrayList<K> preOrderList = new ArrayList<>();
        preOrder(root, preOrderList);
        return preOrderList;
    }

    /**
     * Helper function to recursively go through a tree and the keys to a list
     * Stores root, left, then right
     * @param node
     * @param list
     */
    private void preOrder(BSTNode<K, V> node, ArrayList<K> list) {
        if (node == null) {
            return;
        }
        list.add(node.key);
        preOrder(node.left, list);
        preOrder(node.right, list);
    }

    /**
     * Returns the keys of the data structure in post-order traversal order.
     * In the case of binary search trees, the order is: L R V 
     * 
     * If the SearchTree is empty, an empty list is returned.
     * 
     * @return List of Keys in post-order
     */
    @Override
    public List<K> getPostOrderTraversal() {
        ArrayList<K> postOrderList = new ArrayList<K>();
        postOrder(root, postOrderList);
        return postOrderList;
    }

    /**
     * Helper function to recursively go through a tree and add the keys to a list
     * Stores left, right, then root
     * @param node
     * @param list
     */
    private void postOrder(BSTNode<K, V> node, ArrayList<K> list) {
        if (node == null) {
            return;
        }
        postOrder(node.left, list);
        postOrder(node.right, list);
        list.add(node.key);
    }

    /**
     * Returns the keys of the data structure in level-order traversal order.
     * 
     * The root is first in the list, then the keys found in the next level down,
     * and so on. 
     * 
     * If the SearchTree is empty, an empty list is returned.
     * 
     * @return List of Keys in level-order
     */
    @Override
    public List<K> getLevelOrderTraversal() {
        List<K> levelOrder = new ArrayList<>();
        for (int i = 0; i < getHeight(); i++) {
            levelOrder(root, i, levelOrder);
        }
        return levelOrder;
    }

    /**
     * Helper function to recursively go through a tree and add the keys to a list
     * Stores keys at the root, then next level....
     * @param node
     * @param list
     */
    private void levelOrder(BSTNode<K, V> node, int level, List<K> list) {
        if (node == null) {
            return;
        }
        if (level == 0) {
            list.add(node.key);
        }
        levelOrder(node.left, level - 1, list);
        levelOrder(node.right, level - 1, list);
    }

    /** 
     * Add the key,value pair to the data structure and increase the number of keys.
     * If key is null, throw IllegalNullKeyException;
     * If key is already in data structure, throw DuplicateKeyException(); 
     * Do not increase the num of keys in the structure, if key,value pair is not added.
     * @param Key - key to be stored in a new BSTNode
     * @param Value - value associated with key 
     */
    @Override
    public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
        if (key == null) {
            // If key is null throw Exception
            throw new IllegalNullKeyException();
        }
        root = insert(root, key, value, null); // Call to recursive insert method
        try {
            repair(lookup(key));// Repair Red Black Properties to keep balanced tree
            // root of tree could potential be changed
            // Need to find node with null parent and set as new root
            while (root.parent != null) {
                root = root.parent;
                root.color = Color.BLACK;
            }
        } catch (KeyNotFoundException e) {
            // If key was not properly inserted lookup() method will throw an exception
            e.printStackTrace();
        }
    }

    /**
     * Helper method to go through the tree and add the key if it is not already present 
     * @param node- current node
     * @param key- key to be added
     * @param value- value associated with key 
     * @param parent- parent node of current key 
     * @return new BSTNode with key, if key can be inserted 
     * @throws DuplicateKeyException
     */
    private BSTNode<K, V> insert(BSTNode<K, V> node, K key, V value, BSTNode<K, V> parent)
        throws DuplicateKeyException {

        if (node == null) {
            // add key and increment number of keys by one
            numKeys++;
            return new BSTNode<K, V>(key, value, Color.RED, null, null, parent);
        }
        if (node.key.compareTo(key) == 0) {
            // If key is already in tree throw exception
            throw new DuplicateKeyException();
        }

        if (key.compareTo(node.key) < 0) {
            // If key is less than key at current node, go left
            node.left = insert(node.left, key, value, node);
        } else {// Otherwise go right
            node.right = insert(node.right, key, value, node);
        }
        return node;
    }

    /**
     * Restores Red Black Properties to keep tree balanced
     * @param node - node to start repair at 
     * @throws IllegalNullKeyException
     * @throws KeyNotFoundException
     */

    private void repair(BSTNode<K, V> node) throws IllegalNullKeyException, KeyNotFoundException {
        if (node.parent == null) {
            // Empty Tree add key to root and color black
            node.color = Color.BLACK;
            return;
        }
        // Reference to node's parent's sibling
        BSTNode<K, V> sibling = this.getSibling(node.parent.key);
        BSTNode<K, V> grandMa = node.parent.parent;// Reference to node's GrandMa

        if (node.parent.color == Color.BLACK) {
            // If parent is black no Red Black Tree violation
            return;
        } else if (sibling != null && sibling.color == Color.RED) {
            // If parents sibling is red need to do a re-coloring
            node.parent.color = Color.BLACK;
            sibling.color = Color.BLACK;
            if (grandMa != root) {// If grandMa is not the root color Red
                grandMa.color = Color.RED;
                if (grandMa.parent.color == Color.RED) {
                    // If grandMa's parent is red there is a violation of
                    // Red Black rules need to recursively repair the tree
                    repair(grandMa);
                }
            }
        } else {// Parent is Red, violates Red Black Rules
            if (node == node.parent.right && node.parent == grandMa.left) {
                // node is parents right child and parent is grandMa's left child
                // Left rotate at parent
                node.parent = node.parent.leftRotate();
                grandMa.left = node.parent;
                node = node.parent.left;
            } else if (node == node.parent.left && node.parent == grandMa.right) {
                // node is parent left child and parent is grandMa's right child
                // Right rotate at parent
                node.parent = node.parent.rightRotate();
                grandMa.right = node.parent;
                node = node.parent.right;
            }

            if (node == node.parent.right) {
                // node is parents right child
                if (grandMa.parent != null && grandMa.parent.left == grandMa) {
                    // grandMa is not root and is left child of its parent
                    // Left rotate at grandMa updates its parents left child reference
                    grandMa = grandMa.leftRotate();
                    grandMa.parent.left = grandMa;
                } else {
                    // Left rotate at grandMa
                    grandMa = grandMa.leftRotate();
                    if (grandMa.parent != null) {
                        // If grandMa is not root updates is parents right child reference
                        grandMa.parent.right = grandMa;
                    }
                }
                // Re-color nodes after rotation
                grandMa.left.color = Color.RED;
                node.parent.color = Color.BLACK;
            } else {
                // node is parents left child
                if (grandMa.parent != null && grandMa.parent.left == grandMa) {
                    // grandMa is not root and is left child of its parent
                    // Right rotate at grandma and updates is parents right child
                    grandMa = grandMa.rightRotate();
                    grandMa.parent.left = grandMa;
                } else {
                    // Right rotate at grandMa
                    grandMa = grandMa.rightRotate();
                    if (grandMa.parent != null) {
                        // grandMa is not rot updates its parents right child reference
                        grandMa.parent.right = grandMa;
                    }
                }
                // Re-color nodes after rotation
                node.parent.color = Color.BLACK;
                grandMa.right.color = Color.RED;
            }
        }
    }

    /**
     * Gets the sibling BSTNode of a key  
     * @param key- key to find sibling of
     * @return- null if only child or the BSTNode of the sibling of the key 
     * @throws IllegalNullKeyException
     * @throws KeyNotFoundException
     */
    private BSTNode<K, V> getSibling(K key) throws IllegalNullKeyException, KeyNotFoundException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        BSTNode<K, V> node = lookup(root, key);// Reference to node with key
        if (node == root) {// node is root has no siblings
            return null;
        }
        BSTNode<K, V> parentNode = node.parent;// Reference to nodes parent
        K leftChildKey = getKeyOfLeftChildOf(parentNode.key); // key of parents left child
        K rightChildKey = getKeyOfRightChildOf(parentNode.key);// key of parents right child
        // Parent node only has one child node has no siblings
        if (leftChildKey == null | rightChildKey == null) {
            return null;
        } else {
            if (node.key.compareTo(leftChildKey) == 0) {
                // If key of node is left child of parent then return right child
                return lookup(root, rightChildKey);
            } else {
                // Otherwise key of node is right child of parent return left child;
                return lookup(root, leftChildKey);
            }
        }
    }


    /** 
       * If key is found, remove the key,value pair from the data structure and decrease num keys.
       * If key is not found, do not decrease the number of keys in the data structure.
       * If key is null, throw IllegalNullKeyException
       * If key is not found, throw KeyNotFoundException().
       * Removing the key does not look to repair Red Black Properties, they could be violated depending on 
       * the node removed. 
       * @param key- key to remove
       * @return true if key is properly removed  
       */
    @Override
    public boolean remove(K key) throws IllegalNullKeyException, KeyNotFoundException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        lookup(key);// IF key not present KeyNorFoundException thrown
        root = remove(root, key);// Call to recursive remove method
        numKeys--;// Key removed decrease number of keys
        return true;
    }

    /**
     * Helper method to recursively go through tree and remove specified key
     * @param node - current node in tree
     * @param key - key to remove 
     * @return node - updated reference to subtree with the key removed 
     */
    private BSTNode<K, V> remove(BSTNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) == 0) {// Key found in tree
            if (node.left == null && node.right == null) {
                // If no children return null
                return null;
            }
            if (node.left == null) {
                // If no left child return right child
                return node.right;
            }
            if (node.right == null) {
                // if no right child return left child
                return node.left;
            }
            // If node that has two children
            K inOrderPred = inOrderPred(node.left);// Find in order predecessor
            node.key = inOrderPred;// Set node to the node of its predecessor
            // Remove predecessor and set subtree to left child of the node
            node.left = remove(node.left, inOrderPred);
        }

        if (key.compareTo(node.key) < 0) {
            // If keys is less then key stored in node, go left
            node.left = remove(node.left, key);
        } else {
            // Otherwise go right
            node.right = remove(node.right, key);
        }

        return node;// Return reference to updated node
    }

    /**
     * Method looks in tree to find the largest value 
     * @param node - node to start at
     * @return largest key in tree 
     */
    private K inOrderPred(BSTNode<K, V> node) {
        if (node.right == null) {
            return node.key;
        } else {
            // Go all the way right until you cannot anymore
            return inOrderPred(node.right);
        }
    }

    /**
     *  Returns the value associated with the specified key
     *
     * Does not remove key or decrease number of keys
     * If key is null, throw IllegalNullKeyException 
     * If key is not found, throw KeyNotFoundException().
     * Uses helper method lookup 
     * @param key - key to look for 
     * @return value associated with key 
     */
    @Override
    public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        BSTNode<K, V> node = lookup(root, key);// Call recursive lookup method
        // If no exception thrown key is found return value associated with key
        return node.value;
    }

    /** 
     * Returns true if the key is in the data structure
     * If key is null, throw IllegalNullKeyException 
     * Returns false if key is not null and is not present 
     * @param key - key to look for 
     * @return true if key is found, false if not found 
     */
    @Override
    public boolean contains(K key) throws IllegalNullKeyException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        try {
            lookup(root, key);// Call to recursive lookup method
        } catch (KeyNotFoundException e) {
            // If key not found lookup method will throw a KeyNotFoundException
            return false;
        }
        return true;
    }


    /**
     *  Returns the number of key,value pairs in the data structure
     */
    @Override
    public int numKeys() {
        return numKeys;
    }

    /**
     * Prints the tree. Prints from right to left horizontally
     */
    @Override
    public void print() {
        if (root == null) {
            return;
        }
        print(root, 0, numKeys);// Call to recursive print method
        System.out.println("\n");
    }

    /**
     * Helper method to recursively go through a tree and print the keys 
     */
    private void print(BSTNode<K, V> node, int spaces, int count) {
        if (node == null) {
            return;
        }
        spaces += count;
        print(node.right, spaces, count);// Print right keys first
        for (int i = count; i < spaces; i++) {
            // For loop to print spaces
            System.out.print(" ");
        }
        System.out.println(node.key);
        print(node.left, spaces, count);// Then print left keys
    }
}

