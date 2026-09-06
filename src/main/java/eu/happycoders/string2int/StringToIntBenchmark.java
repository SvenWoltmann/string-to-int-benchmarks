package eu.happycoders.string2int;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class StringToIntBenchmark {

    @Benchmark
    public void integerParseInt(RandomNumberStrings state, Blackhole blackhole) {
        int i = Integer.parseInt(state.positive(state.next()));
        blackhole.consume(i);
    }

}
