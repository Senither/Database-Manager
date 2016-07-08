package com.sendev.databasemanager.grammar;

import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.contracts.Database;
import com.sendev.databasemanager.query.QueryBuilder;

public class UpdateParser extends GrammarParser
{
    @Override
    public String parse(DatabaseManager manager, Database connection, QueryBuilder query)
    {
        switch (getType(connection)) {
            case MySQL:
                return setupAndRun(new com.sendev.databasemanager.grammar.mysql.Update(), query, manager, options);

            case SQLite:
                return setupAndRun(new com.sendev.databasemanager.grammar.sqlite.Update(), query, manager, options);

            default:
                return null;
        }
    }
}
