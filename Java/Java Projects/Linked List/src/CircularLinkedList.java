import java.util.Iterator;

public class CircularLinkedList<E> implements CircularLinkedListInterface, Iterable<E> {

    public static class CircularNode<E> {
        public E data;
        CircularNode next;

        public CircularNode(E data) {

            this.data = data;
        }
    }

    public CircularNode frontNode = null;
    public CircularNode tailNode = null;
    public int size;

    public CircularLinkedList() {
        frontNode = null;
        size = 0;
    }


    /**
     * Retrieves a count of elements being maintained by the list.
     *
     * @return the size of the list (count of elements)
     */
    @Override
    public int getSize() {
        int nodeCount = 0;
        CircularNode current = frontNode;
        if (frontNode == null) {
            return 0;
        } else {
            do {
                nodeCount++;
                current = current.next;
            }
            while (current != frontNode);
        }
        return nodeCount;
    }

    /**
     * Retrieves the data at the specified position in the list
     *
     * @param position 0-based index for the list; must be in the range 0 to size - 1
     * @return the data in the specified position in the list
     */
    @Override
    public Object get(int position) {
        CircularNode current = frontNode;
        boolean found = false;
        int size = 1;
        while (found != true) {
            if (size == position) {
                found = true;
            } else {
                current = current.next;
                size++;
            }
        }
        return current;
    }

    /**
     * Adds a new node to the end of the list; by nature, this element's next will point to the first element
     *
     * @param value the element to add to the list
     */
    @Override
    public void add(Object value) {
        CircularNode newNode = new CircularNode(value);
        if (frontNode == null) {
            addAtFront(value);
        } else {

            tailNode.next = newNode;
            tailNode = newNode;
            tailNode.next = frontNode;
            size++;
        }
    }


    public void addAtFront(Object data) {
        CircularNode newNode = new CircularNode(data);
        frontNode = newNode;
        tailNode = newNode;
        newNode.next = frontNode;
        size++;
    }

    /**
     * Removes the specified item from the list, if it exists there.
     *
     * @param value the element to remove from the list
     * @return true, if the element was found and removed; false, if not found or list is empty
     */
    @Override
    public boolean remove(Object value) {
        CircularNode current = frontNode;
        boolean found = false;
       do{
            if (current == value){
                frontNode = current.next;
                found = true;
            }
            if (current.next.data.toString() == value) {
                current.next = current.next.next;
                found = true;
            } else {
                current = current.next;
            }
        } while (found != true && current != frontNode);
        if (found == true) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the node at the specified position in the list
     *
     * @param position position in the list; must be in range 0 to size - 1
     */
    @Override
    public void remove(int position) {
        CircularNode current = frontNode;
        boolean found = false;
        int size = 1;
       do{
            if (size == position - 1) {
                current.next = current.next.next;
                found = true;
            } else {
                current = current.next;
                size++;
            }
        }while (found != true && current.next != frontNode);
    }

    /**
     * Retrieves an iterator over the list's elements.  Do not do other list operations like add or remove
     * from within an iterator loop; the results are not guaranteed to function as you might expect
     *
     * @return a strongly typed iterator over elements in the list
     */

    @Override
    public Iterator<E> iterator() {
        Iterator<E> itr = new Iterator<E>() {
            CircularNode current = frontNode;
            int size = 0;

            @Override
            public boolean hasNext() {
                size++;
                return size <= getSize();
            }

            @Override
            public E next() {
                Object data = current.data;
                current = current.next;
                return (E) data;
            }
        };
        return itr;
    }
}
