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
                com.sendev.databasemanager.grammar.mysql.Delete delete = new com.sendev.databasemanager.grammar.mysql.Delete();

                return delete.format(query);

            default:
                return null;
        }
    }
}
