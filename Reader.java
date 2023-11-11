import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Manages and performs tasks based on directives provided to the SeminarDB.
 * Reads input from files to conduct actions like addition, removal, lookup,
 * and display.
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class Reader {

    /**
     * Default constructor for SeminarManager.
     */
    public Reader() {
        // Intentionally left blank.
    }

    /**
     * Parses the test file and passes it to SeminarDB
     * 
     * @param args arguments from command line
     */
    public void interpretFile(String[] args) {
        // Initialize database with given capacities
        int memoryCapacity = Integer.parseInt(args[0]);
        int hashTableCapacity = Integer.parseInt(args[1]);
        String inputFile = args[2];
        SeminarDB dbInstance = new SeminarDB(memoryCapacity, hashTableCapacity);
        
        try (Scanner fileScanner = new Scanner(new File(inputFile))) {
            int seminarId; // Holds the ID of the current seminar
            String printParam; // Holds the parameter for the "print" command
            
            while (fileScanner.hasNext()) {
                String action 
                    = fileScanner.next().trim(); // Current operation command
                switch (action) {
                    case "insert": {
                        // Extract seminar details and insert into the database
                        seminarId = Integer.parseInt(
                            fileScanner.nextLine().trim());
                        String seminarTitle = fileScanner.nextLine().trim();
                        String[] details = fileScanner.nextLine(
                            ).trim().replaceAll(" +", " ").split(" ");
                        String seminarDate = details[0];
                        int duration = Integer.parseInt(details[1]);
                        short posX = Short.parseShort(details[2]);
                        short posY = Short.parseShort(details[3]);
                        int fee = Integer.parseInt(details[4]);
                        String[] tags = fileScanner.nextLine(
                            ).trim().replaceAll(" +", " ").split(" ");
                        String summary = fileScanner.nextLine().trim();
                        Seminar seminar = new Seminar(
                            seminarId, seminarTitle, seminarDate
                            , duration, posX, posY, fee, tags, summary);
                        dbInstance.addSeminar(seminarId, seminar);
                        break;
                    }
                    case "delete": {
                        // Delete seminar
                        seminarId = Integer.parseInt(
                            fileScanner.nextLine().trim());
                        dbInstance.eraseSeminar(seminarId);
                        break;
                    }
                    case "search": {
                        // Search for a seminar using its ID
                        seminarId = Integer.parseInt(
                            fileScanner.nextLine().trim());
                        dbInstance.findSeminar(seminarId);
                        break;
                    }
                    case "print": {
                        // Print specific database details based on parameter
                        printParam = fileScanner.nextLine().trim();
                        if (printParam.equals("hashtable"))
                            dbInstance.showHashTableContents();
                        else
                            dbInstance.listFreeBlocks();
                        break;
                    }
                    default:
                        System.out.println("Invalid command");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }
}