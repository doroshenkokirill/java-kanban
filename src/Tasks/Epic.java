package Tasks;
import Manager.TaskManager;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksId;

    public Epic(String name) {
        this.setId(0);
        this.setName(name);
        this.subtasksId = new ArrayList<>();
        this.setStatus("NEW");
    }

    public ArrayList<Integer> getSubtasksList() {
        return subtasksId;
    }

    public void setSubtaskIdList(int id) {
        this.subtasksId.add(id);
    }

    public void checkEpicStatus(HashMap<Integer, Subtask> allSubtasks) {

        if (subtasksId.isEmpty()) { // если Subtask'ов нет -> всегда "NEW"
            this.setStatus("NEW");
            return;
        }

        for (int id : subtasksId) {
            if (!allSubtasks.get(id).getStatus().equals("NEW")) { // если не "NEW" -> "IN_PROGRESS"
                this.setStatus("IN_PROGRESS");
                break;
            }
        }
        for (int id : subtasksId) {
            if (!allSubtasks.get(id).getStatus().equals("DONE")) {
                return;
            }
        }
        this.setStatus("DONE");
    }

    public void clearAllSubtasks() {
        this.subtasksId.clear();
    }

    public String toString(TaskManager taskManager) {
        String component = "Задача: id = '" + getId() + "', name = '" + getName() + "', description = '" + getDescription() +
                "status = '" + getStatus() + "'.\n";
        for (Integer id: subtasksId){
            component += taskManager.getSubtaskById(id).toString();
        }
        return component;
    }
}
