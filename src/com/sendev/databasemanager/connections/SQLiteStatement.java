package com.sendev.databasemanager.connections;

import com.sendev.databasemanager.contracts.StatementContract;

public enum SQLiteStatement implements StatementContract
{
    SELECT("SELECT"),
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    REPLACE("REPLACE"),
    CREATE("CREATE"),
    ALTER("ALTER"),
    DROP("DROP"),
    ANALYZE("ANALYZE"),
    ATTACH("ATTACH"),
    BEGIN("BEGIN"),
    DETACH("DETACH"),
    END("END"),
    EXPLAIN("EXPLAIN"),
    INDEXED("INDEXED"),
    PRAGMA("PRAGMA"),
    REINDEX("REINDEX"),
    RELEASE("RELEASE"),
    SAVEPOINT("SAVEPOINT"),
    VACUUM("VACUUM"),
    LINE_COMMENT("--"),
    BLOCK_COMMENT("/*");

    private final String name;

    private SQLiteStatement(String string)
    {
        this.name = string;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
