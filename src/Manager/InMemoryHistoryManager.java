package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> browsingHistory = new ArrayList<>();
    @Override
    public void add(Task task) { // наполнение истории
        browsingHistory.add(task);
        if (browsingHistory.size() > 10) browsingHistory.remove(0); // записей > 10 => удаляем 1-й элемент
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(browsingHistory);
    }
}
