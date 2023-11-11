import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A simple singly linked list implementation.
 * 
 * @author brettn
 * @version 09/15/2023
 * 
 * @param <T> the type of elements contained within the list
 */
public class LinkedList<T> implements Iterable<T> {

    /**
     * Represents an individual node within the LinkedList.
     * This is an inner class and is private to the LinkedList.
     * 
     * @param <T>  the type of element stored by the node
     */
    private static class Node<T> {
        private T data;
        private Node<T> nextNode;

        /**
         * Constructs a node with the specified data 
         * and reference to the next node.
         * 
         * @param data the data to be 
         * stored in this node
         * @param nextNode reference to the next 
         * node in the list
         */
        public Node(T data, Node<T> nextNode) {
            this.data = data;
            this.nextNode = nextNode;
        }
    }

    /** Reference to the first node of the list */
    private Node<T> firstNode;

    /** Reference to the last node of the list */
    private Node<T> lastNode;

    /** Number of elements in the list */
    private int elementCount;

    /**
     * Initializes a new, empty linked list.
     */
    public LinkedList() {
        firstNode = null;
        lastNode = null;
        elementCount = 0;
    }

    /**
     * Determines if the linked list is empty.
     * 
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return firstNode == null;
    }

    /**
     * Provides the number of elements in the list.
     * 
     * @return total number of elements within the list
     */
    public int count() {
        return elementCount;
    }

    /**
     * Retrieves the element situated at a specific position in the list.
     * 
     * @param index
     *            position of the desired element
     * @return the element at the specified position
     * @throws IndexOutOfBoundsException
     *             if the position is outside the list's bounds
     */
    public T getElementAt(int index) {
        checkIndexValidity(index);

        Node<T> currentNode = firstNode;
        for (int i = 0; i < index; i++) {
            if (currentNode == null) { 
                throw new IllegalStateException(
                    "Unexpected null node at index: " + i);
            }
            currentNode = currentNode.nextNode;
        }

        return currentNode.data;
    }

