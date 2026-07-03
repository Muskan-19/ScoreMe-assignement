import java.util.List;

public class BuiltInstance {

    private final List<Task> tasks;
    private final List<ProcessingSlot> slots;

    public BuiltInstance(List<Task> tasks, List<ProcessingSlot> slots) {
        this.tasks = tasks;
        this.slots = slots;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<ProcessingSlot> getSlots() {
        return slots;
    }
}
