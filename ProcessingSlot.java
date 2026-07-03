import java.util.ArrayList;
import java.util.List;

public class ProcessingSlot {

    private int id;

    private double cpu;
    private double ram;
    private double gpu;
    private double network;

    private double usedCpu = 0;
    private double usedRam = 0;
    private double usedGpu = 0;
    private double usedNetwork = 0;

    private List<Task> assignedTasks = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getRam() {
        return ram;
    }

    public void setRam(double ram) {
        this.ram = ram;
    }

    public double getGpu() {
        return gpu;
    }

    public void setGpu(double gpu) {
        this.gpu = gpu;
    }

    public double getNetwork() {
        return network;
    }

    public void setNetwork(double network) {
        this.network = network;
    }

    public boolean canAssign(Task task) {

        double eps = 1e-9;
        return usedCpu + task.getCpu() <= cpu + eps
                &&
                usedRam + task.getRam() <= ram + eps
                &&
                usedGpu + task.getGpu() <= gpu + eps
                &&
                usedNetwork + task.getNetwork() <= network + eps;
    }

    public void assign(Task task) {

        assignedTasks.add(task);

        usedCpu += task.getCpu();
        usedRam += task.getRam();
        usedGpu += task.getGpu();
        usedNetwork += task.getNetwork();
    }

    public void unassign(Task task) {
        assignedTasks.remove(task);
        usedCpu -= task.getCpu();
        usedRam -= task.getRam();
        usedGpu -= task.getGpu();
        usedNetwork -= task.getNetwork();
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
