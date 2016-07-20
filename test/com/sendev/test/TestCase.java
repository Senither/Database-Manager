package com.sendev.test;

import java.util.logging.Logger;

import com.sendev.databasemanager.ConnectionLevel;
import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.connections.SQLite;
import com.sendev.databasemanager.factory.PluginContainer;
import com.sendev.databasemanager.plugin.type.BukkitPlugin;

import static org.powermock.api.mockito.PowerMockito.*;

public abstract class TestCase
{
    protected final DatabaseManager dbm;

    public TestCase()
    {
        final String pluginName = "DatabaseManagerTestSuit";

        // Creates a factory instance so we can check if our DBM instance already exists.
        DatabaseFactory factory = new DatabaseFactory();

        if (!factory.getContainers().containsKey(pluginName)) {
            // Mock a BukkitPlugin instance to emulate a real Bukkit plugin.
            BukkitPlugin plugin = mock(BukkitPlugin.class);

            // Mocks the BukkitPlugin information, we're using the JUnit as our main class in this case
            // since the Factory origin instance will filter out packages related to the DBM instance.
            when(plugin.getMain()).thenReturn("org.apache.tools.ant.taskdefs.optional.junit.JUnitTestRunner");
            when(plugin.getName()).thenReturn(pluginName);
            when(plugin.getLogger()).thenReturn(Logger.getLogger(pluginName));

            // Creates a new DBM instance from the emulated BukkitPlugin instance
            DatabaseManager instance = new DatabaseManager(plugin);
            factory.getContainers().put(pluginName, new PluginContainer(plugin, instance));

            // Sets up a DBM Prefix
            instance.options().setPrefix("test_");

            // Adds a IN-MEMORY database called test.
            instance.addConnection("test", ConnectionLevel.LOWEST, new SQLite());
        }

        // Returns our DBM instance.
        dbm = factory.getContainers().get(pluginName).getInstance();
    }
}
