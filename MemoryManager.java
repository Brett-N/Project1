/**
 * Handles the memory pool management using the 
 * Buddy system memory allocation strategy.
 * This class initializes a memory pool and manages 
 * memory segments with efficient merging and splitting.
 * 
 * @author brettn
 * @version 09/15/2023
 */
public class MemoryManager {

    /**
     * Memory pool represented as a byte array.
     */
    private byte[] memoryPool;
    
    /**
     * An array of linked lists representing free memory segments.
     */
    private LinkedList<MemorySegment>[] freeBlocksLists;

    /**
     * Constructs the memory manager by initializing 
     * the memory pool and setting up the list of free blocks.
     *
     * @param initialSize Size of the memory pool upon initialization.
     */
    @SuppressWarnings("unchecked")
    public MemoryManager(int initialSize) {
        // Initialize memory pool
        memoryPool = new byte[initialSize];
        
        // Compute the number of free block lists needed
        int totalLists = computeNumberOfLists(initialSize);
        
        // Initialize the array of free block lists
        setFreeBlocksLists(new LinkedList[totalLists]);
        initializeFreeBlocksLists(totalLists);

        // Add the entire memory pool as a free block to the last list
        getFreeBlocksLists()[totalLists - 1].append(
            new MemorySegment(0, initialSize));
    }

    // Helper method to calculate the number of free block lists needed
    private int computeNumberOfLists(int size) {
        return (int) (Math.log(size) / Math.log(2)) + 1;
    }

    // Helper method to initialize the array of free block lists
    private void initializeFreeBlocksLists(int totalLists) {
        for (int idx = 0; idx < totalLists; idx++) {
            getFreeBlocksLists()[idx] = new LinkedList<>();
        }
    }

    /**
     * Searches for the smallest free block 
     * list that can accommodate the specified size.
     *
     * @param size The size requirement.
     * @return The index of the list fitting the size, or -1 if none is found.
     */
    int findFreeBlockList(int size) {
        int computedIndex = 0;

        while ((1 << computedIndex) < size) {
            computedIndex++;
        }

        int iterator = computedIndex;
        while (iterator < getFreeBlocksLists().length) {
            if (!getFreeBlocksLists()[iterator].isEmpty()) {
                return iterator;
            }
            iterator++;
        }
        
        return -1;
    }

    /**
     * Inserts the given data into the memory pool.
     *
     * @param data Byte data to be stored.
     * @return A handle indicating the location of the stored data.
     */
    public Handle insert(byte[] data) {
        if (data == null || data.length == 0) { //Untested
            return null;  
        }

        int index = locateFreeBlock(data.length);

        // Ensure there's enough memory space.
        while (isInsufficientSpace(index, data.length)) {
            amplifyMemory();
            index = locateFreeBlock(data.length);
        }

        MemorySegment block = fetchBlockAt(index);
        while (block != null && suitableBlockSize( //Untested
            index, data.length)) {
            getFreeBlocksLists()[index].removeFirst();
            index--;
            block = divideAndFetch(block, index);
        }

        // Copy data into the memory pool.
        System.arraycopy(data, 0, memoryPool, block.fetchOffset(), data.length);

        removeAndSort(index);

        return new Handle(block.fetchOffset(), data.length);
    }

    private boolean isInsufficientSpace(int index, int length) {
        return index == -1 || getTotalFreeSpace() < length; //Untested
    }

    private int locateFreeBlock(int length) {
        return findFreeBlockList(length);
    }

    private MemorySegment fetchBlockAt(int index) {
        if (getFreeBlocksLists()[index] != null) { //Untested
            return getFreeBlocksLists()[index].getFirst();
        }
        return null;
    }

    private boolean suitableBlockSize(int index, int length) {
        return (int)Math.pow(2, index - 1) >= length;
    }

    private MemorySegment divideAndFetch(MemorySegment block, int index) {
        split(block, index);
        return getFreeBlocksLists()[index].getFirst();
    }

    private void removeAndSort(int index) {
        getFreeBlocksLists()[index].removeFirst();
        for (LinkedList<MemorySegment> list : getFreeBlocksLists()) {
            list.sortFreeBlocks();
        }
    }

    private void amplifyMemory() {
        resizeMemoryPool();
    }


    /**
     * Divides a memory segment into two equal 
     * parts and updates the list of free blocks.
     *
     * @param block Memory segment to be split.
     * @param index Index of the list where the new segments should be added.
     */
    private void split(MemorySegment block, int index) {
        int partitionSize = block.fetchSize() / 2;
        
        MemorySegment firstPart = new MemorySegment(
            block.fetchOffset(), partitionSize);
        MemorySegment secondPart = new MemorySegment(
            block.fetchOffset() + partitionSize, partitionSize); //Untested
        
        LinkedList<MemorySegment> targetList = getFreeBlocksLists()[index];

        targetList.append(firstPart);
        targetList.append(secondPart);
    }


