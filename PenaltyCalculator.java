public class PenaltyCalculator {

    public static double penalty(Task task,
                                 ProcessingSlot slot) {

        double priorityPenalty =
                task.getPriority() * slot.getLoadRatio();

        double balancePenalty =
                Math.pow(slot.getLoadRatio(), 2);

        return priorityPenalty + balancePenalty;
    }
}
