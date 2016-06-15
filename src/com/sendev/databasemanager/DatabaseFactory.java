package com.sendev.databasemanager;

import com.sendev.databasemanager.exceptions.OriginException;
import com.sendev.databasemanager.factory.PluginContainer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;

public class DatabaseFactory
{

    private static final String ORIGIN_INTERFACE = "com.sendev.databasemanager.contracts.DatabaseOriginLookup";
    private static final Map<String, PluginContainer> containers = new HashMap<>();

    /**
     * Gets all the plugin containers currently existing in DBM.
     *
     * @return gets all the plugin containers currently existing in DBM
     */
    public Map<String, PluginContainer> getContainers()
    {
        return containers;
    }

    /**
     * Creates a new Database Manager(DBM) instance, allowing you to communicate with databases easier,
     * using the DBM also gives you access to the Database Schema and Query Builder which makes it
     * even easier to create, delete, modify and update database records.
     *
     * @see com.sendev.databasemanager.query.QueryBuilder
     * @see com.sendev.databasemanager.schema.Schema
     *
     * @param plugin The instance of the plugin that are going to use the DBM.
     *
     * @return either (1) A new a fresh instance of the Database Manager
     *         or (2) the existing Database manager instance for the provided plugin
     */
    public static DatabaseManager createNewInstance(Plugin plugin)
    {
        if (plugin == null) {
            throw new InvalidParameterException("The plugin parameter must be an instance of the Bukkit Plugin instance!");
        }

        if (containers.containsKey(plugin.getName())) {
            containers.get(plugin.getName()).getInstance();
        }

        DatabaseManager dbm = new DatabaseManager(plugin);

        containers.put(plugin.getName(), new PluginContainer(plugin, dbm));

        return dbm;
    }

    /**
     * Makes a dynamic lookup on the class using java reflection, checking to see if the class follows the
     * origin contract, if the class is found to follow the contract, reflection will be used to guess
     * what plugin the class was instantiated from, and the DBM instance linked to that plugin will
     * then be returned, if no DBM instance was found, or a reflection exception is thrown while
     * doing the origin lookup, <code>NULL</code> will be returned instead.
     *
     * @param object the getClass() object to run the origin lookup on.
     *
     * @return either (1) The DBM instance belonging to the provided classes origin
     *         or (2) NULL if something went wrong or the object doesn't follow the origin contract
     */
    public static DatabaseManager getDynamicOrigin(Class<?> object)
    {
        if (!hasOriginInterface(object)) {
            return null;
        }

        List<String> origins = getOriginPackages(object);

        for (PluginContainer plugin : containers.values()) {
            if (plugin.hasBinding(origins)) {
                return plugin.getInstance();
            }
        }

        return null;
    }

    private static List<String> getOriginPackages(Class<?> object)
    {
        try {
            Method method = object.getMethod("throwsOriginException");

            method.setAccessible(true);

            method.invoke(object.newInstance());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException ex) {
            Logger.getLogger(DatabaseFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            System.out.println("Failed to create a new instace of the object type: " + ex.getMessage());
        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof OriginException) {
                StackTraceElement[] traces = ex.getStackTrace();

                List<String> origins = new ArrayList<>();
                for (StackTraceElement element : traces) {
                    String name = element.getClassName();

                    // Skip reflection classes and java objects
                    if (name.startsWith("sun.reflect") || name.startsWith("java.")) {
                        continue;
                    }

                    // Skip all minecraft code
                    if (name.startsWith("org.bukkit") || name.startsWith("net.minecraft.server")) {
                        continue;
                    }

                    // Skip all the DBM classes
                    if (name.startsWith("com.sendev.databasemanager.")) {
                        continue;
                    }

                    origins.add(name);
                }

                return origins;
            }
        }

        return null;
    }

    private static boolean hasOriginInterface(Class<?> interfaces)
    {
        for (Class<?> face : interfaces.getInterfaces()) {
            if (face.getName().equalsIgnoreCase(ORIGIN_INTERFACE)) {
                return true;
            }
        }

        return false;
    }
}
