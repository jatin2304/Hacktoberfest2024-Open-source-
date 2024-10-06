class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}

class CircularLinkedList {
    private Node head = null;

    // Add a node at the end
    public void add(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            head.next = head; // Point to itself, making it circular
        } else {
            Node temp = head;
            while (temp.next != head) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.next = head; // Make it circular
        }
    }

    // Display the circular linked list
    public void display() {
        if (head == null) {
            System.out.println("List is empty.");
            return;
        }
        Node temp = head;
        do {
            System.out.print(temp.data + " ");
            temp = temp.next;
        } while (temp != head);
        System.out.println();
    }

    // Delete a node by value
    public void delete(int key) {
        if (head == null) return;

        Node current = head;
        Node previous = null;

        // If the head node itself holds the key to be deleted
        if (current.data == key && current.next == head) {
            head = null; // List becomes empty
            return;
        }

        // Search for the key to be deleted
        do {
            if (current.data == key) {
                if (previous != null) {
                    previous.next = current.next;
                }
                // If the node to be deleted is the head node
                if (current == head) {
                    previous = head;
                    while (previous.next != head) {
                        previous = previous.next;
                    }
                    head = current.next; // Change head
                    previous.next = head; // Update last node's next
                }
                return;
            }
            previous = current;
            current = current.next;
        } while (current != head);
    }

    public static void main(String[] args) {
        CircularLinkedList cll = new CircularLinkedList();
        cll.add(1);
        cll.add(2);
        cll.add(3);
        cll.add(4);
        
        System.out.print("Circular Linked List: ");
        cll.display();

        cll.delete(3);
        System.out.print("After deleting 3: ");
        cll.display();
        
        cll.delete(1);
        System.out.print("After deleting 1: ");
        cll.display();
    }
}
`
