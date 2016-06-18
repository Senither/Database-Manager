package com.sendev.databasemanager.grammar;

import com.sendev.databasemanager.contracts.TableGrammar;
import com.sendev.databasemanager.query.QueryBuilder;

public class DeleteGrammar extends TableGrammar
{

    public DeleteGrammar()
    {
        query = "DELETE FROM ";
    }

    @Override
    public String format(QueryBuilder builder)
    {
        dbm = getDBMFrom(builder);

        String table = builder.getTable();
        if (!builder.isIgnoringDatabasePrefix()) {
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
