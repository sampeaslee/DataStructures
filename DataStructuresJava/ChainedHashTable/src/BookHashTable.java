/**
 * Filename:   BookHashTable.java
 * Project:    p3a
 * Authors:    Sam Peaslee (speaslee@wisc.edu)
 * Semester:   Fall 2019
 * Course:     CS400
 * 
 * Description: The BookHashTable class implements a hash table using the 
 * HashTableADT. The hash table stores key value pairs that consist
 * of a String key and Book value.The structure of the hash table is 
 * an array of linked list, to deal with collisions. The hash table will 
 * resize and rehash the key value pairs after a specified load factor 
 * threshold is reached.
 * The hash function used is key.hashCode() % size of hash table 
 */
import java.util.ArrayList;
import java.util.LinkedList;

public class BookHashTable implements HashTableADT<String, Book> {
    /** The initial capacity that is used if none is specified user */
    static final int DEFAULT_CAPACITY = 101;
    /** The load factor that is used if none is specified by user */
    static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;
    private LinkedList<KeyValue>[] hashTable;
    private double loadFactorThreshold;
    private int numKeys;
    private int capacity;
    /*
     * Inner class to store key value(book) pairs
     */
    private class KeyValue {
        String key;
        Book value;
        /**
         * Constructor to create new KeyValue object
         * @param key
         * @param value
         */
        KeyValue(String key, Book value) {
            this.key = key;
            this.value = value;
        }
        /**
         * Getter method to return key 
         * @return key
         */
        private String getKey() {
            return this.key;
        }
        /**
         * Getter method to return value
         * @return value
         */
        private Book getValue() {
            return this.value;
        }
    }
    /**
     * REQUIRED default no-arg constructor
     * Uses default capacity and sets load factor threshold 
     * for the newly created hash table.
     */
    public BookHashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR_THRESHOLD);
    }
    /**
     * Creates an empty BookHashTable with the specified capacity 
     * and load factor.
     * @param initialCapacity number of elements table should hold at start.
     * @param loadFactorThreshold the ratio of items/capacity that causes table
     *  to resize and rehash
     */
    @SuppressWarnings("unchecked")
    public BookHashTable(int initialCapacity, double loadFactorThreshold) {
        this.capacity = initialCapacity;// Initializing the capacity
        // Initializing the hash table
        hashTable = new LinkedList[initialCapacity];
        // Setting the loadFactorThreshold
        this.loadFactorThreshold = loadFactorThreshold;
    }
    
    /**
     * This method uses a key to calculate a valid hash index.
     * A vaild hash index is an integer between 0 and capacity - 1
     * Hash function is: 
     * key.hashCode() & 0x7FFFFFFF % capacity 
     * "& 0x7FFFFFFF" makes the sign bit positive,
     * since java's hashCode() can return a negative number
     * @param key- key used to compute hash index
     * @return hashIndex  - integer between 0 and capacity - 1 
     */
    private int hash(String key) {
        // Calculate hashcode for key, "& 0x7FFFFFFF" 
        // makes the sign bit zero(positive)
        int hashCode = key.hashCode() & 0x7FFFFFFF;
        // Get hash index by modding hashcode with capacity
        int hashIndex = hashCode % capacity;
        return hashIndex;
    }
    
    /**
     * Inserts a new key value pair into the hash table, by computing a hash
     * index with the key and then storing the key value pair in the linked 
     * list at that index in the hash table.
     * @param key used to store value in hash table 
     * @param value- value to add to hash table
     * @throws DuplicateKeyException if key already in hash table
     * @throws IllegalNullKeyException if key is null
     */
    @Override
    public void insert(String key, Book value)
        throws IllegalNullKeyException, DuplicateKeyException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        LinkedList<KeyValue> bucket;// Create LinkedList reference
        KeyValue keyVal = new KeyValue(key, value);// Create KeyValue reference
        // Calculate hash index
        int hashIndex = hash(key);
        if (hashTable[hashIndex] == null) {
            // LinkedList at the hashIndex is not initialized 
            // need to create a new empty linked list
            bucket = new LinkedList<>();
        } else {
            // LinkedList at hashIndex is already initialized
            bucket = hashTable[hashIndex];
        }
        if (numKeys == 0) {
            // There are no keys stored in the hash table, 
            // so just add the key to it
            bucket.add(keyVal);
            hashTable[hashIndex] = bucket;
            numKeys++;
        } else {
            try {
                // Using get method to search through hash table to see
                // if the key is already stored or not
                get(key);
                // If get does not throw a KeyNotFoundException, 
                // key is already in the hash table
                throw new DuplicateKeyException();
            } catch (KeyNotFoundException e) {
                // KeyNotFoundException thrown by get method, 
                // key can be added to the hashTable
                bucket.add(keyVal);
                hashTable[hashIndex] = bucket;
                numKeys++;
                if ((double) numKeys / capacity >= loadFactorThreshold) {
                    // Load factor threshold met, need to resize hash table 
                    //and rehash key value pairs
                    // Get all key values pairs stored in hash table
                    ArrayList<KeyValue> keyValues = this.getAllKeyValues();
                    // Increase the hash table capacity and reinsert keyValues
                    reHash(keyValues);
                }
            }
        }
    }
    
    /**
     * This method iterates through the hash tables and grabs all the
     * key values pairs currently stored
     * And returns them in an ArrayList
     * @return keyValues 
     */
    private ArrayList<KeyValue> getAllKeyValues() {
        ArrayList<KeyValue> keyValues = new ArrayList<KeyValue>();
        for (int i = 0; i < hashTable.length; i++) {
            LinkedList<KeyValue> bucket = hashTable[i];
            if (bucket == null || bucket.size() == 0) {
                // nothing stored in bucket, can move to the next element
                continue;
            } else {
                // Bucket has key value pair(s) stored add to ArrayList
                for (int j = 0; j < bucket.size(); j++) {
                    keyValues.add(bucket.get(j));
                }
            }
        }
        return keyValues;
    }
    /**
     * This method is used to update the hash table when 
     * the load factor => load factor threshold
     * It uses an ArrayList of all key value pairs currently 
     * stored in the hash table and reinserts them into a new hash table 
     * with a larger capacity
     * @param keyValues- ArrayList of key values currently stored in hashTable 
     * @throws DuplicateKeyException if key already in hash table
     * @throws IllegalNullKeyException if key is null
     */
    @SuppressWarnings("unchecked")
    private void reHash(ArrayList<KeyValue> keyValues)
        throws IllegalNullKeyException, DuplicateKeyException {
        this.capacity = capacity * 2 + 1;// Increase capacity
        // Initialize new hash table with increased capacity
        this.hashTable = new LinkedList[capacity];
        this.numKeys = 0;// reset number of keys to zero
        // Reinsert key value pairs into new larger hash table
        for (int j = 0; j < keyValues.size(); j++) {
            insert(keyValues.get(j).getKey(), keyValues.get(j).getValue());
        }
    }
    /**
     * If the key is present,removes a key value pair from the hash table 
     * @param key- key to remove from the hash table 
     * @throws IllegalNullKeyException- if key is null
     * @return true- if key is removed, false if not
     */
    @Override
    public boolean remove(String key) throws IllegalNullKeyException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        LinkedList<KeyValue> bucket;// Create LinkedList Reference
        KeyValue keyValue;// Create KeyValue reference
        // Calculate hashIndex for key
        int hashIndex = hash(key);
        if (hashTable[hashIndex] == null) {
            // Nothing stored at that index in the hash table
            return false;
        } else {
            bucket = hashTable[hashIndex];
            if (bucket.size() == 0) {
                // LinkedList is empty no keys stored
                return false;
            }
        }
        // Linked List not empty need to search through the list
        // and remove key value pair if its present
        for (int i = 0; i < bucket.size(); i++) {
            keyValue = bucket.get(i);
            if (key.equals(keyValue.getKey())) {
                bucket.remove(keyValue);
                numKeys--;
                return true;
            }
        }
        // Key was not found in the linked list, so nothing was removed
        return false;
    }
    /**
     * Looks for a key in the hash table, if found returns value 
     * associated with key,
     * if not found throws exception 
     * @param key - key to look for in hash table
     * @throws DuplicateKeyException if key already in hash table
     * @throws IllegalNullKeyException if key is null
     * @return book(value) associated with the key if key is stored in table
     */
    @Override
    public Book get(String key)
        throws IllegalNullKeyException,KeyNotFoundException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        LinkedList<KeyValue> bucket;// Create LinkedList Reference
        KeyValue keyValue;// Create KeyValue reference
        // Calculate hashIndex for key
        int hashIndex = hash(key);
        if (hashTable[hashIndex] == null) {
            // Nothing stored at that index in the hash table yet
            throw new KeyNotFoundException();
        } else {
            bucket = hashTable[hashIndex];
            if (bucket.size() == 0) {
                // LinkedList is empty no keys stored
                throw new KeyNotFoundException();
            }
        }
        // LinkedList is not empty, 
        // iterate through the list to try and find key
        for (int i = 0; i < bucket.size(); i++) {
            keyValue = bucket.get(i);
            if (key.equals(keyValue.getKey())) {
                // key is found in the LinkedList
                return keyValue.getValue();
            }
        }
        // After iterating through linked list key was not found
        throw new KeyNotFoundException();
    }
    /**
     * Returns the number of key value pairs stored in the hash table
     * @return numKeys
     */
    @Override
    public int numKeys() {
        return numKeys;
    }
    /**
     * Returns the load factor threshold(number that causes resizing) of the
     *  hash table
     * @return loadFactorThreshold
     */
    @Override
    public double getLoadFactorThreshold() {
        return loadFactorThreshold;
    }
    /**
     * Returns the current capacity of the hash table 
     * @return capacity 
     */
    @Override
    public int getCapacity() {
        return capacity;
    }
    /**
     * Returns the collision resolution scheme used for this hash table.
     * 5 CHAINED BUCKET: array of linked lists
     * @return 5 
     */
    @Override
    public int getCollisionResolutionScheme() {
        return 5;
    }
}
