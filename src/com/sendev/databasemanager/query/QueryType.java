package com.sendev.databasemanager.query;

import com.sendev.databasemanager.grammar.CreateParser;
import com.sendev.databasemanager.grammar.DeleteParser;
import com.sendev.databasemanager.grammar.InsertParser;
import com.sendev.databasemanager.grammar.SelectParser;
import com.sendev.databasemanager.grammar.UpdateParser;

public enum QueryType
{

    SELECT(SelectParser.class),
    INSERT(InsertParser.class),
    UPDATE(UpdateParser.class),
    DELETE(DeleteParser.class),
    CREATE(CreateParser.class);

    private final Class grammar;

    private QueryType(Class grammar)
    {
        this.grammar = grammar;
    }

    public <T> Class<T> getGrammar()
    {
        return grammar;
    }
}
