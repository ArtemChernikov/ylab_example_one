package ru.ylab.exampleone.chernikov.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import ru.ylab.exampleone.chernikov.lesson04.DbUtil;
import ru.ylab.exampleone.chernikov.lesson04.RabbitMQUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = initMQ();
        DataSource dataSource = initDb();
        try (Connection databaseConnection = dataSource.getConnection();
             com.rabbitmq.client.Connection mqConnection = connectionFactory.newConnection()) {
            PersonApi personApi = new PersonApiImpl(databaseConnection, mqConnection);
            personApi.savePerson(1L, "Artem", "Chernikov", "Vladimirovich");
            personApi.savePerson(2L, "Andrey", "Ivanov", "Grigorievich");
            personApi.savePerson(3L, "Ivan", "Sarkov", "Vladislavovich");
            System.out.println(personApi.findPerson(1L));
            System.out.println(personApi.findAll());
            personApi.savePerson(2L, "Maxim", "Fedorov", "Andreevich");
            personApi.deletePerson(3L);
            System.out.println(personApi.findPerson(3L));
            System.out.println(personApi.findAll());
        }
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }

    private static DataSource initDb() throws SQLException {
        String ddl = ""
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
