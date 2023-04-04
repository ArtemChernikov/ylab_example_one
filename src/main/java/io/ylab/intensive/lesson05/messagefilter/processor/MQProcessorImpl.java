package io.ylab.intensive.lesson05.messagefilter.processor;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.ylab.intensive.lesson05.messagefilter.connectionwrapper.MqConnectionWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 02.04.2023
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
     * Поле название очереди для приема сообщений
     */
    public static final String INPUT_QUEUE_NAME = "input";
    /**
     * Поле название очереди для отправки сообщений
     */
    public static final String OUTPUT_QUEUE_NAME = "output";
    /**
     * Поле название ключа маршрутизации для очереди приема сообщений
     */
    public static final String INPUT_ROUTING_KEY = "input_key";
    /**
     * Поле название ключа маршрутизации для очереди отправки сообщений
     */
    public static final String OUTPUT_ROUTING_KEY = "output_key";
    /**
     * Поле для подключения к RabbitMQ
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
    @Override
    public void sendMessage(String message) {
        try (Channel channel = mqConnectionWrapper.getConnection().createChannel()) {
            createExchangeAndQueue(channel, OUTPUT_QUEUE_NAME, OUTPUT_ROUTING_KEY);
            channel.basicPublish(EXCHANGE_NAME, OUTPUT_ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Метод используется для приема сообщения из очереди
     *
     * @return - если сообщение имеется, возвращает сообщение обернутое в {@link Optional}, иначе {@link Optional#empty()}
     */
    @Override
    public Optional<String> getMessage() {
        try (Channel channel = mqConnectionWrapper.getConnection().createChannel()) {
            createExchangeAndQueue(channel, INPUT_QUEUE_NAME, INPUT_ROUTING_KEY);
            GetResponse response = channel.basicGet(INPUT_QUEUE_NAME, false);
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
    private void createExchangeAndQueue(Channel channel, String queueName, String routingKey) throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
    }
}
