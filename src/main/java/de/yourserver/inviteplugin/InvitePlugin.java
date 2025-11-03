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
     *
     * @param playerName Name des Spielers
     * @return true bei Erfolg, false bei Fehler
     */
    public boolean invitePlayer(String playerName) {
        try {
            // Spieler zur Whitelist hinzufügen
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

            // Whitelist muss auf dem Main-Thread gesetzt werden
            offlinePlayer.setWhitelisted(true);

            // Konsolennachricht
            getLogger().info("========================================");
            getLogger().info("  SPIELER EINGELADEN");
            getLogger().info("  Spieler: " + playerName);
            getLogger().info("  UUID: " + offlinePlayer.getUniqueId());
            getLogger().info("  Whitelist: ✓ Aktiviert");
            getLogger().info("  LuckPerms-Gruppe: default");
            getLogger().info("========================================");

            // LuckPerms-Gruppe setzen (läuft bereits async)
            setPlayerGroup(offlinePlayer, "default");

            return true;
        } catch (Exception e) {
            getLogger().severe("========================================");
            getLogger().severe("  FEHLER beim Einladen!");
            getLogger().severe("  Spieler: " + playerName);
            getLogger().severe("  Fehler: " + e.getMessage());
            getLogger().severe("========================================");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Entfernt einen Spieler von der Whitelist
     *
     * @param playerName Name des Spielers
     * @return true bei Erfolg, false bei Fehler
     */
    public boolean uninvitePlayer(String playerName) {
        try {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            offlinePlayer.setWhitelisted(false);
            
            // Konsolennachricht
            getLogger().info("========================================");
            getLogger().info("  SPIELER ENTFERNT");
            getLogger().info("  Spieler: " + playerName);
            getLogger().info("  UUID: " + offlinePlayer.getUniqueId());
            getLogger().info("  Whitelist: ✗ Deaktiviert");
            getLogger().info("========================================");
            
            return true;
        } catch (Exception e) {
            getLogger().severe("========================================");
            getLogger().severe("  FEHLER beim Entfernen!");
            getLogger().severe("  Spieler: " + playerName);
            getLogger().severe("  Fehler: " + e.getMessage());
            getLogger().severe("========================================");
            return false;
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
