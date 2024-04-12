package Tasks;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int epicId, String name, String description) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public String toStringForFile() {
        return String.format("%s,%s,%s,%s,%s,%s", getId(), TaskTypesList.SUBTASK, getName(), getStatus(), getDescription(), epicId);
    }

    @Override
    public String toString() {
        return "Задача: id = '" + getId() + "', name = '" + getName() + "', description = '" + getDescription() +
                "', status = '" + getStatus() + "'.\n";
    }
}
