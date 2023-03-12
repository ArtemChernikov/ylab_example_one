package ru.ylab.exampleone.chernikov.tasktwo.stats_accumulator;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class StatsAccumulatorImpl implements StatsAccumulator {
    private int min = Integer.MAX_VALUE;

    private int max = Integer.MIN_VALUE;

    private int count = 0;

    private double sum = 0;

    private double average = 0;

    @Override
    public void add(int value) {
        if (value < min) {
            min = value;
        }
        if (value > max) {
            max = value;
        }
        count++;
        sum += value;
        average = sum / count;
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        return average;
    }
}
