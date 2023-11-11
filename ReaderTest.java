import student.TestCase;

/**
 * test class for my Reader
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class ReaderTest extends TestCase {
    
    /** Instance of Reader for testing file interpretations. */
    private Reader reader;
    
    /**
     * Initializes common test fixtures before each test.
     */
    @Override
    public void setUp() {
        reader = new Reader();
    }

    /**
     * Tests the file interpretation of an empty document. 
     * Ensures that reading an empty file produces no output.
     */
    public void testEmptyDocument() {
        reader.interpretFile(new String[] { "2048", "2048", "emptyFile.txt" });
        assertFuzzyEquals("", systemOut().getHistory());
    }

    /**
     * Tests the file interpretation and the resulting hashtable display.
     * Checks the system's output after interpreting a file containing a record 
     * to ensure correct interpretation and display.
     */
    public void testHashTableDisplay() {
        reader.interpretFile(new String[] { "2048", "2048", "hash.txt" });
        String expectedOutput = "Successfully inserted record with ID 456\n"
            + "ID: 456, Title: Software Engineering Talk\n"
            + "Date: 2509223000, Length: 100, X: 25, Y: 30, Cost: 500\n"
            + "Description: Discussion on Software Engineering trends.\n"
            + "Keywords: Ruby, Rust, Go\n" + "Size: 125\n"
            + "Hashtable:\n456: 456\n" + "total records: 1\n";
        assertFuzzyEquals(expectedOutput, systemOut().getHistory());
    }

    /**
     * Tests the file interpretation and the resulting memory block display.
     * Validates the system's output after interpreting a file with a record 
     * and its subsequent deletion to ensure accurate memory block display.
     */
    public void testMemoryBlockDisplay() {
        reader.interpretFile(
            new String[] { "2048", "2048", "memoryBlocks.txt" });
        String expectedOutput = "Successfully inserted record with ID 456\n"
            + "ID: 456, Title: Software Engineering Talk\n"
            + "Date: 2509223000, Length: 100, X: 25, Y: 30, Cost: 500\n"
            + "Description: Discussion on Software Engineering trends.\n"
            + "Keywords: Ruby, Rust, Go\n" + "Size: 125\n"
            + "Record with ID 456 successfully " + "deleted from the database\n"
            + " Freeblock List:\n" + "2048: 0 \n";
        assertFuzzyEquals(expectedOutput, systemOut().getHistory());
    }
}