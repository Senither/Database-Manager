package com.sendev.databasemanager.utils;

import com.sendev.databasemanager.exceptions.InvalidFormatException;
import java.util.Map;
import java.util.Set;

public class DataRow
{

    private final Map<String, Object> items;

    /**
     * Creates a new data row object from a map of data.
     *
     * @param items The map to generate the data row from.
     */
    public DataRow(Map<String, Object> items)
    {
        this.items = items;
    }

    /**
     * Checks to see if the given index exists in the data rows list of items.
     *
     * @param name The index(name) to check if exists.
     *
     * @return true if the index exists, otherwise it will return false.
     */
    public boolean has(String name)
    {
        return items.containsKey(name);
    }

    /**
     * Gets all the keys from the data row.
     *
     * @return All the keys from the data row.
     */
    public Set<String> keySet()
    {
        return items.keySet();
    }

    /**
     * Gets a object from the data rows item list.
     *
     * @param name The index(name) to get.
     *
     * @return either (1) The value of the index given,
     *         or (2) <code>NULL</code> if the index doesn't exists.
     */
    public Object get(String name)
    {
        return get(name, null);
    }

    /**
     * Gets a object from the data rows item list.
     *
     * @param name The index(name) to get.
     * @param def  The default vault to return if the index doesn't exists.
     *
     * @return either (1) The value of the index given,
     *         or (2) the default value given.
     */
    public Object get(String name, Object def)
    {
        if (has(name)) {
            return items.get(name);
        }

        return def;
    }

    /**
     * Gets a boolean object from the data rows item list.
     *
     * @param name The index(name) to get.
     *
     * @return either (1) The value of the index given,
     *         or (2) <code>FALSE</code> if the index doesn't exists.
     */
    public boolean getBoolean(String name)
    {
        return getBoolean(name, false);
    }

    /**
     * Gets a boolean object from the data rows item list.
     *
     * @param name The index(name) to get.
     * @param def  The default vault to return if the index doesn't exists.
     *
     * @return either (1) The value of the index given,
     *         or (2) the default value given.
     */
    public boolean getBoolean(String name, boolean def)
    {
        Object value = get(name, def);

        if (isString(value)) {
            String str = String.valueOf(value);

            return isEqual(str, "1", "true");
        }

        return (boolean) value;
    }

    /**
     * Gets a string object from the data rows item list.
     *
     * @param name The index(name) to get.
     *
     * @return either (1) The value of the index given,
     *         or (2) <code>NULL</code> if the index doesn't exists.
     */
    public String getString(String name)
    {
        return getString(name, null);
    }

    /**
     * Gets a string object from the data rows item list.
     *
     * @param name The index(name) to get.
     * @param def  The default vault to return if the index doesn't exists.
     *
     * @return either (1) The value of the index given,
     *         or (2) the default value given.
     */
    public String getString(String name, String def)
    {
        Object value = get(name, def);

        return String.valueOf(value);
    }

    /**
     * Gets a double object from the data rows item list.
     *
     * @param name The index(name) to get.
     *
     * @return either (1) The value of the index given,
     *         or (2) <code>0.0D</code> if the index doesn't exists.
     */
    public double getDouble(String name)
    {
        return getDouble(name, 0.0D);
    }

    /**
     * Gets a double object from the data rows item list.
     *
     * @param name The index(name) to get.
     * @param def  The default vault to return if the index doesn't exists.
     *
     * @return either (1) The value of the index given,
     *         or (2) the default value given.
     */
    public double getDouble(String name, double def)
    {
        Object value = get(name, def);

        if (isString(value)) {
            String str = String.valueOf(value);

            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException ex) {
                return def;
            }
        }

        return (double) value;
    }

    /**
     * Gets a integer object from the data rows item list.
     *
     * @param name The index(name) to get.
     *
     * @return either (1) The value of the index given,
     *         or (2) <code>0</code> if the index doesn't exists.
     */
    public int getInt(String name)
    {
        return getInt(name, 0);
    }

    /**
     * Gets a integer object from the data rows item list.
     *
     * @param name The index(name) to get.
     * @param def  The default vault to return if the index doesn't exists.
     *
     * @return either (1) The value of the index given,
     *         or (2) the default value given.
     */
    public int getInt(String name, int def)
    {
        Object value = get(name, def);

        if (isString(value)) {
            String str = String.valueOf(value);

            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException ex) {
                return def;
            }
        }

        return (int) value;
    }

    /**
     * Gets a long object from the data rows item list.
     *
     * @param name The index(name) to get.
     *
     * @return either (1) The value of the index given,
     *         or (2) <code>0L</code> if the index doesn't exists.
     */
    public long getLong(String name)
    {
        return getLong(name, 0L);
    }

    /**
     * Gets a long object from the data rows item list.
     *
     * @param name The index(name) to get.
     * @param def  The default vault to return if the index doesn't exists.
     *
     * @return either (1) The value of the index given,
     *         or (2) the default value given.
     */
    public long getLong(String name, long def)
    {
        Object value = get(name, def);

        if (isString(value)) {
            String str = String.valueOf(value);

            try {
                return Long.parseLong(str);
            } catch (NumberFormatException ex) {
                return def;
            }
        }

        return (long) value;
    }

    /**
     * Gets a carbon timestamp object from the data rows item list.
     *
     * @param name The index(name) to get.
     *
     * @return either (1) The value of the index given,
     *         or (2) <code>NULL</code> if the index doesn't exists.
     */
    public Carbon getTimestamp(String name)
    {
        return getTimestamp(name, null);
    }

    /**
     * Gets a carbon timestamp object from the data rows item list.
     *
     * @param name The index(name) to get.
     * @param def  The default vault to return if the index doesn't exists.
     *
     * @return either (1) The value of the index given,
     *         or (2) the default value given.
     */
    public Carbon getTimestamp(String name, Carbon def)
    {
        try {
            String time = getString(name);

            return new Carbon(time);
        } catch (InvalidFormatException ex) {
            return def;
        }
    }

    private boolean isString(Object name)
    {
        return getType(name).equalsIgnoreCase("string");
    }

    private String getType(Object name)
    {
        return name.getClass().getSimpleName();
    }

    private boolean isEqual(String name, String... items)
    {
        for (String item : items) {
            if (name.equalsIgnoreCase(item)) {
                return true;
            }
        }

        return false;
    }
}
