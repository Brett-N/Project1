/**
 * Represents a segment of unused memory.
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class MemorySegment {

    // Attributes of the MemorySegment
    private final int offset;      // Points to beginning of segment
    private final int segmentSize; // Defines size of segment

    /**
     * Constructs a new MemorySegment with given offset and size.
     *
     * @param offset    The beginning of the memory segment.
     * @param segmentSize The size of the segment.
     */
    public MemorySegment(int offset, int segmentSize) {
        this.offset = offset;
        this.segmentSize = segmentSize;
    }

    /**
     * Retrieves the size of the memory segment.
     *
     * @return Size of the segment.
     */
    public int fetchSize() {
        return segmentSize;
    }

    /**
     * Retrieves the starting position of the memory segment.
     *
     * @return Offset of the segment.
     */
    public int fetchOffset() {
        return offset;
    }

    /**
     * Determines if two MemorySegment objects are equivalent.
     *
     * @param other The object to compare against.
     * @return True if objects are the same, otherwise false.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof MemorySegment)) return false;
        
        MemorySegment otherSegment = (MemorySegment) other;
        return (offset == otherSegment.offset) && (
            segmentSize == otherSegment.segmentSize);
    }
}