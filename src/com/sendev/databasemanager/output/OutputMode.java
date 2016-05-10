package com.sendev.databasemanager.output;

public enum OutputMode
{
    SILENT(SilentOutput.class),
    INFO(InfoOutput.class),
    WARNING(WarningOutput.class),
    ERROR(ErrorOutput.class);

    private final Object instance;

    private OutputMode(Object instance)
    {
        this.instance = instance;
    }

    public <T> Class<T> getInstance()
    {
        return (Class<T>) instance;
    }
}
