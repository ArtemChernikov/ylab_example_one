package ru.ylab.exampleone.chernikov.tasktwo.rate_limited_printer;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class RateLimitedPrinterImpl implements RateLimitedPrinter {
    /**
     * Поле интервал вывода сообщения в консоль в миллисекундах
     */
    private int interval;

    /**
     * Поле время последнего вывода в консоль в миллисекундах
     */
    private long lastOutputTime;

    public RateLimitedPrinterImpl(int interval) {
        this.interval = interval;
    }

    @Override
    public void print(String message) {
        if (lastOutputTime == 0) {
            System.out.println(message);
            lastOutputTime = System.currentTimeMillis();
        } else {
            long difference = System.currentTimeMillis() - lastOutputTime;
            if (difference >= interval) {
                System.out.println(message);
                lastOutputTime = System.currentTimeMillis();
            }
        }
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public long getLastOutputTime() {
        return lastOutputTime;
    }

    public void setLastOutputTime(long lastOutputTime) {
        this.lastOutputTime = lastOutputTime;
    }
}
