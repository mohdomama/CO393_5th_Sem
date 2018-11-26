import java.util.*;

class FairScheduler {
    private RBTree tree = new RBTree();
    private int timeQantum, nProcesses;
    private Scanner input = new Scanner(System.in);
    private RBTree.Node minVrtime = tree.new Node(99999);

    public void input() {
        System.out.println("Enter total number of processes, timeQantum");
        nProcesses = input.nextInt();
        timeQantum = input.nextInt();

        int timeSlice = (timeQantum / nProcesses); 

        System.out.println("Enter burst time and priority of each process:");

        for (int i = 0; i < nProcesses ; i++) { 
            System.out.print("Process " + (i+1) + " : ");
            int bTime = input.nextInt();         
            int priority = input.nextInt();
            int vrTime = timeSlice * priority;

            RBTree.Node node = tree.new Node(vrTime);
            node.rTime = bTime;
            node.pid = i+1;
            
            tree.insertNode(node);

        }  

        System.out.println("\nThe red black tree formed is:");
        tree.printTree();   
    }

    public void run() {
        tree.runScheduler(timeQantum);
    }

}

public class RBTree {

    private final boolean BLACK = true;
    private final boolean RED = false;
    public int size = 0;

    class Node {
        int vrTime, bTime, pid, rTime, priority;
        boolean color = BLACK;
        Node left = nil, right = nil, parent = nil;
        Node(int vrTime) {
            this.vrTime = vrTime;
        }
    }
    private final Node nil = new Node(-1);

    private Node root = nil;

    public void insert(int vrTime) {
        
        Node newNode = new Node(vrTime);

        insertNode(newNode);
        //insertFixup(newNode);
    }    

    public void insertNode(Node node) {
        size++;
        Node y = nil;
        Node x = root;
        
        // Y is always one node above x
        while (x != nil) {
            y = x;
            if (node.vrTime < x.vrTime) {
                x = x.left;
            }
            else {
                x = x.right;
            }
        }

        node.parent = y;

        if (y == nil) {
            root = node;
        }

        else if (node.vrTime < y.vrTime) {
            y.left = node;
        }

        else {
            y.right = node;
        }

        node.left = nil;
        node.right = nil;
        node.color = RED; // Why ?

        insertFixup(node);
    }


    public void insertFixup(Node node) {
        while (node.parent.color == RED) {
            if (node.parent == node.parent.parent.left) { // if parent is left child of grand parent
                Node y = node.parent.parent.right;
                if (y.color == RED) {                     // CASE 1: if uncle is red
                    node.parent.color = BLACK;
                    y.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                }
                else{
                    if (node == node.parent.right) {      // CASE 2: if new node is the right child
                        node = node.parent;
                        leftRotate(node);
                    }

                    node.parent.color = BLACK;            // CASE 2: if new node is the left child
                    node.parent.parent.color = RED;
                    rightRotate(node.parent.parent);
                }
            }

            else {                                         // Symmetrically opposite cases
                Node y = node.parent.parent.left;
                if (y.color == RED) {
                    node.parent.color = BLACK;
                    y.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                }
                else{
                    if (node == node.parent.left) {
                        node = node.parent;
                        rightRotate(node);
                    }

                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    leftRotate(node.parent.parent);
                }
            }


        } 
        root.color = BLACK;      
    }

    public void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        
        if (y.left != nil) {
            y.left.parent = x;
        }

        y.parent = x.parent;

        // If x is the root
        if (x.parent == nil) {
            root = y;
        }

        // If x is the left child of its parent
        else if (x == x.parent.left) {
            x.parent.left = y;
        }

        // If x if the right child of its parent
        else {
            x.parent.right = y;
        }
        
