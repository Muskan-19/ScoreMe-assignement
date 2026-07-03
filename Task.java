import java.util.Collections;
import java.util.List;

public class Task {

    private String id;
    private int priority;

    private int cpu;
    private int ram;
    private int gpu;
    private int network;

    private List<Integer> slaWindow;
    private List<String> conflicts;

    public Task() {
    }

    public String getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public int getCpu() {
        return cpu;
    }

    public int getRam() {
        return ram;
    }

    public int getGpu() {
        return gpu;
    }

    public int getNetwork() {
        return network;
    }

    public List<Integer> getSlaWindow() {
        return slaWindow == null ? Collections.emptyList() : slaWindow;
    }
    public List<String> getConflicts() {
        return conflicts == null ? Collections.emptyList() : conflicts;
    }
}
