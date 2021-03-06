import java.util.ArrayList;
import java.util.LinkedList;

// TODO: comment and complete your HashTableADT implementation
//
// TODO: implement all required methods
// DO ADD REQUIRED PUBLIC METHODS TO IMPLEMENT interfaces
//
// DO NOT ADD ADDITIONAL PUBLIC MEMBERS TO YOUR CLASS
// (no public or package methods that are not in implemented interfaces)
//
// TODO: describe the collision resolution scheme you have chosen
// identify your scheme as open addressing or bucket
//
// if open addressing: describe probe sequence
// if buckets: describe data structure for each bucket
//
// TODO: explain your hashing algorithm here
// NOTE: you are not required to design your own algorithm for hashing,
// since you do not know the type for K,
// you must use the hashCode provided by the <K key> object

/**
 * HashTable implementation that uses:
 * 
 * @param <K> unique comparable identifier for each <K,V> pair, may not be null
 * @param <V> associated value with a key, value may be null
 */
public class BookHashTableNoInnerClass implements HashTableADT<String, Book> {

    /** The initial capacity that is used if none is specifed user */
    static final int DEFAULT_CAPACITY = 101;

    /** The load factor that is used if none is specified by user */
    static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;

    private LinkedList<Book>[] hashTable;
    private double loadFactorThreshold;
    private int numKeys;
    private int capacity;

