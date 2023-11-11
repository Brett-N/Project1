import student.TestCase;

/**
 * Test class for SemManager
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class SemManagerTest extends TestCase {
    private String inputFile = "P1Sample_input.txt";
    private String errorMessage = "command line error\n";
    private String[][] testCases;
    
    /**
     * Sets up my test case for SamManager
     */
    @Override
    public void setUp() {
        testCases = new String[][] {
            {"1024", "1024", "emptyFile.txt", ""},
            {"1", "1", "emptyFile.txt", ""},
            {"2", "2", "emptyFile.txt", ""},
            {"-1024", "1024", "emptyFile.txt", errorMessage},
            {"1024", "-1024", "emptyFile.txt", errorMessage},
            {"-1024", "-1024", "emptyFile.txt", errorMessage},
            {"1024", "1023", inputFile, errorMessage},
            {"1023", "1024", inputFile, errorMessage},
            {"1023", "1023", inputFile, errorMessage},
            {"1024", "1024", errorMessage},
            {"1023", "1023", inputFile, errorMessage},
            {"0", "0", inputFile, errorMessage},
            {"1024", "0", inputFile, errorMessage},
            {"0", "1024", inputFile, errorMessage},
            {"0", errorMessage},
            {"1024", "1024", inputFile, "1024", errorMessage},
            {errorMessage}
        };
    }

    /**
     * test class for my main method
     */
    public void testMainInvocations() {
        for (String[] testCase : testCases) {
            String[] args = new String[testCase.length - 1];
            System.arraycopy(testCase, 0, args, 0, args.length);
            SemManager.main(args);
            String expectedOutput = testCase[testCase.length - 1];
            assertEquals(expectedOutput, systemOut().getHistory());
            systemOut().clearHistory();
        }
    }
}