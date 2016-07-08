package com.sendev.databasemanager.grammar.contracts;

import com.sendev.databasemanager.contracts.TableGrammar;
import java.util.ArrayList;
import java.util.List;

public abstract class UpdateGrammar extends TableGrammar
{
    protected final List<String> keyset = new ArrayList<>();

    public UpdateGrammar()
    {
        query = "UPDATE ";
    }
}
