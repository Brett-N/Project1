import student.TestCase;

/**
 * Test class for Handle
 * 
 * @author brettn
 * @version 09/16/2023
 */
public class HandleTest extends TestCase {

    /**
     * Sets up my test cases
     */
    public void setUp() {
        //Empty
    }
    
    /**
     * Tests to make sure objects are equal
     */
    public void testEqualsWithSameAttributes() {
        Handle handle1 = new Handle(1, 100);
        Handle handle2 = new Handle(1, 100);

        assertTrue(handle1.equals(handle2));
    }

    /**
     * Tests to make sure objects aren't equal
     */
    public void testEqualsWithDifferentAttributes() {
        Handle handle1 = new Handle(1, 100);
        Handle handle2 = new Handle(2, 200);

        assertFalse(handle1.equals(handle2));
    }

    /**
     * Tests to make sure different objects aren't equal
     */
    public void testEqualsWithDifferentObjectTypes() {
        Handle handle = new Handle(1, 100);
        String someString = "Test String";

        assertFalse(handle.equals(someString));
    }

    /**
     * Tests to make sure null objects aren't equal
     */
    public void testEqualsWithNullObject() {
        Handle handle = new Handle(1, 100);

        assertFalse(handle.equals(null));
    }
    
    /**
     * Tests to make sure objects with the same startPosition 
     * but different dataLength aren't equal.
     */
    public void testEqualsWithSameStartPositionDifferentDataLength() {
        Handle handle1 = new Handle(1, 100);
        Handle handle2 = new Handle(1, 200);

        assertFalse(handle1.equals(handle2));
    }

}
