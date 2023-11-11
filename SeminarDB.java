/**
 * Orchestrates the operations on seminars, 
 * utilizing HashTable and MemManager for storage and management.
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class SeminarDB {

    private HashTable hashTable; 
    private MemoryManager memoryManager;

    /**
     * Initializes the SeminarDB with specified sizes for memory and hash table.
     * 
     * @param initialMemorySize
     *            Initial space for seminar data storage.
     * @param initialHashSize
     *            Initial capacity of the hash table.
     */
    public SeminarDB(int initialMemorySize, int initialHashSize) {
        this.memoryManager = new MemoryManager(initialMemorySize);
        this.hashTable = new HashTable(initialHashSize);
    }

    /**
     * Adds a seminar to the database.
     * 
     * @param id
     *        Identifier for the seminar.
     * @param seminar
     *        The seminar object to be added.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addSeminar(int id, Seminar seminar) {
        if (hashTable.retrieve(id) != null) {
            notifyInsertionFailure(id);
            return false;
        }
        return tryAddition(id, seminar);
    }

    private void notifyInsertionFailure(int id) {
        System.out.println(
            "Insert FAILED - There is already a record with ID " + id);
    }

    private boolean tryAddition(int id, Seminar seminar) {
        try {
            byte[] serializedData = seminar.serialize();
            Handle handlePosition = memoryManager.insert(serializedData);
            hashTable.addEntry(id, handlePosition);
            confirmAddition(id, seminar, serializedData.length);
            return true;
        } 
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private void confirmAddition(int id, Seminar seminar, int size) {
        System.out.println("Successfully inserted record with ID " + id);
        System.out.println(seminar.toString());
        System.out.println("Size: " + size);
    }

    /**
     * Locates a seminar in the database and displays its details.
     *
     * @param id Identifier for the seminar.
     * @return true if the seminar was found, false otherwise.
     */
    public boolean findSeminar(int id) {
        Handle seminarLocation = fetchHandle(id);
        
        if (seminarLocation == null) {
            displaySearchFailure(id);
            return false;
        }
        
        return displaySeminarDetails(id, seminarLocation);
    }

    private Handle fetchHandle(int id) {
        return hashTable.retrieve(id);
    }

    private void displaySearchFailure(int id) {
        System.out.println("Search FAILED -- There is no record with ID " + id);
    }

    private boolean displaySeminarDetails(int id, Handle handle) {
        byte[] seminarData = memoryManager.get(handle);
        try {
            Seminar seminar = Seminar.deserialize(seminarData);
            displayLocatedSeminar(id, seminar);
            return true;
        } 
        catch (Exception deserializationError) {
            displayError(deserializationError);
            return false;
        }
    }

    private void displayLocatedSeminar(int id, Seminar seminar) {
        System.out.println("Found record with ID " + id + ":");
        System.out.println(seminar.toString());
    }

    private void displayError(Exception error) {
        System.out.println(error);
    }

    /**
     * Removes a seminar from the database.
     * 
     * @param id Identifier for the seminar.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean eraseSeminar(int id) {
        Handle targetHandle = hashTable.retrieve(id);
        
        if (targetHandle == null) {
            System.out.println(
                "Delete FAILED -- There is no record with ID " + id);
            return false;
        }
        
        hashTable.deleteEntry(id);
        memoryManager.remove(targetHandle);
        
        System.out.println("Record with ID " + id
            + " successfully deleted from the database");
        return true;
    }

    /**
     * Displays the current state of the hash table.
     */
    public void showHashTableContents() {
        hashTable.displayHash();
    }

    /**
     * Displays the current memory blocks.
     */
    public void listFreeBlocks() {
        memoryManager.dump();
    }
}