        y.left = x;
        x.parent = y;
    }

    public void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        
        if (y.right != nil) {
            y.right.parent = x;
        }

        y.parent = x.parent;

        // If x is the root
        if (x.parent == nil) {
            root = y;
        }

        // If x is the left child of its parent
        else if (x == x.parent.left) {
            x.parent.left = y;
        }

        // If x if the right child of its parent
        else {
            x.parent.right = y;
        }
        
        y.right = x;
        x.parent = y;
    }

    public void transplant(Node u, Node v) {
        if (u.parent == nil) {
            root = v;
        }
        else if (u == u.parent.left) {
            u.parent.left = v;
        }
        else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    public void delete(int vrTime) {
        Node node = searchNode(vrTime);
        if (node == null){
            System.out.println("Not Found");
            return;
        }

        else {
            
            deleteNode(node);
        }
    }    


    public void deleteNode(Node node) {
        size--;
        Node y = node;
        Node x;                                 // x point to the original position of y
        boolean yOrgColor = y.color;
        if (node.left == nil) {                 // If node has only right child
            x = node.right;
            transplant(node, node.right);
        }
        else if (node.right == nil) {           // If node has only left child
            x = node.left;
            transplant(node, node.left);
        }

        else {
            y = treeMinimum(node.right);        // Find the successor of node
            yOrgColor = y.color;
            x = y.right;

            if (y.parent == node) {
                x.parent = y;
            }
            else {
                transplant(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }
            transplant(node, y);
            y.left = node.left;
            y.left.parent = y;
            y.color = node.color;
        }

        if (yOrgColor == BLACK) {
            deleteFixup(x);
        }
    }

    public void deleteFixup(Node x) {
        while (x != root && x.color == BLACK) {
            Node w;
            if (x == x.parent.left) {
                w = x.parent.right;
                if(w.color == RED) {                                    // CASE 1: x's sibling is red
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {  // CASE 2: x's sibling is black and both if sibling's child are black
                    w.color = RED;
                    x = x.parent;
                }
                else {
                    if (w.right.color == BLACK) {                       // CASE 3: x's sibling w is black. W's left red, right black
                        w.left.color = BLACK;
                        w.color = RED;
                        rightRotate(w);
                        w = x.parent.right;

                    }
                    w.color = x.parent.color;                           // CASE 4: x's sibling is balck. W's right child is red.
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            }


            if (x == x.parent.right) {       // Symmetrically opposite
                w = x.parent.left;
                if(w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                }
                else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        leftRotate(w);
                        w = x.parent.left;

                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    public Node searchNode(Node presentNode, int toFind) {
        if (presentNode == nil) {
            return null;
        }
        else {
            if (toFind < presentNode.vrTime) {
                return searchNode(presentNode.left, toFind);
            }
            else if (toFind > presentNode.vrTime) {
                return searchNode(presentNode.right, toFind);
            }
            else {
                return presentNode;
            }
        }
    }
    public Node searchNode(int toFind) {
        return searchNode(root, toFind);
    }

    public Node treeMinimum(Node node) {
        while (node.left != nil) {
            node = node.left;
        }
        return node;
    }

    public void printTree(Node node) {
        if (node == nil){
            return;
        }
        printTree(node.left);
        System.out.println("Value: " + node.pid + " \t| Left Child: " + node.left.pid + " \t| Right Child: " + node.right.pid + "\t| Color: " + (node.color ? "BLACK":"RED"));
        printTree(node.right);

    }

    public void printTree() {
        printTree(root);
        System.out.println();
    }

    public void runScheduler(int timeQantum) {
        while (size > 0){
            int nReadyProcesses = size;
            int timeSlice = timeQantum/size;
            for (int i = 0; i < nReadyProcesses; i++) {
                Node min = treeMinimum(root);
                System.out.println("Running process " + min.pid + " for " + timeSlice + " cycles");
                min.vrTime += timeSlice*min.priority;
                min.rTime -=   timeSlice;
                deleteNode(min);
                if (min.rTime > 0) {
                    insertNode(min);
                }
                printTree();
            }

        }
    }

    public static void main(String[] args) {
        
        /*
        RBTree tree = new RBTree();
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
        tree.insert(4);
        tree.printTree();

        System.out.println(tree.searchNode(3).vrTime);
        System.out.println(tree.searchNode(1).vrTime);
        System.out.println(tree.searchNode(4).vrTime);

        tree.delete(3);
        tree.delete(1);
        tree.delete(4);
        tree.delete(4);
        tree.printTree();
        //tree.delete(2);
        */

        FairScheduler fairScheduler = new FairScheduler();
        fairScheduler.input();
        fairScheduler.run();


    }
}