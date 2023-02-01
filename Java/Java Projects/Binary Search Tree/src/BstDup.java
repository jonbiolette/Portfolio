import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
/**
 * @author Jon Biolette
 * @version 4.17
 * BsTDup class is a binary tree that has the ability to discard duplicates
 */

public class BstDup<E extends Comparable<E>> implements BstDupInterface<E> {
    Node<E> rootNode;

    public BstDup() {
        rootNode = null;
    }

    private static class Node<E> {

        public E data;
        public Node<E> left;
        public Node<E> right;


        public Node(E data) {
            this.data = data;
            left = right = null;
        }
    }

    /**
     * Adds data to the tree, incrementing match count as necessary (if data already exists), or adding
     * new data to the tree (if data does not already exist)
     *
     * @param data the data to add to the tree
     */
    @Override
    public void add(E data) {
        rootNode = add(data, rootNode);
    }

    /**
     * Recursive add ensures tree is not empty and increments through the tree using
     * the given rootNode to add new nodes
     * @param data provided by user
     * @param start represents the starting point to add new nodes
     * @return new node
     */

    private Node<E> add(E data, Node<E> start) {
        Node<E> newNode = new Node<E>(data);
        if (isNum(newNode)) {
            if (start == null) {
                start = newNode;
                toNum(start);
            } else {
                if (toNum(newNode).data.compareTo(start.data) < 0) {
                    start.left = add(data, start.left);
                } else {
                    start.right = add(data, start.right);
                }
            }
        } else {
            if (start == null) {
                start = newNode;
            } else {
                if (newNode.data.compareTo(start.data) < 0) {
                    start.left = add(data, start.left);
                } else {
                    start.right = add(data, start.right);
                }
            }
        }
        return start;
    }

    /**
     * Removes data from the tree, decrementing match count as necessary (if count is above 0),
     * or deleting the data entirely if match count reaches 0
     *
     * @param data the data to delete from the tree
     */
//    @Override
    public void delete(E data) {
        Node<E> deleteNode = new Node<>(data);
        if (rootNode == null) {
            return;
        } else {
           rootNode =  delete(deleteNode, rootNode);
        }
    }

    /**
     * Recursive delete finds the node to be deleted and
     * -If no children delete
     * -Left Children make left
     * -Right Children make right
     * -2 Children find the lowest child and replace, then delete the duplicated node
     * @param deleteNode the requested node to be deleted
     * @param start starting point for incrementation
     * @return a replacement node
     */
    //search for children
    private Node<E> delete(Node<E>deleteNode, Node<E> start){
        //found
        if(deleteNode.data.compareTo(start.data) == 0){
            //No Children
            if (start.left == null && start.right == null) {
                     return null;
            //No left Child
            }else if (start.left == null) {
                //Handling duplicates
                if(start.right.data.compareTo(deleteNode.data) == 0 && start.right.left == null
                        &&start.right.right == null){
                    start.right = null;
                }else{
                    start = start.right;
                }
            //No right Child
            }else if ( start.right == null )
                //Handling duplicates
                if(start.left.data.compareTo(deleteNode.data) == 0 && start.left.left == null
                    &&start.right.right == null){
                    start.left = null;
                }else{
                    start = start.left;
                }
            //2 Children
               else {
                E me = smallest(start.right);
                start.data = me;
                start.right = delete(start,start.right);
            }
        //Searching left
        }else if(deleteNode.data.compareTo(start.data) < 0){
          start.left = delete(deleteNode,start.left);
        //Searching right
        }else{
          start.right =  delete(deleteNode,start.right);
        }
        //Deleting node without children
        if(start.right != null && start.right == deleteNode){
            start.right = null;
        } else if(start.left != null && start.left == deleteNode){
            start.left = null;
        }
        return start;
    }

    /**
     * Removes data from the tree, including all matches
     *
     * @param data the data to delete from the tree
     */
    @Override
    public void deleteAll(E data) {
        while(getMatchCount(data) > 0){
            delete(data);
        }
    }

