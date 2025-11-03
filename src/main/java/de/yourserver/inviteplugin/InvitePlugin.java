package de.yourserver.inviteplugin;

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

public class InvitePlugin extends JavaPlugin {

    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        getLogger().info("========================================");
        getLogger().info("  InvitePlugin wird geladen...");
        getLogger().info("========================================");
        
        // LuckPerms API laden
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
            getLogger().info("✓ LuckPerms API erfolgreich geladen!");
        } else {
            getLogger().severe("✗ LuckPerms wurde nicht gefunden! Plugin wird deaktiviert.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Commands registrieren
        getCommand("invite").setExecutor(new InviteCommand(this));
        getCommand("invite").setTabCompleter(new InviteTabCompleter());
        getCommand("uninvite").setExecutor(new UninviteCommand(this));
        getCommand("uninvite").setTabCompleter(new UninviteTabCompleter());
        getLogger().info("✓ Commands registriert: /invite, /uninvite");

        getLogger().info("========================================");
        getLogger().info("  InvitePlugin v1.0.0 erfolgreich aktiviert!");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("========================================");
        getLogger().info("  InvitePlugin wurde deaktiviert!");
        getLogger().info("========================================");
    }

    /**
     * Gibt die LuckPerms API-Instanz zurück
     */
    public LuckPerms getLuckPerms() {
        return luckPerms;
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
                getLogger().warning("Spieler '" + playerName + "' existiert nicht oder konnte nicht gefunden werden!");
                getServer().getScheduler().runTask(this, () -> {
                    sender.sendMessage(createMessage(
                        "§c✗ Spieler §e" + playerName + "§c wurde nicht gefunden!",
                        NamedTextColor.RED
                    ));
                    sender.sendMessage(createMessage(
                        "§7Stelle sicher, dass der Name korrekt geschrieben ist.",
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
                getLogger().info("========================================");
                getLogger().info("  SPIELER EINGELADEN");
                getLogger().info("  Spieler: " + playerName);
                getLogger().info("  UUID: " + uuid);
                getLogger().info("  Whitelist: ✓ Aktiviert");
                getLogger().info("  LuckPerms-Gruppe: default");
                getLogger().info("========================================");

                // Feedback an Sender
                sender.sendMessage(createMessage(
                    "§a✓ Spieler §e" + playerName + "§a wurde erfolgreich eingeladen!",
                    NamedTextColor.GREEN
                ));
                sender.sendMessage(createMessage(
                    "§7→ Whitelist: §aAktiviert",
                    NamedTextColor.GRAY
                ));
                sender.sendMessage(createMessage(
                    "§7→ LuckPerms-Gruppe: §edefault",
                    NamedTextColor.GRAY
                ));

                // Broadcast an alle Online-Spieler mit der Permission
                getServer().getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission("invite.use"))
                    .forEach(p -> {
                        if (!p.equals(sender)) {
                            p.sendMessage(createMessage(
                                "§7[§aInvite§7] §e" + sender.getName() +
                                " §7hat §e" + playerName + " §7eingeladen.",
                                NamedTextColor.GRAY
                            ));
                        }
                    });
            });

            // LuckPerms-Gruppe setzen (läuft bereits async)
            setPlayerGroup(offlinePlayer, "default");

        } catch (Exception e) {
            getLogger().severe("========================================");
            getLogger().severe("  FEHLER beim Einladen!");
            getLogger().severe("  Spieler: " + playerName);
            getLogger().severe("  Fehler: " + e.getMessage());
            getLogger().severe("========================================");
            e.printStackTrace();
            
            getServer().getScheduler().runTask(this, () -> {
                sender.sendMessage(createMessage(
                    "§c✗ Fehler beim Einladen von §e" + playerName + "§c!",
                    NamedTextColor.RED
                ));
                sender.sendMessage(createMessage(
                    "§7Bitte überprüfe die Logs für weitere Informationen.",
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
                getLogger().warning("Mojang-API antwortet mit Status: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            getLogger().severe("Fehler beim Abrufen der UUID von Mojang: " + e.getMessage());
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
                getLogger().warning("Spieler '" + playerName + "' existiert nicht oder konnte nicht gefunden werden!");
                getServer().getScheduler().runTask(this, () -> {
                    sender.sendMessage(createMessage(
                        "§c✗ Spieler §e" + playerName + "§c wurde nicht gefunden!",
                        NamedTextColor.RED
                    ));
                    sender.sendMessage(createMessage(
                        "§7Stelle sicher, dass der Name korrekt geschrieben ist.",
                        NamedTextColor.GRAY
                    ));
                });
                return;
            }

            // OfflinePlayer mit der echten UUID erstellen und Whitelist auf Main-Thread setzen
            getServer().getScheduler().runTask(this, () -> {
                // Verwende den nativen whitelist Befehl
                getServer().dispatchCommand(getServer().getConsoleSender(), "whitelist remove " + playerName);
                
                // Konsolennachricht
                getLogger().info("========================================");
                getLogger().info("  SPIELER ENTFERNT");
                getLogger().info("  Spieler: " + playerName);
                getLogger().info("  UUID: " + uuid);
                getLogger().info("  Whitelist: ✗ Deaktiviert");
                getLogger().info("========================================");
                
                // Feedback an Sender
                sender.sendMessage(createMessage(
                    "§a✓ Spieler §e" + playerName + "§a wurde von der Whitelist entfernt!",
                    NamedTextColor.GREEN
                ));

                // Broadcast an alle Admins
                getServer().getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission("invite.admin"))
                    .forEach(p -> {
                        if (!p.equals(sender)) {
                            p.sendMessage(createMessage(
                                "§7[§cUninvite§7] §e" + sender.getName() +
                                " §7hat §e" + playerName + " §7von der Whitelist entfernt.",
                                NamedTextColor.GRAY
                            ));
                        }
                    });
            });
            
        } catch (Exception e) {
            getLogger().severe("========================================");
            getLogger().severe("  FEHLER beim Entfernen!");
            getLogger().severe("  Spieler: " + playerName);
            getLogger().severe("  Fehler: " + e.getMessage());
            getLogger().severe("========================================");
            e.printStackTrace();
            
            getServer().getScheduler().runTask(this, () -> {
                sender.sendMessage(createMessage(
                    "§c✗ Fehler beim Entfernen von §e" + playerName + "§c!",
                    NamedTextColor.RED
                ));
                sender.sendMessage(createMessage(
                    "§7Bitte überprüfe die Logs für weitere Informationen.",
                    NamedTextColor.GRAY
                ));
            });
        }
    }

    /**
     * Setzt die Hauptgruppe eines Spielers in LuckPerms
     * Entspricht: /lp user <Username> parent set default
     *
     * @param player Der Spieler
     * @param groupName Name der Gruppe
     */
    private void setPlayerGroup(OfflinePlayer player, String groupName) {
        // Prüfen ob die Gruppe existiert
        if (luckPerms.getGroupManager().getGroup(groupName) == null) {
            getLogger().warning("Gruppe '" + groupName + "' existiert nicht in LuckPerms!");
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
                    getLogger().info("Spieler " + player.getName() + " wurde zur Gruppe '" + groupName + "' hinzugefügt.");
                });
            }
        }).exceptionally(throwable -> {
            getLogger().severe("Fehler beim Setzen der Gruppe für " + player.getName() + ": " + throwable.getMessage());
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
