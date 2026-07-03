# ScoreMe-assignment
# Resource-Aware Priority Load Balancing Scheduler (RPLBS)

A Java scheduler that assigns tasks to processing slots based on priority,
resource capacity, SLA windows, and conflict constraints, and picks the
slot that adds the least penalty for each task.

## Language
- Java 17

## Dependency
- Jackson Databind 2.17.1

## Files
All files sit at the project root:
- Main.java
- Scheduler.java
- Task.java
- ProcessingSlot.java
- InputParser.java
- OutputGenerator.java
- PenaltyCalculator.java
- SchedulingInstance.java
- Benchmark.java

## Input
`input.json` — single scheduling instance (tasks + slots).

## Output
`output.json` — the assignment, total penalty, runtime, and feasibility.

## How to Run

### Using Maven
```bash
mvn compile
mvn exec:java
```
The pom is set up to look for sources at the project root instead of the
usual `src/main/java`, since that's how this repo is laid out. First run
needs internet access so Maven can pull down Jackson and the exec plugin.

### Compiling manually (no Maven)
Since we're not using Maven's dependency resolution here, you need
Jackson's jars on the classpath yourself. Easiest way on Ubuntu/Debian:
```bash
sudo apt install libjackson2-databind-java libjackson2-core-java libjackson2-annotations-java
```
That drops the jars in `/usr/share/java`. Then:
```bash
javac -cp "/usr/share/java/jackson-databind.jar:/usr/share/java/jackson-core.jar:/usr/share/java/jackson-annotations.jar" -d out *.java
java -cp "out:/usr/share/java/jackson-databind.jar:/usr/share/java/jackson-core.jar:/usr/share/java/jackson-annotations.jar" Main
```
(If you're on macOS/Windows, just point `-cp` at wherever you downloaded
the three Jackson jars from Maven Central instead.)

## Benchmark mode
```bash
java -cp "out:<jackson jars>" Main benchmark
```
This runs the 9 benchmark instances defined in `Benchmark.java`, reading
each from `benchmark/*.json`, and writes the results to
`benchmark_results.csv`.

Heads up: the `benchmark/` folder with the 9 instance files
(`small8.json`, `small10.json`, `small12.json`, `medium50.json`,
`medium100.json`, `medium150.json`, `stress200a.json`, `stress200b.json`,
`stress200c.json`) needs to exist before this will do anything useful —
they're not generated automatically. If one's missing, that row just
shows up as `FAILED` in the CSV instead of crashing the whole run, but
you still need to actually provide those files to get real numbers out
of it.
