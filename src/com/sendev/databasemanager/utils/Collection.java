package com.sendev.databasemanager.utils;

import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

public class Collection implements Cloneable, Iterable<DataRow>
{

    private static final Random random = new Random();

    private final HashMap<String, String> keys;
    private final List<DataRow> items;

    /**
     * Creates an empty collection.
     */
    public Collection()
    {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();
    }

    /**
     * Creates a new Collection object from a multidimensional map.
     *
     * @param items the map of items to create the collection from
     */
    public Collection(List<Map<String, Object>> items)
    {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();

        for (Map<String, Object> row : items) {
            row.keySet().stream().filter(( key ) -> (!keys.containsKey(key))).forEach(( key ) -> {
                keys.put(key, row.get(key).getClass().getTypeName());
            });

            this.items.add(new DataRow(row));
        }
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
     * Gets all the <code>DataRow</code> items from the collection.
     *
     * @return All the <code>DataRow</code> items from the collection.
     */
    public List<DataRow> all()
    {
        return items;
    }

    /**
     * Calculates the average for a field
     *
     * @param field the field to calculated the average of
     *
     * @return the average for the provided field
     */
    public double avgInt(String field)
    {
        if (isEmpty()) {
            return 0;
        }

        int avg = 0;

        for (DataRow row : items) {
            avg += row.getInt(field);
        }

        return avg / items.size();
    }

    /**
     * Breaks the collection into multiple, smaller lists of the given size.
     *
     * @param size the size to chunk the collection down to
     *
     * @return the chunked down collection
     */
    public List<List<DataRow>> chunk(int size)
    {
        List<List<DataRow>> chunk = new ArrayList<>();

        int index = 0, counter = 0;
        for (DataRow row : items) {
            if (counter++ >= size) {
                index++;
                counter = 0;
            }

            try {
                chunk.get(index);
            } catch (IndexOutOfBoundsException e) {
                chunk.add(index, new ArrayList<>());
            } finally {
                chunk.get(index).add(row);
            }
        }

        return chunk;
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
     * Gets the result of the provided index.
     *
     * @param index the index to get from the collection
     *
     * @return the DataRow object in the provided index
     */
    public DataRow get(int index)
    {
        if (items.isEmpty()) {
            return null;
        }

        return items.get(index);
    }

    /**
     * Checks to see the collection contains the provided field.
     *
     * @param field the field to check if exists
     *
     * @return ture if the field exists in the collection, false if it doesn't
     */
    public boolean has(String field)
    {
        return keys.containsKey(field);
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    public boolean isEmpty()
    {
        return items.isEmpty();
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

    /**
     * Gets the last DataRow of the collection.
     *
     * @return the last DataRow of the collection
     */
    public DataRow last()
    {
        if (isEmpty()) {
            return null;
        }

        return items.get(items.size() - 1);
    }

    /**
     * Gets the max/highest integer value from the provided field.
     *
     * @param field the field to use
     *
     * @return either (1) the highest value from the provided field
     *         or (2) <code>Integer.MIN_VALUE</code>
     */
    public int maxInt(String field)
    {
        if (!has(field)) {
            return Integer.MIN_VALUE;
        }

        int max = Integer.MIN_VALUE;
        for (DataRow row : items) {
            int x = row.getInt(field);

            if (max < x) {
                max = x;
            }
        }

        return max;
    }

    /**
     * Gets the min/lowest integer value from the provided field.
     *
     * @param field the field to use
     *
     * @return either (1) the lowest value from the provided field
     *         or (2) <code>Integer.MAX_VALUE</code>
     */
    public int minInt(String field)
    {
        if (!has(field)) {
            return Integer.MAX_VALUE;
        }

        int min = Integer.MAX_VALUE;
        for (DataRow row : items) {
            int x = row.getInt(field);

            if (min > x) {
                min = x;
            }
        }

        return min;
    }

    /**
     * Removes and returns the last item of the collection.
     *
     * @return the last item of the collection
     */
    public DataRow pop()
    {
        if (isEmpty()) {
            return null;
        }

        return items.remove(items.size() - 1);
    }

    /**
     * Gets a random item from the collection
     *
     * @return a random item from the collection
     */
    public DataRow random()
    {
        if (isEmpty()) {
            return null;
        }

        return items.get(random.nextInt(items.size()));
    }

    /**
     * Search the collection where the field is equal to the value.
     *
     * @param field the field to check
     * @param value the value to use
     *
     * @return either (1) the index of the item that matches the search
     *         or (2) -1
     */
    public int search(String field, Object value)
    {
        if (isEmpty() || !has(field)) {
            return -1;
        }

        String rValue = value.toString();

        for (int index = 0; index < items.size(); index++) {
            DataRow row = get(index);

            if (row.getString(field).equals(rValue)) {
                return index;
            }
        }

        return -1;
    }

    /**
     * Gets the total number of items in the collection.
     *
     * @return the total number of items in the collection
     */
    public int size()
    {
        return items.size();
    }

    /**
     * Calculates the sum of a list of integers.
     *
     * @param field the field to calculated the sum of
     *
     * @return the sum for the provided field
     */
    public long sumInt(String field)
    {
        long sum = 0;

        if (!has(field)) {
            return sum;
        }

        for (DataRow row : items) {
            sum += row.getInt(field);
        }

        return sum;
    }

    /**
     * Gets all the data rows where the field equals the value, this uses strict
     * comparisons to match the values, use the {@link #whereLoose(String, Object) whereLosse}
     * method to filter using "losse" comparisons.
     *
     * @param field The field that should be matched
     * @param value The value that should match the field
     *
     * @return a list of data row objects that match the where clause
     */
    public List<DataRow> where(String field, Object value)
    {
        if (isEmpty() || !has(field)) {
            return new ArrayList<>();
        }

        String rValue = value.toString();
        List<DataRow> rows = new ArrayList<>();

        items.stream().filter(( row ) -> (row.getString(field).equals(rValue))).forEach(( row ) -> {
            rows.add(row);
        });

        return rows;
    }

    /**
     * Gets all the data rows where the field equals the value, this uses strict
     * comparisons to match the values, use the {@link #whereLoose(String, Object) whereLosse}
     * method to filter using "losse" comparisons.
     *
     * @param field The field that should be matched
     * @param value The value that should match the field
     *
     * @return a list of data row objects that match the where clause
     */
    public List<DataRow> whereLoose(String field, Object value)
    {
        if (isEmpty() || !has(field)) {
            return new ArrayList<>();
        }

        String rValue = value.toString();
        List<DataRow> rows = new ArrayList<>();

        items.stream().filter(( row ) -> (row.getString(field).equalsIgnoreCase(rValue))).forEach(( row ) -> {
            rows.add(row);
        });

        return rows;
    }

    @Override
    public String toString()
    {
        return toJson();
    }

    /**
     * Converts the collection to a JSON string using Gson.
     *
     * @return the JSON collection string
     */
    public String toJson()
    {
        Gson gson = new Gson();

        return gson.toJson(items);
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
