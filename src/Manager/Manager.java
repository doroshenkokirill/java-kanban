package Manager;

public class Manager {
    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
