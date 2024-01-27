package manager;

import Interfaces.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> customBrowsingHistory = new CustomLinkedList<>();
    HashMap <Integer, Node<Task>> browsingHistoryMap = new HashMap<>();

    private static class CustomLinkedList<Task> {
        private Node<Task> head;
        private Node<Task> tail;
        private int size = 0;

        void linkLast(Task task) {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(task, oldTail, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
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

        List<Task> getTasks() {
            ArrayList<Task> tasksList = new ArrayList<>();
            if (head == null) {
                return tasksList;
            }
            Node<Task> currentNode = head;
            while (currentNode != null) {
                tasksList.add(currentNode.data);
                currentNode = currentNode.next;
            }
            return tasksList;
        }
    }

    private static class Node<Task> {
        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Task data, Node<Task> prev, Node<Task> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public void add(Task task) { // наполнение истории
        if (browsingHistoryMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        customBrowsingHistory.linkLast(task);
        browsingHistoryMap.put(task.getId(), customBrowsingHistory.tail);
    }
    @Override
    public void remove(int id) {
        if (browsingHistoryMap.get(id) != null) {
            Node<Task> someNodeToDelete = browsingHistoryMap.get(id);
            customBrowsingHistory.removeNode(someNodeToDelete);
        }
    }
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(customBrowsingHistory.getTasks());
    }
}
