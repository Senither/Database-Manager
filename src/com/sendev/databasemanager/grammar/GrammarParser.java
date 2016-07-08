package com.sendev.databasemanager.grammar;

import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.connections.MySQL;
import com.sendev.databasemanager.connections.SQLite;
import com.sendev.databasemanager.contracts.AlterGrammar;
import com.sendev.databasemanager.contracts.Database;
import com.sendev.databasemanager.contracts.TableGrammar;
import com.sendev.databasemanager.query.QueryBuilder;
import com.sendev.databasemanager.schema.Blueprint;
import java.util.HashMap;
import java.util.Map;

public abstract class GrammarParser
{
    protected Map<String, Boolean> options = new HashMap<>();

    public String parse(DatabaseManager manager, Database connection, QueryBuilder query)
    {
        return null;
    }

    public String parse(DatabaseManager manager, Database connection, Blueprint blueprint)
    {
        return null;
    }

    public void setOption(String option, boolean value)
    {
        this.options.put(option, value);
    }

    protected ConnectionType getType(Database connection)
    {
        if (connection instanceof MySQL) {
            return ConnectionType.MySQL;
        }

        if (connection instanceof SQLite) {
            return ConnectionType.SQLite;
        }

        return null;
    }

    protected String setupAndRun(TableGrammar grammar, QueryBuilder builder, DatabaseManager manager, Map<String, Boolean> options)
    {
        grammar.setDBM(manager);
        grammar.setOptions(options);
        
        return grammar.format(builder);
    }
    
    protected String setupAndRun(AlterGrammar grammar, Blueprint blueprint, DatabaseManager manager, Map<String, Boolean> options)
    {
        grammar.setDBM(manager);
        grammar.setOptions(options);
        
        return grammar.format(blueprint);
    }

    protected enum ConnectionType
    {
        MySQL,
        SQLite;
    }
}
