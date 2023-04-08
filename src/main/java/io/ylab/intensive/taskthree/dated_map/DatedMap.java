package io.ylab.intensive.taskthree.dated_map;

import java.util.Date;
import java.util.Set;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 18.03.2023
 */
public interface DatedMap {
    void put(String key, String value);

    String get(String key);

    boolean containsKey(String key);

    void remove(String key);

    Set<String> keySet();

    Date getKeyLastInsertionDate(String key);
}
