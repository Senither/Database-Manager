package com.sendev.databasemanager.grammar;

import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.contracts.Database;
import com.sendev.databasemanager.query.QueryBuilder;

public class DeleteParser extends GrammarParser
{
    @Override
    public String parse(DatabaseManager manager, Database connection, QueryBuilder query)
    {
        switch (getType(connection)) {
            case MySQL:
                return setupAndRun(new com.sendev.databasemanager.grammar.mysql.Delete(), query, manager, options);

            case SQLite:
                return setupAndRun(new com.sendev.databasemanager.grammar.sqlite.Delete(), query, manager, options);

            default:
                return null;
        }
    }
}
