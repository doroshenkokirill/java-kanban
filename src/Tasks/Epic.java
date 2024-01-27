package Tasks;
import Manager.InMemoryTaskManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Epic extends Task {
    private final List<Integer> subtasksId;

    public Epic(String name) {
        this.setId(0);
        this.setName(name);
        this.subtasksId = new ArrayList<>();
        this.setStatus(TaskStatusList.NEW);
    }

    public List<Integer> getSubtasksList() {
        return subtasksId;
    }

    public void setSubtaskIdList(int id) {
        this.subtasksId.add(id);
    }

    public void checkEpicStatus(Map<Integer, Subtask> allSubtasks) {

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
