package eu.happycoders.string2int;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class StringToIntMultiBenchmark {

    @Benchmark
    public void integerParsePositiveInt(RandomNumberStrings state, Blackhole blackhole) {
        int i = Integer.parseInt(state.positive(state.next()));
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParsePositiveIntWithPlus(RandomNumberStrings state, Blackhole blackhole) {
        int i = Integer.parseInt(state.positiveWithPlus(state.next()));
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParseNegativeInt(RandomNumberStrings state, Blackhole blackhole) {
        int i = Integer.parseInt(state.negative(state.next()));
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParseUnsignedInt(RandomNumberStrings state, Blackhole blackhole) {
        int i = Integer.parseUnsignedInt(state.positive(state.next()));
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParseUnsignedIntWithPlus(RandomNumberStrings state, Blackhole blackhole) {
        int i = Integer.parseUnsignedInt(state.positiveWithPlus(state.next()));
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParsePositiveIntAndBox(RandomNumberStrings state, Blackhole blackhole) {
        Integer i = Integer.parseInt(state.positive(state.next()));
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParsePositiveInteger(RandomNumberStrings state, Blackhole blackhole) {
        Integer i = Integer.valueOf(state.positive(state.next()));
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParsePositiveIntegerAndUnbox(RandomNumberStrings state, Blackhole blackhole) {
        int i = Integer.valueOf(state.positive(state.next()));
        blackhole.consume(i);
    }

}
