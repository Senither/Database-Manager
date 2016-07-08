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
                com.sendev.databasemanager.grammar.mysql.Update update = new com.sendev.databasemanager.grammar.mysql.Update();

                return update.format(query);

            default:
                return null;
        }
    }
}
