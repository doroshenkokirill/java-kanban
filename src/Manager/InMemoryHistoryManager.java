package manager;

import Interfaces.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_SIZE = 10;

    private final LinkedList<Task> browsingHistory = new LinkedList<>();

    @Override
    public void add(Task task) { // наполнение истории
        browsingHistory.addLast(task);
        if (browsingHistory.size() > MAX_HISTORY_SIZE) {
            browsingHistory.removeFirst(); // записей > 10 => удаляем 1-й элемент
        }
    }
    @Override
    public void remove(int id) {
        browsingHistory.remove(id);
    }
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(browsingHistory);
    }
}
