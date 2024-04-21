import Interfaces.TaskManager;
import Manager.Manager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefault();
        taskContent(taskManager); // наполнение и работа с задачами
    }

    public static void taskContent(TaskManager taskManager) {
    }
}