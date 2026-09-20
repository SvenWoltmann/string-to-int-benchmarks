# Benchmark results

## 2026-09 (`2026-09/`)

Two measurements on two machines, one directory per machine
(`machine.json` describes the hardware). Java 17 is Temurin 17.0.20 on arm64
and 17.0.2 on x86. Every file is JMH JSON with the primary metric, the
error interval (99.9 %), every fork's iterations, and the GC profiler's
bytes per operation.

### The campaign of 2026-09-17 (`<machine>/series-2026-09-17/`)

The series behind the current version of the article (revised
2026-09-20): Java 8, 11, 17, 21, 22, 23, 24, 25 and 27, every JDK
measured in several rounds in alternating order - once from Java 8
upwards, then from Java 27 downwards - so that any drift during the
campaign hits every column equally. The article prints the mean over
the rounds, and it states a difference between two neighbouring
versions only if that difference exceeds twice the standard error of
both means together.

- `java<N>-r<K>.json` - round `K` of the five `integerParsePositiveInt*`
  methods on JVM `N`; five rounds per JDK on x86, four on arm64.
- `all-methods/` - two runs of all eight parse methods on Java 27
  (`java27-all-methods-r<K>.json` on arm64, `java27-r<K>.json` on x86).
- `drift/` (x86 only) - two runs of the five-method series on Java 27,
  measured fourteen hours after the campaign's own Java 27 rounds; the
  level moved by at most 0.7 % in between, against 2 to 5 % between
  the sessions eleven days apart.

Why a campaign and not one run per JDK: the same benchmark on the same
machine and JDK moves by several percent between measuring sessions,
which is more than most differences a version table invites readers to
compare.

### The single runs of 2026-09-06 (`<machine>/java*.json`)

One run per JDK, Java 8 to 25 - the series the article printed until
2026-09-20. Kept as the record; the campaign supersedes them for every
comparison between versions. The `nocompact` experiment below was
measured in this session and is compared against its own baseline.

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
