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
        addPart(String.format(" `%s`", builder.getTable()));

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
