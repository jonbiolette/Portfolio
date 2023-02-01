import java.util.LinkedList;
import java.util.Iterator;

public class LinkedIntList {
        IntNode front;
        int size;

        public LinkedIntList(){

            clear();
        }

        public void clear(){
            front = null;
            size = 0;
        }

        public static class IntNode{
            int data;
            IntNode next;
            public IntNode(int data){
                this.data = data;
            }
        }

        public int size(){
            return size;
        }


        public int countNode(){
            int nodeCount=0;
            IntNode current = front;
            while(current != null){
                current = current.next;
                nodeCount++;
            }
            return nodeCount;
        }

        public void add(int data){
            if (front == null){
                addAtFront(data);
            }
            else{
                IntNode newNode = new IntNode(data);
                IntNode current = front;
                while(current.next != null){
                    current = current.next;
                }
                current.next = newNode;
            }
        }


        public void addAtFront(int data){
            IntNode newNode = new IntNode(data);
            newNode.data = data;
            newNode.next = front;
            front = newNode;
            size++;
        }


        public void remove(int data){
            //to remove look beyond current
            //current.next = current.next.next;

        }

        }
