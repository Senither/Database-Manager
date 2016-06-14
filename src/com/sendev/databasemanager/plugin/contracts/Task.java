package com.sendev.databasemanager.plugin.contracts;

import com.sendev.databasemanager.plugin.DBMPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public abstract class Task implements Runnable
{

    protected final DBMPlugin plugin;
    protected BukkitTask task = null;

    public Task(DBMPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void startTask()
    {
        if (task == null) {
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay(), delay());
        }
    }

    public void stopTask()
    {
        if (task != null) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());

            task = null;
        }
    }

    public abstract int delay();
}
