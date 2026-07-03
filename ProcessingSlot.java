import java.util.ArrayList;
import java.util.List;

public class ProcessingSlot {

    private int id;

    private int cpu;
    private int ram;
    private int gpu;
    private int network;

    private int usedCpu = 0;
    private int usedRam = 0;
    private int usedGpu = 0;
    private int usedNetwork = 0;

    private List<Task> assignedTasks = new ArrayList<>();

    public int getId() {
        return id;
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

    public boolean canAssign(Task task) {

        return usedCpu + task.getCpu() <= cpu
                &&
                usedRam + task.getRam() <= ram
                &&
                usedGpu + task.getGpu() <= gpu
                &&
                usedNetwork + task.getNetwork() <= network;
    }

    public void assign(Task task) {

        assignedTasks.add(task);

        usedCpu += task.getCpu();
        usedRam += task.getRam();
        usedGpu += task.getGpu();
        usedNetwork += task.getNetwork();
    }

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }
    public double getLoadRatio() {

        double c = cpu == 0 ? 1 : cpu;
        double r = ram == 0 ? 1 : ram;
        double g = gpu == 0 ? 1 : gpu;
        double n = network == 0 ? 1 : network;

        return ((double) usedCpu / c
                + (double) usedRam / r
                + (double) usedGpu / g
                + (double) usedNetwork / n) / 4.0;
    }
}