    /**
     * Retrieves the match count for the provided data
     *
     * @param data the data to search for
     * @return the match count, or -1 if the data is not found in the tree
     */
    @Override
    public int getMatchCount(E data) {
        int count = 0;
        count += getMatchCount(data, rootNode, count);
        return count;

    }

    /**
     * Recursive getMatchCount finds all the data within the tree that matches
     * @param data data that we are looking for
     * @param start starting point for incrementation
     * @param count count of the matched nodes
     * @return the number of matches in the tree
     */

    private int getMatchCount(E data, Node<E> start, int count) {
        if (rootNode == null) {
            return -1;
        } else if (start == null) {
            if (count == 0) {
                return -1;
            }else{
                return count;
            }
        } else if (start.data.compareTo(data) == 0) {
            count++;
            count = getMatchCount(data, start.right, count);
        } else if (data.compareTo(start.data) < 0) {
            count = getMatchCount(data, start.left, count);
        } else {
            count = getMatchCount(data, start.right, count);
        }
        return count;
    }

    /**
     * Retrieves all data from the tree, using an in-order traversal and
     * Ensures array is proper size
     *
     * @param template an array of the right datatype; can be of 0 length
     * @return an array containing the tree data
     */
    @Override
    public E[] getAllDataInOrder(E[] template) {
        int pos = 0;
        template = checkSize(template);
        template = getAllDataInOrder(template, rootNode, pos);
        return template;
    }

    /**
     * Recursive getAllDataInOrder puts all data in order inside the provided array
     * @param array array provided by user
     * @param start starting point for incrementation
     * @param pos position of the current node in the array
     * @return an array of all data within the tree in order.
     */

    private E[] getAllDataInOrder(E[] array, Node<E> start, int pos) {
        if (start == null) {
            return array;
        }
        getAllDataInOrder(array, start.left, pos);
        if (array[pos] != null) {
            while (array[pos] != null) {
                pos++;
            }
        }
        array[pos] = (E) start.data;
        getAllDataInOrder(array, start.right, pos);
        return array;
    }
    //-------------------------------------------------------------
    //                     HELPER METHODS
    //--------------------------------------------------------------

    /**
     * Finds the smallest node data o the left side of tree
     * @param node starting point
     * @return
     */
    public E smallest(Node<E> node) {
        E data = node.data;
        while (node.left != null) {
            data = node.left.data;
            node = node.left;
        }
        return data;
    }

    /**
     * Checks the size of the parameter array and makes sure
     * It's big enough to hold data
     *
     * @param template passed from user
     * @return template sized correctly for data
     */
    public E[] checkSize(E[] template) {
        if (template.length < treeSize(rootNode)) {
            int newSize = treeSize(rootNode);
            template = Arrays.copyOf(template, newSize);
        }
        return template;

    }

    /**
     * Checks the size of the tree, returns 0 if tree is empty
     *
     * @param node starting node
     * @return the size of tree
     */
    public int treeSize(Node<E> node) {
        if (node == null)
            return 0;
        else
            return (treeSize(node.left) + 1 + treeSize(node.right));
    }

    /**
     * Writes tree data to file
     *
     * @param array of nodes from getAllData method
     */
    public void toFile(E[] array, String file) {
        try {
            PrintStream output = new PrintStream(file);
            output.println("Nodes in tree");
            output.println(Arrays.toString(array));


        } catch (FileNotFoundException e) {
            System.out.println("Please enter a proper file name");
        }

    }

    /**
     * Determines whether the String data contains an int
     *
     * @param newNode starting node
     * @return a int if available
     */
    public boolean isNum(Node<E> newNode) {
        try {
            int isInt = Integer.parseInt((String.valueOf(newNode.data)));
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Changes String values that contain ints to a usable int value
     *
     * @param node starting node
     * @return int value of the string
     */
    public Node<E> toNum(Node<E> node) {
        try {
            node.data = (E) Integer.valueOf((String) node.data);
            return node;
        } catch (ClassCastException e) {
            return node;
        }
    }
}
