import java.io.FileWriter;
import java.io.PrintWriter;

public class Benchmark {

    static class Spec {
        String label; int n, K; double density; long seed; String category;
        Spec(String label, int n, int K, double density, long seed, String category) {
            this.label = label; this.n = n; this.K = K; this.density = density;
            this.seed = seed; this.category = category;
        }
        String filename() {
            return String.format("benchmark/n%d_K%d_d%s_seed%d.json", n, K, density, seed);
        }
    }

    private static final Spec[] SPECS = {
        new Spec("small-1",  8,   3,  0.30, 1,  "small"),
        new Spec("small-2",  10,  4,  0.40, 2,  "small"),
        new Spec("small-3",  12,  4,  0.50, 3,  "small"),
        new Spec("medium-1", 50,  8,  0.25, 10, "medium"),
        new Spec("medium-2", 100, 10, 0.30, 11, "medium"),
        new Spec("medium-3", 150, 12, 0.35, 12, "medium"),
        new Spec("stress-1", 200, 15, 0.40, 20, "stress"),
        new Spec("stress-2", 200, 5,  0.60, 21, "stress"),
        new Spec("stress-3", 200, 20, 0.10, 22, "stress"),
    };

    private static final int REPEATS = 5;

    public static void runBenchmark() throws Exception {

        try (PrintWriter writer = new PrintWriter(new FileWriter("benchmark_results.csv"))) {

            writer.println("label,n,K,density,seed,status,tasks_assigned,tasks_total,penalty,runtime_ms,feasible," +
                    "optimal_penalty,optimal_runtime_ms,approx_ratio");

            for (Spec spec : SPECS) {

                try {
                    SchedulingInstance raw = InputParser.parse(spec.filename());
                    BuiltInstance instance = raw.toBuiltInstance();

                    double[] runtimes = new double[REPEATS];
                    Scheduler.Result result = null;
                    for (int r = 0; r < REPEATS; r++) {

                        BuiltInstance fresh = raw.toBuiltInstance();
                        long t0 = System.nanoTime();
                        result = new Scheduler(fresh).schedule();
                        runtimes[r] = (System.nanoTime() - t0) / 1_000_000.0;
                    }
                    java.util.Arrays.sort(runtimes);
                    double medianRuntime = runtimes[REPEATS / 2];

                    Double optimalPenalty = null;
                    Double optimalRuntime = null;
                    Double approxRatio = null;

                    if (spec.category.equals("small")) {
                        BuiltInstance freshForBf = raw.toBuiltInstance();
                        long bt0 = System.nanoTime();
                        BruteForce.Result bf = BruteForce.solve(freshForBf, 60.0);
                        optimalRuntime = (System.nanoTime() - bt0) / 1_000_000.0;
                        optimalPenalty = bf.penalty;
                        if (bf.timedOut) {
                            System.out.println(spec.label + ": brute force timed out at 60s");
                        }
                        if (optimalPenalty != null && optimalPenalty > 1e-9 && result.feasible()) {
                            approxRatio = result.penalty() / optimalPenalty;
                        } else if (optimalPenalty != null && optimalPenalty <= 1e-9 && result.feasible()
                                && result.penalty() <= 1e-9) {
                            approxRatio = 1.0;
                        }
                    }

                    writer.println(String.join(",",
                            spec.label,
                            String.valueOf(spec.n),
                            String.valueOf(spec.K),
                            String.valueOf(spec.density),
                            String.valueOf(spec.seed),
                            "OK",
                            String.valueOf(result.assignment().size()),
                            String.valueOf(spec.n),
                            String.valueOf(result.penalty()),
                            String.valueOf(medianRuntime),
                            String.valueOf(result.feasible()),
                            optimalPenalty == null ? "" : String.valueOf(optimalPenalty),
                            optimalRuntime == null ? "" : String.valueOf(optimalRuntime),
                            approxRatio == null ? "" : String.valueOf(approxRatio)
                    ));

                    System.out.println(spec.label + " (" + spec.filename() + ") completed. penalty="
                            + result.penalty() + " feasible=" + result.feasible());

                } catch (Exception e) {

                    writer.println(String.join(",",
                            spec.label, String.valueOf(spec.n), String.valueOf(spec.K),
                            String.valueOf(spec.density), String.valueOf(spec.seed),
                            "FAILED: " + e.getClass().getSimpleName() + " " + e.getMessage(),
                            "", "", "", "", "", "", ""
                    ));
                    System.out.println(spec.label + " FAILED: " + e.getMessage());
                }
            }
        }

        System.out.println("benchmark_results.csv generated successfully.");
        System.out.println("Run make_charts.py to produce the two required charts.");
        System.out.println("Benchmark completed.");
    }
}
