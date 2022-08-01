package service;

import interfaces.HistoryManager;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node<Task>> browsingHistory = new HashMap<>();
    private Node<Task> first;
    private Node<Task> last;

    private void linkLast(Task task) { // != не скопировал из класса LinkedList
        final Node<Task> l = last;
        final Node<Task> newNode = new Node<>(l, task, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> node = first;
        while (node != null){
            tasks.add(node.data);
            node = node.next;
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if(node != null) {
            if(node.prev != null){
                node.prev.next = node.next;
            } else {
                node.next.prev = null;
                first = node.next;
            }

            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                node.prev.next = null;
                last = node.prev;
            }
        }
    }

    private static class Node<T extends Task> { // != скопировал из класса LinkedList
        public Task data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public void addTask(Task task) {
        removeTask(task.getId());
        linkLast(task);
        browsingHistory.put(task.getId(), last);
    }

    @Override
    public void removeTask(int id) {
        Node<Task> node = browsingHistory.get(id);
        removeNode(node);
        browsingHistory.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }
}