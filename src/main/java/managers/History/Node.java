package managers.History;

import Tasks.Task;

public class Node {
    private Task task;
    private Node next;
    private Node pred;

    public Node() {
    }

    public Node(Task task, Node next, Node last) {
        this.task = task;
        this.next = next;
        this.pred = last;
    }

    public Node(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPred() {
        return pred;
    }

    public void setPred(Node pred) {
        this.pred = pred;
    }
}
