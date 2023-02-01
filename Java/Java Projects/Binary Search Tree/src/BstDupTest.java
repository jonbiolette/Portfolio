import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BstDupTest {
    BstDup testInt;
    BstDup testString;
    @BeforeEach
    void setup() {
        testInt = new BstDup();
        testString = new BstDup();

        testInt.add(7);
        testInt.add(3);
        testInt.add(4);
        testInt.add(3);
        testInt.add(3);
        testInt.add(3);
        testInt.add(9);

        testString.add("Ashley");
        testString.add("John");
        testString.add("Page");
        testString.add("Christopher");
        testString.add("Page");
    }

    @Test
    void add() {

        assertTrue(testInt.treeSize(testInt.rootNode)== 7);
        testInt.rootNode = null;
        assertTrue(testInt.treeSize(testInt.rootNode)== 0);
        assertTrue(testString.treeSize(testString.rootNode)== 5);

    }

    @Test
    void delete() {
        assertTrue(testInt.getMatchCount(3) == 4);
        testInt.delete(3);
        assertTrue(testInt.getMatchCount(3) == 3);
        testInt.delete(3);
        assertTrue(testInt.getMatchCount(3) == 2);
        testInt.delete(7);
        assertTrue(testInt.treeSize(testInt.rootNode) == 4);
    }

    @Test
    void deleteAll() {
        assertTrue(testInt.getMatchCount(3) == 4);
        assertTrue(testInt.treeSize(testInt.rootNode)==7);
        testInt.deleteAll(3);
        assertTrue(testInt.getMatchCount(3) == -1);
        assertTrue(testInt.treeSize(testInt.rootNode)==3);
    }

    @Test
    void getMatchCount() {
        assertEquals(4,testInt.getMatchCount(3));
        assertEquals(1,testInt.getMatchCount(7));
        assertEquals(-1,testInt.getMatchCount(1));

        assertEquals(1,testString.getMatchCount("Ashley"));
        assertEquals(-1,testString.getMatchCount("Jon"));
        assertEquals(2,testString.getMatchCount("Page"));



    }

    @Test
    void getAllDataInOrder() {
        Comparable array[] = new Comparable[0];
        array = testInt.getAllDataInOrder(array);
        assertTrue((int)array[1] <= (int)array[2]);
        assertTrue((int)array[5] <= (int)array[6]);

        assertTrue(array.length == testInt.treeSize(testInt.rootNode));

    }
}