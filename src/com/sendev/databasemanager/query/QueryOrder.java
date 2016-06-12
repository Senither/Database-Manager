package com.sendev.databasemanager.query;

public class QueryOrder
{
    private final String prefix;
    private final String field;
    private final String type;
    private final boolean rawSQL;

    public QueryOrder(String field, String type)
    {
        this.prefix = null;
        this.rawSQL = false;

        this.field = field;
        this.type = type;
    }

    public QueryOrder(String field, String type, boolean rawSQL)
    {
        this.prefix = null;

        this.field = field;
        this.type = type;
        this.rawSQL = rawSQL;
    }

    public QueryOrder(String prefix, String field, String type, boolean rawSQL)
    {
        this.prefix = prefix;
        this.field = field;
        this.type = type;
        this.rawSQL = rawSQL;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getField()
    {
        return field;
    }

    public String getType()
    {
        return type;
    }

    public boolean isRawSQL()
    {
        return rawSQL;
    }
}
