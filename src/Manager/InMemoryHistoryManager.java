package Manager;

import Interfaces.HistoryManager;
import Tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    protected CustomLinkedList<Task> customBrowsingHistory = new CustomLinkedList<>();
    protected Map<Integer, Node<Task>> browsingHistoryMap = new HashMap<>();

    public static class CustomLinkedList<Task> {
        private Node<Task> head;
        private Node<Task> tail;
        private int size = 0;

        void linkLast(Task task) {
            Node<Task> oldTail = tail;
            Node<Task> newNode = new Node<Task>(task, oldTail, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
        }

        List<Task> getTasks() {
            ArrayList<Task> tasksList = new ArrayList<>();
            if (head == null) {
                return tasksList;
            }
            Node<Task> currentNode = head;
            while (currentNode != null) {
                tasksList.add(currentNode.info);
                currentNode = currentNode.next;
            }
            return tasksList;
        }

        void removeFirst() {
            Node<Task> nextHead = head.next;
            if (nextHead != null) {
                nextHead.prev = null;
                head.next = null;
                head = nextHead;
            } else {
                head = null;
                tail = null;
            }
            size--;
        }

        public void removeNode(Node<Task> node) {
            if (head.equals(node)) {
                removeFirst();
            } else if (tail.equals(node)) {
                removeLast();
            } else {
                Node<Task> prevNode = node.prev;
                Node<Task> nextNode = node.next;
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
                node.prev = null;
                node.next = null;
                size--;
            }
        }

        void removeLast() {
            Node<Task> prevTail = tail.prev;
            prevTail.next = null;
            tail.prev = null;
            tail = prevTail;
            size--;
        }
    }

    @Override
    public void add(Task task) {
        if (browsingHistoryMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        customBrowsingHistory.linkLast(task);
        browsingHistoryMap.put(task.getId(), customBrowsingHistory.tail);
    }

    @Override
    public void remove(int id) {
        if (browsingHistoryMap.get(id) != null) {
            customBrowsingHistory.removeNode(browsingHistoryMap.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(customBrowsingHistory.getTasks());
    }
}