    /**
     * REQUIRED default no-arg constructor
     * Uses default capacity and sets load factor threshold 
     * for the newly created hash table.
     */
    public BookHashTableNoInnerClass() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR_THRESHOLD);
    }

    /**
     * Creates an empty hash table with the specified capacity 
     * and load factor.
     * @param initialCapacity number of elements table should hold at start.
     * @param loadFactorThreshold the ratio of items/capacity that causes table to resize and rehash
     */
    @SuppressWarnings("unchecked")
    public BookHashTableNoInnerClass(int initialCapacity, double loadFactorThreshold) {
        // TODO: comment and complete a constructor that accepts initial capacity
        // and load factor threshold and initializes all fields
        this.capacity = initialCapacity;
        hashTable = new LinkedList[initialCapacity];
        this.loadFactorThreshold = loadFactorThreshold;
    }

    // Add the key,value pair to the data structure and increase the number of keys.
    // If key is null, throw IllegalNullKeyException;
    // If key is already in data structure, throw DuplicateKeyException();
    @Override
    public void insert(String key, Book value)
        throws IllegalNullKeyException, DuplicateKeyException {
      //  System.out.println("Inserting: Key: " + key + " Book: " + value);
       
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        LinkedList<Book> bucket;// Create LinkedList reference
        Book book = value;
      /*  if(!book.getKey().equals(key)) {
            book = new Book(value);
            book.setKey(key);
        }*/
    
        int hashCode = key.hashCode() & 0x7FFFFFFF;// Calculate hashcode for key
        int hashIndex = hashCode % capacity;// Get hash index by modding hashcode with hashTable
                                            // size
        if (hashTable[hashIndex] == null) {
            // LinkedList at the hashIndex is not initialized need to create a new empty linked list
            bucket = new LinkedList<>();
        } else {
            // LinkedList at hashIndex is already initialized
            bucket = hashTable[hashIndex];
        }
        if (numKeys == 0) {
            // There are no keys stored in the hashtable, so just add the key to it
            bucket.add(book);
            hashTable[hashIndex] = bucket;
            numKeys++;
        } else {
            try {
                // Using get to search through hashtable to see if the key is already stored or not
                get(key);
                // If get does not throw a KeyNotFoundException, key is already in the hashtable
                throw new DuplicateKeyException();

            } catch (KeyNotFoundException e) {
                // KeyNotFoundException thrown by get method, key can be added to the hashTable
                bucket.add(book);
                hashTable[hashIndex] = bucket;
                numKeys++;
                if ((double) numKeys / capacity >= loadFactorThreshold) {
                    // Get all key values pairs stored in hash table
                    ArrayList<Book> books = this.getAllKeyValues();
                    // Increase the hash table capacity and reinsert keyValues
                    reHash(books);
                    System.out.println("Done Rehashing");
                }
            }
        }
    }

    /**
     * This method iterates through the hash tables and grabs all the key values pairs currently stored
     * And returns them in an ArrayList
     * @return keyValues - ArrayList<KeyValue> that contains all key values pairs 
     */
    private ArrayList<Book> getAllKeyValues() {
        ArrayList<Book> books = new ArrayList<Book>();
        for (int i = 0; i < hashTable.length; i++) {
            LinkedList<Book> bucket = hashTable[i];
            if (bucket == null) {
                continue;
            } else {
                for (int j = 0; j < bucket.size(); j++) {
                    books.add(bucket.get(j));
                }
            }
        }
        return books;
    }

    /**
     * This method is used to update the hash table when the load factor => load factor threshold
     * It uses an ArrayList of all key value pairs currently stored in the hash table 
     * and reinserts them into a new hash table with a larger capacity = capacity*2 + 1.
     * @param keyValues- ArrayList of key values currently stored in hashTable 
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     */
    @SuppressWarnings("unchecked")
    private void reHash(ArrayList<Book> books)
        throws IllegalNullKeyException, DuplicateKeyException {
        System.out.println("Rehashing");
        this.capacity = capacity * 2 + 1;
        this.hashTable = new LinkedList[capacity];
        this.numKeys = 0;
        for (int j = 0; j < books.size(); j++) {
            insert(books.get(j).getKey(), books.get(j));
        }
    }

    // If key is found,
    // remove the key,value pair from the data structure
    // decrease number of keys.
    // return true
    // If key is null, throw IllegalNullKeyException
    // If key is not found, return false
    @Override
    public boolean remove(String key) throws IllegalNullKeyException {
       // System.out.println("Removing: Key: " + key);
        if (key == null) {
            throw new IllegalNullKeyException();
        }
        LinkedList<Book> bucket;// Create LinkedList Reference
        Book book;// Create KeyValue reference
        int hashCode = key.hashCode() & 0x7FFFFFFF;// Calculate hashcode for key
        int hashIndex = hashCode % hashTable.length;
        if (hashTable[hashIndex] == null) {
            // Nothing stored at that index in the hashtable yet
            return false;
        } else {
            bucket = hashTable[hashIndex];
            if (bucket.size() == 0) {
                // LinkedList is empty no keys stored
                return false;
            }
        }
        // Linked List not empty need to search through the list and remove key if its present
        for (int i = 0; i < bucket.size(); i++) {
            book = bucket.get(i);
            if (key.equals(book.getKey())) {
                bucket.remove(book);
                numKeys--;
                return true;
            }
        }
        // Key was not found in the linked list, so nothing was removed
        return false;
    }

    // Returns the value associated with the specified key
    // Does not remove key or decrease number of keys
    //
    // If key is null, throw IllegalNullKeyException
    // If key is not found, throw KeyNotFoundException().
    @Override
    public Book get(String key) throws IllegalNullKeyException, KeyNotFoundException {
       
        if (key == null) {
            // If key is null throw exception
            throw new IllegalNullKeyException();
        }

        LinkedList<Book> bucket;// Create LinkedList Reference
        int hashCode = key.hashCode() & 0x7FFFFFFF;// Calculate hashcode for key
        int hashIndex = hashCode % capacity;
        if (hashTable[hashIndex] == null) {
            // Nothing stored at that index in the hashtable yet
            throw new KeyNotFoundException();
        } else {
            bucket = hashTable[hashIndex];
            if (bucket.size() == 0) {
                // LinkedList is empty no keys stored
                throw new KeyNotFoundException();
            }
        }
        // LinkedList is not empty, iterate through the list to try and find key
        Book book;
        for (int i = 0; i < bucket.size(); i++) {
            book = bucket.get(i);
            if (key.equals(book.getKey())) {
                // key is found in the LinkedList
                return book;
            }
        }
        // After iterating through list key was not found
        throw new KeyNotFoundException();
    }


    /*
     * Returns the number of key value pairs stored in the hash table
     */
    @Override
    public int numKeys() {
        return numKeys;
    }

    /**
     * Returns the load factor threshold(number that causes resizing) of the hash table
     */
    @Override
    public double getLoadFactorThreshold() {
        return loadFactorThreshold;
    }

    /**
     * Returns the current capacity of the hash table
     */
    @Override
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the collision resolution scheme used for this hash table.
     * 5 CHAINED BUCKET: array list of linked lists
     */
    @Override
    public int getCollisionResolutionScheme() {
        return 5;
    }


    public static void main(String[] args) {
        try {
            ArrayList<Book> bookTable = BookParser.parse("books.csv");
            System.out.println("From CSV:" + bookTable.get(0).toString());
            System.out.println("From CSV:" + bookTable.get(1).toString());
            BookHashTableNoInnerClass hashTable = new BookHashTableNoInnerClass(11, .7);
            hashTable.insert(bookTable.get(0).getKey(), bookTable.get(0));
            hashTable.insert(bookTable.get(1).getKey(), bookTable.get(1));
            hashTable.insert(bookTable.get(2).getKey(), bookTable.get(2));
            System.out.println(bookTable.get(1).getKey());
            System.out.println(bookTable.get(2).getKey());
            System.out.println(bookTable.get(3).getKey());
           
  
  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
