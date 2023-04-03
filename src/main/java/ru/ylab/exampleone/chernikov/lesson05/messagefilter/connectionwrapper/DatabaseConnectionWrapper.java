package ru.ylab.exampleone.chernikov.lesson05.messagefilter.connectionwrapper;

import org.springframework.beans.factory.DisposableBean;

import java.sql.Connection;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
public class DatabaseConnectionWrapper implements DisposableBean {
    private final Connection connection;

    public DatabaseConnectionWrapper(Connection connection) {
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
