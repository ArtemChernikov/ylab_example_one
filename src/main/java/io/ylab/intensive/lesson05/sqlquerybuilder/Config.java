package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import io.ylab.intensive.lesson05.sqlquerybuilder.connectionwrapper.DatabaseConnectionWrapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@ComponentScan("io.ylab.intensive.lesson05.sqlquerybuilder")
public class Config {

    @Bean
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("postgres");
        dataSource.setPortNumber(5432);
        return dataSource;
    }

    @Bean(destroyMethod = "destroy")
    public DatabaseConnectionWrapper databaseConnectionWrapper(DataSource dataSource) throws SQLException {
        Connection databaseConnection = dataSource.getConnection();
        return new DatabaseConnectionWrapper(databaseConnection);
    }
}
