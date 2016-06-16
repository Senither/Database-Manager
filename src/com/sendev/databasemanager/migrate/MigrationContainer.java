package com.sendev.databasemanager.migrate;

import com.sendev.databasemanager.migrate.contracts.Migration;

public class MigrationContainer
{
    private final String plugin;
    private String name;
    private Migration migration;
    private int batch;

    public MigrationContainer(String plugin)
    {
        this.plugin = plugin;

        this.batch = -1;
    }

    public MigrationContainer(String plugin, Migration migration)
    {
        this.plugin = plugin;
        this.migration = migration;

        name = migration.getClass().getSimpleName();
        batch = -1;
    }

    /**
     * Gets the plugin name the migration is registered to.
     *
     * @return the plugin name the migration is registered to
     */
    public String getPlugin()
    {
        return plugin;
    }

    /**
     * Sets the name of the plugin the migration is registered to.
     *
     * @param name the name of the plugin the migration is registered to
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the migration name.
     *
     * @return the name of the migration
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the migration instance for the container.
     *
     * @param migration the migration the container is linked to
     */
    public void setMigration(Migration migration)
    {
        this.migration = migration;
    }

    /**
     * Gets the migration instance linked to the container.
     *
     * @return the migration instance linked to the container
     */
    public Migration getMigration()
    {
        return migration;
    }

    /**
     * Sets the migration batch value.
     *
     * @param batch the migration batch value
     */
    public void setBatch(int batch)
    {
        this.batch = batch;
    }

    /**
     * Gets the migration batch value.
     *
     * @return the migration batch value.
     */
    public int getBatch()
    {
        return batch;
    }

    /**
     * Checks to see if the provided migration matches the container.
     *
     * @param plugin    the plugin the migration is coming from
     * @param migration the migration to compare with
     *
     * @return either (1) true if the migration matches
     *         or (2) false if it doesn't match.
     */
    public boolean match(String plugin, Migration migration)
    {
        if (!getPlugin().equals(plugin)) {
            return false;
        }

        return getName() != null && migration.getClass().getName().equals(getName());
    }
}
