package com.betterwhitelist;

import com.betterwhitelist.commands.WhitelistCommand;
import com.betterwhitelist.listeners.PlayerJoinListener;
import com.betterwhitelist.manager.WhitelistManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterWhitelistPlugin extends JavaPlugin {
    
    private WhitelistManager whitelistManager;
    
    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();
        
        // Initialize whitelist manager
        whitelistManager = new WhitelistManager(this);
        
        // Register commands
        WhitelistCommand whitelistCommand = new WhitelistCommand(this);
        getCommand("bwhitelist").setExecutor(whitelistCommand);
        getCommand("bwhitelist").setTabCompleter(whitelistCommand);
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        
        // Start auto-save task if enabled
        int autoSaveInterval = getConfig().getInt("auto-save-interval", 5);
        if (autoSaveInterval > 0) {
            getServer().getScheduler().runTaskTimerAsynchronously(this, 
                () -> whitelistManager.save(), 
                autoSaveInterval * 60 * 20L, 
                autoSaveInterval * 60 * 20L);
        }
        
        // Start temporary whitelist cleanup task
        getServer().getScheduler().runTaskTimerAsynchronously(this,
            () -> whitelistManager.cleanupExpiredEntries(),
            20L * 60L, // Run every minute
            20L * 60L);
        
        getLogger().info("BetterWhitelist has been enabled!");
    }
    
    @Override
    public void onDisable() {
        // Save whitelist data
        if (whitelistManager != null) {
            whitelistManager.save();
        }
        
        getLogger().info("BetterWhitelist has been disabled!");
    }
    
    public WhitelistManager getWhitelistManager() {
        return whitelistManager;
    }
    
    public String getMessage(String key) {
        String prefix = getConfig().getString("prefix", "&8[&6BetterWhitelist&8]&r ");
        String message = getConfig().getString("messages." + key, "");
        return colorize(prefix + message);
    }
    
    public String getMessageNoPrefix(String key) {
        String message = getConfig().getString("messages." + key, "");
        return colorize(message);
    }
    
    public String colorize(String message) {
        return message.replace("&", "§");
    }
}
