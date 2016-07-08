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
                com.sendev.databasemanager.grammar.mysql.Select select = new com.sendev.databasemanager.grammar.mysql.Select();

                return select.format(query);

            default:
                return null;
        }
    }
}
