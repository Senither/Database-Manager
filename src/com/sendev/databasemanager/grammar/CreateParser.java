package com.sendev.databasemanager.grammar;

import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.contracts.Database;
import com.sendev.databasemanager.schema.Blueprint;

public class CreateParser extends GrammarParser
{
    @Override
    public String parse(DatabaseManager manager, Database connection, Blueprint blueprint)
    {
        switch (getType(connection)) {
            case MySQL:
                return setupAndRun(new com.sendev.databasemanager.grammar.mysql.Create(), blueprint, manager, options);

            case SQLite:
                return setupAndRun(new com.sendev.databasemanager.grammar.sqlite.Create(), blueprint, manager, options);

            default:
                return null;
        }
    }
}
