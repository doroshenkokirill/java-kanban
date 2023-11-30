class Subtask extends Task{

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


    public String toString() {
        return "Задача: id = '" + id + "', name = '" + name + "', description = '" + description +
                "', status = '" + status + "'.\n";
    }
}
