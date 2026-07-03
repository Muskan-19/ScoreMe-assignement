import java.util.ArrayList;
import java.util.List;

public class BruteForce {

    public static class Result {
        public Double penalty;
        public boolean timedOut;
    }

    public static Result solve(BuiltInstance instance, double timeLimitSeconds) {

        List<Task> tasks = instance.getTasks();
        List<ProcessingSlot> slots = instance.getSlots();
        int n = tasks.size();

        Result result = new Result();
        result.penalty = null;
        result.timedOut = false;

        double[] best = {Double.MAX_VALUE};
        long startNanos = System.nanoTime();
        long limitNanos = (long) (timeLimitSeconds * 1e9);

        backtrack(0, 0.0, tasks, slots, best, startNanos, limitNanos, result);

        if (best[0] < Double.MAX_VALUE) {
            result.penalty = best[0];
        }
        return result;
    }

    private static void backtrack(int idx, double runningPenalty,
                                   List<Task> tasks, List<ProcessingSlot> slots,
                                   double[] best, long startNanos, long limitNanos,
                                   Result result) {

        if (result.timedOut) return;
        if (System.nanoTime() - startNanos > limitNanos) {
            result.timedOut = true;
            return;
        }
        if (runningPenalty >= best[0]) return;

        if (idx == tasks.size()) {
            best[0] = runningPenalty;
            return;
        }

        Task task = tasks.get(idx);

        for (ProcessingSlot slot : slots) {

            if (!task.isWithinWindow(slot.getId())) continue;
            if (!slot.canAssign(task)) continue;

            boolean conflict = false;
            for (Task t : slot.getAssignedTasks()) {
                if (task.getConflicts().contains(t.getId())
                        || t.getConflicts().contains(task.getId())) {
                    conflict = true;
                    break;
                }
            }
            if (conflict) continue;

            double penalty = PenaltyCalculator.penalty(task, slot);

            slot.assign(task);
            backtrack(idx + 1, runningPenalty + penalty, tasks, slots, best, startNanos, limitNanos, result);
            slot.unassign(task);

            if (result.timedOut) return;
        }

    }
}
