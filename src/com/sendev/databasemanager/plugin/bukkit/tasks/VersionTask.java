package com.sendev.databasemanager.plugin.bukkit.tasks;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sendev.databasemanager.plugin.bukkit.DBMPlugin;
import com.sendev.databasemanager.plugin.bukkit.contracts.Task;
import com.sendev.databasemanager.plugin.utils.Version;

public class VersionTask extends Task
{
    private String lastVersionRecived = null;

    public VersionTask(DBMPlugin plugin, String lastVersionRecived)
    {
        super(plugin);

        this.lastVersionRecived = lastVersionRecived;
    }

    /**
     * Gets the last received version from the version fetcher.
     *
     * @return the last received version from the version fetcher
     */
    public String getLastVersionReceived()
    {
        return lastVersionRecived;
    }

    @Override
    public int delay()
    {
        return 36000;
    }

    @Override
    public void run()
    {
        try {
            lastVersionRecived = Version.fetch();
        } catch (IOException ex) {
            Logger.getLogger(VersionTask.class.getName()).log(Level.INFO,
            "[DBM] Attempted to fetch the latest version from SenDevelopment has failed, trying again in 30 minutes!");
        }
    }
}
