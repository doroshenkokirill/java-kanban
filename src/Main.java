import Interfaces.TaskManager;
import manager.Manager;
import tasks.TaskStatusList;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefault();
        taskContent(taskManager); // наполнение и работа с задачами
    }

    public static void taskContent(TaskManager taskManager) {
        // Task с 2 задачами
        ArrayList<String> tasksList = new ArrayList<>();
        tasksList.add("Помыть посуду");
        Task task = new Task("Задача - 1: ", tasksList.get(0));
        taskManager.createNewTask(task);
        ArrayList<String> tasksList1 = new ArrayList<>();
        tasksList1.add("Позвонить бабушке");
        Task task1 = new Task("Задача - 2: ", tasksList1.get(0));
        taskManager.createNewTask(task1);

        // Epic с 2 подзадачами
        Epic epic = new Epic("Сбор вещей для переезда");
        taskManager.createNewEpic(epic);
        ArrayList<String> subtasksList = new ArrayList<>();
        subtasksList.add("Собрать коробки");
        subtasksList.add("Упаковать кошку");
        Subtask subtask = new Subtask(epic.getId(), "Сбор вещей для переезда", subtasksList.get(0));
        taskManager.createNewSubtask(subtask);
        Subtask subtask1 = new Subtask(epic.getId(), "Сбор вещей для переезда", subtasksList.get(1));
        taskManager.createNewSubtask(subtask1);

        // Epic1 с 1 подзадачей
        Epic epic1 = new Epic("Разбор вещей после переезда");
        taskManager.createNewEpic(epic1);
        ArrayList<String> subtasksList2 = new ArrayList<>();
        subtasksList2.add("Распаковать кошку");
        Subtask subtask2 = new Subtask(epic1.getId(), "Разбор вещей для переезда", subtasksList2.get(0));
        taskManager.createNewSubtask(subtask2);

        /* Не до конца понял зачем удалять консольный интерфейс. Ведь удобно просто создать 1 раз все задачи и
        каждый раз к нужной обращаться */

        // исправление и проверка setter
        epic1.setStatus(TaskStatusList.DONE);
        taskManager.updateEpic(epic1);

        //проверка истории
        taskManager.getTaskById(1); // вызываю 11 задач
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(7);
        System.out.println("История просмотров (10 последних):");
        for (Task someTask: taskManager.getHistory()) {
            System.out.println(someTask);
        }
    }
}