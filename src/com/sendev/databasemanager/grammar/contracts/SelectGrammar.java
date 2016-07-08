package com.sendev.databasemanager.grammar.contracts;

import com.sendev.databasemanager.contracts.TableGrammar;

public abstract class SelectGrammar extends TableGrammar
{
    public SelectGrammar()
    {
        query = "SELECT ";
    }
}
