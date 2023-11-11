import student.TestCase;

/**
 * Test class for my MemoryManager
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class MemoryManagerTest extends student.TestCase {

    private MemoryManager manager;
    private MemoryManager manager2;

    /**
     * Initializes common test fixtures before each test.
     */
    @Override
    public void setUp() {
        manager = new MemoryManager(64);
    }

    /**
     * Tests the constructor's validity by 
     * ensuring the created instance is not null.
     */
    public void testConstructorValidity() {
        assertNotNull(manager);
    }

    /**
     * Tests a specific memory management scenario 
     * involving multiple inserts and removals.
     */
    public void testMemoryManagementScenarioOne() {
        MemoryManager localMemManager = new MemoryManager(4096);
        Handle[] handles = new Handle[11];
        int[] sizes = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};

        for (int i = 0; i < sizes.length; i++) {
            handles[i] = localMemManager.insert(new byte[sizes[i]]);
        }

        System.out.println();
        for (Handle handle : handles) {
            localMemManager.remove(handle);
        }
    }

    /**
     * Tests another memory management scenario 
     * with multiple insert and remove operations,
     * and prints the state after each action.
     */
    public void testMemoryManagementScenarioTwo() {
        MemoryManager localMemManager = new MemoryManager(16);
        Handle handleOne = localMemManager.insert(
            new byte[] { 1, 2, 3 });
        Handle handleTwo = localMemManager.insert(
            new byte[] { 4, 5, 6, 7, 8 });
        Handle handleThree = localMemManager.insert(
            new byte[] { 9, 10, 11, 12 });
        System.out.println("After 3 inserts:");
        localMemManager.dump();

        localMemManager.remove(handleTwo);

        System.out.println("After removing 2nd block:");
        localMemManager.dump();

        Handle handleFour = localMemManager.insert(
            new byte[] { 13, 14, 15 });

        System.out.println("After 4th insert:");
        localMemManager.dump();

        System.out.println(
            "Data at handleOne: " + java.util.Arrays.toString(
                localMemManager.get(handleOne)));
        System.out.println(
            "Data at handleThree: " + java.util.Arrays.toString(
                localMemManager.get(handleThree)));
        System.out.println(
            "Data at handleFour: " + java.util.Arrays.toString(
                localMemManager.get(handleFour)));
    }

    /**
     * Tests the insert method by ensuring 
     * non-null handles are returned for data inserts.
     */
    public void testInsertMethod() {
        byte[] sampleDataOne = new byte[32];
        byte[] sampleDataTwo = new byte[40];
        byte[] sampleDataThree = new byte[1];

        Handle handleOne = manager.insert(
            sampleDataOne);
        Handle handleTwo = manager.insert(
            sampleDataTwo);
        Handle handleThree = manager.insert(
            sampleDataThree);
            
        assertNotNull(handleOne);
        assertNotNull(handleTwo);
        assertNotNull(handleThree);
    }

    /**
     * Tests the get method by ensuring data 
     * retrieval yields correct length arrays.
     */
    public void testGetMethod() {
        byte[] sampleDataOne = new byte[32];
        byte[] sampleDataTwo = new byte[40];
        
        Handle handleOne = manager.insert(
            sampleDataOne);
        Handle handleTwo = manager.insert(
            sampleDataTwo);

        assertEquals(32, manager.get(
            handleOne).length);
        assertEquals(40, manager.get(
            handleTwo).length);
    }

    /**
     * Tests the remove method by ensuring that 
     * data can be effectively removed from memory.
     */
    public void testRemoveMethod() {
        manager2 = new MemoryManager(32);
        byte[] open = new byte[32];
        Handle handle1 = manager2.insert(open);
        byte[] open2 = new byte[2];
        Handle handleTwo = manager2.insert(open2);
        
        assertEquals(manager2.get(handleTwo).length, 2);
        manager2.remove(handle1);

        assertEquals(0, manager2.get(handle1).length);
    }

    /**
     * Tests the dump method by checking the printout of memory state.
     */
    public void testDumpMemory() {
        manager.dump();
        assertEquals("Freeblock List:\n64: 0\n", systemOut().getHistory());
        
        byte[] sampleDataOne = new byte[64];
        manager.insert(sampleDataOne);

        systemOut().clearHistory();
        manager.dump();

        assertEquals(
            "Freeblock List:\nThere are no freeblocks in the memory pool\n",
            systemOut().getHistory()
        );
    }
    
    /**
     * Tests the expand method with specific 
     * conditions to ensure complete coverage.
     */
    public void testExpandScenario() {
        MemoryManager localMemManager = new MemoryManager(4); 
        Handle handle1 = localMemManager.insert(new byte[3]); 
        Handle handle2 = localMemManager.insert(new byte[3]); 
        assertNotNull(handle1);
        assertNotNull(handle2);
    }

    /**
     * Tests insert method with conditions to 
     * ensure free blocks are correctly split.
     */
    public void testInsertWithSplitScenario() {
        MemoryManager localMemManager = new MemoryManager(8);
        Handle handle1 = localMemManager.insert(new byte[3]);
        Handle handle2 = localMemManager.insert(new byte[2]);
        
        assertNotNull(handle1);
        assertNotNull(handle2);
    }
    
    /**
     * Test method for findFreeBlockList()
     */
    public void testFindFreeBlockList() {
        // Using an initial size of 64 for simplicity
        MemoryManager mm = new MemoryManager(64);

        // create some segments in different lists
        mm.getFreeBlocksLists()[1].append(new MemorySegment(0, 2));
        mm.getFreeBlocksLists()[2].append(new MemorySegment(2, 4));
        mm.getFreeBlocksLists()[3].append(new MemorySegment(6, 8));
        mm.getFreeBlocksLists()[4].append(new MemorySegment(14, 16));
        // Skipping the last list (5) on purpose

        // Test with different sizes:
        assertEquals(1, mm.findFreeBlockList(2));  
        assertEquals(2, mm.findFreeBlockList(3));  
        assertEquals(2, mm.findFreeBlockList(4));  
        assertEquals(3, mm.findFreeBlockList(5));  
        assertEquals(3, mm.findFreeBlockList(6));  
        assertEquals(3, mm.findFreeBlockList(7));  
        assertEquals(3, mm.findFreeBlockList(8));  
        assertEquals(4, mm.findFreeBlockList(9));  
        // Skipping further checks

        // Clean up by removing segments
        mm.getFreeBlocksLists()[1].removeFirst();
        mm.getFreeBlocksLists()[2].removeFirst();
        mm.getFreeBlocksLists()[3].removeFirst();
        mm.getFreeBlocksLists()[4].removeFirst();
    }
    
    /**
     * Tests inserting null data into the memory manager.
     */
    public void testInsertWithNullData() {
        MemoryManager mm = new MemoryManager(32);
        assertNull(mm.insert(null));
    }
    
    /**
     * Tests inserting data of suitable block size into the memory manager.
     */
    public void testInsertSuitableBlockSize() {
        MemoryManager mm = new MemoryManager(32);
        byte[] data = new byte[5];
        Handle h = mm.insert(data);
        assertNotNull(h);
    }
    
    /**
     * Tests insertion when there is insufficient space in the memory manager.
     */
    public void testIsInsufficientSpace() {
        MemoryManager mm = new MemoryManager(8);
        byte[] data = new byte[10];
        Handle h = mm.insert(data);
        assertNotNull(h);
    }
    
    /**
     * Tests the split functionality of the memory manager.
     */
    public void testSplit() {
        MemoryManager mm = new MemoryManager(32);
        byte[] data = new byte[8];
        mm.insert(data);
        assertEquals(24, mm.getTotalFreeSpace());
    }
    
    /**
     * Tests retrieving the total free space available in the memory manager.
     */
    public void testGetTotalFreeSpace() {
        MemoryManager mm = new MemoryManager(32);
        assertEquals(32, mm.getTotalFreeSpace());
    }
    
    /**
     * Tests resizing the memory pool of the memory manager.
     */
    public void testResizeMemoryPool() {
        MemoryManager mm = new MemoryManager(16);
        byte[] data = new byte[20];
        mm.insert(data);
        assertEquals(0, mm.getTotalFreeSpace());
    }
    
    /**
     * Tests finding the adjusted block size in the memory manager.
     */
    public void testFindAdjustedBlockSize() {
        MemoryManager mm = new MemoryManager(32);
        assertEquals(16, mm.findAdjustedBlockSize(9));
    }
    
    
}