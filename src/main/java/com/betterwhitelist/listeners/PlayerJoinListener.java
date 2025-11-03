package com.betterwhitelist.listeners;

import com.betterwhitelist.BetterWhitelistPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerJoinListener implements Listener {
    
    private final BetterWhitelistPlugin plugin;
    
    public PlayerJoinListener(BetterWhitelistPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        // Check if whitelist is enabled
        if (!plugin.getWhitelistManager().isWhitelistEnabled()) {
            return;
        }
        
        // Check if player is whitelisted
        if (!plugin.getWhitelistManager().isWhitelisted(event.getPlayer().getUniqueId())) {
            String kickMessage = plugin.getConfig().getString("kick-message", "You are not whitelisted on this server!");
            kickMessage = plugin.colorize(kickMessage);
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, kickMessage);
        }
    }
}
