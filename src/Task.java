class Task {

    protected int id;
    protected String name, description, status;

    public Task() {
    }

    public Task(Task task) {
        this.name = task.name;
        this.description = task.description;
        this.id = task.id;
        this.status = task.status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }
    protected int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Задача: id = '" + id + "', name = '" + name + "', description = '" + description +
                "', status = '" + status + "'.";
    }
}
