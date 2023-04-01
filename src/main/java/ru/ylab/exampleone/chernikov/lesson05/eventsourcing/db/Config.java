package ru.ylab.exampleone.chernikov.lesson05.eventsourcing.db;

import com.rabbitmq.client.ConnectionFactory;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.ylab.exampleone.chernikov.lesson05.DbUtil;
import ru.ylab.exampleone.chernikov.lesson05.eventsourcing.db.connectionwrapper.DatabaseConnectionWrapper;
import ru.ylab.exampleone.chernikov.lesson05.eventsourcing.db.connectionwrapper.MqConnectionWrapper;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Configuration
@ComponentScan("ru.ylab.exampleone.chernikov.lesson05.eventsourcing.db")
public class Config {

    @Bean
    public DataSource dataSource() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("postgres");
        dataSource.setPortNumber(5432);

        String ddl = """
                drop table if exists person;create table if not exists person (
                person_id bigint primary key,
                first_name varchar,
                last_name varchar,
                middle_name varchar
                )""";
        DbUtil.applyDdl(ddl, dataSource);

        return dataSource;
    }

    @Bean(destroyMethod = "destroy")
    public DatabaseConnectionWrapper databaseConnectionWrapper(DataSource dataSource) throws SQLException {
        Connection databaseConnection = dataSource.getConnection();
        return new DatabaseConnectionWrapper(databaseConnection);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean(destroyMethod = "destroy")
    public MqConnectionWrapper mqConnectionWrapper(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
        com.rabbitmq.client.Connection connection = connectionFactory.newConnection();
        return new MqConnectionWrapper(connection);
    }

}
