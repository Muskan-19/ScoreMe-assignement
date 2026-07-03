import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

public class OutputGenerator {

    public static void write(
            String file,
            Map<String, Integer> assignment,
            double penalty,
            long runtime,
            boolean feasible,
            String reason)
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(file),
                        new Output(
                                assignment,
                                penalty,
                                runtime,
                                feasible,
                                reason
                        ));
    }

    static class Output {

        public Map<String, Integer> assignment;
        public double penalty;
        public long runtime_ms;
        public boolean feasible;
        public String violation_reason;

        public Output(Map<String, Integer> assignment,
                      double penalty,
                      long runtime,
                      boolean feasible,
                      String reason) {

            this.assignment = assignment;
            this.penalty = penalty;
            this.runtime_ms = runtime;
            this.feasible = feasible;
            this.violation_reason = reason;
        }
    }
}
