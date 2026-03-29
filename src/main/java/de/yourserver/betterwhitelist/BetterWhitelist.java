package de.yourserver.betterwhitelist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.geysermc.floodgate.api.FloodgateApi;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BetterWhitelist extends JavaPlugin {

    private LuckPerms luckPerms;
    private boolean luckPermsEnabled;
    private boolean floodgateEnabled;
    private String fuidAPI;
    private FloodgateApi floodgateApi;
    private String fuidField;
    private String floodgatePrefix;
    private String defaultGroup;
    private Messages messages;
    private InviteData inviteData;
    private int maxInvites;
    private MutualBoostManager boostManager;

    @Override
    public void onEnable() {
        // Config laden oder erstellen
        saveDefaultConfig();
        loadConfiguration();
        // Invite-Datenbank laden
        inviteData = new InviteData(getDataFolder());
        
        // Mutual Boost Manager initialisieren
        boostManager = new MutualBoostManager(this, inviteData);
        
        // Commands registrieren
        getCommand("invite").setExecutor(new InviteCommand(this));
        getCommand("invite").setTabCompleter(new InviteTabCompleter(this));
        getCommand("uninvite").setExecutor(new UninviteCommand(this));
        getCommand("uninvite").setTabCompleter(new UninviteTabCompleter(this));
        getCommand("invitelist").setExecutor(new ListCommand(this));
        getCommand("betterwhitelist").setExecutor(new ReloadCommand(this));
        getLogger().info(messages.get("loading.commands"));

    }

    @Override
    public void onDisable() {
        // Boost-Manager stoppen
        if (boostManager != null) {
            boostManager.stop();
        }
        
        getLogger().info(messages.get("unloading.header"));
        getLogger().info(messages.get("unloading.message"));
        getLogger().info(messages.get("unloading.footer"));
    }

    /**
     * Lädt die Konfigurationswerte
     */
    private void loadConfiguration() {
        String lang = getConfig().getString("language", "de");
        messages = new Messages(lang);
        getLogger().info(messages.get("loading.header"));
        getLogger().info(messages.get("loading.starting"));
        getLogger().info(messages.get("loading.header"));
        
        floodgateEnabled = getConfig().getBoolean("floodgate-support.enabled", false);
        fuidAPI = getConfig().getString("floodgate-support.fuid-api", "https://mcprofile.io/api/v1/bedrock/gamertag/{gamertag}");
        fuidField = getConfig().getString("floodgate-support.fuid-field", "floodgateuid");
        floodgatePrefix = getConfig().getString("floodgate-support.prefix", ".");
        luckPermsEnabled = getConfig().getBoolean("luckperms.enabled", true);
        defaultGroup = getConfig().getString("luckperms.default-group", "default");
        maxInvites = getConfig().getInt("max-invites", 5);
        // LuckPerms API laden
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
            getLogger().info(messages.get("loading.luckperms.found"));
        } else {
            if (luckPermsEnabled) {
                getLogger().warning(messages.get("loading.luckperms.notfound"));
                getLogger().warning(messages.get("loading.luckperms.disabled"));
                luckPermsEnabled = false;
            } else {
                getLogger().info(messages.get("loading.luckperms.config_disabled"));
            }
        }
        // Check if floodgate support is enabled mistakenly
        if (floodgateEnabled && Bukkit.getPluginManager().getPlugin("floodgate") == null) {
            getLogger().warning(messages.get("loading.floodgate.notfound"));
            getLogger().warning(messages.get("loading.floodgate.disabled"));
            floodgateEnabled = false;
        }
        if (floodgateEnabled) {
            floodgateApi = FloodgateApi.getInstance();
        }
        
        getLogger().info(messages.get("loading.config"));
        getLogger().info(messages.get("loading.config.language") + lang);
        getLogger().info(messages.get("loading.config.luckperms") + 
            (luckPermsEnabled ? "✓" : "✗"));
        if (luckPermsEnabled) {
            getLogger().info(messages.get("loading.config.group") + defaultGroup);
        }
        getLogger().info(messages.get("loading.config.floodgate") + 
            (floodgateEnabled ? "✓" : "✗"));
        getLogger().info(messages.get("loading.config.max_invites") + maxInvites);
        getLogger().info(messages.get("loading.header"));
        getLogger().info(messages.get("loading.success", 
            "version", getPluginMeta().getVersion()));
        getLogger().info(messages.get("loading.header"));
    }

    /**
     * Lädt die Konfiguration neu
     */
    public void reloadConfiguration() {
        reloadConfig();
        loadConfiguration();
        inviteData.load();
        
        // Boost-Manager neu laden
        if (boostManager != null) {
            boostManager.stop();
        }
        boostManager = new MutualBoostManager(this, inviteData);
    }
    
    /**
     * Gibt die Messages-Instanz zurück
     */
    public Messages getMessages() {
        return messages;
    }

    /**
     * Gibt die InviteData-Instanz zurück
     */
    public InviteData getInviteData() {
        return inviteData;
    }

    /**
     * Gibt das maximale Invite-Limit zurück
     */
    public int getMaxInvites() {
        return maxInvites;
    }

    /**
     * Gibt die LuckPerms API-Instanz zurück
     */
    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    /**
     * Prüft ob LuckPerms-Integration aktiviert ist
     */
    public boolean isLuckPermsEnabled() {
        return luckPermsEnabled && luckPerms != null;
    }

    /**
     * Returns whether Floodgate is enabled
     */
    public boolean isFloodgateEnabled() {
        return floodgateEnabled;
    }

    /**
     * Returns Floodgate player FUID query API
     */
    public String getFuidApi() {
        return fuidAPI;
    }

    /**
     * Gibt den Namen der Standard-Gruppe zurück
     */
    public String getDefaultGroup() {
        return defaultGroup;
    }


    /**
     * Fügt einen Spieler zur Whitelist hinzu und weist ihm die default-Gruppe zu
     * Holt die UUID über die Mojang-API, ähnlich wie /whitelist add
     * Diese Methode läuft async und führt die Whitelist-Operation auf dem Main-Thread aus
     *
     * @param playerName Name des Spielers
     * @param sender Der CommandSender für Feedback
     * @param isBedrock if target player is bedrock player (floodgate)
     */
    public void invitePlayer(String playerName, org.bukkit.command.CommandSender sender, boolean isBedrock) {
        try {
            // Check invite limit (nur für Spieler, nicht für Console/Admins)
            if (sender instanceof org.bukkit.entity.Player) {
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
                int currentInvites = inviteData.getInviteCount(player.getUniqueId());
                
                if (currentInvites >= maxInvites) {
                    getServer().getScheduler().runTask(this, () -> {
                        sender.sendMessage(createMessage(
                            messages.get("invite.limit_reached", 
                                "limit", maxInvites,
                                "count", currentInvites),
                            NamedTextColor.RED
                        ));
                    });
                    return;
                }
            }
            UUID uuid;
            if (isBedrock) {
                uuid = getFUID(playerName);
                if (uuid == null) {
                    playerNotFound(playerName, sender);
                    return;
                }
                getServer().getScheduler().runTask(this, () -> {
                    getServer().dispatchCommand(getServer().getConsoleSender(), "fwhitelist add " + uuid.toString());
                    // Invite-Daten speichern (nur wenn Spieler, nicht Console)
                    if (sender instanceof org.bukkit.entity.Player) {
                        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
                        inviteData.addInvite(player.getUniqueId(), player.getName(), uuid, floodgatePrefix + playerName);
                    }
                });
            } else {
                // UUID von der Mojang-API holen (läuft bereits async)
                uuid = getUUIDFromMojang(playerName);
                if (uuid == null) {
                    playerNotFound(playerName, sender);
                    return;
                }
                // Whitelist muss auf dem Main-Thread gesetzt werden
                getServer().getScheduler().runTask(this, () -> {
                    // Verwende den nativen whitelist Befehl, um sicherzustellen, dass der Name gespeichert wird
                    getServer().dispatchCommand(getServer().getConsoleSender(), "whitelist add " + playerName);
                    // Invite-Daten speichern (nur wenn Spieler, nicht Console)
                    if (sender instanceof org.bukkit.entity.Player) {
                        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
                        inviteData.addInvite(player.getUniqueId(), player.getName(), uuid, playerName);
                    }
                });
            }
            getServer().getScheduler().runTask(this, () -> {
                // Konsolennachricht
                getLogger().info(messages.get("console.invite.header"));
                getLogger().info(messages.get("console.invite.title"));
                getLogger().info(messages.get("console.invite.player", "player", playerName));
                getLogger().info(messages.get("console.invite.uuid", "uuid", uuid));
                getLogger().info(messages.get("console.invite.whitelist"));
                if (isLuckPermsEnabled()) {
                    getLogger().info(messages.get("console.invite.group", "group", defaultGroup));
                }
                getLogger().info(messages.get("console.invite.footer"));

                // Feedback an Sender
                sender.sendMessage(createMessage(
                    messages.get("invite.success", "player", playerName),
                    NamedTextColor.GREEN
                ));
                sender.sendMessage(createMessage(
                    messages.get("invite.whitelist"),
                    NamedTextColor.GRAY
                ));
                if (isLuckPermsEnabled()) {
                    sender.sendMessage(createMessage(
                        messages.get("invite.group", "group", defaultGroup),
                        NamedTextColor.GRAY
                    ));
                }
                
                // Zeige verbleibende Invites (nur für Spieler)
                if (sender instanceof org.bukkit.entity.Player) {
                    org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
                    int remaining = maxInvites - inviteData.getInviteCount(player.getUniqueId());
                    sender.sendMessage(createMessage(
                        messages.get("invite.remaining", "remaining", remaining),
                        remaining > 0 ? NamedTextColor.YELLOW : NamedTextColor.RED
                    ));
                }

                // Broadcast an alle Online-Spieler mit der Permission
                getServer().getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission("invite.use"))
                    .forEach(p -> {
                        if (!p.equals(sender)) {
                            p.sendMessage(createMessage(
                                messages.get("invite.broadcast", 
                                    "sender", sender.getName(),
                                    "player", playerName),
                                NamedTextColor.GRAY
                            ));
                        }
                    });
                // OfflinePlayer mit UUID UND Namen erstellen (Paper API)
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                // LuckPerms-Gruppe setzen (läuft bereits async), nur wenn aktiviert
                if (isLuckPermsEnabled()) {
                    setPlayerGroup(offlinePlayer, defaultGroup);
                }
            });
        } catch (Exception e) {
            getLogger().severe(messages.get("console.error.header"));
            getLogger().severe(messages.get("console.error.invite_title"));
            getLogger().severe(messages.get("console.error.player", "player", playerName));
            getLogger().severe(messages.get("console.error.message", "error", e.getMessage()));
            getLogger().severe(messages.get("console.error.footer"));
            e.printStackTrace();
            
            getServer().getScheduler().runTask(this, () -> {
                sender.sendMessage(createMessage(
                    messages.get("invite.error", "player", playerName),
                    NamedTextColor.RED
                ));
                sender.sendMessage(createMessage(
                    messages.get("invite.check_logs"),
                    NamedTextColor.GRAY
                ));
            });
        }
    }

    /**
     * Entfernt einen Spieler von der Whitelist
     * Holt die UUID über die Mojang-API
     * Diese Methode läuft async und führt die Whitelist-Operation auf dem Main-Thread aus
     *
     * @param playerName Name des Spielers
     * @param sender Der CommandSender für Feedback
     */
    public void uninvitePlayer(String playerName, org.bukkit.command.CommandSender sender, boolean isBedrock) {
        try {
            UUID uuid;
            if (isBedrock) {
                uuid = getFUID(playerName);
                if (uuid == null) {
                    playerNotFound(playerName, sender);
                    return;
                }
                getServer().getScheduler().runTask(this, () -> {
                    getServer().dispatchCommand(
                        getServer().getConsoleSender(),
                        "fwhitelist remove " + uuid.toString()
                    );
                });
            } else {
                // UUID von der Mojang-API holen (läuft bereits async)
                uuid = getUUIDFromMojang(playerName);
                if (uuid == null) {
                    playerNotFound(playerName, sender);
                    return;
                }

                // OfflinePlayer mit der echten UUID erstellen und Whitelist auf Main-Thread setzen
                getServer().getScheduler().runTask(this, () -> {                    
                    // Verwende den nativen whitelist Befehl
                    getServer().dispatchCommand(getServer().getConsoleSender(), "whitelist remove " + playerName);
                });
            }

            getServer().getScheduler().runTask(this, () -> {
                // Invite-Daten entfernen
                inviteData.removeInvite(uuid);
                // Spieler kicken, falls online
                org.bukkit.entity.Player onlinePlayer = getServer().getPlayer(uuid);
                if (onlinePlayer != null && onlinePlayer.isOnline()) {
                    onlinePlayer.kick(createMessage(
                        messages.get("uninvite.kick_message"),
                        NamedTextColor.RED
                    ));
                    getLogger().info(messages.get("console.uninvite.kicked", "player", playerName));
                }
                
                // Zeige wer den Spieler eingeladen hatte
                Optional<String> inviter = inviteData.getInviter(uuid);
                
                // Konsolennachricht
                getLogger().info(messages.get("console.uninvite.header"));
                getLogger().info(messages.get("console.uninvite.title"));
                getLogger().info(messages.get("console.uninvite.player", "player", playerName));
                getLogger().info(messages.get("console.uninvite.uuid", "uuid", uuid));
                if (inviter.isPresent()) {
                    getLogger().info(messages.get("console.uninvite.inviter", "inviter", inviter.get()));
                }
                getLogger().info(messages.get("console.uninvite.whitelist"));
                getLogger().info(messages.get("console.uninvite.footer"));

                // Feedback an Sender
                sender.sendMessage(createMessage(
                    messages.get("uninvite.success", "player", playerName),
                    NamedTextColor.GREEN
                ));
                
                if (inviter.isPresent()) {
                    sender.sendMessage(createMessage(
                        messages.get("uninvite.inviter_info", "inviter", inviter.get()),
                        NamedTextColor.GRAY
                    ));
                }

                // Broadcast an alle Admins
                getServer().getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission("invite.admin"))
                    .forEach(p -> {
                        if (!p.equals(sender)) {
                            p.sendMessage(createMessage(
                                messages.get("uninvite.broadcast",
                                    "sender", sender.getName(),
                                    "player", playerName),
                                NamedTextColor.GRAY
                            ));
                        }
                    });
            });
        } catch (Exception e) {
            getLogger().severe(messages.get("console.error.header"));
            getLogger().severe(messages.get("console.error.uninvite_title"));
            getLogger().severe(messages.get("console.error.player", "player", playerName));
            getLogger().severe(messages.get("console.error.message", "error", e.getMessage()));
            getLogger().severe(messages.get("console.error.footer"));
            e.printStackTrace();
            
            getServer().getScheduler().runTask(this, () -> {
                sender.sendMessage(createMessage(
                    messages.get("uninvite.error", "player", playerName),
                    NamedTextColor.RED
                ));
                sender.sendMessage(createMessage(
                    messages.get("uninvite.check_logs"),
                    NamedTextColor.GRAY
                ));
            });
        }
    }

    /**
     * Send messages to command sender, indicating player is not found
     */
    private void playerNotFound(String playerName, org.bukkit.command.CommandSender sender) {
        getLogger().warning(messages.get("mojang.player_not_exists", "player", playerName));
        getServer().getScheduler().runTask(this, () -> {  // Both invite and uninvite uses the same messages, maybe remove duplicated messages if they weren't planned for future implementations?
            sender.sendMessage(createMessage(
                messages.get("invite.not_found", "player", playerName),
                NamedTextColor.RED
            ));
            sender.sendMessage(createMessage(
                messages.get("invite.check_name"),
                NamedTextColor.GRAY
            ));
        });
    }

    /**
     * Holt die UUID eines Spielers von der Mojang-API
     * Entspricht dem Verhalten von /whitelist add
     *
     * @param playerName Der Spielername
     * @return UUID des Spielers oder null wenn nicht gefunden
     */
    private UUID getUUIDFromMojang(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // JSON parsen
                JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();
                String uuidString = json.get("id").getAsString();

                // UUID formatieren (Mojang gibt sie ohne Bindestriche zurück)
                return UUID.fromString(
                    uuidString.replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                        "$1-$2-$3-$4-$5"
                    )
                );
            } else if (responseCode == 204 || responseCode == 404) {
                // Spieler existiert nicht
                return null;
            } else {
                getLogger().warning(messages.get("mojang.status", "status", responseCode));
                return null;
            }
        } catch (Exception e) {
            getLogger().severe(messages.get("mojang.error", "error", e.getMessage()));
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get player Floodgate UUID from player name, try using floodgate api first, fallback to web api
     * @param playerName
     * @return Floodgate UUID
     */
    private UUID getFUID(String playerName) {
        UUID uuid;
        try {
            getLogger().warning(messages.get("invite.loading_floodgate", "api", "Floodgate"));
            uuid = floodgateApi.getUuidFor(playerName).join();
            if (uuid != null) {
                return uuid;
            }
        } catch (Exception e) {
            getLogger().warning(messages.get("fuid.error", "api", "FloodGateAPI", "error", e));
        }
        try {
            getLogger().warning(messages.get("invite.loading_floodgate", "api", fuidAPI));
            String encodedName = java.net.URLEncoder.encode(playerName, java.nio.charset.StandardCharsets.UTF_8);
            URL url = new URL(fuidAPI.replace("{gamertag}", encodedName));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // JSON parsen
                JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();
                String uuidString = json.get(fuidField).getAsString();
                return UUID.fromString(uuidString);
            } else {
                getLogger().warning(messages.get("fuid.status", "api", fuidAPI, "status", responseCode));
                return null;
            }
        } catch (Exception e) {
            getLogger().severe(messages.get("fuid.error", "api", fuidAPI, "error", e.getMessage()));
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Setzt die Hauptgruppe eines Spielers in LuckPerms
     * Entspricht: /lp user <Username> parent set <groupName>
     *
     * @param player Der Spieler
     * @param groupName Name der Gruppe
     */
    private void setPlayerGroup(OfflinePlayer player, String groupName) {
        if (!isLuckPermsEnabled()) {
            return;
        }

        // Prüfen ob die Gruppe existiert
        if (luckPerms.getGroupManager().getGroup(groupName) == null) {
            getLogger().warning(messages.get("luckperms.group_not_found", "group", groupName));
            getLogger().warning(messages.get("luckperms.check_config", "group", groupName));
            return;
        }

        // User laden oder erstellen (async)
        luckPerms.getUserManager().loadUser(player.getUniqueId()).thenAcceptAsync(user -> {
            if (user != null) {
                // Alle bisherigen Parent-Gruppen entfernen (entspricht "parent set")
                user.data().clear(net.luckperms.api.node.NodeType.INHERITANCE::matches);

                // Neue Gruppe als InheritanceNode setzen (korrekte API-Verwendung)
                net.luckperms.api.node.types.InheritanceNode node =
                    net.luckperms.api.node.types.InheritanceNode.builder(groupName).build();
                user.data().add(node);

                // Änderungen speichern (gibt automatisch einen CompletableFuture zurück)
                luckPerms.getUserManager().saveUser(user).thenRunAsync(() -> {
                    getLogger().info(messages.get("luckperms.group_set", 
                        "player", player.getName(), 
                        "group", groupName));
                });
            }
        }).exceptionally(throwable -> {
            getLogger().severe(messages.get("luckperms.error", 
                "player", player.getName(),
                "error", throwable.getMessage()));
            throwable.printStackTrace();
            return null;
        });
    }

    /**
     * Erstellt eine farbige Nachricht
     */
    public Component createMessage(String text, NamedTextColor color) {
        return Component.text(text).color(color);
    }
}
