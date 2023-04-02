package ru.ylab.exampleone.chernikov.lesson05.sqlquerybuilder;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.List;

public class SQLQueryExtenderTest {
    public static void main(String[] args) throws SQLException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        SQLQueryBuilder queryBuilder = applicationContext.getBean(SQLQueryBuilderImpl.class);
        List<String> tables = queryBuilder.getTables();
        for (String tableName : tables) {
            System.out.println(queryBuilder.queryForTable(tableName));
        }
        applicationContext.close();
    }
}
