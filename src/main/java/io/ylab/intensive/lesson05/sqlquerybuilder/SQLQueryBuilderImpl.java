package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.ylab.intensive.lesson05.sqlquerybuilder.connectionwrapper.DatabaseConnectionWrapper;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 02.04.2023
 */
@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {
    /**
     * Поле для работы с БД
     */
    private final DatabaseConnectionWrapper databaseConnectionWrapper;

    @Autowired
    public SQLQueryBuilderImpl(DatabaseConnectionWrapper databaseConnectionWrapper) {
        this.databaseConnectionWrapper = databaseConnectionWrapper;
    }

    /**
     * Метод используется для получения запроса “SELECT <col1>, <col2>, <col3> FROM <tablename>;"
     * в виде строки из таблицы по ее имени
     *
     * @param tableName - имя таблицы
     * @return - возвращает запрос, если таблица существует, null если нет,
     * (если таблица существует, но у нее нет столбцов возвращает "Table with no columns")
     * @throws SQLException - может выбросить {@link SQLException}
     */
    @Override
    public String queryForTable(String tableName) throws SQLException {
        DatabaseMetaData metaData = databaseConnectionWrapper.getConnection().getMetaData();
        ResultSet table = metaData.getTables(null, null, tableName, null);
        if (!table.next()) {
            table.close();
            return null;
        }
        ResultSet columns = metaData.getColumns(null, null, tableName, null);
        List<String> columnsList = new ArrayList<>();
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            columnsList.add(columnName);
        }
        columns.close();
        return getQuery(columnsList, tableName);
    }

    /**
     * Метод используется для получения списка имен всех существующих таблиц в БД
     *
     * @return - возвращает список таблиц
     * @throws SQLException - может выбросить {@link SQLException}
     */
    @Override
    public List<String> getTables() throws SQLException {
        DatabaseMetaData metaData = databaseConnectionWrapper.getConnection().getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "%", null);
        List<String> tablesNames = new ArrayList<>();
        while (resultSet.next()) {
            tablesNames.add(resultSet.getString("TABLE_NAME"));
        }
        resultSet.close();
        return tablesNames;
    }

    /**
     * Метод используется для составления запроса по входным данным:
     * “SELECT <col1>, <col2>, <col3> FROM <tablename>;"
     *
     * @param columns   - список названий столбцов таблицы
     * @param tableName - имя таблицы
     * @return - возвращает запрос SELECT, если таблицы со столбцами и "Table with no columns", если иначе
     */
    private String getQuery(List<String> columns, String tableName) {
        if (columns.isEmpty()) {
            return "Table with no columns";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT ");
        for (int i = 0; i < columns.size(); i++) {
            stringBuilder.append(columns.get(i));
            if (i < (columns.size() - 1)) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(" FROM ");
        stringBuilder.append(tableName);
        stringBuilder.append(";");
        return stringBuilder.toString();
    }
}
