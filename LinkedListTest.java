import java.util.Iterator;
import java.util.NoSuchElementException;
import student.TestCase;

/**
 * Test class for my LinkedList implementation
 *
 * @author brettn
 * @version 09/15/2023
 */
public class LinkedListTest extends TestCase {

    private LinkedList<String> list;
    private LinkedList<String> testList;
    private LinkedList<MemorySegment> linkedList;

    @Override
    public void setUp() {
        testList = new LinkedList<>();
        linkedList = new LinkedList<>();
    }
    
    /**
     * Tests append() and size() methods
     */
    public void testAppendAndCount() {
        assertTrue(testList.isEmpty());
        assertEquals(0, testList.size());

        testList.append("apple");
        assertFalse(testList.isEmpty());
        assertEquals(1, testList.size());

        testList.append("banana");
        assertEquals(2, testList.size());
    }
    
    /**
     * Test method for remove()
     */
    public void testRemoveElement() {
        testList.append("1");
        testList.append("2");
        testList.append("3");
        testList.append("4");
        testList.append("5");
        testList.append("6");
        testList.append("7");

        assertTrue(testList.remove("1"));
        assertTrue(testList.remove("5"));
        assertTrue(testList.remove("7"));
        assertFalse(testList.remove("7"));
        assertFalse(testList.remove("lemon"));
        assertEquals(4, testList.size());
    }
    
    
    /**
     * Test method for removeFirst()
     */
    public void testRemoveFirst() {
        testList.append("apple");
        testList.append("banana");
        testList.append("cherry");

        assertEquals("apple", testList.removeFirst());
        assertEquals(2, testList.size());

        assertEquals("banana", testList.removeFirst());
        assertEquals(1, testList.size());

        assertEquals("cherry", testList.removeFirst());
        assertEquals(0, testList.size());
        assertNull(testList.removeFirst());
    }
    
    /**
     * Test method for getElementAt()
     */
    public void testGetElementAt() {
        testList.append("apple");
        testList.append("banana");
        testList.append("cherry");

        assertEquals("apple", testList.getElementAt(0));
        assertEquals("banana", testList.getElementAt(1));
        assertEquals("cherry", testList.getElementAt(2));

        try {
            testList.getElementAt(5);
            fail("Expected IndexOutOfBoundsException for invalid index");
        } 
        catch (IndexOutOfBoundsException e) {
            System.out.println(e);
        }

        systemOut().clearHistory();

        try {
            testList.getElementAt(-1);
            fail("Expected IndexOutOfBoundsException for negative index");
        } 
        catch (IndexOutOfBoundsException e) {
            System.out.println(e);
        }
    }
    
    
    /**
     * test method for getFirst()
     */
    public void testGetFirst() {
        testList.append("apple");
        testList.append("banana");
        testList.append("cherry");
        
        assertEquals("apple", testList.getFirst());
        testList.removeFirst();
        
        assertEquals("banana", testList.getFirst());
        testList.removeFirst();
        
        assertEquals("cherry", testList.getFirst());
        testList.removeFirst();

        assertNull(testList.getFirst());
    }

