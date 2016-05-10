package com.sendev.databasemanager.connections;

import com.sendev.databasemanager.contracts.StatementContract;

public enum MySQLStatement implements StatementContract
{
    SELECT("SELECT"),
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    DO("DO"),
    REPLACE("REPLACE"),
    LOAD("LOAD"),
    HANDLER("HANDLER"),
    CALL("CALL"),
    CREATE("CREATE"),
    ALTER("ALTER"),
    DROP("DROP"),
    TRUNCATE("TRUNCATE"),
    RENAME("RENAME"),
    START("START"),
    COMMIT("COMMIT"),
    SAVEPOINT("SAVEPOINT"),
    ROLLBACK("ROLLBACK"),
    RELEASE("RELEASE"),
    LOCK("LOCK"),
    UNLOCK("UNLOCK"),
    PREPARE("PREPARE"),
    EXECUTE("EXECUTE"),
    DEALLOCATE("DEALLOCATE"),
    SET("SET"),
    SHOW("SHOW"),
    DESCRIBE("DESCRIBE"),
    EXPLAIN("EXPLAIN"),
    HELP("HELP"),
    USE("USE");

    private final String string;

    private MySQLStatement(String string)
    {
        this.string = string;
    }

    @Override
    public String toString()
    {
        return string;
    }
}
