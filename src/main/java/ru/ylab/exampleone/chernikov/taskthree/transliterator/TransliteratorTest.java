package ru.ylab.exampleone.chernikov.taskthree.transliterator;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 18.03.2023
 */
public class TransliteratorTest {
    public static void main(String[] args) {
        Transliterator transliterator = new TransliteratorImpl();
        System.out.println(transliterator.transliterate("Привет, PRIVET, privet! "));
        System.out.println(transliterator.transliterate("HELLO! ПРИВЕТ! Go, boy!"));
    }
}
