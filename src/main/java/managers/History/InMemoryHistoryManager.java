package managers.History;

import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private Node first = null;
    private Node last = null;
    private HashMap<Long, Node> nodesById=new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if(nodesById.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node newNode=new Node(task);
        linkLast(newNode);
        nodesById.put(newNode.getTask().getId(), newNode);
    }

    @Override
    public void remove(Long id) {
        Node node=nodesById.get(id);
        nodesById.remove(id);
        removeNode(node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }



    private void linkLast(Node newNode) {

        if (isEmpty()) {
            first = newNode;
        } else {
            last.setNext(newNode);
            newNode.setPred(last);
        }
        last = newNode;
    }

    private boolean isEmpty() {
        return (last == null);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList=new ArrayList<>();
        Node current = first;
        while (current != null) {
            tasksList.add(current.getTask());
            current = current.getNext();
        }
        return tasksList;
    }

    private void removeNode(Node node) {
        if(isEmpty()||node==null){
            return;
        }
        if(node==last&&node==first){
            last=null;
            first=null;
            return;
        }
        if (node == last) {
            if (node.getPred() != null) {
                node.getPred().setNext(null);
            }
            last = node.getPred();
            return;
        }
        if (node == first) {
            if (node.getNext() != null) {
                node.getNext().setPred(null);
            }
            first = node.getNext();
            return;
        }
        Node current = node.getNext();
        node.getPred().setNext(current);
        current.setPred(node.getPred());
    }


}
