package com.sendev.databasemanager;

public enum ConnectionLevel
{
    /**
     * The default value will always be used before any other
     * level, it's level is -1 but it isn't actually used.
     * <p>
     * For any other connection level, the higher it is the
     * higher priority it would be equivalent to.
     * <p>
     * Level: -1
     */
    DEFAULT(-1),
    /**
     * Highest connection level.
     * <p>
     * Level: 10
     */
    HIGHEST(10),
    /**
     * High connection level.
     * <p>
     * Level: 7
     */
    HIGH(7),
    /**
     * Medium connection level.
     * <p>
     * Level: 5
     */
    MEDIUM(5),
    /**
     * Low connection level.
     * <p>
     * Level: 3
     */
    LOW(3),
    /**
     * Lowest connection level.
     * <p>
     * Level: 0
     */
    LOWEST(0);

    private final int level;

    private ConnectionLevel(int level)
    {
        this.level = level;
    }

    /**
     * Returns the connection level.
     *
     * @return the connection level
     */
    public int getLevel()
    {
        return level;
    }
}
