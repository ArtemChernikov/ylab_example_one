package io.ylab.intensive.taskthree.transliterator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 18.03.2023
 */
public class TransliteratorImpl implements Transliterator {

    /**
     * Поле словарь-сопоставление каждой букве кириллического
     * алфавита букве латинского алфавита (в верхнем регистре),
     * где ключ кириллица, а значение-латиница
     */
    private final Map<Character, String> translit;

    public TransliteratorImpl() {
        this.translit = new HashMap<>();
        initTranslit();
    }

    /**
     * Метод используется для преобразования строки
     * (кириллические буквы в верхнем регистре заменяются на латнские буквы в верхнем регистре)
     *
     * @param source - строка для преобразования
     * @return - возвращает преобразованную строку
     */
    @Override
    public String transliterate(String source) {
        inputValidate(source);
        StringBuilder transliteratedWord = new StringBuilder();
        char[] letters = source.toCharArray();
        for (char letter : letters) {
            transliteratedWord.append(translit.getOrDefault(letter, String.valueOf(letter)));
        }
        return transliteratedWord.toString();
    }

    /**
     * Метод используется для валидации входной строки
     *
     * @param source -  строка
     */
    private void inputValidate(String source) {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("Строка должна содержать хотя-бы 1 символ.");
        }
    }

    /**
     * Метод используется для заполнения словаря сопоставлениями букв
     */
    private void initTranslit() {
        translit.put('А', "A");
        translit.put('Б', "B");
        translit.put('В', "V");
        translit.put('Г', "G");
        translit.put('Д', "D");
        translit.put('Е', "E");
        translit.put('Ё', "E");
        translit.put('Ж', "ZH");
        translit.put('З', "Z");
        translit.put('И', "I");
        translit.put('Й', "I");
        translit.put('К', "K");
        translit.put('Л', "L");
        translit.put('М', "M");
        translit.put('Н', "N");
        translit.put('О', "O");
        translit.put('П', "P");
        translit.put('Р', "R");
        translit.put('С', "S");
        translit.put('Т', "T");
        translit.put('У', "U");
        translit.put('Ф', "F");
        translit.put('Х', "KH");
        translit.put('Ц', "TS");
        translit.put('Ч', "CH");
        translit.put('Ш', "SH");
        translit.put('Щ', "SHCH");
        translit.put('Ы', "Y");
        translit.put('Ь', "");
        translit.put('Ъ', "IE");
        translit.put('Э', "E");
        translit.put('Ю', "IU");
        translit.put('Я', "IA");
    }
}
