package com.sendev.databasemanager.query;

import com.sendev.databasemanager.grammar.CreateGrammar;
import com.sendev.databasemanager.grammar.DeleteGrammar;
import com.sendev.databasemanager.grammar.InsertGrammar;
import com.sendev.databasemanager.grammar.SelectGrammar;
import com.sendev.databasemanager.grammar.UpdateGrammar;

public enum QueryType
{

    SELECT(SelectGrammar.class),
    INSERT(InsertGrammar.class),
    UPDATE(UpdateGrammar.class),
    DELETE(DeleteGrammar.class),
    CREATE(CreateGrammar.class);

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
