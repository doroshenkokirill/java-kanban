package tasks;
import manager.InMemoryTaskManager;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksId;

    public Epic(String name) {
        this.setId(0);
        this.setName(name);
        this.subtasksId = new ArrayList<>();
        this.setStatus(TaskStatusList.NEW);
    }

    public ArrayList<Integer> getSubtasksList() {
        return subtasksId;
    }

    public void setSubtaskIdList(int id) {
        this.subtasksId.add(id);
    }

    public void checkEpicStatus(HashMap<Integer, Subtask> allSubtasks) {

        if (subtasksId.isEmpty()) { // если Subtask'ов нет -> всегда "NEW"
            this.setStatus(TaskStatusList.NEW);
            return;
        }

        for (int id : subtasksId) {
            if (!allSubtasks.get(id).getStatus().equals(TaskStatusList.NEW)) { // если не "NEW" -> "IN_PROGRESS"
                this.setStatus(TaskStatusList.IN_PROGRESS);
                break;
            }
        }
        for (int id : subtasksId) {
            if (!allSubtasks.get(id).getStatus().equals(TaskStatusList.DONE)) {
                return;
            }
        }
        this.setStatus(TaskStatusList.DONE);
    }

    public void clearAllSubtasks() {
        this.subtasksId.clear();
    }

    public String toString(InMemoryTaskManager taskManager) {
        String component = "Задача: id = '" + getId() + "', name = '" + getName() + "', description = '" + getDescription() +
                "status = '" + getStatus() + "'.\n";
        for (Integer id: subtasksId){
            component += taskManager.getSubtaskById(id).toString();
        }
        return component;
    }
}
