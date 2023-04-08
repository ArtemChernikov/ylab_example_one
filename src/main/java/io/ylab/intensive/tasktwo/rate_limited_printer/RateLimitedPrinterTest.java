package io.ylab.intensive.tasktwo.rate_limited_printer;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class RateLimitedPrinterTest {
    public static void main(String[] args) {
        RateLimitedPrinter rateLimitedPrinter = new RateLimitedPrinterImpl(2000);
        for (int i = 0; i < 1_000_000_000; i++) {
            rateLimitedPrinter.print(String.valueOf(i));
        }
    }
}
