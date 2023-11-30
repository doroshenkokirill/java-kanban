import java.util.ArrayList;
import java.util.HashMap;

class Epic extends Task{
    private ArrayList<Integer> subtasksList;

    public Epic(String name) {
        this.id = 0;
        this.name = name;
        this.subtasksList = new ArrayList<>();
        this.status = "NEW";
    }

    public Epic(Epic epic) {
        this.id = epic.id;
        this.name = epic.name;
        this.status = epic.status;
    }

    public ArrayList<Integer> getSubtasksList() {
        return subtasksList;
    }

    public void setSubtaskIdList(int id) {
        this.subtasksList.add(id);
    }

    public void checkEpicStatus(HashMap<Integer, Subtask> allSubtasks) {

        if (subtasksList.isEmpty()) { // если Subtask'ов нет -> всегда "NEW"
            this.status = "NEW";
            return;
        }

        for (int id : subtasksList) {
            if (!allSubtasks.get(id).getStatus().equals("NEW")) { // если не "NEW" -> "IN_PROGRESS"
                this.status = "IN_PROGRESS";
                break;
            }
        }
        for (int id : subtasksList) {
            if (!allSubtasks.get(id).getStatus().equals("DONE")) {
                return;
            }
        }
        this.status = "DONE";
    }

    public void clearAllSubtasks() {
        this.subtasksList.clear();
    }
    public String toString(TaskManager taskManager) {
        String component = "Задача: id = '" + id + "', name = '" + name + "', description = '" + description +
                "status = '" + status + "'.\n";
        for (Integer id: subtasksList){
            component += taskManager.getSubtaskById(id).toString();
        }
        return component;
    }
}
