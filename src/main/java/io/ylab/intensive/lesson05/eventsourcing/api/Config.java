package io.ylab.intensive.lesson05.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson05.DbUtil;
import io.ylab.intensive.lesson05.eventsourcing.api.connectionwrapper.DatabaseConnectionWrapper;
import io.ylab.intensive.lesson05.eventsourcing.api.connectionwrapper.MqConnectionWrapper;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Configuration
@ComponentScan("io.ylab.intensive.lesson05.eventsourcing.api")
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
                create table if not exists person (
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
