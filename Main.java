public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length > 0 && args[0].equalsIgnoreCase("benchmark")) {

            Benchmark.runBenchmark();

            return;
        }

        SchedulingInstance raw =
                InputParser.parse("input.json");

        BuiltInstance instance = raw.toBuiltInstance();

        long start = System.currentTimeMillis();

        Scheduler scheduler =
                new Scheduler(instance);

        Scheduler.Result result =
                scheduler.schedule();

        long end = System.currentTimeMillis();

        OutputGenerator.write(
                "output.json",
                result.assignment(),
                result.penalty(),
                end - start,
                result.feasible(),
                result.violationReason()
        );

        System.out.println("Scheduling completed.");
    }
}
