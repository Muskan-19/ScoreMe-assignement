import java.io.FileWriter;
import java.io.PrintWriter;

public class Benchmark {

    public static void runBenchmark() throws Exception {

        String[] files = {

                "benchmark/small8.json",
                "benchmark/small10.json",
                "benchmark/small12.json",

                "benchmark/medium50.json",
                "benchmark/medium100.json",
                "benchmark/medium150.json",

                "benchmark/stress200a.json",
                "benchmark/stress200b.json",
                "benchmark/stress200c.json"

        };

        int[] tasks = {

                8,10,12,
                50,100,150,
                200,200,200

        };

        // FIX: try-with-resources so the writer is always closed/flushed,
        // even if an exception happens partway through the loop.
        try (PrintWriter writer =
                     new PrintWriter(new FileWriter("benchmark_results.csv"))) {

            writer.println("Tasks,Penalty,Runtime,Feasible,Status");

            for (int i = 0; i < files.length; i++) {

                // FIX: previously, one missing/bad file (e.g. a benchmark
                // JSON that doesn't exist yet) threw an uncaught exception
                // and killed the entire benchmark run -- every remaining
                // instance silently never got a result. Each instance is
                // now isolated: a failure is recorded as a row instead of
                // aborting everything after it.
                try {

                    SchedulingInstance instance =
                            InputParser.parse(files[i]);

                    long start =
                            System.currentTimeMillis();

                    Scheduler scheduler =
                            new Scheduler(instance);

                    Scheduler.Result result =
                            scheduler.schedule();

                    long end =
                            System.currentTimeMillis();

                    writer.println(

                            tasks[i] + ","
                                    + result.penalty() + ","
                                    + (end - start) + ","
                                    + result.feasible() + ","
                                    + "OK"

                    );

                    System.out.println(files[i] + " completed.");

                } catch (Exception e) {

                    writer.println(
                            tasks[i] + ",,,,"
                                    + "FAILED: " + e.getClass().getSimpleName() + ": " + e.getMessage()
                    );

                    System.out.println(files[i] + " FAILED: " + e.getMessage());
                }
            }

            System.out.println("benchmark_results.csv generated successfully.");
            System.out.println("Use Excel or Google Sheets to create the required charts.");
            System.out.println("Benchmark completed.");
        }
    }

}
