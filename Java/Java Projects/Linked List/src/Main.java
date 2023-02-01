import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        CircularLinkedList<Player> mainList = new CircularLinkedList();

        Player player1 = new Player("Bob");
        Player player2 = new Player("Joe");
        Player player3 = new Player("Mike");
        Player player4 = new Player("Jon");

        mainList.add(player1);
        mainList.add(player2);
        mainList.add(player3);
        mainList.add(player4);

        int limit = 25;
        lobby(mainList, limit);
    }

    //Starts new round and puts all players in game
    public static void lobby(CircularLinkedList list, int limit){

            Iterator<Player> it = list.iterator();
            int score = 0;
            while (score <= limit) {
                while (it.hasNext() && score < limit) {
                    Player player = it.next();
                    player.newRound();
                    if (player.getPoints() > score) {
                        score = player.getPoints();
                    } else if (score >= limit) {
                        firstTo100(list, limit);
                    }
                }
                firstTo100(list, limit);
                it = list.iterator();
            }
        }

    //Finds first place and displays rounds
    private static void firstTo100(CircularLinkedList list,int limit) {

        sleep();
        System.out.println("\nNew Round Starting");

        CircularLinkedList.CircularNode current = list.frontNode;
        CircularLinkedList.CircularNode temp;

        Player firstPlace = null;

        Player player = (Player) current.data;
        Player tempPlayer;

        do {
            //first place
            if (current == list.frontNode) {
                firstPlace = player;
            } else if (player.getPoints() >= firstPlace.getPoints()) {
                tempPlayer = firstPlace;
                firstPlace = player;
                        sleep();
                        System.out.println(tempPlayer.getName() + " rolls " + tempPlayer.die1 + " and " +
                                tempPlayer.die2 + ", score now totaling " + tempPlayer.getPoints());
            }
            //Catch all
            if (player.getPoints() < firstPlace.points) {
                sleep();
                System.out.println(player.getName() + " rolls " + player.die1 + " and " +
                        player.die2 + ", score now totaling " + player.getPoints());
            }
            if(firstPlace.getPoints() >= limit){
                current = list.frontNode;
            }
            else {
                current = current.next;
                player = (Player) current.data;
            }
        } while (current != list.frontNode && player.getPoints() < limit);

        sleep();
        System.out.println(firstPlace.getName() + " rolls " + firstPlace.die1 + " and " +
                firstPlace.die2 + ", score now totaling " + firstPlace.getPoints() + "...new high player!");

        winner(firstPlace ,limit);

    }

    //Displays the winner
    private static void winner(Player first,int limit){
        if(first.getPoints() >= limit) {
            sleep();
                System.out.println("\n"+first.getName() + " wins with a score of " + first.getPoints());
        }
    }

    //One sleep variable to change all sleep times
    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}




