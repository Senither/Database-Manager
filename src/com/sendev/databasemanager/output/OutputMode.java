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

    /**
     * Returns the output mode as a object instance.
     *
     * @param <T> the class instance that is parsed
     *
     * @return the output mode as a object instance
     */
    public <T> Class<T> getInstance()
    {
        return (Class<T>) instance;
    }
}
