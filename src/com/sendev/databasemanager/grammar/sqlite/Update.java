package com.sendev.databasemanager.grammar.sqlite;

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
        if (!isIgnoreingDatabasePrefix) {
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

            for (String key : keyset) {
                if (!row.containsKey(key)) {
                    addPart("NULL, ");

                    continue;
                }

                String value = row.get(key).toString();
                String formatKey = formatField(key);

                if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                    addPart(String.format(" %s = %s, ", formatKey, value.equalsIgnoreCase("true") ? 1 : 0));

                    continue;
                }

                if (isNumeric(value)) {
                    addPart(String.format("%s = %s, ", formatKey, value.toUpperCase()));

                    continue;
                }

                addPart(String.format("%s = %s, ", formatKey, value.toUpperCase()));
            }

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
