package com.sendev.databasemanager.utils;

import com.sendev.databasemanager.exceptions.DatabaseException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Collection implements Cloneable, Iterable<DataRow>
{

    private final HashMap<String, String> keys;
    private final List<DataRow> items;

    public Collection(ResultSet result) throws SQLException
    {
        ResultSetMetaData meta = result.getMetaData();

        this.keys = new HashMap<>();
        this.items = new ArrayList<>();

        for (int i = 1; i <= meta.getColumnCount(); i++) {
            keys.put(meta.getColumnLabel(i), meta.getColumnClassName(i));
        }

        while (result.next()) {
            Map<String, Object> array = new HashMap<>();

            for (String key : keys.keySet()) {
                array.put(key, result.getString(key));
            }

            items.add(new DataRow(array));
        }
    }

    public DataRow first()
    {
        if (items.isEmpty()) {
            return null;
        }

        return items.get(0);
    }

    public List<DataRow> all()
    {
        return items;
    }

    public List<Object> get(String name)
    {
        if (!keys.containsKey(name)) {
            throw new DatabaseException("");
        }

        List<Object> objects = new ArrayList<>();

        items.stream().forEach(( row ) -> {
            objects.add(row.get(name));
        });

        return objects;
    }

    public List<String> getStrings(String name)
    {
        if (!keys.containsKey(name)) {
            throw new DatabaseException("");
        }

        List<String> objects = new ArrayList<>();

        items.stream().forEach(( row ) -> {
            objects.add((String) row.get(name));
        });

        return objects;
    }

    public HashMap<String, String> getKeys()
    {
        return keys;
    }

    @Override
    public Iterator<DataRow> iterator()
    {
        return new CollectionIterator();
    }

    private class CollectionIterator implements Iterator<DataRow>
    {

        private int cursor = 0;

        @Override
        public boolean hasNext()
        {
            return cursor < Collection.this.items.size();
        }

        @Override
        public DataRow next()
        {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return Collection.this.items.get(cursor++);
        }
    }
}
