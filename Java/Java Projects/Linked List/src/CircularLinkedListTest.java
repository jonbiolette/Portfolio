import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class CircularLinkedListTest {
    CircularLinkedList<Player> testList;
    @BeforeEach
    void setUp() {
        testList = new CircularLinkedList();

        Player player1 = new Player("Ashley");
        Player player2 = new Player("Julie");
        Player player3 = new Player("Sarah");
        Player player4 = new Player("Courtney");

        testList.add(player1);
        testList.add(player2);
        testList.add(player3);
        testList.add(player4);
    }
    @Test
    void getSize() {
        int beforeAdd = testList.getSize();
        int afterAdd;
        assertEquals(4, testList.getSize());
        testList.add("Julian");
        afterAdd = testList.getSize();
        assertTrue(afterAdd > beforeAdd);
        testList.frontNode = null;
        assertEquals(0,testList.getSize());
    }

    @Test
    void get() {
        assertEquals(testList.frontNode, testList.get(1));
        assertEquals(testList.frontNode.next, testList.get(2));
        assertEquals(testList.tailNode, testList.get(4));

    }

    @Test
    void add() {
        int beforeAdd = testList.size;
        int afterAdd ;
        testList.add("Naomi");
        afterAdd = testList.size;
        assertTrue(afterAdd > beforeAdd);


    }

    @Test
    void addAtFront() {
        CircularLinkedList.CircularNode front = testList.frontNode;
        CircularLinkedList.CircularNode newNode;
        testList.addAtFront("Christy");
        newNode = testList.frontNode;
        assertNotSame(newNode, front);
        assertSame(newNode.next, testList.frontNode);
    }

    @Test
    void remove() {
        CircularLinkedList.CircularNode frontNode = testList.frontNode;
        CircularLinkedList.CircularNode temp;


        assertTrue(testList.remove(frontNode));
        temp = testList.frontNode;
        assertEquals(temp, testList.frontNode);

        Player player5 = new Player("Angelica");
        assertFalse(testList.remove(player5));

    }

    @Test
    void testRemove() {
        Object player = testList.get(3);
        testList.remove(3);
        assertNotSame(player, testList.get(3));
    }

    @Test
    void iterator() {
        Iterator it = testList.iterator();

        assertTrue(it.hasNext());
        testList.frontNode = null;
        assertFalse(it.hasNext());
    }
}