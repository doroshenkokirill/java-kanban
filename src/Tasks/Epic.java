package Tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Epic extends Task {
    private final List<Integer> subtasksId;
    private LocalDateTime endTime;

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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void clearAllSubtasks() {
        this.subtasksId.clear();
    }

    @Override
    public LocalDateTime getEndTime() {
        if (this.endTime==null) return LocalDateTime.MIN;
        return endTime;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", getId(), TaskTypesList.EPIC, getName(),
                getStatus(), getDescription(), getStartTime().format(DATE_TIME_FORMATTER),
                getDuration().toMinutes(), getEndTime().format(DATE_TIME_FORMATTER), "");
    }
}
