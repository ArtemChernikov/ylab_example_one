package io.ylab.intensive.taskthree.dated_map;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 18.03.2023
 */
public class DatedMapImpl implements DatedMap {
    /**
     * Поле хранилище значений
     */
    private final Map<String, String> repository;
    /**
     * Поле хранилище со временем добавления каждого значения в {@link DatedMapImpl#repository},
     */
    private final Map<String, Date> addedKeyTime;

    public DatedMapImpl() {
        this.repository = new HashMap<>();
        this.addedKeyTime = new HashMap<>();
    }

    /**
     * Метод используется для добавления пары ключ-значение в {@link DatedMapImpl#repository}
     * и добавления пары ключ-значение (ключ из {@link DatedMapImpl#repository},
     * значение-дата добавления значения в {@link DatedMapImpl#repository})
     *
     * @param key - ключ для удаления
     */
    @Override
    public void put(String key, String value) {
        repository.put(key, value);
        addedKeyTime.put(key, new Date());
    }

    @Override
    public String get(String key) {
        return repository.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return repository.containsKey(key);
    }

    /**
     * Метод используется для удаления данных по ключу из хранилищ
     * {@link DatedMapImpl#repository} и {@link DatedMapImpl#addedKeyTime}
     *
     * @param key - ключ для удаления
     */
    @Override
    public void remove(String key) {
        repository.remove(key);
        addedKeyTime.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return repository.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        return addedKeyTime.get(key);
    }
}
