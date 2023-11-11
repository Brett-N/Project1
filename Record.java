/**
 * Denotes a record in the HashTable.
 * A record contains an ID, a pointer 
 * to a handle, and a marker indicating its state.
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class Record {

    private final int key; 
    private Handle handlePointer; 
    private boolean markerState; 

    /**
     * Constructs a record using its key and the associated handle.
     * 
     * @param key Distinct key for the record.
     * @param handlePointer Pointer to the handle correlated with the key.
     */
    public Record(int key, Handle handlePointer) {
        this.key = key;
        this.handlePointer = handlePointer;
        this.markerState = false;
    }

    /**
     * Fetches the distinct key of this record.
     * 
     * @return Key related to this record.
     */
    public int getKey() {
        return key;
    }

    /**
     * Retrieves the handle correlated with this record.
     * 
     * @return Handle tied to the key.
     */
    public Handle getHandle() {
        return handlePointer;
    }

    /**
     * Modifies the handle correlated with this record.
     * 
     * @param handlePointer1 Updated handle for the record.
     */
    public void updateHandle(Handle handlePointer1) {
        this.handlePointer = handlePointer1;
    }

    /**
     * Validates if this record is marked (e.g. for deletion).
     * 
     * @return The marker's state.
     */
    public boolean isMarked() {
        return markerState;
    }

    /**
     * Marks or unmarks the record based on the provided state.
     * 
     * @param state True to mark, false to unmark.
     */
    public void markRecord(boolean state) {
        this.markerState = state;
    }
}