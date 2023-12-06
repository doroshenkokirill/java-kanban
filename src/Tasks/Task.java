package Tasks;

import Manager.StatusList;

public class Task {

    private int id;
    private String name, description, status;

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
        this.status = String.valueOf(StatusList.NEW);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return "Задача: id = '" + getId() + "', name = '" + getName() + "', description = '" + getDescription() +
                "', status = '" + getStatus() + "'.";
    }
}
