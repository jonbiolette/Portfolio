import org.w3c.dom.Node;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Player {
    String name;
    int points;
    int die1;
    int die2;

    public Player(String name) {
        this.name = name;
        this.points = 0;
    }

    public int newRound() {
        Random rand = new Random();
        int limit = 7;
        die1 = rand.nextInt(limit);
        die2 = rand.nextInt(limit);
        int newRoll = (die1 + die2);
        this.points += newRoll;

        return points;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Name: " + name +", points: " + points;
    }

}