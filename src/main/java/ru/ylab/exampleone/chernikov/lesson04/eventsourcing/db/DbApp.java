package ru.ylab.exampleone.chernikov.lesson04.eventsourcing.db;

import com.rabbitmq.client.ConnectionFactory;
import ru.ylab.exampleone.chernikov.lesson04.DbUtil;
import ru.ylab.exampleone.chernikov.lesson04.RabbitMQUtil;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbApp {
    public static void main(String[] args) throws Exception {
        DataSource dataSource = initDb();
        ConnectionFactory connectionFactory = initMQ();
        try (Connection databaseConnection = dataSource.getConnection();
             com.rabbitmq.client.Connection mqConnection = connectionFactory.newConnection()) {
            DataProcessor dataProcessor = new DataProcessorImpl(databaseConnection, mqConnection);
            dataProcessor.getMessage();
        }
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }

    private static DataSource initDb() throws SQLException {
        String ddl = ""
                + "drop table if exists person;"
                + "create table if not exists person (\n"
                + "person_id bigint primary key,\n"
                + "first_name varchar,\n"
                + "last_name varchar,\n"
                + "middle_name varchar\n"
                + ")";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(ddl, dataSource);
        return dataSource;
    }
}