    private void checkIndexValidity(int index) {
        if (index < 0 || index >= elementCount) {
            throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + elementCount);
        }
    }
    
    /**
     * Fetches the first element in the linked list.
     *
     * @return The first element, or null if the list is empty.
     */
    public T getFirst() {
        return (firstNode != null) ? fetchFirstValue() : null;
    }
    
    /**
     * Helper method to retrieve the value of the head node.
     *
     * @return The value of the head node.
     */
    private T fetchFirstValue() {
        return firstNode.data;
    }
    
    /**
     * Retrieves the number of elements in the linked list.
     *
     * @return The size of the list.
     */
    public int size() {
        return elementCount;
    }
    
    /**
     * Removes and returns the first element from this linked list.
     * 
     * @return the first element from this list or null if the list is empty.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        
        Node<T> oldHead = firstNode;
        firstNode = oldHead.nextNode;
        elementCount--;

        oldHead.nextNode = null;
        return oldHead.data;
    }

    /**
     * Adds a specified element to the end of the list.
     *
     * @param data
     *            the element to be added
     */
    public void append(T data) {
        Node<T> newNode = new Node<>(data, null);

        if (isEmpty()) {
            firstNode = newNode;
            lastNode = firstNode;
        } 
        else {
            lastNode.nextNode = newNode;
            lastNode = newNode;
        }

        elementCount++;
    }

    /**
     * Removes a specific element from the list.
     * 
     * @param data
     *            the element to be removed
     * @return true if the element was successfully removed, false otherwise
     */
    public boolean removeElement(T data) {
        if (isEmpty())
            return false;

        if (firstNode.data.equals(data)) { 
            firstNode = firstNode.nextNode;
            elementCount--;
            return true;
        }

        Node<T> previousNode = null;
        Node<T> currentNode = firstNode;
        while (currentNode != null
            && !currentNode.data.equals(data)) { 
            previousNode = currentNode;
            currentNode = currentNode.nextNode;
        }

        if (currentNode != null) { 
            previousNode.nextNode = currentNode.nextNode;
            elementCount--; 
            return true;
        }

        return false;
    }
    
    /**
     * Sorts the LinkedList based on the MemorySegment's offset.
     */
    public void sortFreeBlocks() {
        if (firstNode == null || firstNode.nextNode == null) { 
            return;
        }

        // New list to hold the sorted elements
        Node<T> sortedStart = null;

        Node<T> current = firstNode;
        while (current != null) {
            Node<T> next = current.nextNode;

            // Positioning the node in the sorted list
            if (sortedStart == null 
                || ((MemorySegment) sortedStart.data).fetchOffset(
                ) >= ((MemorySegment) current.data).fetchOffset()) {
                current.nextNode = sortedStart;
                sortedStart = current;
            } 
            else {
                placeNodeInSortedOrder(sortedStart, current);
            }

            current = next;
        }

        // Updating the head of the list to point to the sorted list
        firstNode = sortedStart;
    }
    
    /**
     * Removes the specified value from the list.
     * 
     * @param value The value to be removed.
     * @return true if the value was found and removed, false otherwise.
     */
    public boolean remove(T value) {
        if (isEmpty()) return false;

        // Handle head
        if (firstNode.data.equals(value)) {
            removeFirst();
            return true;
        }

        // Iterate and find node before target
        Node<T> prevNode = null;
        Node<T> currentNode = firstNode;
        while (currentNode != null && !currentNode.data.equals(value)) {
            prevNode = currentNode;
            currentNode = currentNode.nextNode;
        }

        // Adjust links if found
        if (currentNode != null) {
            prevNode.nextNode = currentNode.nextNode;
            elementCount--;
            return true;
        }

        return false;
    }
    
    /**
     * Determines whether the linked list contains the specified value.
     * 
     * @param value The value to be checked.
     * @return true if the value is present in the list, false otherwise.
     */
    public boolean includes(T value) {
        if (firstNode == null) { 
            return false;
        }

        for (T item : this) {
            if (item.equals(value)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Removes and provides the first element from the list.
     * 
     * @return the first element in the list or null if the list is empty
     */
    public T poll() {
        if (isEmpty()) {
            return null;
        }

        Node<T> tempNode = firstNode;
        firstNode = firstNode.nextNode;
        elementCount--; 

        return tempNode.data;
    }

    /**
     * Provides the first element in the list without removing it.
     * 
     * @return the first element in the list, or null if the list is empty
     */
    public T peekFirst() {
        return (firstNode != null) ? firstNode.data : null;
    }

    /**
     * Checks if the list contains a specific element.
     * 
     * @param data
     *            the element to be searched for
     * @return true if the list contains the specified element, false otherwise
     */
    public boolean containsElement(T data) {
        for (T item : this) {
            if (item.equals(data)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Utility method to place a node in its correct 
     * position within the sorted list.
     *
     * @param sortedStart The beginning of the sorted list.
     * @param newNode     The node to be placed.
     */
    private void placeNodeInSortedOrder(Node<T> sortedStart, Node<T> newNode) {
        Node<T> current = sortedStart;

        // Finding the correct position in the sorted list for the newNode
        while (current.nextNode != null && (
            (MemorySegment) current.nextNode.data).fetchOffset(
                ) < ((MemorySegment) newNode.data).fetchOffset()) {
            current = current.nextNode;
        }

        newNode.nextNode = current.nextNode;
        current.nextNode = newNode;
    }    
    
    /**
     * Organizes the list's elements in a specific order.
     *
     */
    public void organizeElements() {
        if (firstNode == null || firstNode.nextNode == null) { 
            return;
        }

        // New list to hold the sorted elements
        Node<T> sortedStart = null;

        Node<T> current = firstNode;
        while (current != null) { 
            Node<T> next = current.nextNode;

            // Positioning the node in the sorted list
            if (sortedStart == null 
                || ((MemorySegment) sortedStart.data).fetchOffset(
                    ) >= ((MemorySegment) current.data).fetchOffset()) {
                current.nextNode = sortedStart;
                sortedStart = current;
            } 
            else {
                placeNodeInSortedOrder(sortedStart, current);
            }

            current = next;
        }

        // Updating the head of the list to point to the sorted list
        firstNode = sortedStart;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<T> {
        private Node<T> currentPointer = firstNode;

        @Override
        public boolean hasNext() {
            return currentPointer != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException(
                    "No more elements in the list.");
            }

            T element = currentPointer.data;
            currentPointer = currentPointer.nextNode;

            return element;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                "Remove operation is not supported for this iterator");
        }
    }
}