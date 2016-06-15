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

    /**
     * Starts the task by using the Bukkit scheduler.
     */
    public void startTask()
    {
        if (task == null) {
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay(), delay());
        }
    }

    /**
     * Stops/Cancels the task using the Bukkit scheduler, the task can be
     * restarted by using the {@link #startTask() startTask} method.
     */
    public void stopTask()
    {
        if (task != null) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());

            task = null;
        }
    }

    /**
     * The delay at which the task should repeat at, the delay should
     * be declared in Minecraft ticks, 20 ticks per real second.
     *
     * @return delay the task should repeat at
     */
    public abstract int delay();
}
