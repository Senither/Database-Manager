package com.sendev.databasemanager.grammar.contracts;

import com.sendev.databasemanager.contracts.AlterGrammar;

public abstract class CreateGrammar extends AlterGrammar
{
    public CreateGrammar()
    {
        query = "CREATE TABLE ";
    }
}
