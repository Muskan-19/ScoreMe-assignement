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
        try (PrintWriter writer =
                     new PrintWriter(new FileWriter("benchmark_results.csv"))) {

            writer.println("Tasks,Penalty,Runtime,Feasible,Status");

            for (int i = 0; i < files.length; i++) {
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
