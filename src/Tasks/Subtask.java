package Tasks;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int epicId, String name, String description, String startTime, long duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", getId(), TaskTypesList.SUBTASK, getName(),
                getStatus(), getDescription(), getStartTime().format(DATE_TIME_FORMATTER),
                getDuration().toMinutes(), getEndTime().format(DATE_TIME_FORMATTER), epicId);
    }
}
