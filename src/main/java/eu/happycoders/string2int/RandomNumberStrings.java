package eu.happycoders.string2int;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The input of every String-to-int benchmark: 1024 random seven-digit
 * numbers as Strings - plain, with a leading plus, and negative - drawn once
 * per trial and handed out round-robin.
 *
 * <p>Seven digits, so that every String has the same length. Drawn once per
 * trial and not per invocation: with {@code @Setup(Level.Invocation)} JMH has
 * to timestamp every single call to subtract the setup time, and for an
 * operation of 5–20 ns that measures the clock more than the parsing (on
 * Apple Silicon {@code System.nanoTime()} ticks in 41 ns steps). The 2019
 * numbers were measured that way; see the README.
 */
@State(Scope.Thread)
public class RandomNumberStrings {

    private static final int SIZE = 1024;

    private final String[] positive = new String[SIZE];
    private final String[] positiveWithPlus = new String[SIZE];
    private final String[] negative = new String[SIZE];
    private int index;

    @Setup(Level.Trial)
    public void doSetup() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int k = 0; k < SIZE; k++) {
            String digits = Integer.toString(1_000_000 + random.nextInt(9_000_000));
            positive[k] = digits;
            positiveWithPlus[k] = "+" + digits;
            negative[k] = "-" + digits;
        }
    }

    /** Advances to the next number; the three getters then return its variants. */
    public int next() {
        return index++ & (SIZE - 1);
    }

    public String positive(int k) {
        return positive[k];
    }

    public String positiveWithPlus(int k) {
        return positiveWithPlus[k];
    }

    public String negative(int k) {
        return negative[k];
    }

}
