package com.sendev.databasemanager.grammar.contracts;

import java.util.ArrayList;
import java.util.List;

import com.sendev.databasemanager.contracts.TableGrammar;

public abstract class UpdateGrammar extends TableGrammar
{
    protected final List<String> keyset = new ArrayList<>();

    public UpdateGrammar()
    {
        query = "UPDATE ";
    }
}
