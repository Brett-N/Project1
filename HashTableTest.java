import static org.junit.Assert.assertNotEquals;
import student.TestCase;

/**
 * Test class for my HashTable design
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class HashTableTest extends TestCase {
    private HashTable tableUnderTest;

    @Override
    public void setUp() {
        tableUnderTest = new HashTable(4);
    }
    
    /**
     * Test method for get()
     */
    public void testGet() {
        tableUnderTest.addEntry(1, new Handle(10, 10));
        Handle h = tableUnderTest.retrieve(1);
        assertEquals(10, h.getStartPosition());

        assertNull(tableUnderTest.retrieve(2));
        tableUnderTest.deleteEntry(1);
        assertNull(tableUnderTest.retrieve(1));

        tableUnderTest.addEntry(2, new Handle(10, 10));
        assertNull(tableUnderTest.retrieve(3));

        int x = 1;
        int y = 4 + (x % 4);

        assertTrue(tableUnderTest.addEntry(x, new Handle(10, 10)));
        assertFalse(tableUnderTest.addEntry(x, new Handle(20, 20)));
        tableUnderTest.displayHash();

        tableUnderTest.deleteEntry(x);
        assertTrue(tableUnderTest.addEntry(y, new Handle(10, 10)));
        assertFalse(tableUnderTest.addEntry(y, new Handle(20, 20)));
        tableUnderTest.displayHash();

        Handle handleY = tableUnderTest.retrieve(y);
        assertEquals(10, handleY.getStartPosition());

        HashTable anotherTable = new HashTable(4);
        for (int i = 0; i < 4; i++) {
            assertTrue(anotherTable.addEntry(i, new Handle(10, 10)));
        }
        assertTrue(anotherTable.deleteEntry(3));
        assertNull(anotherTable.retrieve(3));

        Handle h2 = anotherTable.retrieve(2);
        assertEquals(anotherTable.retrieve(2), h2);

        Exception ex;
        try {
            anotherTable.retrieve(-1);
        } 
        catch (Exception e) {
            ex = e;
            assertNotNull(ex);
        }
    }
    
    /**
     * Test method for deleteEntry()
     */
    public void testRemoval() {
        for (int i = 0; i < 3; i++) {
            tableUnderTest.addEntry(i, new Handle(10, 10));
            assertTrue(tableUnderTest.deleteEntry(i));
            assertFalse(tableUnderTest.deleteEntry(i));
        }

        for (int i = 0; i < 5; i++) {
            assertTrue(tableUnderTest.addEntry(i, new Handle(1, 1)));
        }

        assertTrue(tableUnderTest.deleteEntry(2));
        assertTrue(tableUnderTest.deleteEntry(4));
    }
    
    
    /**
     * Test method to make sure the table is being resized
     */
    public void testResizing() {
        int threshold = (4 / 2) + 1;
        for (int i = 0; i < threshold; i++) {
            assertTrue(tableUnderTest.addEntry(i, new Handle(10, 10)));
            assertFalse(tableUnderTest.addEntry(i, new Handle(10, 10)));
        }

        tableUnderTest = new HashTable(4);
        for (int i = 0; i < threshold; i++) {
            assertTrue(tableUnderTest.addEntry(i, new Handle(10, 10)));
        }

        tableUnderTest.deleteEntry(1);

        HashTable differentTable = new HashTable(4);
        for (int i = 0; i < 4; i++) {
            differentTable.addEntry(i, new Handle(10, 10));
        }
        differentTable.deleteEntry(1);

        assertEquals(differentTable.limit(), 8);
        assertNull(differentTable.retrieve(1));
    }
    
    
    /**
     * tests addEntry()
     */
    public void testInsertOps() {
        for (int i = 1; i <= 5; i++) {
            assertTrue(tableUnderTest.addEntry(i, new Handle(10, 10)));
            if (i != 1) {
                assertFalse(tableUnderTest.addEntry(1, new Handle(20, 20)));
            }
        }
        assertTrue(tableUnderTest.deleteEntry(3));
        assertTrue(tableUnderTest.addEntry(3, new Handle(10, 10)));
    }
    
    
    /**
     * Tests collision handling for my table
     */
    public void testCollisionHandling() {
        int x = 1;
        int y = 4 + (x % 4);

        assertTrue(tableUnderTest.addEntry(x, new Handle(10, 10)));
        assertFalse(tableUnderTest.addEntry(x, new Handle(20, 20)));

        tableUnderTest.deleteEntry(x);
        assertTrue(tableUnderTest.addEntry(y, new Handle(10, 10)));
        assertFalse(tableUnderTest.addEntry(y, new Handle(20, 20)));
        tableUnderTest.displayHash();
    }
    
    
    /**
     * tests the displayHash() method
     */
    public void testPrinting() {
        for (int i = 1; i <= 3; i++) {
            tableUnderTest.addEntry(i, new Handle(i * 10, i * 10));
        }
        tableUnderTest.deleteEntry(3);

        systemOut().clearHistory();
        tableUnderTest.displayHash();

        String expectedOutput = "Hashtable:\n1: 1\n2: 2\n3: "
            + "TOMBSTONE\ntotal records: 2\n";
        assertEquals(expectedOutput, systemOut().getHistory());
    }
    
    /**
     * Tests various functions for my table
     */
    public void testHashFunctions() {
        for (int i = 0; i < 4; i++) {
            assertEquals(true, tableUnderTest.addEntry(
                i * 5, new Handle(10, 10)));
        }

        HashTable anotherTable = new HashTable(0);
        Exception ex;
        try {
            anotherTable.addEntry(0, new Handle(10, 10));
        } 
        catch (ArithmeticException e) {
            ex = e;
            assertNotNull(ex);
        }

        HashTable newTable = new HashTable(2);
        Handle h1 = new Handle(10, 10);
        Handle h2 = new Handle(5, 5);

        assertNotNull(newTable.addEntry(0, h1));
        assertEquals(h1, newTable.retrieve(0));

        assertNotNull(newTable.addEntry(1, h2));
        assertEquals(h1, newTable.retrieve(0));
    }
    
    /**
     * Tests adding an entry with a negative ID
     */
    public void testAddEntryWithNegativeID() {
        assertFalse(tableUnderTest.addEntry(-5, new Handle(10, 10)));
    }
    
    
    /**
     * Tests adding an entry with a Null entry spot
     */
    public void testAddEntryWithNullEntrySpot() {
        assertTrue(tableUnderTest.addEntry(
            100, new Handle(10, 10)));
    }
    
    /**
     * Tests my second hash calculation
     */
    public void testSecondaryHashCalculation() {
        HashTable customTable = new HashTable(6); 
        customTable.addEntry(100, new Handle(10, 10)); 
    }
    
    /**
     * Tests addEntry()
     */
    public void testSearchPositionContinuation() {
        tableUnderTest.addEntry(1, new Handle(10, 10));
        tableUnderTest.addEntry(5, new Handle(20, 20)); 
    }
    
    /**
     * Tests retrieve()
     */
    public void testLocateEntryLoopContinuation() {
        tableUnderTest.addEntry(1, new Handle(10, 10));
        tableUnderTest.addEntry(5, new Handle(20, 20));
        tableUnderTest.retrieve(5);
    }
    
    /**
     * Tests adding a null entry
     */
    public void testAddEntryWithNullEntry() {
        HashTable hashTable = new HashTable(10);
        Handle handle = new Handle(0, 5);
        assertTrue(hashTable.addEntry(15, handle));
    }
    
    /**
     * Tests my secondary hash method
     */
    public void testSecondaryHash() {
        HashTable hashTable = new HashTable(10);
        int hash = hashTable.primaryHash(30);
        int secondary = hashTable.secondaryHash(30);
        assertNotEquals(hash, secondary);
    }

    /**
     * Tests searchPos()
     */
    public void testSearchPositionWithMarkedEntry() {
        HashTable hashTable = new HashTable(10);
        Handle handle1 = new Handle(0, 5);
        Handle handle2 = new Handle(5, 5);

        assertTrue(hashTable.addEntry(15, handle1));
        assertTrue(hashTable.deleteEntry(15));
        int searchPos = hashTable.searchPosition(15);
        assertNotEquals(-1, searchPos);
        assertTrue(hashTable.addEntry(15, handle2));
    }

    /**
     * Tests equals()
     */
    public void testHandleEquals() {
        Handle handle1 = new Handle(0, 5);
        Handle handle2 = new Handle(0, 5);
        Handle handle3 = new Handle(1, 6);
        
        assertTrue(handle1.equals(handle2));
        assertFalse(handle1.equals(handle3));
    }
    
    /**
     *  test method for my calculation
     */
    public void testSecondaryHashwhenKIsLessThanSize() {
        HashTable hashTable = new HashTable(10);
        int result = hashTable.secondaryHash(5);
        // Expected: (5 / 10) % 5 = 0
        // 0 * 2 + 1 = 1
        assertEquals(1, result);
    }

    /**
     * Another test method for my calculation
     */
    public void testSecondaryHashwhenKIsEqualToSize() {
        HashTable hashTable = new HashTable(10);
        int result = hashTable.secondaryHash(10);
        // Expected: (10 / 10) % 5 = 1 % 5 = 1
        // 1 * 2 + 1 = 3
        assertEquals(3, result);
    }

    /**
     * Another test method for my calculation
     */
    public void testSecondaryHashwhenKIsGreaterThanSize() {
        HashTable hashTable = new HashTable(10);
        int result = hashTable.secondaryHash(15);
        // Expected: (15 / 10) % 5 = 1 % 5 = 1
        // 1 * 2 + 1 = 3
        assertEquals(3, result);
    }

    /**
     * Another test method for my calculation
     */
    public void testSecondaryHashwhenKProducesModuloGreaterThanHalfSize() {
        HashTable hashTable = new HashTable(10);
        int result = hashTable.secondaryHash(55);
        // Expected: (55 / 10) % 5 = 5 % 5 = 0
        // 0 * 2 + 1 = 1
        assertEquals(1, result);
    }

}