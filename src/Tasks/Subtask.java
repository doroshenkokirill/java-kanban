package Tasks;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int epicId, String name, String description) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.epicId;
    }
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Задача: id = '" + getId() + "', name = '" + name + "', description = '" + description +
                "', status = '" + status + "'.\n";
    }
}
