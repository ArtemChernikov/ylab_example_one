package ru.ylab.exampleone.chernikov.lesson05.eventsourcing.api.processor;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ylab.exampleone.chernikov.lesson05.eventsourcing.api.connectionwrapper.MqConnectionWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
@Component
public class MQProcessorImpl implements MQProcessor {
    /**
     * Поле логгер
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MQProcessorImpl.class);
    /**
     * Поле название обменника
     */
    public static final String EXCHANGE_NAME = "exchange";
    /**
     * Поле название очереди
     */
    public static final String QUEUE_NAME = "queue";
    /**
     * Поле название ключа маршрутизации для сохранения персоны
     */
    public static final String SAVE_ROUTING_KEY = "save_key";
    /**
     * Поле название ключа маршрутизации для удаления персоны
     */
    public static final String DELETE_ROUTING_KEY = "delete_key";
    /**
     * Поле для получения соединения с RabbitMQ
     */
    private final MqConnectionWrapper mqConnectionWrapper;

    @Autowired
    public MQProcessorImpl(MqConnectionWrapper mqConnectionWrapper) {
        this.mqConnectionWrapper = mqConnectionWrapper;
    }

    /**
     * Метод используется для отправки сообщения в очередь
     *
     * @param message - отправляемое сообщение
     */
    public void sendMessage(String message, String routingKey) {
        try (Channel channel = mqConnectionWrapper.getConnection().createChannel()) {
            createExchangeAndQueue(channel);
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Метод используется для создания обменника, очереди и связывания их по ключам маршуртизации
     *
     * @param channel - {@link Channel}
     * @throws IOException - может выбросить {@link IOException}
     */
    private void createExchangeAndQueue(Channel channel) throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, DELETE_ROUTING_KEY);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, SAVE_ROUTING_KEY);
    }
}
