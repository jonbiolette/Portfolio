import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Comparable array[] = new Comparable[5];
        BstDup intTree = new BstDup();
        BstDup nameTree = new BstDup();
        try {
            Scanner read = new Scanner(new File("nums25.txt"));
            while (read.hasNext()) {
                intTree.add(read.next());
            }
            read = new Scanner(new File("name25.txt"));
            while (read.hasNext()) {
                nameTree.add(read.next());
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.out.println("Please enter valid file path");
        }
        displayData(intTree,nameTree,array);
    }
        public static void displayData(BstDup intTree, BstDup nameTree, Comparable[] array){

            System.out.println("\nintTree in order");
            System.out.println(Arrays.toString(intTree.getAllDataInOrder(array)));
            intTree.delete(28);
            System.out.println("\nintTree in order after deleting 28");
            System.out.println(Arrays.toString(intTree.getAllDataInOrder(array)));
            intTree.delete(36);
            System.out.println("\nintTree in order after deleting 36");
            System.out.println(Arrays.toString(intTree.getAllDataInOrder(array)));
            intTree.delete(49);
            System.out.println("\nintTree in order after deleting 49");
            System.out.println(Arrays.toString(intTree.getAllDataInOrder(array)));
            intTree.delete(76);
            System.out.println("\nintTree in order after deleting 76");
            System.out.println(Arrays.toString(intTree.getAllDataInOrder(array)));
            System.out.println("\nMatch count of 62 before deleteAll:  " + intTree.getMatchCount(62));
            intTree.deleteAll(62);
            System.out.println("\nMatch count of 62 after deleteAll:  " + intTree.getMatchCount(62));
            System.out.println("\nintTree in order after deleting all 62's");
            System.out.println(Arrays.toString(intTree.getAllDataInOrder(array)));
            intTree.toFile(intTree.getAllDataInOrder(array),"intTreeInfo");

            System.out.println("\n" + "-".repeat(75) + "\n");

            System.out.println("\nnameTree in order");
            System.out.println(Arrays.toString(nameTree.getAllDataInOrder(array)));
            nameTree.delete("Billie");
            System.out.println("\nnameTree in order after deleting Billie");
            System.out.println(Arrays.toString(nameTree.getAllDataInOrder(array)));
            nameTree.delete("Harold");
            System.out.println("\nnameTree in order after deleting Harold");
            System.out.println(Arrays.toString(nameTree.getAllDataInOrder(array)));
            nameTree.delete("Lillian");
            System.out.println("\nnameTree in order after deleting Lillian");
            System.out.println(Arrays.toString(nameTree.getAllDataInOrder(array)));
            nameTree.delete("Tom");
            System.out.println("\nnameTree in order after deleting Tom");
            System.out.println(Arrays.toString(nameTree.getAllDataInOrder(array)));
            System.out.println("\nMatch count of Jon before deleteAll:  " + nameTree.getMatchCount("Jon"));
            nameTree.deleteAll("Jon");
            System.out.println("\nMatch count of Jon after deleteAll:  " + nameTree.getMatchCount("Jon"));
            System.out.println("\nnameTree in order after deleting all Jon's");
            System.out.println(Arrays.toString(nameTree.getAllDataInOrder(array)));
            nameTree.toFile(nameTree.getAllDataInOrder(array),"nameTreeInfo");
        }

    }
