package com.sendev.databasemanager;

public enum ConnectionLevel
{
    DEFAULT(-1),
    HIGEST(10),
    HIGH(7),
    MEDIUM(5),
    LOW(3),
    LOWEST(0);

    private final int level;

    private ConnectionLevel(int level)
    {
        this.level = level;
    }

    public int getLevel()
    {
        return level;
    }
}
