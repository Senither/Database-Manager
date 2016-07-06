package com.sendev.databasemanager.plugin.bungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public abstract class ProxyPlugin extends Plugin
{
    private Configuration config = new Configuration();

    /**
     * Saves the raw contents of the default config file to the location retrievable by the
     * getConfig() method. If there is no default config embedded in the plugin, an empty
     * config.yml file is saved. This should fail silently if the config already exists.
     */
    public final void saveDefaultConfig()
    {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File file = getFile("config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                file.delete();

                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(ProxyPlugin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        reloadConfig();
    }

    /**
     * Saves the FileConfiguration retrievable by getConfig().
     */
    public final void saveConfig()
    {
        try {
            getProvider().save(getConfig(), getFile("config.yml"));
        } catch (IOException ex) {
            Logger.getLogger(ProxyPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Discards any data in getConfig() and reloads from disk.
     */
    public final void reloadConfig()
    {
        try {
            config = getProvider().load(getFile("config.yml"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }

    /**
     * Returns the loaded configuration, if nothing has been loaded
     * yet an empty Configuration object will be returned.
     *
     * @return The loaded configuration.
     */
    public final Configuration getConfig()
    {
        return config;
    }

    private ConfigurationProvider getProvider()
    {
        return ConfigurationProvider.getProvider(YamlConfiguration.class);
    }

    private File getFile(String name)
    {
        return new File(getDataFolder(), name);
    }
}
