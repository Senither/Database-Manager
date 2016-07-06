package com.sendev.databasemanager.plugin.contracts;

import com.sendev.databasemanager.plugin.type.BukkitPlugin;
import com.sendev.databasemanager.plugin.type.BungeePlugin;

public enum PlatformType
{

    Bukkit(false, BukkitPlugin.class),
    BungeeCord(true, BungeePlugin.class);

    private final boolean proxy;
    private final Object instance;

    private PlatformType(boolean proxy, Object instance)
    {
        this.proxy = proxy;
        this.instance = instance;
    }

    public boolean isProxy()
    {
        return proxy;
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
