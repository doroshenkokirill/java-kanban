package Tasks;

public class Task {

    private int id;
    private String name;
    private String description;
    private TaskStatusList status;

    public Task() {
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatusList.NEW;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected TaskStatusList getStatus() {
        return status;
    }
    public void setStatus(TaskStatusList status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Задача: id = '" + getId() + "', name = '" + getName() + "', description = '" + getDescription() +
                "', status = '" + getStatus() + "'.";
    }
}
