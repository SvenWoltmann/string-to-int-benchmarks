package eu.happycoders.string2int;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.ThreadLocalRandom;

public class StringToIntMultiBenchmark {

    @State(Scope.Thread)
    public static class MyState {
        String pos;
        String posWithPlus;
        String neg;

        @Setup(Level.Invocation)
        public void doSetup() {
            // always 7-digits, so that the String always has the same length
            pos = "" + (1_000_000 + ThreadLocalRandom.current().nextInt(9_000_000));
            posWithPlus = "+" + pos;
            neg = "-" + pos;
        }
    }

    @Benchmark
    public void integerParsePositiveInt(MyState state, Blackhole blackhole) {
        int i = Integer.parseInt(state.pos);
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParsePositiveIntWithPlus(MyState state, Blackhole blackhole) {
        int i = Integer.parseInt(state.posWithPlus);
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParseNegativeInt(MyState state, Blackhole blackhole) {
        int i = Integer.parseInt(state.neg);
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParseUnsignedInt(MyState state, Blackhole blackhole) {
        int i = Integer.parseUnsignedInt(state.pos);
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParseUnsignedIntWithPlus(MyState state, Blackhole blackhole) {
        int i = Integer.parseUnsignedInt(state.posWithPlus);
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParsePositiveIntAndBox(MyState state, Blackhole blackhole) {
        Integer i = Integer.parseInt(state.pos);
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParsePositiveInteger(MyState state, Blackhole blackhole) {
        Integer i = Integer.valueOf(state.pos);
        blackhole.consume(i);
    }

    @Benchmark
    public void integerParsePositiveIntegerAndUnbox(MyState state, Blackhole blackhole) {
        int i = Integer.valueOf(state.pos);
        blackhole.consume(i);
    }

}
