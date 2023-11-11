import student.TestCase;

/**
 * Test class for SeminarDB
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class SeminarDBTest extends TestCase {

    private SeminarDB seminarDatabase;
    private LinkedList<Seminar> testSeminars;
    private Seminar primarySeminar;
    private Seminar placeholderSeminar;
    private int seminarId = 0;
    private final String[] descriptors = { "Excellent", "Poor", "Mediocre" };

    /**
     * Sets up the test environment.
     */
    @Override
    public void setUp() {
        seminarDatabase = new SeminarDB(1024, 1024);
        testSeminars = new LinkedList<>();
        
        primarySeminar = new Seminar(123, "Seminar Topic", "2405231000", 75,
                (short) 15, (short) 33, 125,
                descriptors, "This seminar is outstanding");
        
        placeholderSeminar = new Seminar();

        testSeminars.append(primarySeminar);
        testSeminars.append(placeholderSeminar);
    }

    /**
     * Tests the method to remove a seminar from the database.
     */
    public void testEraseSeminarFromDB() {
        seminarId = 123;
        seminarDatabase.addSeminar(seminarId, primarySeminar);
        assertTrue(seminarDatabase.eraseSeminar(seminarId));
        seminarId = 789; // Some random ID for negative testing
        assertFalse(seminarDatabase.eraseSeminar(seminarId));
    }

    /**
     * Tests the method to add a seminar to the database.
     */
    public void testAddSeminarToDB() {
        seminarId = 123;
        assertTrue(seminarDatabase.addSeminar(seminarId, primarySeminar));
        assertFalse(seminarDatabase.addSeminar(
            seminarId, primarySeminar)); 
        assertFalse(seminarDatabase.addSeminar(
            seminarId, placeholderSeminar)); 
    }

    /**
     * Tests the method to find a seminar in the database.
     */
    public void testLocateSeminarInDB() {
        seminarId = 123;
        seminarDatabase.addSeminar(seminarId, primarySeminar);
        assertTrue(seminarDatabase.findSeminar(seminarId));
        seminarId = 789; // Some random ID for negative testing
        assertFalse(seminarDatabase.findSeminar(seminarId));
    }
}