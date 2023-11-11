/**
 * Represents a pointer to a specific location within memory storage.
 * The handle provides details regarding the starting point 
 * and the size of the associated data.
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class Handle {

    private final int startPosition;
    private final int dataLength;

    /**
     * Constructs a new memory pointer indicating
     *  the starting position and length of data.
     * 
     * @param startPosition The initial position within memory storage.
     * @param dataLength Length of the data associated with this pointer.
     */
    public Handle(int startPosition, int dataLength) {
        this.startPosition = startPosition;
        this.dataLength = dataLength;
    }

    /**
     * Provides the starting position within the memory storage.
     * 
     * @return The initial memory position.
     */
    public int getStartPosition() {
        return startPosition;
    }

    /**
     * Offers information about the length of the associated data.
     * 
     * @return The length of the data.
     */
    public int getDataLength() {
        return dataLength;
    }

    /**
     * Validates if the current MemoryPointer is equivalent to another object.
     *
     * @param other The object to be compared.
     * @return True if they are equivalent, otherwise false.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Handle) {
            Handle compareHandle = (Handle) other;
            return startPosition == compareHandle.startPosition 
                && dataLength == compareHandle.dataLength;
        }
        return false;
    }
}