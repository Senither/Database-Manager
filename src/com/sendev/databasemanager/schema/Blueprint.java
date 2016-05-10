package com.sendev.databasemanager.schema;

import java.util.LinkedHashMap;
import java.util.Map;

public class Blueprint
{
    private final String table;
    private final LinkedHashMap<String, Field> fields = new LinkedHashMap<>();
    private String engine = DatabaseEngine.InnoDB.toString();

    public Blueprint(String table)
    {
        this.table = table;
    }

    public void setEngine(DatabaseEngine databaseEngine)
    {
        setEngine(databaseEngine.getEngine());
    }

    public void setEngine(String engine)
    {
        this.engine = engine;
    }

    public String getEngine()
    {
        return engine;
    }

    public String getTable()
    {
        return table;
    }

    public Map<String, Field> getFields()
    {
        return fields;
    }

    public void Increments(String field)
    {
        makeField(field, FieldType.INTEGER).unsigned().autoIncrement();
    }

    public void BigIncrements(String field)
    {
        makeField(field, FieldType.LONG).unsigned().autoIncrement();
    }

    public Field Integer(String field)
    {
        return Integer(field, 17);
    }

    public Field Integer(String field, int length)
    {
        return makeField(field, FieldType.INTEGER, length);
    }

    public Field Decimal(String field)
    {
        return Decimal(field, 17);
    }

    public Field Decimal(String field, int length)
    {
        return Decimal(field, length, 2);
    }

    public Field Decimal(String field, int length, int tail)
    {
        return makeField(field, FieldType.DECIMAL, length, tail);
    }

    public Field Double(String field)
    {
        return Double(field, 15);
    }

    public Field Double(String field, int length)
    {
        return Double(field, length, 8);
    }

    public Field Double(String field, int length, int tail)
    {
        return makeField(field, FieldType.DOUBLE, length, tail);
    }

    public Field Float(String field)
    {
        return Float(field, 17);
    }

    public Field Float(String field, int length)
    {
        return makeField(field, FieldType.FLOAT, length);
    }

    public Field Boolean(String field)
    {
        return makeField(field, FieldType.BOOLEAN);
    }

    public Field Date(String field)
    {
        return makeField(field, FieldType.DATE);
    }

    public Field DateTime(String field)
    {
        return makeField(field, FieldType.DATETIME);
    }

    public Field String(String field)
    {
        return String(field, 256);
    }

    public Field String(String field, int length)
    {
        return makeField(field, FieldType.STRING, length);
    }

    public Field LongText(String field)
    {
        return makeField(field, FieldType.LONGTEXT);
    }

    public Field MediumText(String field)
    {
        return makeField(field, FieldType.MEDIUMTEXT);
    }

    public Field SmallText(String field)
    {
        return makeField(field, FieldType.SMALLTEXT);
    }

    public Field Text(String field)
    {
        return makeField(field, FieldType.TEXT);
    }

    public void Timestamps()
    {
        makeField("created_at", FieldType.DATETIME).defaultValue(new DefaultSQLAction("NOW()"));
        makeField("updated_at", FieldType.DATETIME).defaultValue(new DefaultSQLAction("NOW() ON UPDATE NOW()"));
    }

    private Field makeField(String field, FieldType type)
    {
        Field obj = new Field(type);

        fields.put(field, obj);

        return obj;
    }

    private Field makeField(String field, FieldType type, int length)
    {
        Field obj = new Field(type, length);

        fields.put(field, obj);

        return obj;
    }

    private Field makeField(String field, FieldType type, int legnth, int tail)
    {
        Field obj = new Field(type, legnth, tail);

        fields.put(field, obj);

        return obj;
    }
}
