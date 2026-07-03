import java.util.List;

public class SchedulingInstance {

    private List<Task> tasks;
    private List<ProcessingSlot> slots;

    public List<Task> getTasks() {
        return tasks;
    }

    public List<ProcessingSlot> getSlots() {
        return slots;
    }
}
