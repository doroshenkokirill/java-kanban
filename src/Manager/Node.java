package Manager;

public class Node<Task> {
    protected Task info;
    public Node<Task> prev;
    public Node<Task> next;

    public Node(Task info, Node<Task> prev, Node<Task> next) {
        this.info = info;
        this.prev = prev;
        this.next = next;
    }
}