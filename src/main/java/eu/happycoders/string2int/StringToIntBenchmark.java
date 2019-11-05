package eu.happycoders.string2int;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.ThreadLocalRandom;

public class StringToIntBenchmark {

    @State(Scope.Thread)
    public static class MyState {
        public String s;

        @Setup(Level.Invocation)
        public void doSetup() {
            // always 7-digits, so that the String always has the same length
            s = "" + (1_000_000 + ThreadLocalRandom.current().nextInt(9_000_000));
        }
    }

    @Benchmark
    public void integerParseInt(MyState state, Blackhole blackhole) {
        int i = Integer.parseInt(state.s);
        blackhole.consume(i);
    }

}