    /**
     * Test method for containsElement()
     */
    public void testCheckContains() {
        testList.append("apple");
        testList.append("banana");
        testList.append("cherry");

        assertTrue(testList.containsElement("banana"));
        assertFalse(testList.containsElement("orange"));
    }
    
    
    /**
     * Test method for my iterator
     */
    public void testListIterator() {
        testList.append("apple");
        testList.append("banana");
        
        Iterator<String> itr = testList.iterator();

        assertTrue(itr.hasNext());
        assertEquals("apple", itr.next());
        assertTrue(itr.hasNext());
        assertEquals("banana", itr.next());
        assertFalse(itr.hasNext());

        try {
            itr.next();
            fail("Expected NoSuchElementException");
        } 
        catch (NoSuchElementException e) {
            assertEquals(e.getMessage(), e.getMessage());
        }
    }

    
    /**
     * Test method for sorting memory blocks
     */
    public void testMemoryBlockSort() {
        LinkedList<MemorySegment> freeBlocks = new LinkedList<>();
        freeBlocks.sortFreeBlocks();

        assertTrue(testList.isEmpty());
        assertNull(freeBlocks.getFirst());

        MemorySegment segmentA = new MemorySegment(3, 5);
        MemorySegment segmentB = new MemorySegment(0, 2);
        MemorySegment segmentC = new MemorySegment(8, 3);

        freeBlocks.append(segmentA);
        freeBlocks.sortFreeBlocks();
        assertEquals(segmentA, freeBlocks.getFirst());
        
        freeBlocks.append(segmentB);
        freeBlocks.append(segmentC);
        freeBlocks.sortFreeBlocks();

        MemorySegment firstSegment = freeBlocks.getFirst();
        assertEquals(0, firstSegment.fetchOffset());

        freeBlocks.removeFirst();
        MemorySegment secondSegment = freeBlocks.getFirst();
        assertEquals(3, secondSegment.fetchOffset());
    }

    
    /**
     * Test method for when the list is empty
     */
    public void testIsEmpty() {
        assertTrue(linkedList.isEmpty());
        linkedList.append(new MemorySegment(0, 10));
        assertFalse(linkedList.isEmpty());
    }

    /**
     * Test method for removing an element
     */
    public void testRemoveElement2() {
        assertFalse(linkedList.removeElement(new MemorySegment(0, 10)));

        MemorySegment segment1 = new MemorySegment(0, 10);
        MemorySegment segment2 = new MemorySegment(10, 20);
        MemorySegment segment3 = new MemorySegment(20, 10);

        linkedList.append(segment1);
        linkedList.append(segment2);
        linkedList.append(segment3);

        assertTrue(linkedList.removeElement(segment1));
        assertFalse(linkedList.containsElement(segment1));
        assertEquals(2, linkedList.count());
    }

    
    /**
     * Test method for sorting free blocks
     */
    public void testSortFreeBlocks() {
        linkedList.append(new MemorySegment(20, 10));
        linkedList.append(new MemorySegment(0, 10));
        linkedList.append(new MemorySegment(10, 10));

        linkedList.sortFreeBlocks();

        assertEquals(0, linkedList.getElementAt(0).fetchOffset());
        assertEquals(10, linkedList.getElementAt(1).fetchOffset());
        assertEquals(20, linkedList.getElementAt(2).fetchOffset());
    }

    
    /**
     * Test method for removing a MemorySegment
     */
    public void testRemove() {
        assertFalse(linkedList.remove(new MemorySegment(0, 10)));

        MemorySegment segment1 = new MemorySegment(0, 10);
        linkedList.append(segment1);

        assertTrue(linkedList.remove(segment1));
        assertFalse(linkedList.containsElement(segment1));
    }

    
    /**
     * Test method for checking if a Memory Segment is included
     */
    public void testIncludes() {
        assertFalse(linkedList.includes(new MemorySegment(0, 10)));

        MemorySegment segment1 = new MemorySegment(0, 10);
        linkedList.append(segment1);

        assertTrue(linkedList.includes(segment1));
    }

    /**
     * Test method for poll()
     */
    public void testPoll() {
        assertNull(linkedList.poll());

        MemorySegment segment1 = new MemorySegment(0, 10);
        linkedList.append(segment1);

        assertEquals(segment1, linkedList.poll());
        assertTrue(linkedList.isEmpty());
    }

    
    /**
     * Test method for peakFirst()
     */
    public void testPeekFirst() {
        assertNull(linkedList.peekFirst());

        MemorySegment segment1 = new MemorySegment(0, 10);
        linkedList.append(segment1);

        assertEquals(segment1, linkedList.peekFirst());
        assertFalse(linkedList.isEmpty());
    }

