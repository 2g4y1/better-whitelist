# InvitePlugin

Ein Paper/Spigot Plugin für Minecraft, das das Whitelisten von Spielern vereinfacht und automatisch LuckPerms-Gruppen zuweist.

## Features

- **Einfaches Whitelisting**: Spieler können mit einem einzigen Befehl eingeladen werden
- **Automatische Gruppenzuweisung**: Neue Spieler werden automatisch zur "default"-Gruppe in LuckPerms hinzugefügt
- **Berechtigungssystem**: Zwei Berechtigungsstufen für normale User und Admins
- **Asynchrone Verarbeitung**: Keine Server-Lags durch blockierende Operationen
- **Broadcast-Nachrichten**: Benachrichtigungen an berechtigte Spieler

## Anforderungen

- **Minecraft**: 1.21.8 oder höher (kompatibel mit 1.21.x, inkl. 1.21.10)
- **Server**: Paper, Purpur oder ein anderer Paper-Fork
- **Java**: Version 21 oder höher
- **Abhängigkeiten**: LuckPerms muss installiert sein

## Installation

1. LuckPerms auf deinem Server installieren (falls noch nicht geschehen)
2. Die `InvitePlugin-1.0.0.jar` in den `plugins`-Ordner kopieren
3. Server neu starten
4. Berechtigungen vergeben (siehe unten)

## Befehle

### `/invite <Spielername>`
Lädt einen Spieler auf den Server ein.

**Was passiert:**
- Spieler wird zur Whitelist hinzugefügt
- Spieler erhält automatisch die Gruppe "default" in LuckPerms (entspricht `/lp user <Username> parent set default`)

**Berechtigung:** `invite.use`

**Beispiel:**
```
/invite Steve
```

### `/uninvite <Spielername>`
Entfernt einen Spieler von der Whitelist.

**Berechtigung:** `invite.admin`

**Beispiel:**
```
/uninvite Steve
```

## Berechtigungen

### `invite.use`
- **Standard**: `false` (muss explizit vergeben werden)
- **Beschreibung**: Erlaubt das Einladen von Spielern
- **Empfohlen für**: Trusted Players, Moderatoren, Admins

**LuckPerms Befehl:**
```
/lp group <gruppenname> permission set invite.use true
```
oder für einzelne Spieler:
```
/lp user <username> permission set invite.use true
```

### `invite.admin`
- **Standard**: `op` (nur für Operator)
- **Beschreibung**: Erlaubt das Entfernen von Spielern (uninvite)
- **Empfohlen für**: Admins, Server-Owner

**LuckPerms Befehl:**
```
/lp group admin permission set invite.admin true
```

## Konfiguration

### Gruppe ändern
Standardmäßig werden eingeladene Spieler der Gruppe "default" zugewiesen. Um dies zu ändern:

1. Öffne `InvitePlugin.java`
2. Suche nach der Zeile: `setPlayerGroup(offlinePlayer, "default");`
3. Ersetze `"default"` durch den Namen deiner gewünschten Gruppe
4. Plugin neu kompilieren

## Kompilierung

```bash
mvn clean package
```

Die fertige JAR-Datei findest du dann in `target/InvitePlugin-1.0.0.jar`

## Verwendete APIs

- **Paper API**: 1.21.8-R0.1-SNAPSHOT (aktuelle stabile Version, kompatibel mit 1.21.x)
- **LuckPerms API**: 5.4 (aktuelle stabile Version)
- **Java**: 21 (erforderlich für Paper 1.21.x)
- **Maven Compiler Plugin**: 3.13.0
- **Maven Shade Plugin**: 3.6.0

## Support

Bei Problemen oder Fragen:
1. Überprüfe die Server-Logs (`logs/latest.log`)
2. Stelle sicher, dass LuckPerms korrekt installiert ist
3. Überprüfe die Berechtigungen mit `/lp user <username> permission check invite.use`

## Lizenz

Dieses Plugin wurde für private Zwecke erstellt. Frei verwendbar und anpassbar.

## Changelog

### Version 1.0.0
- Initiales Release
- `/invite` Command mit Whitelist-Funktion
- `/uninvite` Command zum Entfernen
- Automatische LuckPerms-Gruppenzuweisung
- Berechtigungssystem
- Broadcast-Nachrichten
