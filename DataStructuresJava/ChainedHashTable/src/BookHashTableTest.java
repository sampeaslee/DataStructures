/**
 * Filename:   BookHashTableTest.java
 * Project:    p3a
 * Authors:    Sam Peaslee (speaslee@wisc.edu)
 * 
 * Semester:   Fall 2019
 * Course:     CS400
 * Description: Junit tests to check functionality of
 * BookHashTable implementation
 */

import org.junit.After;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/** 
 * Test HashTable class implementation to ensure that required 
 * functionality works for all cases.
 */
public class BookHashTableTest {

    // Default name of books data file
    public static final String BOOKS = "books.csv";

    // Empty hash tables that can be used by tests
    static BookHashTable bookObject;
    static ArrayList<Book> bookTable;

    static final int INIT_CAPACITY = 2;
    static final double LOAD_FACTOR_THRESHOLD = 0.49;
       
    static Random RNG = new Random(0);//seeded to make results repeatable

    /** Create a large array of keys and matching values for use in any test */
    @BeforeAll
    public static void beforeClass() throws Exception{
        bookTable = BookParser.parse(BOOKS);
    }
    
    /** Initialize empty hash table to be used in each test */
    @BeforeEach
    public void setUp() throws Exception {
         bookObject = new BookHashTable(INIT_CAPACITY,LOAD_FACTOR_THRESHOLD);
    }

    /** Not much to do, just make sure that variables are reset     */
    @AfterEach
    public void tearDown() throws Exception {
        bookObject = null;
    }
    /**
     * Inserts a specified amount of key values pairs into a 
     * BookHashtable object from an ArrayList of books
     * @param bookTable
     * @param j
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     */
    private void insertMany(ArrayList<Book> bookTable, int j) 
        throws IllegalNullKeyException, DuplicateKeyException {
        for (int i = 0; i < j; i++ ) {
            bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
        }
    }
    /**
     * Removes a specified amount of key values pairs from a 
     * BookHashtable object from an ArrayList of books
     * @param bookTable
     * @param j
     * @throws IllegalNullKeyException
     */
    private void removeMany(ArrayList<Book> bookTable, int j) 
        throws IllegalNullKeyException {
        for (int i = 0; i < j; i++ ) {
            bookObject.remove(bookTable.get(i).getKey());
        } 
    }

    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is empty upon initialization
     */
    
