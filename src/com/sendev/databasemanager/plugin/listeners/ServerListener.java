package com.sendev.databasemanager.plugin.listeners;

import com.sendev.databasemanager.plugin.DBMPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerListener implements Listener
{

    private final DBMPlugin plugin;
    private final String version;

    public ServerListener(DBMPlugin plugin)
    {
        this.plugin = plugin;

        this.version = plugin.getDescription().getVersion();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        if (!player.hasPermission("databasemanager.update-notice")) {
            return;
        }

        try {
            if (plugin.getVersion().getLastVersionReceived().equals(version)) {
                return;
            }
        } catch (NullPointerException ex) {
            return;
        }

        String prefix = plugin.getPrefix('2', 'a');

        plugin.getChat().sendMessage(player, "%s &7There is a new version of DBM available!", prefix);
        plugin.getChat().sendMessage(player, "%s &7Server is running &2v%s &7new version is &2v%s", prefix, version, plugin.getVersion().getLastVersionReceived());
        plugin.getChat().sendMessage(player, "%s &7Site: &2https://bitbucket.org/Senither/database-manager", prefix);
    }
}
