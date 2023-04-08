package io.ylab.intensive.lesson04.persistentmap;

import io.ylab.intensive.lesson04.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PersistenceMapTest {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = initDb();
        PersistentMap persistentMap = new PersistentMapImpl(dataSource);
        persistentMap.init("map1");
        persistentMap.put("key1", "value1");
        System.out.println(persistentMap.containsKey("key1"));
        persistentMap.remove("key1");
        System.out.println(persistentMap.containsKey("key1"));

        persistentMap.put("key1", "value1");
        persistentMap.put("key2", "value2");
        System.out.println(persistentMap.getKeys());

        System.out.println(persistentMap.get("key2"));

        System.out.println(persistentMap.getKeys());
        persistentMap.clear();
        System.out.println(persistentMap.getKeys());
    }

    public static DataSource initDb() throws SQLException {
        String createMapTable = ""
                + "drop table if exists persistent_map; "
                + "CREATE TABLE if not exists persistent_map (\n"
                + "   map_name varchar,\n"
                + "   KEY varchar,\n"
                + "   value varchar\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createMapTable, dataSource);
        return dataSource;
    }
}
