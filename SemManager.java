/**
 * The main class for the seminar management system.
 * Initializes and launches the CommandProcessor based on input arguments.
 * 
 * @author brettn
 * @version 09/15/2023
 */

// On my honor:
// - I have not used source code obtained from another current or
// former student, or any other unauthorized source, either
// modified or unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class SemManager {

    /**
     * Entry point to start the process with given parameters.
     * 
     * @param parameters Input parameters from command prompt.
     */
    public static void main(String[] parameters) {
        if (validateInputs(parameters)) {
            new Reader().interpretFile(parameters);
        }
        else {
            showCmdError();
        }
    }

    /**
     * Validates the given input parameters.
     * 
     * @param inputs Array of input strings.
     * @return true if inputs meet criteria, false otherwise.
     */
    private static boolean validateInputs(String[] inputs) {
        return inputs.length == 3 
               && isStringPowerOfTwo(inputs[0]) 
               && isStringPowerOfTwo(inputs[1]);
    }

    /**
     * Evaluates if the provided string can 
     * be parsed to a number that's a power of 2.
     * 
     * @param strValue String representation of the number.
     * @return true if parsed number is a power of 2, false otherwise.
     */
    private static boolean isStringPowerOfTwo(String strValue) {
        int parsedValue;
        try {
            parsedValue = Integer.parseInt(strValue);
        } 
        catch (NumberFormatException ex) {
            return false;
        }
        return isIntegerPowerOfTwo(parsedValue);
    }

    /**
     * Checks if the given integer is an exponent of 2.
     * 
     * @param integerVal Integer value to be evaluated.
     * @return true if the value is an exponent of 2, false otherwise.
     */
    private static boolean isIntegerPowerOfTwo(int integerVal) {
        return integerVal > 0 && (integerVal & (integerVal - 1)) == 0;
    }

    /**
     * Prints an error message related to command prompt misuse.
     */
    private static void showCmdError() {
        System.out.println("command line error");
    }
}