    @Test
    public void test000_collision_scheme() {
        if (bookObject == null)
        	fail("Gg");
    	int scheme = bookObject.getCollisionResolutionScheme();
        if (scheme < 1 || scheme > 9) 
            fail("collision resolution must be indicated with 1-9");
    }
    

    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is empty upon initialization
     */
    @Test
    public void test000_IsEmpty() {
        //"size with 0 entries:"
        assertEquals(0, bookObject.numKeys());
    }

    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is not empty after adding one (key,book) pair
     * @throws DuplicateKeyException 
     * @throws IllegalNullKeyException 
     */
    @Test
    public void test001_IsNotEmpty() throws IllegalNullKeyException,
    DuplicateKeyException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
        String expected = ""+1;
        //"size with one entry:"
        assertEquals(expected, ""+bookObject.numKeys());
    }
    
    /** IMPLEMENTED AS EXAMPLE FOR YOU 
    * Test if the hash table  will be resized after adding two (key,book) pairs
    * given the load factor is 0.49 and initial capacity to be 2.
    */
    
    @Test 
    public void test002_Resize() throws IllegalNullKeyException, 
    DuplicateKeyException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
    	int cap1 = bookObject.getCapacity(); 
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
    	int cap2 = bookObject.getCapacity(); 
        //"size with one entry:"
        assertTrue(cap2 > cap1 & cap1 ==2);
    }
    
    /**
     * Test to make sure that insert rehashes(re inserts) the key value
     * pairs when a resizing happens
     */
    @Test
    public void test003_Resize_check_keys_were_reinserted()
        throws DuplicateKeyException, IllegalNullKeyException,
    KeyNotFoundException {
        bookObject = new BookHashTable(20,.8);
        insertMany(bookTable,17);
        if(bookObject.getCapacity() != 41) {
            fail();
        }
        for(int i = 0; i < bookObject.numKeys(); i++) {
            if(!bookObject.get(bookTable.get(i).getKey()).
                equals(bookTable.get(i))) {
                fail();
            }
        }
    }
    /**
     * Test to make sure that insert rehashes(re inserts) the key value pairs
     *  when multiple resizing happens. Also test that capacity was updated 
     *  correctly capacity after a resizing is capacity*2 + 1 
     */
    @Test
    public void test004_Multiple_resizes_check_keys_were_reinserted()
        throws DuplicateKeyException, IllegalNullKeyException,
        KeyNotFoundException {
        bookObject = new BookHashTable(200,.8);
        insertMany(bookTable,350);    
        if(bookObject.getCapacity() !=803) {
            fail();
        }
        for(int i = 0; i < bookObject.numKeys(); i++) {
            if(!bookObject.get(bookTable.get(i).getKey()).
                equals(bookTable.get(i))) {
                fail();
            }
        }
    }
    /*
     * Test that insert method throws the proper exceptions 
     */
    @Test 
    public void test005_insert_throws_correct_exceptions() {
        try {
            bookObject.insert(null, bookTable.get(0));
            fail("IllegalNullKeyException NOT THROWN!!!");  
        }catch(IllegalNullKeyException e) {
            
        }catch(DuplicateKeyException e) {
            fail("DuplicateKeyExcepion should NOT Have been thrown");
        }
        try {
            bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
            bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
            bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
            fail("DuplicateKeyException NOT THROWN!!!");  
        }catch(IllegalNullKeyException e) {
            fail("IllegalNullKeyException should NOT Have been thrown");
        }catch(DuplicateKeyException e) {
            
        }
    }
    /*
     * Test that get throws the proper exceptions
     */
    
    @Test 
    public void test006_get_throws_correct_exceptions()
        throws DuplicateKeyException {
        try {
            bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
            bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
            bookObject.insert(bookTable.get(2).getKey(),bookTable.get(2));
            bookObject.get(null);
            fail("IllegalNullKeyExceptions Should have been thrown");
        }catch(IllegalNullKeyException e) {
            
        }catch(KeyNotFoundException e) {
            fail("KeyNotFoundException should NOT Have been Thrown");
        }
        try {
            bookObject.get(bookTable.get(4).getKey());      
            fail("KeyNotFoundException NOT THROWN!!!");
        }catch(IllegalNullKeyException e) {
            fail("IllegalNullKeyException should NOT Have been Thrown");
        }catch(KeyNotFoundException e) {
    
        }
    }
    
    /*
     * Test that remove throws exception when null key entered as argument 
     */
    @Test
    public void test007_remove_throws_IllegalNullKeyException() {
        try {
           bookObject.remove(null);
           fail("Exception not thrown");
        }catch(IllegalNullKeyException e) {
        }
        
    }
    /*
     * Test that remove return true when a key value pair is removed
     * and false when its not removed
     */
    
    @Test
    public void test008_remove_returns_correct_boolean() 
       throws  IllegalNullKeyException, DuplicateKeyException{
       insertMany(bookTable, 20);
       if(!bookObject.remove(bookTable.get(15).getKey())) {
           fail();
       }
       if(bookObject.remove(bookTable.get(15).getKey())) {
           fail();
       }
       if(bookObject.remove(bookTable.get(115).getKey())) {
           fail();
       }
    }
    
    /*
     * Insert 1000 keys values and then test that get returns
     *  all 1000 values correctly 
     */
    @Test
    public void test009_insert_1000_keys_get_1000_values() 
        throws DuplicateKeyException, IllegalNullKeyException, 
        KeyNotFoundException {
        bookObject = new BookHashTable(101,.8);
        insertMany(bookTable, 1000);
        for(int i = 0; i < bookObject.numKeys(); i ++) {
            if(!bookObject.get(bookTable.get(i).getKey()).
                equals(bookTable.get(i))){
                fail();
            }
        }
    }
    /**
     * Insert the same value with two different key 
     */
    @Test
    public void test010_different_keys_same_value()
        throws DuplicateKeyException,
        IllegalNullKeyException, KeyNotFoundException {
        bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
        bookObject.insert(bookTable.get(1).getKey(), bookTable.get(0));
        if(!bookObject.get(bookTable.get(0).getKey()).
            equals(bookTable.get(0))){
            fail();
        }
        if(!bookObject.get(bookTable.get(1).getKey()).
            equals(bookTable.get(0))){
            fail();
        }
  
    }
    
    /*
     * Test that remove correctly removes the key value pair 
     * Insert key, get key, remove key. 
     * Then try to get key again and check if KeyNotFoundException was thrown 
     */
    @Test void test011_insert_key_get_value_delete_key_get_value()
    throws DuplicateKeyException, IllegalNullKeyException{
       try {
        bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
        bookObject.get(bookTable.get(0).getKey());  
        bookObject.remove(bookTable.get(0).getKey());
       }catch(KeyNotFoundException e) {
           fail("Key should have been found");
       }
       try {
           bookObject.get(bookTable.get(0).getKey());  
           fail("Get should have thrown a key not found exception");
          }catch(KeyNotFoundException e) {
             
          }
    }

    /*
     * Insert 20 key value pairs 
     * Check that get returns all 20 values 
     * Then delete all 20 keys 
     * Check that get throws a KeyNotFoundException for all 20 keys
     */
    @Test void test012_insert_20_keys_remove_20_keys() 
        throws DuplicateKeyException, IllegalNullKeyException{
       try {
        bookObject = new BookHashTable(101,1.0);
        insertMany(bookTable, 20);
        for(int i = 0; i < bookObject.numKeys(); i ++) {
           bookObject.get(bookTable.get(i).getKey());      
        }
       }catch(KeyNotFoundException e) {
           fail("Keys should have been found");
        }
     
        for(int i = 0; i < 20; i ++) {
            bookObject.remove(bookTable.get(i).getKey());
        try {    
            bookObject.get(bookTable.get(i).getKey()); 
            fail("KeyNotFoundExceptions should have been thrown, "
                + "remove not working correctly");
            }catch(KeyNotFoundException e) {
            }
        }      
    }
    /*
     * Insert 20 key value pairs 
     * Then delete all twenty keys 
     * Then insert the same 20 keys and check if there
     * are stored in the hash table
     */
    @Test void test013_insert_20_keys_remove_20_keys_insert_20_again()
        throws DuplicateKeyException, IllegalNullKeyException{
       try {
        bookObject = new BookHashTable(101,1.0);
        insertMany(bookTable, 20);  
        removeMany(bookTable,20);
        insertMany(bookTable, 20);
        for(int i = 0; i < bookObject.numKeys(); i ++) {
            bookObject.get(bookTable.get(i).getKey());      
         }
       }catch(KeyNotFoundException e) {
           fail("Keys should have been found");
        }
     
        
    }


    /*
     * Test that numKeys returns the right amount of keys after multiple 
     * inserts and removes
     */
    @Test
    public void test014_check_numKeys_after_mutiple_inserts_and_deletes()
    throws Exception{
        insertMany(bookTable,50);
        if(bookObject.numKeys() != 50) {    
            fail();
        }
       removeMany(bookTable,50);         
       if(bookObject.numKeys() != 0) {    
           fail();
       }
       insertMany(bookTable,50);
       if(bookObject.numKeys() != 50) {    
           fail();
       }        
    }
    /**
     * Check behavior of get on empty hash table
     * KeyNotFoundException should be thrown
     */
    
    @Test
    public void test015_get_empty_hash_table_functionality() {
        try {
            bookObject.get(bookTable.get(0).getKey());
            fail("KeyNotFoundException not thrown");
        }catch( KeyNotFoundException e){            
        }catch(IllegalNullKeyException e) {
            fail();
        }
    }
    /**
     * Insert 50 keys, then call get on all 50 keys twice in a row
     * to make sure the keys are still in the hash table
     */
    @Test
    public void test016_get_does_not_remove_key()
        throws IllegalNullKeyException, DuplicateKeyException{
        try {
            insertMany(bookTable, 50);
            for(int i = 0; i < bookObject.numKeys(); i ++) {
                bookObject.get(bookTable.get(i).getKey());      
             }      
            for(int i = 0; i < bookObject.numKeys(); i ++) {
                 bookObject.get(bookTable.get(i).getKey());      
             }  
        }catch( KeyNotFoundException e){   
            fail();
        }catch(IllegalNullKeyException e) {
            fail();
        }
    }
    /*
     * Test that getLoadFactorThreshold returns correct value
     */
    @Test
    public void test017_loadThreshold_initialized_correctly() {
        if(bookObject.getLoadFactorThreshold() != .49) {
            fail();
        }
        bookObject = new BookHashTable(10, .8);
        if(bookObject.getLoadFactorThreshold() != .8) {
            fail();
        }
    }
    /*
     * Test that getCapacity returns correct value
     */
    @Test
    public void test018_capacity_initialized_correctly() {
        if(bookObject.getCapacity() != 2) {
            fail();
        }
        bookObject = new BookHashTable(10, .8);
        if(bookObject.getCapacity() != 10) {
            fail();
        }
    }

}