    /**
     * Finds the total free space in the memory pool
     *
     * @return The amount of free space
     */
    int getTotalFreeSpace() {
        int totalSpace = 0;

        for (int i = 0; i < getFreeBlocksLists().length; i++) {
            LinkedList<MemorySegment> currentList = getFreeBlocksLists()[i];
            
            int index = 0;
            while (index < currentList.size()) {
                MemorySegment block = currentList.getElementAt(index);
                totalSpace += block.fetchSize();
                index++;
            }
        }

        return totalSpace;
    }


    /**
     * Doubles the size of the memory pool, then creates a free block
     */
    private void resizeMemoryPool() {
        // Double the memory pool's size
        byte[] expandedMemory = new byte[memoryPool.length * 2];
        for (int i = 0; i < memoryPool.length; i++) { //Untested
            expandedMemory[i] = memoryPool[i];
        }
        memoryPool = expandedMemory;

        // Create new storage for free blocks lists with one additional slot
        LinkedList<MemorySegment>[] updatedFreeBlocksLists = new LinkedList[
            getFreeBlocksLists().length + 1];
        for (int i = 0; i < getFreeBlocksLists().length; i++) {
            updatedFreeBlocksLists[i] = getFreeBlocksLists()[i];
        }
        
        updatedFreeBlocksLists[getFreeBlocksLists(
            ).length] = new LinkedList<>();
        setFreeBlocksLists(updatedFreeBlocksLists);

        // Add new free block representing the additional memory
        MemorySegment additionalBlock = new MemorySegment( 
            memoryPool.length / 2, memoryPool.length / 2); 
        getFreeBlocksLists(
            )[getFreeBlocksLists().length - 2].append(additionalBlock);

        // Attempt to merge any adjacent free blocks
        mergeFreeBlocks();

        System.out.println(
            "Memory pool expanded to " + memoryPool.length + " bytes");
    }


    /**
     * Removes data from the memory pool using the provided handle.
     *
     * @param handle Handle pointing at the block to be removed.
     */
    public void remove(Handle handle) {
        int sizeOfBlock = findAdjustedBlockSize(handle.getDataLength());
        int listIndex = calculateListIndex(sizeOfBlock);
        MemorySegment newBlock = new MemorySegment(
            handle.getStartPosition(), sizeOfBlock);
        
        addToFreeList(listIndex, newBlock);
        
        sortAllFreeBlocks();
        
        consolidateFreeBlocks();
    }
    /**
     * Method that finds the adjusted block size
     * @param originalSize the original size
     * @return the adjusted size
     */
    int findAdjustedBlockSize(int originalSize) { 
        if ((originalSize & (originalSize - 1)) == 0) { //Untested
            return originalSize;
        }
        int nearestPower = (int)(Math.log(originalSize) / Math.log(2));
        return (int)Math.pow(2, nearestPower + 1);
    }

    private void addToFreeList(int index, MemorySegment block) {
        getFreeBlocksLists()[index].append(block);
    }

    private void sortAllFreeBlocks() {
        for (LinkedList<MemorySegment> list : getFreeBlocksLists()) {
            list.sortFreeBlocks();
        }
    }

    private void consolidateFreeBlocks() {
        mergeFreeBlocks();
    }


