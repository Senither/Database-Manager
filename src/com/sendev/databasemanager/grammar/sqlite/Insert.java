package com.sendev.databasemanager.grammar.sqlite;

import java.util.List;
import java.util.Map;

import com.sendev.databasemanager.grammar.contracts.InsertGrammar;
import com.sendev.databasemanager.query.QueryBuilder;

public class Insert extends InsertGrammar
{
    @Override
    public String format(QueryBuilder builder)
    {
        dbm = getDBMFrom(builder);

        String table = builder.getTable();
        if (!builder.isIgnoringDatabasePrefix()) {
            table = buildTable(table);
        }

        addPart(String.format(" %s", formatField(table)));

        buildKeyset(builder);

        buildValues(builder);

        return finalize(builder);
    }

    private void buildKeyset(QueryBuilder builder)
    {
        List<Map<String, Object>> items = builder.getItems();

        items.stream().forEach(( map ) -> {
            map.keySet().stream().filter(( key ) -> (!keyset.contains(key))).forEach(( key ) -> {
                keyset.add(key);
            });
        });

        addPart(" (");

        keyset.stream().forEach(( key ) -> {
            addPart(String.format("`%s`, ", key));
        });

        removeLast(2).addPart(")");
    }

    private void buildValues(QueryBuilder builder)
    {
        addPart(" VALUES ");

        List<Map<String, Object>> items = builder.getItems();

        for (Map<String, Object> row : items) {
            addPart(" (");

            keyset.stream().forEach(( key ) -> {
                addPart((row.containsKey(key)) ? String.format("'%s', ", row.get(key)) : "NULL, ");
            });

            removeLast(2).addPart("),");
        }

        removeLast(1);
    }

    @Override
    protected String finalize(QueryBuilder builder)
    {
        addPart(";");

        return query;
    }
}
