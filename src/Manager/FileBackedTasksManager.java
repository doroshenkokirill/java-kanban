package Manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Tasks.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static Tasks.TaskTypesList.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager taskManager = new FileBackedTasksManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Task")) {
                    Task task = createTaskFromString(line);
                    taskManager.createNewTask(task);
                } else if (line.contains("Epic")) {
                    Epic epic = (Epic) createTaskFromString(line);
                    taskManager.createNewEpic(epic);
                } else {
                    Subtask subtask = (Subtask) createTaskFromString(line);
                    taskManager.createNewSubtask(subtask);
                }
            }
        }
        return taskManager;
    }

    public static Task createTaskFromString(String value) {
        Task task;
        String[] elements = value.split(",");
        int id = Integer.parseInt(elements[0]);
        TaskTypesList type = TaskTypesList.valueOf(elements[1]);
        String name = elements[2];
        TaskStatusList status = TaskStatusList.valueOf(elements[3]);
        String description = elements[4];
        int epicId;
        if (type.equals(TASK)) {
            task = new Task(name, description);
            task.setId(id);
            task.setStatus(status);
        } else if (type.equals(EPIC)) {
            task = new Epic(name);
            task.setId(id);
            task.setStatus(status);
        } else {
            epicId = Integer.parseInt(elements[5]);
            task = new Subtask(epicId, name, description);
            task.setId(id);
            task.setStatus(status);
        }
        return task;
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] elements = value.split(",");
        List<Integer> historyId = new ArrayList<>();
        for (String element : elements) {
            historyId.add(Integer.parseInt(element));
        }
        return historyId;
    }

    public void save() throws ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Task task : getAllTasks()) {
                try {
                    fileWriter.write(task.toStringForFile() + "\n"); //создал новый метод
                } catch (IOException e) {
                    throw new ManagerSaveException("Ошибка при записи", e);
                }
            }
            for (Epic epic : getAllEpics()) {
                try {
                    fileWriter.write(epic.toStringForFile() + "\n");
                } catch (IOException e) {
                    throw new ManagerSaveException("Ошибка при записи", e);
                }
            }
            for (Subtask subtask: getAllSubtasks()) {
                try {
                    fileWriter.write(subtask.toStringForFile() + "\n");
                } catch (IOException e) {
                    throw new ManagerSaveException("Ошибка при записи", e);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при создании", e);
        }
    }

    // Методы которые требуют сохранения в файл
    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeTaskById(int Id) {
        super.removeTaskById(Id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeEpicById(int Id) {
        super.removeEpicById(Id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeSubtaskById(int Id) {
        super.removeSubtaskById(Id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }
}