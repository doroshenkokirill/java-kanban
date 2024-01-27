import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefault();
        taskContent(taskManager); // наполнение и работа с задачами
    }

    public static void taskContent(TaskManager taskManager) {
        // Task с 2 задачами
        List<String> tasksList = new ArrayList<>();
        tasksList.add("Помыть посуду");
        Task task = new Task("Задача - 1: ", tasksList.get(0));
        taskManager.createNewTask(task);
        List<String> tasksList1 = new ArrayList<>();
        tasksList1.add("Позвонить бабушке");
        Task task1 = new Task("Задача - 2: ", tasksList1.get(0));
        taskManager.createNewTask(task1);

        // Epic с 3 подзадачами
        Epic epic = new Epic("Сбор вещей для переезда");
        taskManager.createNewEpic(epic);
        ArrayList<String> subtasksList = new ArrayList<>();
        subtasksList.add("Собрать коробки");
        subtasksList.add("Упаковать кошку");
        subtasksList.add("Упаковать Алису");
        Subtask subtask = new Subtask(epic.getId(), "Сбор вещей для переезда", subtasksList.get(0));
        taskManager.createNewSubtask(subtask);
        Subtask subtask1 = new Subtask(epic.getId(), "Сбор вещей для переезда", subtasksList.get(1));
        taskManager.createNewSubtask(subtask1);
        Subtask subtask2 = new Subtask(epic.getId(), "Сбор вещей для переезда", subtasksList.get(2));
        taskManager.createNewSubtask(subtask2);

        // Epic1 с 1 подзадачей
        Epic epic1 = new Epic("Разбор вещей после переезда");
        taskManager.createNewEpic(epic1);
        List<String> subtasksList2 = new ArrayList<>();
        subtasksList2.add("Распаковать кошку");
        Subtask subtask3 = new Subtask(epic1.getId(), "Разбор вещей для переезда", subtasksList2.get(0));
        taskManager.createNewSubtask(subtask3);

        // проверка
        taskManager.getTaskById(1); // не должна попасть в историю
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);

        taskManager.getEpicById(3); // не должна попасть в историю
        taskManager.getEpicById(7);
        taskManager.getEpicById(3);

        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5); // не должна попасть в историю
        taskManager.getSubtaskById(6);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(8);

        // выводим историю просмотров без повторов
        List<Task> browsingHistory = taskManager.getHistory();
        System.out.println("История просмотров:");
        for (Task someTask: browsingHistory) {
            System.out.println(someTask);
        }

        // выводим историю просмотров после удаления Задачи из истории просмотров
        browsingHistory.remove(0);
        System.out.println("История просмотров после удаления задачи:");
        for (Task someTask1: browsingHistory) {
            System.out.println(someTask1);
        }

        // выводим историю просмотров после удаления Эпика и подзадач из истории просмотров
        browsingHistory.remove(2);
        browsingHistory.remove(2);
        browsingHistory.remove(2);
        browsingHistory.remove(2);
        System.out.println("История просмотров после удаления эпика и подзадач:");
        for (Task someTask2: browsingHistory) {
            System.out.println(someTask2);
        }
    }
}