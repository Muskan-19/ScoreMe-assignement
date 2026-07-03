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
- SchedulingInstance.java
- BuiltInstance.java
- InputParser.java
- OutputGenerator.java
- PenaltyCalculator.java
- BruteForce.java
- Benchmark.java

`BuiltInstance` holds the internal Task/ProcessingSlot object model the
scheduler actually runs on. `SchedulingInstance` is just the raw
deserialization target for the input JSON and knows how to convert itself
into a `BuiltInstance` via `toBuiltInstance()` -- this split exists so the
JSON schema (which has to match the assignment's generator exactly) is
decoupled from the internal representation. `BruteForce` is the exact
solver used only on the small benchmark instances, to get a real
optimal-penalty number to compare against.

## Input format
`input.json` must match the exact schema produced by the assignment's
`generate_instance()` (Section 5) -- flat parallel arrays, not per-task
objects:
```json
{
  "tasks": ["T0", "T1", ...],
  "conflicts": [[0,1], [0,4], ...],
  "resources": [[cpu,ram,gpu,net], ...],
  "capacities": [[cpu,ram,gpu,net], ...],
  "windows": [[lo,hi], ...],
  "weights": [w0, w1, ...],
  "K": 3
}
```
Slots are 0-indexed (`0..K-1`), matching the generator's own window
values. `generate_benchmarks.py` (below) produces files in this format
for you -- you shouldn't need to hand-write one.

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

Benchmark mode needs the 9 required instance files to exist first. They
are **not** committed to the repo (they're generated, not hand-written) --
run the generator script before benchmarking:

```bash
python3 generate_benchmarks.py
```

This calls the assignment's own unmodified `generate_instance()` for the
9 required (n, K, density, seed) combinations and writes them to
`benchmark/n<N>_K<K>_d<density>_seed<seed>.json` (e.g.
`benchmark/n8_K3_d0.3_seed1.json`). Requires Python 3 -- no extra
packages needed for this script.

Then run:
```bash
java -cp "out:<jackson jars>" Main benchmark
```
This reads all 9 files, times each run (median of 5 repeats), and for the
3 "small" instances (n=8, n=10, n=12) also runs the exact brute-force
solver in `BruteForce.java` to get a real optimal penalty for comparison.
Results go to `benchmark_results.csv`, with one row per instance
including `tasks_assigned`/`tasks_total` (useful context when an instance
comes back infeasible -- tells you how far it got before giving up),
`penalty`, `runtime_ms`, `feasible`, and (for the small instances)
`optimal_penalty` and `approx_ratio`.

A missing or unreadable instance file is recorded as a `FAILED` row
instead of crashing the rest of the run.

## Charts

```bash
python3 make_charts.py
```
Reads `benchmark_results.csv` and writes `chart_penalty_vs_n.png` and
`chart_runtime_vs_n.png` (the two charts Task 6 requires). Needs
`matplotlib` (`pip install matplotlib` if you don't have it).
