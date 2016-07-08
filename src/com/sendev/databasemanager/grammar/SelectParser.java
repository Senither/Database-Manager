package com.sendev.databasemanager.grammar;

import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.contracts.Database;
import com.sendev.databasemanager.query.QueryBuilder;

public class SelectParser extends GrammarParser
{
    @Override
    public String parse(DatabaseManager manager, Database connection, QueryBuilder query)
    {
        switch (getType(connection)) {
            case MySQL:
                return setupAndRun(new com.sendev.databasemanager.grammar.mysql.Select(), query, manager, options);

            case SQLite:
                return setupAndRun(new com.sendev.databasemanager.grammar.sqlite.Select(), query, manager, options);

            default:
                return null;
        }
    }
}
