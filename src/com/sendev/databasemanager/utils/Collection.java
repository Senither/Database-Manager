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

    public Collection()
    {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();
    }

    /**
     * Creates a new Collection instance, allowing you to the loop
     * and fetch data from a ResultSet object a lot easier.
     *
     * @param result The ResultSet to generate the collection from.
     *
     * @exception SQLException if a database access error occurs,
     *                         this exception is thrown if the collection was unable to read
     *                         form the database <code>ResultSet</code> object, or if the object
     *                         didn't return a valid response.
     */
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

    /**
     * Gets the first index of the collection.
     *
     * @return either (1) The first <code>DataRow</code> object, generated from the <code>ResultSet</code> object,
     *         or (2) <code>NULL</code> if the collection doesn't have any items.
     */
    public DataRow first()
    {
        if (items.isEmpty()) {
            return null;
        }

        return items.get(0);
    }

    /**
     * Gets all the <code>DataRow</code> items from the collection.
     *
     * @return All the <code>DataRow</code> items from the collection.
     */
    public List<DataRow> all()
    {
        return items;
    }

    /**
     * Gets every column with the given index, if the index doesn't
     * exists a <code>DatabaseException</code> will be thrown.
     *
     * @param name The index(name) to lookup.
     *
     * @return either (1) A list of objects with the given index,
     *         or (2) <code>NULL</code> if the given index doesn't exists.
     */
    public List<Object> get(String name)
    {
        if (!keys.containsKey(name)) {
            return null;
        }

        List<Object> objects = new ArrayList<>();

        items.stream().forEach(( row ) -> {
            objects.add(row.get(name));
        });

        return objects;
    }

    /**
     * Gets every column with the given index, if the index doesn't
     * exists a <code>DatabaseException</code> will be thrown.
     *
     * @param name The index(name) to lookup.
     *
     * @return A list of strings with the given index.
     */
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

    /**
     * Gets all the keys from the <code>ResultSet</code> object in the form of a
     * <code>HashMap</code>, where the key is the database table column
     * name, and the value is the database column type.
     *
     * @return A map of all the database keys.
     */
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
