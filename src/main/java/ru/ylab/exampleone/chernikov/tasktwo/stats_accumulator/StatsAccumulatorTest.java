package ru.ylab.exampleone.chernikov.tasktwo.stats_accumulator;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class StatsAccumulatorTest {
    public static void main(String[] args) {
        StatsAccumulator statsAccumulator = new StatsAccumulatorImpl();
        statsAccumulator.add(1);
        statsAccumulator.add(2);
        System.out.println(statsAccumulator.getAvg());
        statsAccumulator.add(0);
        System.out.println(statsAccumulator.getMin());
        statsAccumulator.add(3);
        statsAccumulator.add(8);
        System.out.println(statsAccumulator.getMax());
        System.out.println(statsAccumulator.getCount());
    }
}
