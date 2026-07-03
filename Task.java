import java.util.Collections;
import java.util.List;

public class Task {

    private String id;
    private double priority;

    private double cpu;
    private double ram;
    private double gpu;
    private double network;

    private int windowLo;
    private int windowHi;

    private List<String> conflicts;

    public Task() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
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

    public int getWindowLo() {
        return windowLo;
    }

    public void setWindowLo(int windowLo) {
        this.windowLo = windowLo;
    }

    public int getWindowHi() {
        return windowHi;
    }

    public void setWindowHi(int windowHi) {
        this.windowHi = windowHi;
    }

    public boolean isWithinWindow(int slotId) {
        return slotId >= windowLo && slotId <= windowHi;
    }

    public List<String> getConflicts() {
        return conflicts == null ? Collections.emptyList() : conflicts;
    }

    public void setConflicts(List<String> conflicts) {
        this.conflicts = conflicts;
    }
}
