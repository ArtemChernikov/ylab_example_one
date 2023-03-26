package ru.ylab.exampleone.chernikov.lesson04.filesort;

import javax.sql.DataSource;
import java.io.File;

public class FileSortImpl implements FileSorter {
  private DataSource dataSource;

  public FileSortImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public File sort(File data) {
    // ТУТ ПИШЕМ РЕАЛИЗАЦИЮ
    return null;
  }
}
