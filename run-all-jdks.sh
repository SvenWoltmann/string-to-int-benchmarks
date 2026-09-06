#!/usr/bin/env bash
#
# Measures the String-to-int benchmarks across the JDK version series and
# writes one JMH JSON file per JDK to results/.
#
# One jar, built with --release 8 and run unchanged on every JDK: nothing in
# the measured path depends on the compiler, so the bytecode is not a
# variable here (unlike in int-to-string-benchmarks, which needs three jars).
#
#   run                          methods                                 result file
#   series, every JDK            the five integerParsePositiveInt* ones  java<N>.json
#   all methods, newest JDK      all eight StringToIntMultiBenchmark     java<N>-all-methods.json
#
# Usage:
#   ./run-all-jdks.sh [--plan] [--majors 8,11,17,...] [--no-all-methods]
#
#   --plan            print the schedule (runs, estimated durations, clock times) and stop
#   --majors          restrict the JDK majors (default: 8 11 17 21 22 23 24 25 27)
#   --no-all-methods  skip the all-methods run on the newest JDK
#
# JDKs are resolved from the SDKMAN candidates by major version (newest GA
# build, no early access); a missing major is skipped with a message. Every
# run uses 3 forks x (5 warmup + 5 measurement) x 5 s per method, the GC
# profiler (bytes per operation), and the blackhole mode pinned with
# -Djmh.blackhole.autoDetect=false, so that every JDK measures with the same
# blackhole instead of the cheaper compiler blackhole JMH picks on 17+.
# Expect about 2.7 minutes per method and JDK.

set -euo pipefail

MAJORS=(8 11 17 21 22 23 24 25 27)
ALL_METHODS=1
PLAN=0

# JMH_OPTS in the environment overrides these, e.g. for a smoke test of the
# script: JMH_OPTS="-f 1 -wi 1 -i 1 -w 1 -r 1" ./run-all-jdks.sh --majors 25
read -r -a JMH_OPTS <<< "${JMH_OPTS:--f 3 -wi 5 -i 5 -w 5 -r 5 -prof gc}"
MINUTES_PER_METHOD=2.7   # 3 x (25 + 25) s plus JVM start and the profiler
MINUTES_PER_BUILD=0.5

# Package-qualified, so that the gitignored experiment classes in a local
# checkout (eu.happycoders.string2int.gitignore.*) never match.
SERIES_FILTER='eu\.happycoders\.string2int\.StringToIntMultiBenchmark\.integerParsePositiveInt'
SERIES_METHODS=5
ALL_FILTER='eu\.happycoders\.string2int\.StringToIntMultiBenchmark\.'
ALL_METHODS_COUNT=8

JAR="target/benchmarks.jar"
SDKMAN_JAVA="${SDKMAN_DIR:-$HOME/.sdkman}/candidates/java"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --plan) PLAN=1 ;;
    --majors) IFS=', ' read -r -a MAJORS <<< "$2"; shift ;;
    --no-all-methods) ALL_METHODS=0 ;;
    *) echo "unknown argument: $1" >&2; exit 2 ;;
  esac
  shift
done

cd "$(dirname "$0")"

# Newest installed SDKMAN candidate for a major version, e.g. 21 -> 21.0.2-open.
# Early-access and vendor-specific builds are skipped: a version series is only
# comparable when every entry is a GA build. `|| true`: grep exits 1 when
# nothing matches, and under `set -eo pipefail` that would abort the whole
# script instead of skipping the missing major.
resolve_jdk() {
  local major="$1"
  ls -1 "$SDKMAN_JAVA" 2>/dev/null \
    | grep -E "^${major}(\.[0-9]+)*-(open|zulu|tem|oracle)$" \
    | sort -V | tail -1 || true
}

clock() { date -r "$1" +%H:%M 2>/dev/null || date -d "@$1" +%H:%M; }

# --- plan the runs ---------------------------------------------------------
# Resolved once; plain lines "major version", because macOS ships bash 3.2
# without associative arrays.
RESOLVED=""
NEWEST=""
for major in "${MAJORS[@]}"; do
  v="$(resolve_jdk "$major")"
  if [[ -z "$v" ]]; then
    echo "SKIP Java $major - no GA build installed (sdk list java | grep '^ *$major')" >&2
  else
    RESOLVED+="$major $v"$'\n'
    if [[ -z "$NEWEST" || "$major" -gt "$NEWEST" ]]; then NEWEST="$major"; fi
  fi
done
version_of() { printf '%s' "$RESOLVED" | awk -v m="$1" '$1 == m {print $2}'; }

# One entry per run: major|filter|methods|outfile
RUNS=()
for major in "${MAJORS[@]}"; do
  [[ -z "$(version_of "$major")" ]] && continue
  RUNS+=("$major|$SERIES_FILTER|$SERIES_METHODS|results/java${major}")
done
if (( ALL_METHODS )) && [[ -n "$NEWEST" ]]; then
  RUNS+=("$NEWEST|$ALL_FILTER|$ALL_METHODS_COUNT|results/java${NEWEST}-all-methods")
fi

# --- print the schedule ----------------------------------------------------
now=$(date +%s)
t=$(awk -v t="$now" -v m="$MINUTES_PER_BUILD" 'BEGIN{printf "%d", t + m*60}')
printf "%-6s %-8s %-32s %6s  %5s  %5s\n" JDK methods "result" min start end
printf "%-6s %-8s %-32s %6s  %5s  %5s\n" - - "build" "$MINUTES_PER_BUILD" "$(clock "$now")" "$(clock "$t")"
for run in "${RUNS[@]}"; do
  IFS='|' read -r major filter methods out <<< "$run"
  minutes=$(awk -v n="$methods" -v m="$MINUTES_PER_METHOD" 'BEGIN{print n*m}')
  start=$t
  t=$(awk -v t="$t" -v m="$minutes" 'BEGIN{printf "%d", t + m*60}')
  printf "%-6s %-8s %-32s %6s  %5s  %5s\n" "$major" "$methods" "$out.json" "$minutes" "$(clock "$start")" "$(clock "$t")"
done
total=$(awk -v a="$now" -v b="$t" 'BEGIN{printf "%.1f", (b-a)/60}')
echo "Total: ${#RUNS[@]} runs, about $total minutes, done around $(clock "$t")."
(( PLAN )) && exit 0

# --- build and run ---------------------------------------------------------
echo "=== Building $JAR ..."
mvn -q -B clean package
mkdir -p results

for run in "${RUNS[@]}"; do
  IFS='|' read -r major filter methods out <<< "$run"
  version="$(version_of "$major")"
  java_bin="$SDKMAN_JAVA/$version/bin/java"
  echo
  echo "=== $(date +%H:%M) Java $major ($version) -> $out.json"
  "$java_bin" -Djmh.blackhole.autoDetect=false -jar "$JAR" "$filter" "${JMH_OPTS[@]}" \
    -rf json -rff "$out.json" \
    | tee "$out.txt"
done

echo
echo "Done at $(date +%H:%M). JSON results in results/, human-readable logs next to them."
