package io.ylab.intensive.tasktwo.stats_accumulator;

import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class StatsAccumulatorImpl implements StatsAccumulator {
    private Optional<Integer> min = Optional.empty();

    private Optional<Integer> max = Optional.empty();

    private int count = 0;

    private double sum = 0;

    private double average = 0;

    @Override
    public void add(int value) {
        if (min.isEmpty() && max.isEmpty()) {
            min = Optional.of(value);
            max = Optional.of(value);
        } else {
            if (value < min.get()) {
                min = Optional.of(value);
            }
            if (value > max.get()) {
                max = Optional.of(value);
            }
        }
        count++;
        sum += value;
        average = sum / count;
    }

    @Override
    public int getMin() {
        if (min.isEmpty()) {
            throw new IllegalStateException("Не было добавлено ни одного числа");
        }
        return min.get();
    }

    @Override
    public int getMax() {
        if (max.isEmpty()) {
            throw new IllegalStateException("Не было добавлено ни одного числа");
        }
        return max.get();
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
