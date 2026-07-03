import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class InputParser {

    public static SchedulingInstance parse(String file)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(
                new File(file),
                SchedulingInstance.class);
    }
}
