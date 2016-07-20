package com.sendev.databasemanager.grammar.mysql;

import java.util.List;
import java.util.Map;

import com.sendev.databasemanager.grammar.contracts.UpdateGrammar;
import com.sendev.databasemanager.query.QueryBuilder;

public class Update extends UpdateGrammar
{
    @Override
    public String format(QueryBuilder builder)
    {
        dbm = getAndBuildDBMFrom(builder);

        String table = builder.getTable();
        if (!builder.isIgnoringDatabasePrefix()) {
            table = buildTable(table);
        }

        addPart(String.format(" %s SET", formatField(table)));

        buildKeyset(builder);

        buildValues(builder);

        buildWhereClause(builder);

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

    }

    private void buildValues(QueryBuilder builder)
    {
        List<Map<String, Object>> items = builder.getItems();

        for (Map<String, Object> row : items) {

            keyset.stream().forEach(( key ) -> {
                addPart((row.containsKey(key)) ? String.format(" %s = '%s', ", formatField(key), row.get(key)) : " NULL, ");
            });

            removeLast(2).addPart(" ");
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
