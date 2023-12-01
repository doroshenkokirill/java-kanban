package Tasks;

import Manager.TaskManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId;

    public Epic(String name) {
        this.setId(0);
        this.name = name;
        this.subtasksId = new ArrayList<>();
        this.status = "NEW";
    }

    public Epic(Epic epic) {
        this.setId(epic.getId());
        this.name = epic.name;
        this.status = epic.status;
    }

    public ArrayList<Integer> getSubtasksList() {
        return subtasksId;
    }

    public void setSubtaskIdList(int id) {
        this.subtasksId.add(id);
    }

    public void checkEpicStatus(HashMap<Integer, Subtask> allSubtasks) {

        if (subtasksId.isEmpty()) { // если Subtask'ов нет -> всегда "NEW"
            this.status = "NEW";
            return;
        }

        for (int id : subtasksId) {
            if (!allSubtasks.get(id).getStatus().equals("NEW")) { // если не "NEW" -> "IN_PROGRESS"
                this.status = "IN_PROGRESS";
                break;
            }
        }
        for (int id : subtasksId) {
            if (!allSubtasks.get(id).getStatus().equals("DONE")) {
                return;
            }
        }
        this.status = "DONE";
    }

    public void clearAllSubtasks() {
        this.subtasksId.clear();
    }
    public String toString(TaskManager taskManager) {
        String component = "Задача: id = '" + getId() + "', name = '" + name + "', description = '" + description +
                "status = '" + status + "'.\n";
        for (Integer id: subtasksId){
            component += taskManager.getSubtaskById(id).toString();
        }
        return component;
    }
}
