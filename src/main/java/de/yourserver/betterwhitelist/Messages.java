package de.yourserver.betterwhitelist;

import java.util.HashMap;
import java.util.Map;

/**
 * Verwaltung aller Plugin-Nachrichten mit Mehrsprachigkeit
 * Management of all plugin messages with multi-language support
 */
public class Messages {
    
    private final String language;
    private final Map<String, Map<String, String>> messages;
    
    public Messages(String language) {
        this.language = language.toLowerCase();
        this.messages = new HashMap<>();
        loadMessages();
    }
    
    /**
     * Lädt alle Übersetzungen
     * Loads all translations
     */
    private void loadMessages() {
        // LOADING
        addMessage("loading.header", 
            "========================================",
            "========================================");
        addMessage("loading.starting",
            "  BetterWhitelist wird geladen...",
            "  Loading BetterWhitelist...");
        addMessage("loading.config",
            "✓ Konfiguration geladen:",
            "✓ Configuration loaded:");
        addMessage("loading.config.luckperms",
            "  - LuckPerms-Integration: ",
            "  - LuckPerms Integration: ");
        addMessage("loading.config.group",
            "  - Standard-Gruppe: ",
            "  - Default Group: ");
        addMessage("loading.config.language",
            "  - Sprache: ",
            "  - Language: ");
        addMessage("loading.luckperms.found",
            "✓ LuckPerms API erfolgreich geladen!",
            "✓ LuckPerms API loaded successfully!");
        addMessage("loading.luckperms.notfound",
            "✗ LuckPerms wurde nicht gefunden, aber in der Config aktiviert!",
            "✗ LuckPerms not found but enabled in config!");
        addMessage("loading.luckperms.disabled",
            "  LuckPerms-Integration wird deaktiviert.",
            "  LuckPerms integration will be disabled.");
        addMessage("loading.luckperms.config_disabled",
            "✓ LuckPerms-Integration ist deaktiviert.",
            "✓ LuckPerms integration is disabled.");
        addMessage("loading.commands",
            "✓ Commands registriert: /invite, /uninvite",
            "✓ Commands registered: /invite, /uninvite");
        addMessage("loading.success",
            "  BetterWhitelist v{version} erfolgreich aktiviert!",
            "  BetterWhitelist v{version} successfully activated!");
        addMessage("loading.success.luckperms",
            "  LuckPerms: ✓ Aktiviert (Gruppe: {group})",
            "  LuckPerms: ✓ Enabled (Group: {group})");
        addMessage("loading.success.luckperms_disabled",
            "  LuckPerms: ✗ Deaktiviert",
            "  LuckPerms: ✗ Disabled");
        addMessage("loading.footer",
            "========================================",
            "========================================");
        
        // UNLOADING
        addMessage("unloading.header",
            "========================================",
            "========================================");
        addMessage("unloading.message",
            "  BetterWhitelist wurde deaktiviert!",
            "  BetterWhitelist has been disabled!");
        addMessage("unloading.footer",
            "========================================",
            "========================================");
        
        // GENERAL
        addMessage("no_permission",
            "§cDu hast keine Berechtigung, diesen Befehl zu verwenden!",
            "§cYou don't have permission to use this command!");
        
        // INVITE COMMAND
        addMessage("invite.usage",
            "§eVerwendung: /invite <Spielername>",
            "§eUsage: /invite <playername>");
        addMessage("invite.loading",
            "§7Lade Spielerdaten von Mojang...",
            "§7Loading player data from Mojang...");
        addMessage("invite.not_found",
            "§c✗ Spieler §e{player}§c wurde nicht gefunden!",
            "§c✗ Player §e{player}§c not found!");
        addMessage("invite.check_name",
            "§7Stelle sicher, dass der Name korrekt geschrieben ist.",
            "§7Make sure the name is spelled correctly.");
        addMessage("invite.success",
            "§a✓ Spieler §e{player}§a wurde erfolgreich eingeladen!",
            "§a✓ Player §e{player}§a was successfully invited!");
        addMessage("invite.whitelist",
            "§7→ Zur Whitelist hinzugefügt",
            "§7→ Added to whitelist");
        addMessage("invite.group",
            "§7→ LuckPerms-Gruppe: §e{group}",
            "§7→ LuckPerms Group: §e{group}");
        addMessage("invite.error",
            "§c✗ Fehler beim Einladen von §e{player}§c!",
            "§c✗ Error inviting §e{player}§c!");
        addMessage("invite.check_logs",
            "§7Bitte überprüfe die Logs für weitere Informationen.",
            "§7Please check the logs for more information.");
        addMessage("invite.broadcast",
            "§7[§aInvite§7] §e{sender} §7hat §e{player} §7eingeladen.",
            "§7[§aInvite§7] §e{sender} §7invited §e{player}§7.");
        
        // UNINVITE COMMAND
        addMessage("uninvite.usage",
            "§eVerwendung: /uninvite <Spielername>",
            "§eUsage: /uninvite <playername>");
        addMessage("uninvite.loading",
            "§7Lade Spielerdaten von Mojang...",
            "§7Loading player data from Mojang...");
        addMessage("uninvite.not_found",
            "§c✗ Spieler §e{player}§c wurde nicht gefunden!",
            "§c✗ Player §e{player}§c not found!");
        addMessage("uninvite.check_name",
            "§7Stelle sicher, dass der Name korrekt geschrieben ist.",
            "§7Make sure the name is spelled correctly.");
        addMessage("uninvite.success",
            "§a✓ Spieler §e{player}§a wurde von der Whitelist entfernt!",
            "§a✓ Player §e{player}§a was removed from the whitelist!");
        addMessage("uninvite.error",
            "§c✗ Fehler beim Entfernen von §e{player}§c!",
            "§c✗ Error removing §e{player}§c!");
        addMessage("uninvite.check_logs",
            "§7Bitte überprüfe die Logs für weitere Informationen.",
            "§7Please check the logs for more information.");
        addMessage("uninvite.broadcast",
            "§7[§cUninvite§7] §e{sender} §7hat §e{player} §7von der Whitelist entfernt.",
            "§7[§cUninvite§7] §e{sender} §7removed §e{player} §7from the whitelist.");
        
        // CONSOLE MESSAGES
        addMessage("console.invite.header",
            "========================================",
            "========================================");
        addMessage("console.invite.title",
            "  SPIELER EINGELADEN",
            "  PLAYER INVITED");
        addMessage("console.invite.player",
            "  Spieler: {player}",
            "  Player: {player}");
        addMessage("console.invite.uuid",
            "  UUID: {uuid}",
            "  UUID: {uuid}");
        addMessage("console.invite.whitelist",
            "  Zur Whitelist hinzugefügt: ✓",
            "  Added to Whitelist: ✓");
        addMessage("console.invite.group",
            "  LuckPerms-Gruppe: {group}",
            "  LuckPerms Group: {group}");
        addMessage("console.invite.footer",
            "========================================",
            "========================================");
        
        addMessage("console.uninvite.header",
            "========================================",
            "========================================");
        addMessage("console.uninvite.title",
            "  SPIELER ENTFERNT",
            "  PLAYER REMOVED");
        addMessage("console.uninvite.player",
            "  Spieler: {player}",
            "  Player: {player}");
        addMessage("console.uninvite.uuid",
            "  UUID: {uuid}",
            "  UUID: {uuid}");
        addMessage("console.uninvite.whitelist",
            "  Von Whitelist entfernt: ✓",
            "  Removed from Whitelist: ✓");
        addMessage("console.uninvite.footer",
            "========================================",
            "========================================");
        
        addMessage("console.error.header",
            "========================================",
            "========================================");
        addMessage("console.error.invite_title",
            "  FEHLER beim Einladen!",
            "  ERROR while inviting!");
        addMessage("console.error.uninvite_title",
            "  FEHLER beim Entfernen!",
            "  ERROR while removing!");
        addMessage("console.error.player",
            "  Spieler: {player}",
            "  Player: {player}");
        addMessage("console.error.message",
            "  Fehler: {error}",
            "  Error: {error}");
        addMessage("console.error.footer",
            "========================================",
            "========================================");
        
        // LUCKPERMS MESSAGES
        addMessage("luckperms.group_not_found",
            "Gruppe '{group}' existiert nicht in LuckPerms!",
            "Group '{group}' does not exist in LuckPerms!");
        addMessage("luckperms.check_config",
            "Überprüfe deine config.yml und erstelle die Gruppe mit: /lp creategroup {group}",
            "Check your config.yml and create the group with: /lp creategroup {group}");
        addMessage("luckperms.group_set",
            "Spieler {player} wurde zur Gruppe '{group}' hinzugefügt.",
            "Player {player} was added to group '{group}'.");
        addMessage("luckperms.error",
            "Fehler beim Setzen der Gruppe für {player}: {error}",
            "Error setting group for {player}: {error}");
        
        // MOJANG API MESSAGES
        addMessage("mojang.player_not_exists",
            "Spieler '{player}' existiert nicht oder konnte nicht gefunden werden!",
            "Player '{player}' does not exist or could not be found!");
        addMessage("mojang.error",
            "Fehler beim Abrufen der UUID von Mojang: {error}",
            "Error retrieving UUID from Mojang: {error}");
        addMessage("mojang.status",
            "Mojang-API antwortet mit Status: {status}",
            "Mojang API responds with status: {status}");
    }
    
    /**
     * Fügt eine Nachricht in beiden Sprachen hinzu
     * Adds a message in both languages
     */
    private void addMessage(String key, String de, String en) {
        messages.computeIfAbsent("de", k -> new HashMap<>()).put(key, de);
        messages.computeIfAbsent("en", k -> new HashMap<>()).put(key, en);
    }
    
    /**
     * Gibt eine übersetzte Nachricht zurück
     * Returns a translated message
     */
    public String get(String key) {
        Map<String, String> langMessages = messages.get(language);
        if (langMessages == null) {
            langMessages = messages.get("en"); // Fallback to English
        }
        return langMessages.getOrDefault(key, "Missing translation: " + key);
    }
    
    /**
     * Gibt eine übersetzte Nachricht mit Platzhaltern zurück
     * Returns a translated message with placeholders
     */
    public String get(String key, Object... replacements) {
        String message = get(key);
        
        // Replace placeholders
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                String placeholder = "{" + replacements[i] + "}";
                String value = String.valueOf(replacements[i + 1]);
                message = message.replace(placeholder, value);
            }
        }
        
        return message;
    }
    
    /**
     * Gibt die aktuelle Sprache zurück
     * Returns the current language
     */
    public String getLanguage() {
        return language;
    }
}
