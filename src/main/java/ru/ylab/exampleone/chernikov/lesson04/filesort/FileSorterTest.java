package ru.ylab.exampleone.chernikov.lesson04.filesort;

import ru.ylab.exampleone.chernikov.lesson04.DbUtil;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class FileSorterTest {
  public static void main(String[] args) throws SQLException, IOException {
    DataSource dataSource = initDb();
    File data = new Generator().generate("src/main/resources/file_sort/data.txt", 1000000);
    System.out.println(new Validator(data).isSorted());
    FileSorter fileSorter = new FileSortImpl(dataSource);
    File res = fileSorter.sort(data);
    System.out.println(new Validator(res).isSorted());
  }
  
  public static DataSource initDb() throws SQLException {
    String createSortTable = "" 
                                 + "drop table if exists numbers;" 
                                 + "CREATE TABLE if not exists numbers (\n"
                                 + "\tval bigint\n"
                                 + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createSortTable, dataSource);
    return dataSource;
  }
}
