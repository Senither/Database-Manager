package com.sendev.databasemanager.schema;

public enum DatabaseEngine
{
    MyISAM("MyISAM"),
    InnoDB("InnoDB"),
    MERGE("MERGE"),
    MEMORY("MEMORY"),
    BDB("BDB"),
    EXAMPLE("EXAMPLE"),
    FEDERATED("FEDERATED"),
    ARCHIVE("ARCHIVE"),
    CSV("CSV"),
    BLACKHOLE("BLACKHOLE");

    private final String engine;

    private DatabaseEngine(String engine)
    {
        this.engine = engine;
    }

    public String getEngine()
    {
        return engine;
    }
}
