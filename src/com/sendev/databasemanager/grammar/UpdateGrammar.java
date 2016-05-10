package com.sendev.databasemanager.grammar;

import com.sendev.databasemanager.contracts.TableGrammar;
import com.sendev.databasemanager.query.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateGrammar extends TableGrammar
{

    private final List<String> keyset = new ArrayList<>();

    public UpdateGrammar()
    {
        query = "UPDATE ";
    }

    @Override
    public String format(QueryBuilder builder)
    {
        addPart(String.format(" `%s` SET", builder.getTable()));

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
