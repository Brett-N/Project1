/**
 * A structure for storing seminar entries using hashing.
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class HashTable {

    private static final double THRESHOLD_RATIO = 0.5;
    private Record[] entries;
    private int totalEntries;
    private int maxEntries;

    /**
     * Initializes a hash table with a specified size.
     *
     * @param initialSize Initial size of the table.
     */
    public HashTable(int initialSize) {
        this.maxEntries = initialSize;
        this.entries = new Record[initialSize];
        this.totalEntries = 0;  
    }

    /**
     * Retrieves a handle based on its ID.
     *
     * @param id The ID of the handle.
     * @return The handle associated with the ID, or null if not found.
     */
    public Handle retrieve(int id) {
        int index = locateEntry(id);
        if (index != -1) {
            return entries[index].getHandle();
        }
        return null;
    }

    /**
     * Adds an entry to the hash table.
     *
     * @param id The ID associated with the handle.
     * @param handle The handle to be stored.
     * @return true if added successfully, false otherwise.
     */
    public boolean addEntry(int id, Handle handle) {
        if (id < 0) return false;

        adjustSizeIfNeeded();

        int index = searchPosition(id);
        if (index != -1 && (entries[index] == null 
            || entries[index].isMarked())) {
            entries[index] = new Record(id, handle);
            totalEntries++;
            return true;
        }
        return false;
    }

    /**
     * Deletes an entry based on its ID.
     *
     * @param id The ID associated with the handle.
     * @return true if removed successfully, false otherwise.
     */
    public boolean deleteEntry(int id) {
        int index = locateEntry(id);
        if (index != -1) {
            entries[index].markRecord(true);
            totalEntries--;
            return true;
        }
        return false;
    }

    /**
     * Prints the current state of the hash table.
     */
    public void displayHash() {
        listEntries();
        System.out.println("total records: " + totalEntries);
    }

    /**
     * Returns the total number of entries.
     *
     * @return The count of entries in the table.
     */
    public int count() {
        return totalEntries;
    }

    /**
     * Returns the maximum allowed entries.
     *
     * @return The maximum entries that can be stored.
     */
    public int limit() {
        return maxEntries;
    }

    /**
     * Mod hashtable length
     * @param k seminar number
     * @return the output of the simple equation
     */
    public int primaryHash(int k) {
        return k % entries.length;
    }

    /**
     * Calculates prime number to hashtable length
     * @param k seminar number
     * @return the output of the simple equation
     */
    public int secondaryHash(int k) {
        int size = entries.length;
        return ((k / size) % (size / 2)) * 2 + 1; 
    }

    private void listEntries() {
        System.out.println("Hashtable:");
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] != null) {
                System.out.println(i + (entries[i].isMarked(
                    ) ? ": TOMBSTONE" : ": " + entries[i].getKey()));
            }
        }
    }

    /**
     * Simple search method(public for testing)
     * @param id the id for the segment
     * @return idx integer position
     */
    public int searchPosition(int id) {
        int idx = primaryHash(id);
        int jump = secondaryHash(id);
        while (entries[idx] != null && entries[idx].getKey( 
            ) != id && !entries[idx].isMarked()) { 
            idx = (idx + jump) % entries.length; 
        }
        return idx;
    }

    private int locateEntry(int id) {
        int idx = primaryHash(id);
        int jump = secondaryHash(id);
        while (entries[idx] != null) {
            if (entries[idx].getKey() == id && !entries[idx].isMarked()) {
                return idx;
            }
            idx = (idx + jump) % entries.length;
        }
        return -1;
    }

    private void adjustSizeIfNeeded() {
        if (totalEntries < maxEntries * THRESHOLD_RATIO) return;

        Record[] previousEntries = entries;
        entries = new Record[previousEntries.length * 2];
        maxEntries = entries.length;
        totalEntries = 0;

        for (Record record : previousEntries) {
            if (record != null && !record.isMarked()) {
                addEntry(record.getKey(), record.getHandle());
            }
        }
        System.out.println(
            "Hash table expanded to " + entries.length + " records");
    }
}