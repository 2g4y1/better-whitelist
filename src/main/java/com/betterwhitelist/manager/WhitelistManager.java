package com.betterwhitelist.manager;

import com.betterwhitelist.BetterWhitelistPlugin;
import com.betterwhitelist.data.WhitelistEntry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WhitelistManager {
    
    private final BetterWhitelistPlugin plugin;
    private final Map<UUID, WhitelistEntry> whitelistEntries;
    private final File dataFile;
    private FileConfiguration dataConfig;
    private final Object saveLock = new Object();
    
    public WhitelistManager(BetterWhitelistPlugin plugin) {
        this.plugin = plugin;
        this.whitelistEntries = new ConcurrentHashMap<>();
        this.dataFile = new File(plugin.getDataFolder(), "whitelist.yml");
        
        loadData();
    }
    
    private void loadData() {
        if (!dataFile.exists()) {
            try {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create whitelist data file!");
                e.printStackTrace();
            }
        }
        
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        
        // Load entries from file
        ConfigurationSection entriesSection = dataConfig.getConfigurationSection("entries");
        if (entriesSection != null) {
            for (String uuidString : entriesSection.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    ConfigurationSection entrySection = entriesSection.getConfigurationSection(uuidString);
                    
                    String playerName = entrySection.getString("name");
                    String addedBy = entrySection.getString("addedBy");
                    long addedDateMs = entrySection.getLong("addedDate");
                    String reason = entrySection.getString("reason", "No reason provided");
                    
                    Date addedDate = new Date(addedDateMs);
                    Date expirationDate = null;
                    
                    if (entrySection.contains("expirationDate")) {
                        long expirationMs = entrySection.getLong("expirationDate");
                        expirationDate = new Date(expirationMs);
                    }
                    
                    WhitelistEntry entry = new WhitelistEntry(uuid, playerName, addedBy, addedDate, reason, expirationDate);
                    whitelistEntries.put(uuid, entry);
                    
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in whitelist data: " + uuidString);
                }
            }
        }
        
        plugin.getLogger().info("Loaded " + whitelistEntries.size() + " whitelist entries");
    }
    
    public void save() {
        synchronized (saveLock) {
            dataConfig.set("entries", null); // Clear existing entries
            
            for (Map.Entry<UUID, WhitelistEntry> entry : whitelistEntries.entrySet()) {
                String path = "entries." + entry.getKey().toString();
                WhitelistEntry wlEntry = entry.getValue();
                
                dataConfig.set(path + ".name", wlEntry.getPlayerName());
                dataConfig.set(path + ".addedBy", wlEntry.getAddedBy());
                dataConfig.set(path + ".addedDate", wlEntry.getAddedDate().getTime());
                dataConfig.set(path + ".reason", wlEntry.getReason());
                
                if (wlEntry.getExpirationDate() != null) {
                    dataConfig.set(path + ".expirationDate", wlEntry.getExpirationDate().getTime());
                }
            }
            
            try {
                dataConfig.save(dataFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not save whitelist data!");
                e.printStackTrace();
            }
        }
    }
    
    public boolean addPlayer(UUID playerId, String playerName, String addedBy, String reason) {
        if (whitelistEntries.containsKey(playerId)) {
            return false;
        }
        
        WhitelistEntry entry = new WhitelistEntry(playerId, playerName, addedBy, new Date(), reason);
        whitelistEntries.put(playerId, entry);
        save();
        return true;
    }
    
    public boolean addPlayerTemporary(UUID playerId, String playerName, String addedBy, String reason, long durationMinutes) {
        if (whitelistEntries.containsKey(playerId)) {
            return false;
        }
        
        Date expirationDate = new Date(System.currentTimeMillis() + (durationMinutes * 60 * 1000));
        WhitelistEntry entry = new WhitelistEntry(playerId, playerName, addedBy, new Date(), reason, expirationDate);
        whitelistEntries.put(playerId, entry);
        save();
        return true;
    }
    
    public boolean removePlayer(UUID playerId) {
        if (!whitelistEntries.containsKey(playerId)) {
            return false;
        }
        
        whitelistEntries.remove(playerId);
        save();
        return true;
    }
    
    public boolean isWhitelisted(UUID playerId) {
        WhitelistEntry entry = whitelistEntries.get(playerId);
        if (entry == null) {
            return false;
        }
        
        // Check if temporary whitelist has expired
        if (entry.isExpired()) {
            // Remove in sync context to avoid race conditions
            removePlayer(playerId);
            return false;
        }
        
        return true;
    }
    
    public WhitelistEntry getEntry(UUID playerId) {
        return whitelistEntries.get(playerId);
    }
    
    public List<WhitelistEntry> getAllEntries() {
        return new ArrayList<>(whitelistEntries.values());
    }
    
    public void cleanupExpiredEntries() {
        List<UUID> expiredPlayers = whitelistEntries.values().stream()
            .filter(WhitelistEntry::isExpired)
            .map(WhitelistEntry::getPlayerId)
            .collect(Collectors.toList());
        
        for (UUID playerId : expiredPlayers) {
            WhitelistEntry entry = whitelistEntries.get(playerId);
            // Null check in case entry was removed by another thread
            if (entry != null) {
                plugin.getLogger().info("Temporary whitelist expired for player: " + entry.getPlayerName());
                removePlayer(playerId);
            }
        }
    }
    
    public boolean isWhitelistEnabled() {
        return plugin.getConfig().getBoolean("whitelist-enabled", true);
    }
    
    public void setWhitelistEnabled(boolean enabled) {
        plugin.getConfig().set("whitelist-enabled", enabled);
        plugin.saveConfig();
    }
}