    /**
     * Finds the "buddy" of the given memory segment, 
     * which can be merged with the segment.
     *
     * @param block The memory segment for which the buddy is to be found.
     * @return Buddy segment if found, otherwise null.
     */
    private MemorySegment findBuddy(MemorySegment block) {
        int blockStart = block.fetchOffset();
        int blockSize = block.fetchSize();

        // Determine the potential starting position of the buddy block
        int potentialBuddyPosition = blockStart ^ blockSize;

        // Fetch the list of free blocks of the same size as the input block
        int index = calculateListIndex(blockSize);
        LinkedList<MemorySegment> relevantList = getFreeBlocksLists()[index];

        // Go through each free block in the relevant list
        for (MemorySegment candidate : relevantList) {
            if (candidate.fetchOffset() == potentialBuddyPosition) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Calculate the index in the list based on size of the block.
     *
     * @param size The size of the memory block
     * @return The index of the block in the list
     */
    private int calculateListIndex(int size) {
        return (int) (Math.log(size) / Math.log(2));
    }


    /**
     * Merges two given blocks.
     *
     * @param block1
     *            The first block.
     * @param block2
     *            The second block.
     * @return The merged block.
     */
    private MemorySegment merge(
        MemorySegment block1, MemorySegment block2) {
        int startPos = Math.min(
            block1.fetchOffset(), block2.fetchOffset());
        return new MemorySegment(
            startPos, block1.fetchSize() + block2.fetchSize());
    }


    /**
     * Attempts to merge adjacent free blocks in the memory pool after memory
     * expansion
     */
    private void mergeFreeBlocks() {
        boolean merged;
        do {
            merged = false;
            for (int i = getFreeBlocksLists().length - 1; i >= 0; i--) {
                LinkedList<MemorySegment> currentList = getFreeBlocksLists()[i];
                for (int j = 0; j < currentList.size(); j++) {
                    MemorySegment block = currentList.getElementAt(j);
                    MemorySegment buddy = findBuddy(block);
                    if (buddy != null) {
                        merged = true;
                        MemorySegment mergedBlock = merge(block, buddy);
                        currentList.remove(block);
                        currentList.remove(buddy);
                        int mergedIndex = (int)(
                            Math.log(mergedBlock.fetchSize())
                            / Math.log(2));
                        getFreeBlocksLists()[mergedIndex].append(mergedBlock);
                        j = -1;
                    }
                }
            }
        }
        while (merged);
    }


    /**
     * Retrieves data from the memory pool using the given handle.
     *
     * @param handle Handle that points to the data block's location.
     * @return Data as a byte array. 
     * Returns an empty array if data doesn't exist or was removed.
     */
    public byte[] get(Handle handle) {
        // Deduce the block's size
        int dataSize = handle.getDataLength();

        int listIndex = computeLogBaseTwo(dataSize);

        MemorySegment queryBlock = new MemorySegment(
            handle.getStartPosition(), dataSize);

        if (isBlockInList(getFreeBlocksLists()[listIndex], queryBlock)) {
            return new byte[0];
        }

        // Extract the data from the memory pool
        byte[] extractedData = new byte[dataSize];
        extractDataFromMemory(memoryPool, handle.getStartPosition(
            ), extractedData, dataSize);

        return extractedData;
    }

    // Utility method to compute logarithm to the base 2
    private int computeLogBaseTwo(int value) {
        return (int)(Math.log(value) / Math.log(2));
    }

    // Utility method to check if a block is present in a list
    private boolean isBlockInList(
        LinkedList<MemorySegment> list, MemorySegment block) {
        return list.includes(block);
    }

    // Utility method to extract data from the memory pool
    private void extractDataFromMemory(
        byte[] source, int srcPos, byte[] destination, int length) {
        System.arraycopy(source, srcPos, destination, 0, length);
    }

    /**
     * Displays the current status of the free blocks in the memory pool.
     */
    public void dump() {
        String report = buildFreeBlocksReport();
        System.out.println("Freeblock List:");
        if (report.isEmpty()) {
            System.out.println("There are no freeblocks in the memory pool");
        } 
        else {
            System.out.print(report);
        }
    }

    private String buildFreeBlocksReport() {
        StringBuilder report = new StringBuilder();
        
        boolean foundAnyFreeBlocks = false;

        for (int idx = 0; idx < getFreeBlocksLists().length; idx++) {
            LinkedList<MemorySegment> currentList = getFreeBlocksLists()[idx];
            if (currentList.size() > 0) {
                foundAnyFreeBlocks = true;
                report.append(computeBlockSize(idx)).append(": ");
                report.append(concatenateBlockStartPositions(
                    currentList)).append("\n");
            }
        }

        return foundAnyFreeBlocks ? report.toString() : ""; //Untested
    }

    private int computeBlockSize(int index) {
        return (int) Math.pow(2, index);
    }

    private String concatenateBlockStartPositions(
        LinkedList<MemorySegment> blockList) {
        StringBuilder blockPositions = new StringBuilder();
        
        for (MemorySegment block : blockList) {
            blockPositions.append(block.fetchOffset()).append(" ");
        }
        
        return blockPositions.toString().trim();
    }

    /**
     * Getter method for freeBlocksLists
     * @return the freeBlocksLists
     */
    public LinkedList<MemorySegment>[] getFreeBlocksLists() {
        return freeBlocksLists;
    }

    /**
     * Setter method for freeBlocksLists
     * 
     * @param freeBlocksLists the freeBlocksLists to set
     */
    public void setFreeBlocksLists(LinkedList<MemorySegment>[] freeBlocksLists) {
        this.freeBlocksLists = freeBlocksLists;
    }
}