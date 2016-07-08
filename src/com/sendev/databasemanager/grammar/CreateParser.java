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
                com.sendev.databasemanager.grammar.mysql.Create create = new com.sendev.databasemanager.grammar.mysql.Create();

                create.setDBM(manager);
                create.setOptions(options);

                return create.format(blueprint);

            default:
                return null;
        }
    }
}
