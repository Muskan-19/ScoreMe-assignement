import java.util.*;

public class Scheduler {

    private final SchedulingInstance instance;

    private Map<String, Integer> assignment =
            new LinkedHashMap<>();

    private boolean feasible = true;

    private String violationReason = "";

    public Scheduler(SchedulingInstance instance) {
        this.instance = instance;
    }

    public Result schedule() {

        List<Task> tasks =
                new ArrayList<>(instance.getTasks());

        tasks.sort((a, b) ->
                Integer.compare(b.getPriority(), a.getPriority()));

        double totalPenalty = 0;

        for (Task task : tasks) {

            ProcessingSlot bestSlot = null;

            double bestPenalty = Double.MAX_VALUE;

            for (ProcessingSlot slot : instance.getSlots()) {

                if (!task.getSlaWindow().contains(slot.getId()))
                    continue;

                if (!slot.canAssign(task))
                    continue;

                boolean conflict = false;

                for (Task t : slot.getAssignedTasks()) {
                    if (task.getConflicts().contains(t.getId())
                            || t.getConflicts().contains(task.getId())) {

                        conflict = true;
                        break;
                    }
                }

                if (conflict)
                    continue;

                double penalty =
                        PenaltyCalculator.penalty(task, slot);

                if (penalty < bestPenalty) {

                    bestPenalty = penalty;
                    bestSlot = slot;
                }
            }

            if (bestSlot == null) {

                feasible = false;

                violationReason =
                        "No feasible slot for " + task.getId();

                break;
            }

            bestSlot.assign(task);

            assignment.put(task.getId(),
                    bestSlot.getId());

            totalPenalty += bestPenalty;
        }

        return new Result(
                assignment,
                totalPenalty,
                feasible,
                violationReason
        );
    }

    public record Result(
            Map<String, Integer> assignment,
            double penalty,
            boolean feasible,
            String violationReason
    ) {
    }
}
