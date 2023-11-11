import student.TestCase;

/**
 * My test class for MemorySegment
 *
 * @author brettn
 * @version 09/15/2023
 */
public class MemorySegmentTest extends TestCase {

    // Test member for MemorySegment.
    private MemorySegment testSegment;

    /**
     * Setups up test fixtures before each test.
     */
    public void setUp() {
        testSegment = new MemorySegment(0, 10);
    }

    /**
     * Checks the equals method of MemorySegment.
     */
    public void testEquals() {

        // Create several different MemorySegment objects.
        MemorySegment sameStartEnd = new MemorySegment(0, 10);
        MemorySegment differentStart = new MemorySegment(10, 20);
        MemorySegment differentEnd = new MemorySegment(0, 20);
        MemorySegment sameTotal = new MemorySegment(10, 10);

        // Validate equality with several scenarios.
        assertEqualityOfSegments(testSegment, sameStartEnd);
        assertNonEqualityOfSegments(testSegment, differentStart);
        assertNonEqualityOfSegments(testSegment, differentEnd);
        assertNonEqualityOfSegments(testSegment, sameTotal);
        
        // Checking with other data types.
        assertFalse(testSegment.equals(null));
        assertFalse(testSegment.equals(new Object()));
    }

    /**
     * Asserts that two MemorySegment instances are equal.
     *
     * @param a The first MemorySegment.
     * @param b The second MemorySegment.
     */
    private void assertEqualityOfSegments(MemorySegment a, MemorySegment b) {
        assertTrue(a.equals(b));
    }

    /**
     * Asserts that two MemorySegment instances are not equal.
     *
     * @param a The first MemorySegment.
     * @param b The second MemorySegment.
     */
    private void assertNonEqualityOfSegments(MemorySegment a, MemorySegment b) {
        assertFalse(a.equals(b));
    }
}