package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    private int id;
    private String name;
    private String description;
    private TaskStatusList status;
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    public Task(String name, String description, String startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = TaskStatusList.NEW;
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        this.duration = Duration.ofMinutes(duration);
    }

    public Task() {
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

    public TaskStatusList getStatus() {
        return status;
    }

    public void setStatus(TaskStatusList status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        if (startTime==null) return LocalDateTime.MIN;
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        if (duration==null) return Duration.ZERO;
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) return LocalDateTime.MIN;
        return getStartTime().plusMinutes(duration.toMinutes());
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", getId(), TaskTypesList.TASK, getName(),
                getStatus(), getDescription(), getStartTime().format(DATE_TIME_FORMATTER),
                getDuration().toMinutes(), getEndTime().format(DATE_TIME_FORMATTER), "");
    }


}