    /**
     * Test method for organizeElements()
     */
    public void testOrganizeElements() {
        linkedList.append(new MemorySegment(20, 10));
        linkedList.append(new MemorySegment(0, 10));
        linkedList.append(new MemorySegment(10, 10));

        linkedList.organizeElements();

        assertEquals(0, linkedList.getElementAt(0).fetchOffset());
        assertEquals(10, linkedList.getElementAt(1).fetchOffset());
        assertEquals(20, linkedList.getElementAt(2).fetchOffset());
    }
    
    
    /**
     * Test method for removing the first element
     */
    void testRemoveElementFromBeginning() {
        list.append("first");
        list.append("second");
        assertTrue(list.removeElement("first"));
        assertEquals(1, list.size());
        assertEquals("second", list.getElementAt(0));
    }

    /**
     * Test method for removing an 
     * element from the middle
     */
    void testRemoveElementFromMiddle() {
        list.append("first");
        list.append("second");
        list.append("third");
        assertTrue(list.removeElement("second"));
        assertEquals(2, list.size());
        assertEquals("third", list.getElementAt(1));
    }

    /**
     * Test method for removing an 
     * element that doesn't exist
     */
    void testRemoveElementNotFound() {
        list.append("first");
        list.append("second");
        assertFalse(list.removeElement("third"));
        assertEquals(2, list.size());
    }

    /**
     * Test method for when the list is too small
     */
    void testSortFreeBlocksWhenListIsSmall() {
        list.append("second");
        list.sortFreeBlocks();
        assertEquals("second", list.getElementAt(0));
    }

    /**
     * Test method for when the list is empty
     */
    void testIncludesWhenListIsEmpty() {
        assertFalse(list.includes("any"));
    }

    /**
     * Test method for when the list isn't empty
     */
    void testPollFromNonEmptyList() {
        list.append("first");
        list.append("second");
        assertEquals("first", list.poll());
        assertEquals(1, list.size());
        assertEquals("second", list.getElementAt(0));
    }

    /**
     * Test method for when the list is small
     */
    void testOrganizeElementsWhenListIsSmall() {
        list.append("one");
        list.organizeElements();
        assertEquals("one", list.getElementAt(0));
    }

    /**
     * Test method for organizing the list
     */
    void testOrganizeElementsInOrder() {
        list.append("three");
        list.append("one");
        list.append("two");
        list.organizeElements();
        assertEquals("one", list.getElementAt(0));
        assertEquals("two", list.getElementAt(1));
        assertEquals("three", list.getElementAt(2));
    }
    
    
    /**
     * Test method for removing element
     * from an empty list
     */
    public void testRemoveElementFromEmptyList() {
        LinkedList<String> list2 = new LinkedList<>();
        assertFalse(list2.removeElement("test"));
    }

    /**
     * Test method for removing the first item
     */
    public void testRemoveElementFirstItem() {
        LinkedList<String> list3 = new LinkedList<>();
        list3.append("first");
        list3.append("second");
        
        assertTrue(list3.removeElement("first"));
        assertEquals(1, list3.size());
        assertEquals("second", list3.getFirst());
    }

    /**
     * Test method for removing the middle item
     */
    public void testRemoveElementMiddleItem() {
        LinkedList<String> list4 = new LinkedList<>();
        list4.append("first");
        list4.append("second");
        list4.append("third");
        
        assertTrue(list4.removeElement("second"));
        assertEquals(2, list4.size());
        assertEquals("first", list4.getFirst());
        assertEquals("third", list4.getElementAt(1));
    }

    /**
     * Test remove element when no element is there
     */
    public void testRemoveElementNotPresent() {
        LinkedList<String> list5 = new LinkedList<>();
        list5.append("first");
        list5.append("second");
        
        assertFalse(list5.removeElement("third"));
        assertEquals(2, list5.size());
    }
}