import java.io.FileNotFoundException;

public interface BstDupInterface<E> {

    // BstDup should have a no-parameter constructor

    /**
     * Adds data to the tree, incrementing match count as necessary (if data already exists), or adding
     *      new data to the tree (if data does not already exist)
     *
     * @param data          the data to add to the tree
     */
    public void add(E data) throws FileNotFoundException;

    /**
     * Removes data from the tree, decrementing match count as necessary (if count is above 0),
     *      or deleting the data entirely if match count reaches 0
     * @param data      the data to delete from the tree
     */
    public void delete(E data);

    /**
     * Removes data from the tree, including all matches
     * @param data      the data to delete from the tree
     */
    public void deleteAll(E data);

    /**
     * Retrieves the match count for the provided data
     * @param data      the data to search for
     * @return          the match count, or -1 if the data is not found in the tree
     */
    public int getMatchCount(E data);

    /**
     * Retrieves all data from the tree, using an in-order traversal
     *
     * @param   template    an array of the right datatype; can be of 0 length
     * @return              an array containing the tree data
     */
    public E[] getAllDataInOrder(E[] template);

}
