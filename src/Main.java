import Manager.TaskStatusList;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Manager.TaskManager;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskContent(taskManager); // наполнение и работа с задачами
    }

    public static void taskContent(TaskManager taskManager) {

        // Tasks.Task с 2 задачами
        ArrayList<String> tasksList = new ArrayList<>();
        tasksList.add("Помыть посуду");
        Task task = new Task("Задача - 1: ", tasksList.get(0));
        taskManager.createNewTask(task);
        ArrayList<String> tasksList1 = new ArrayList<>();
        tasksList1.add("Позвонить бабушке");
        Task task1 = new Task("Задача - 2: ", tasksList1.get(0));
        taskManager.createNewTask(task1);

        // Tasks.Epic с 2 подзадачами
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

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.println("Выберите команду:");
            int command = scanner.nextInt();

            switch (command) {
                case 1:
                    System.out.println("Выводим все Tasks:");
                    System.out.println(taskManager.getAllTasks());
                    break;

                case 2:
                    System.out.println("Выводим все Epics:");
                    System.out.println(taskManager.getAllEpics());
                    break;

                case 3:
                    System.out.println("Выводим все Subtasks:");
                    System.out.println(taskManager.getAllSubtasks());
                    break;

                case 4:
                    taskManager.clearAllSubtasks(); // Удаляем все Subtasks
                    System.out.println("Список Subtasks после удаления:");
                    System.out.println(taskManager.getAllSubtasks()); // проверка наличия
                    break;

                case 5:
                    taskManager.clearAllSubtasks();
                    taskManager.clearAllEpics(); // Удаляем все Epics - если удалены все Tasks.Epic удаляются и Tasks.Subtask
                    System.out.println("Список Epics после удаления:");
                    System.out.println(taskManager.getAllEpics()); // проверка наличия
                    break;

                case 6:
                    taskManager.clearAllTasks(); // Удаляем все Tasks
                    System.out.println("Список Tasks после удаления:");
                    System.out.println(taskManager.getAllTasks()); // проверка наличия
                    break;

                case 7:
                    System.out.println("Tasks.Task с id = '2':");
                    System.out.println(taskManager.getTaskById(2)); // вывод Tasks.Task с id = '2'
                    System.out.println("Tasks.Epic с id = '3':");
                    System.out.println(taskManager.getEpicById(3)); // вывод Tasks.Epic с id = '3'
                    System.out.println("Tasks.Subtask с id = '7':");
                    System.out.println(taskManager.getSubtaskById(7)); // вывод Tasks.Subtask с id = '7'
                    break;

                case 8:
                    taskManager.removeTaskById(2); // Удаляем Tasks.Task с id = '2'
                    System.out.println("Tasks.Task с id = '2' удалена.");
                    taskManager.removeEpicById(3); // Удаляем Tasks.Epic с id = '3'
                    System.out.println("Tasks.Epic с id = '3' удалена.");
                    taskManager.removeSubtaskById(7); // Удаляем Tasks.Subtask с id = '7'
                    System.out.println("Tasks.Subtask с id = '7' удалена.");
                    break;

                case 9:
                    taskManager.updateTask(task, String.valueOf(TaskStatusList.DONE)); // присваиваем task статус "DONE"
                    taskManager.updateEpic(epic1, String.valueOf(TaskStatusList.IN_PROGRESS)); // присваиваем epic1 статус "IN_PROGRESS"
                    taskManager.updateSubtask(subtask2, String.valueOf(TaskStatusList.DONE)); // присваиваем subtask2 статус "DONE"
                    break;

                case 10:
                    System.out.println("Список Tasks.Subtask в Tasks.Epic с id = '3':");
                    System.out.println(taskManager.getSubtasksByEpicId(3)); // вызываем все Tasks.Subtask в Tasks.Epic c id = '6'
                    break;

                case 11:
                    scanner.close();
                    return;

                default:
                    System.out.println("Такой функции нет!");
            }
        }
    }
    private static void printMenu() {
        System.out.println("Меню:");
        System.out.println("1 - Получить список всех Tasks");
        System.out.println("2 - Получить список всех Epics");
        System.out.println("3 - Получить список всех Subtasks");
        System.out.println("4 - Удаление Subtasks");
        System.out.println("5 - Удаление Epics");
        System.out.println("6 - Удаление Tasks");
        System.out.println("7 - Вывод задачи по id");
        System.out.println("8 - Удаление задачи по id");
        System.out.println("9 - Перезапись задачи");
        System.out.println("10 - Получение Subtasks для Tasks.Epic");
        System.out.println("11 - Выход");
    }
}