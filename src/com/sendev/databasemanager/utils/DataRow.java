package com.sendev.databasemanager.utils;

import java.text.ParseException;
import java.util.Map;
import java.util.Set;

public class DataRow
{

    private final Map<String, Object> items;

    public DataRow(Map<String, Object> items)
    {
        this.items = items;
    }

    public boolean has(String name)
    {
        return items.containsKey(name);
    }

    public Set<String> keySet()
    {
        return items.keySet();
    }

    public Object get(String name)
    {
        if (has(name)) {
            return items.get(name);
        }

        return null;
    }

    public Object get(String name, Object def)
    {
        if (has(name)) {
            return items.get(name);
        }

        return def;
    }

    public boolean getBoolean(String name)
    {
        return getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean def)
    {
        Object value = get(name, def);

        if (isString(value)) {
            String str = String.valueOf(value);

            return isEqual(str, "1", "true");
        }

        return (boolean) value;
    }

    public String getString(String name)
    {
        return getString(name, null);
    }

    public String getString(String name, String def)
    {
        Object value = get(name, def);

        return String.valueOf(value);
    }

    public double getDouble(String path)
    {
        return getDouble(path, 0.0D);
    }

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

    public int getInt(String name)
    {
        return getInt(name, 0);
    }

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

    public long getLong(String name)
    {
        return getLong(name, 0L);
    }

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

    public Carbon getTimestamp(String name)
    {
        return getTimestamp(name, null);
    }

    public Carbon getTimestamp(String name, Carbon def)
    {
        try {
            String time = getString(name);

            return new Carbon(time);
        } catch (ParseException ex) {
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
