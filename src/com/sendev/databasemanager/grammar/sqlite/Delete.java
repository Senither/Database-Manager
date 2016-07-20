package com.sendev.databasemanager.grammar.sqlite;

import com.sendev.databasemanager.grammar.contracts.DeleteGrammar;
import com.sendev.databasemanager.query.QueryBuilder;

public class Delete extends DeleteGrammar
{
    @Override
    public String format(QueryBuilder builder)
    {
        String table = builder.getTable();
        if (!isIgnoreingDatabasePrefix) {
            table = buildTable(table);
        }

        addPart(String.format(" %s", formatField(table)));

        buildWhereClause(builder);

        return finalize(builder);
    }

    @Override
    protected String finalize(QueryBuilder builder)
    {
        addPart(";");

        return query;
    }
}
