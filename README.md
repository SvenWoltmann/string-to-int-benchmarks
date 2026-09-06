# How to Convert String to Int in Java – Peculiarities and Pitfalls

JMH benchmarks to measure the speed of various methods to convert a String to an int / Integer in Java.

Related article on my blog:
* English: [How to Convert String to Int in Java – Peculiarities and Pitfalls](https://www.happycoders.eu/java/convert-string-to-int-peculiarities-pitfalls/)
* German: [Java: String in int umwandeln – Besonderheiten und Fallstricke](https://www.happycoders.eu/de/java/string-in-int-umwandeln-besonderheiten-fallstricke/)

## Running the series

```
./run-all-jdks.sh --plan   # what would run, with estimated clock times
./run-all-jdks.sh          # the whole series
```

The script resolves the JDKs (8, 11, 17, 21, 22, 23, 24, 25, 27) from the
SDKMAN candidates and skips the ones that are not installed. One jar, built
with `--release 8`, runs unchanged on every JDK – nothing in the measured
path depends on the compiler.

| Run | Methods | Result file |
|---|---|---|
| series, every JDK | the five `integerParsePositiveInt*` variants | `results/java<N>.json` |
| all methods, newest JDK | all eight of `StringToIntMultiBenchmark` | `results/java<N>-all-methods.json` |

Every run uses 3 forks × (5 warmup + 5 measurement) × 5 s per method, JMH's
GC profiler (bytes per operation), and `-Djmh.blackhole.autoDetect=false`, so
that every JDK measures with the same blackhole. `--majors` restricts a run;
`JMH_OPTS` in the environment overrides the JMH settings.

## The 2019 results

`results/result_java*.txt` are the measurements the article was originally
based on (Java 7 to 14, Dell XPS 15 with an i7-8750H). They were taken with a
`@Setup(Level.Invocation)` state, which JMH documents as unusable for
sub-millisecond operations: it timestamps every invocation, so a 5–20 ns
parse was measured together with two `System.nanoTime()` calls. The
benchmarks now draw their input once per trial (`RandomNumberStrings`), and
the current measurements live with the article's other data in the website
repository.

Happy Coding!
