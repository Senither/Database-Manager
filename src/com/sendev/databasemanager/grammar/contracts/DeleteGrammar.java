package com.sendev.databasemanager.grammar.contracts;

import com.sendev.databasemanager.contracts.TableGrammar;

public abstract class DeleteGrammar extends TableGrammar
{
    public DeleteGrammar()
    {
        query = "DELETE FROM ";
    }
}
