package io.ylab.intensive.lesson05.eventsourcing.db.connectionwrapper;

import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.DisposableBean;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
public class MqConnectionWrapper implements DisposableBean {
    private final Connection connection;

    public MqConnectionWrapper(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void destroy() throws Exception {
        connection.close();
    }
}
