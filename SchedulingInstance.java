import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SchedulingInstance {

    private List<String> tasks;
    private List<List<Integer>> conflicts;
    private List<List<Double>> resources;
    private List<List<Double>> capacities;
    private List<List<Integer>> windows;
    private List<Double> weights;
    @JsonProperty("K")
    private int K;

    public List<String> getTasks() {
        return tasks;
    }

    public List<List<Integer>> getConflicts() {
        return conflicts;
    }

    public List<List<Double>> getResources() {
        return resources;
    }

    public List<List<Double>> getCapacities() {
        return capacities;
    }

    public List<List<Integer>> getWindows() {
        return windows;
    }

    public List<Double> getWeights() {
        return weights;
    }

    @JsonProperty("K")
    public int getK() {
        return K;
    }

    public BuiltInstance toBuiltInstance() {

        int n = tasks.size();

        List<List<String>> conflictIdsByIndex = new ArrayList<>();
        for (int i = 0; i < n; i++) conflictIdsByIndex.add(new ArrayList<>());
        for (List<Integer> pair : conflicts) {
            int a = pair.get(0);
            int b = pair.get(1);
            conflictIdsByIndex.get(a).add(tasks.get(b));
            conflictIdsByIndex.get(b).add(tasks.get(a));
        }

        List<Task> builtTasks = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Task t = new Task();
            t.setId(tasks.get(i));
            t.setPriority(weights.get(i));
            List<Double> r = resources.get(i);
            t.setCpu(r.get(0));
            t.setRam(r.get(1));
            t.setGpu(r.get(2));
            t.setNetwork(r.get(3));
            List<Integer> w = windows.get(i);
            t.setWindowLo(w.get(0));
            t.setWindowHi(w.get(1));
            t.setConflicts(conflictIdsByIndex.get(i));
            builtTasks.add(t);
        }

        List<ProcessingSlot> builtSlots = new ArrayList<>();
        for (int k = 0; k < K; k++) {
            ProcessingSlot slot = new ProcessingSlot();
            List<Double> c = capacities.get(k);
            slot.setId(k);
            slot.setCpu(c.get(0));
            slot.setRam(c.get(1));
            slot.setGpu(c.get(2));
            slot.setNetwork(c.get(3));
            builtSlots.add(slot);
        }

        return new BuiltInstance(builtTasks, builtSlots);
    }
}
