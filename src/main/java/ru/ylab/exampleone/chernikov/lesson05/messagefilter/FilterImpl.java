package ru.ylab.exampleone.chernikov.lesson05.messagefilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ylab.exampleone.chernikov.lesson05.messagefilter.processor.DatabaseProcessor;
import ru.ylab.exampleone.chernikov.lesson05.messagefilter.processor.MQProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 02.04.2023
 */
@Component
public class FilterImpl implements Filter {
    /**
     * Поле для работы с RabbitMQ
     */
    private final MQProcessor mqProcessor;

    /**
     * Поле для работы с БД
     */
    private final DatabaseProcessor databaseProcessor;

    @Autowired
    public FilterImpl(MQProcessor mqProcessor, DatabaseProcessor databaseProcessor) {
        this.mqProcessor = mqProcessor;
        this.databaseProcessor = databaseProcessor;
        databaseProcessor.initDatabase();
    }

    /**
     * Метод используется для зацензуривания входящего сообщения:
     * 1. Получает сообщение из очереди
     * 2. Обрабатывает сообщение (если сообщение содержит запрещенное слово, оно будет зацензурено)
     * 3. Отправляет обработанное сообщение в очередь
     */
    @Override
    public void processMessage() {
        while (!Thread.currentThread().isInterrupted()) {
            Optional<String> optionalInputMessage = mqProcessor.getMessage();
            if (optionalInputMessage.isPresent()) {
                String inputMessage = optionalInputMessage.get();
                List<String> swearWords = searchSwearWords(inputMessage);
                if (!swearWords.isEmpty()) {
                    String censoredMessage = replaceSwearWords(swearWords, inputMessage);
                    mqProcessor.sendMessage(censoredMessage);
                } else {
                    mqProcessor.sendMessage(inputMessage);
                }
            }
        }
    }

    /**
     * Метод используется для замены запрещенных слов в сообщении
     *
     * @param swearWords   - список запрещенных слов в входящем сообщении
     * @param inputMessage - входящее сообщение
     * @return - возвращает зацензурированное сообщение
     */
    private String replaceSwearWords(List<String> swearWords, String inputMessage) {
        String censoredMessage = inputMessage;
        for (String swearWord : swearWords) {
            String censorWord = censorWord(swearWord);
            censoredMessage = censoredMessage.replaceAll(swearWord, censorWord);
        }
        return censoredMessage;
    }

    /**
     * Метод используется для поиска запрещенных слов в сообщении
     *
     * @param message - сообщение
     * @return - возвращает список запрещенных слов
     */
    private List<String> searchSwearWords(String message) {
        List<String> swearWords = new ArrayList<>();
        String[] splitLine = message.replaceAll("[\\s.,;?!]+", " ").trim().split(" ");
        for (String word : splitLine) {
            if (databaseProcessor.checkWord(word)) {
                swearWords.add(word);
            }
        }
        return swearWords;
    }

    /**
     * Метод используется для зацензуривания входного слова
     * (все символы кроме превого и последнего заменяются "*")
     *
     * @param word - слово
     * @return - возвращает зацензурированное слово
     */
    private String censorWord(String word) {
        String firstChar = word.substring(0, 1);
        String lastChar = word.substring(word.length() - 1);
        return firstChar + (word.substring(1, word.length() - 1).replaceAll(".", "*") + lastChar);
    }
}
