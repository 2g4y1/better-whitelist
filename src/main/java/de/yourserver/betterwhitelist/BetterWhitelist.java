package de.yourserver.betterwhitelist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// Test: CI-Check funktioniert
public class BetterWhitelist extends JavaPlugin {

    private LuckPerms luckPerms;
    private boolean luckPermsEnabled;
    private String defaultGroup;
    private Messages messages;

    @Override
    public void onEnable() {
        // Config laden oder erstellen
        saveDefaultConfig();
        loadConfiguration();
        
        getLogger().info(messages.get("loading.header"));
        getLogger().info(messages.get("loading.starting"));
        getLogger().info(messages.get("loading.header"));
        
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

        // Commands registrieren
        getCommand("invite").setExecutor(new InviteCommand(this));
        getCommand("invite").setTabCompleter(new InviteTabCompleter());
        getCommand("uninvite").setExecutor(new UninviteCommand(this));
        getCommand("uninvite").setTabCompleter(new UninviteTabCompleter());
        getLogger().info(messages.get("loading.commands"));

        getLogger().info(messages.get("loading.header"));
        getLogger().info(messages.get("loading.success", 
            "version", getPluginMeta().getVersion()));
        if (isLuckPermsEnabled()) {
            getLogger().info(messages.get("loading.success.luckperms",
                "group", defaultGroup));
        } else {
            getLogger().info(messages.get("loading.success.luckperms_disabled"));
        }
        getLogger().info(messages.get("loading.footer"));
    }

    @Override
    public void onDisable() {
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
        
        luckPermsEnabled = getConfig().getBoolean("luckperms.enabled", true);
        defaultGroup = getConfig().getString("luckperms.default-group", "default");
        
        getLogger().info(messages.get("loading.config"));
        getLogger().info(messages.get("loading.config.language") + lang);
        getLogger().info(messages.get("loading.config.luckperms") + 
            (luckPermsEnabled ? "✓" : "✗"));
        if (luckPermsEnabled) {
            getLogger().info(messages.get("loading.config.group") + defaultGroup);
        }
    }

    /**
     * Lädt die Konfiguration neu
     */
    public void reloadConfiguration() {
        reloadConfig();
        loadConfiguration();
    }
    
    /**
     * Gibt die Messages-Instanz zurück
     */
    public Messages getMessages() {
        return messages;
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
     */
    public void invitePlayer(String playerName, org.bukkit.command.CommandSender sender) {
        try {
            // UUID von der Mojang-API holen (läuft bereits async)
            UUID uuid = getUUIDFromMojang(playerName);
            if (uuid == null) {
                getLogger().warning(messages.get("mojang.player_not_exists", "player", playerName));
                getServer().getScheduler().runTask(this, () -> {
                    sender.sendMessage(createMessage(
                        messages.get("invite.not_found", "player", playerName),
                        NamedTextColor.RED
                    ));
                    sender.sendMessage(createMessage(
                        messages.get("invite.check_name"),
                        NamedTextColor.GRAY
                    ));
                });
                return;
            }

            // OfflinePlayer mit UUID UND Namen erstellen (Paper API)
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            
            // Whitelist muss auf dem Main-Thread gesetzt werden
            getServer().getScheduler().runTask(this, () -> {
                // Verwende den nativen whitelist Befehl, um sicherzustellen, dass der Name gespeichert wird
                getServer().dispatchCommand(getServer().getConsoleSender(), "whitelist add " + playerName);


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
            });

            // LuckPerms-Gruppe setzen (läuft bereits async), nur wenn aktiviert
            if (isLuckPermsEnabled()) {
                setPlayerGroup(offlinePlayer, defaultGroup);
            }

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
     * Entfernt einen Spieler von der Whitelist
     * Holt die UUID über die Mojang-API
     * Diese Methode läuft async und führt die Whitelist-Operation auf dem Main-Thread aus
     *
     * @param playerName Name des Spielers
     * @param sender Der CommandSender für Feedback
     */
    public void uninvitePlayer(String playerName, org.bukkit.command.CommandSender sender) {
        try {
            // UUID von der Mojang-API holen (läuft bereits async)
            UUID uuid = getUUIDFromMojang(playerName);
            if (uuid == null) {
                getLogger().warning(messages.get("mojang.player_not_exists", "player", playerName));
                getServer().getScheduler().runTask(this, () -> {
                    sender.sendMessage(createMessage(
                        messages.get("uninvite.not_found", "player", playerName),
                        NamedTextColor.RED
                    ));
                    sender.sendMessage(createMessage(
                        messages.get("uninvite.check_name"),
                        NamedTextColor.GRAY
                    ));
                });
                return;
            }

            // OfflinePlayer mit der echten UUID erstellen und Whitelist auf Main-Thread setzen
            getServer().getScheduler().runTask(this, () -> {
                // Verwende den nativen whitelist Befehl
                getServer().dispatchCommand(getServer().getConsoleSender(), "whitelist remove " + playerName);
                
                // Spieler kicken, falls online
                org.bukkit.entity.Player onlinePlayer = getServer().getPlayer(uuid);
                if (onlinePlayer != null && onlinePlayer.isOnline()) {
                    onlinePlayer.kick(createMessage(
                        messages.get("uninvite.kick_message"),
                        NamedTextColor.RED
                    ));
                    getLogger().info(messages.get("console.uninvite.kicked", "player", playerName));
                }
                
                // Konsolennachricht
                getLogger().info(messages.get("console.uninvite.header"));
                getLogger().info(messages.get("console.uninvite.title"));
                getLogger().info(messages.get("console.uninvite.player", "player", playerName));
                getLogger().info(messages.get("console.uninvite.uuid", "uuid", uuid));
                getLogger().info(messages.get("console.uninvite.whitelist"));
                getLogger().info(messages.get("console.uninvite.footer"));
                
                // Feedback an Sender
                sender.sendMessage(createMessage(
                    messages.get("uninvite.success", "player", playerName),
                    NamedTextColor.GREEN
                ));

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
