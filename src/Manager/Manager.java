package Manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;

public final class Manager {

    private Manager() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
