package io.ylab.intensive.lesson05.eventsourcing.db.processor;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import io.ylab.intensive.lesson05.eventsourcing.db.connectionwrapper.MqConnectionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
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
     * Метод используется для получения сообщения из очереди RabbitMQ и дальнейшего его удаления из нее
     *
     * @return - возвращает сообщение
     */
    public Optional<String> getMessage() {
        try (Channel channel = mqConnectionWrapper.getConnection().createChannel()) {
            createExchangeAndQueue(channel);
            GetResponse response = channel.basicGet(QUEUE_NAME, false);
            if (response != null) {
                byte[] body = response.getBody();
                String message = new String(body, StandardCharsets.UTF_8);
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                channel.basicAck(deliveryTag, false);
                return Optional.of(message);
            }
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
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
