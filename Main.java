public class Main {

    public static void main(String[] args)
            throws Exception {

        SchedulingInstance instance =
                InputParser.parse("input.json");

        long start =
                System.currentTimeMillis();

        Scheduler scheduler =
                new Scheduler(instance);

        Scheduler.Result result =
                scheduler.schedule();

        long end =
                System.currentTimeMillis();

        OutputGenerator.write(
                "output.json",
                result.assignment(),
                result.penalty(),
                end - start,
                result.feasible(),
                result.violationReason()
        );

        System.out.println("Scheduling Complete");
    }
}
