package Manager;

import Interfaces.HistoryManager;
import Tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> browsingHistory = new LinkedList<>();

    @Override
    public void add(Task task) { // наполнение истории
        int historySize = 10;
        browsingHistory.addLast(task);
        if (browsingHistory.size() > historySize) {
            browsingHistory.removeFirst(); // записей > 10 => удаляем 1-й элемент
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(browsingHistory);
    }
}
