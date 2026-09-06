# Benchmark results

## 2026-09 (`2026-09/`)

The series behind the current version of the article: Java 8, 11, 17,
21, 22, 23, 24 and 25 on two machines, one directory per machine
(`machine.json` describes the hardware). Java 17 is Temurin 17.0.20 on arm64
and 17.0.2 on x86. JMH JSON with the primary
metric, the error interval (99.9 %), every fork's iterations, and the
GC profiler's bytes per operation.

- `java<N>.json` – the five `integerParsePositiveInt*` methods on JVM `N`.
- `java25-all-methods.json` – all eight parse methods on Java 25.
- `java<N>-nocompact.json` – `integerParsePositiveInt` with `-XX:-CompactStrings`
  (arm64, Java 11 and 25): the experiment that attributes the Java 8 → 11 gain
  to Compact Strings (JEP 254).

Measured with the `Level.Trial` input state (`RandomNumberStrings`),
three forks of five warm-up and five measurement iterations of five
seconds each, and the blackhole mode pinned
(`-Djmh.blackhole.autoDetect=false`) - see `run-all-jdks.sh`.

## 2019 (`result_java*.txt`)

The measurements of the article's first version: Java 7 to 14 on a
Dell XPS 15 (i7-8750H). They used a per-invocation JMH state
(`@Setup(Level.Invocation)`), which for a nanosecond operation measures
the timestamps more than the parsing. Kept as the historical record; do
not compare them with the 2026 numbers.
