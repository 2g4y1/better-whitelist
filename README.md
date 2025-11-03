# 🎮 BetterWhitelist# BetterWhitelist



<div align="center">Ein konfigurierbares Paper/Spigot Plugin für Minecraft, das das Whitelisten von Spielern vereinfacht und optional automatisch LuckPerms-Gruppen zuweist.



**Ein konfigurierbares Minecraft-Plugin, das Spielern ermöglicht, Freunde einzuladen**## Features



[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-green.svg)](https://papermc.io/)- **Einfaches Whitelisting**: Spieler können mit einem einzigen Befehl eingeladen werden

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)- **Konfigurierbare LuckPerms-Integration**: Optional können neue Spieler automatisch einer konfigurierbaren Gruppe zugewiesen werden

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)- **Flexible Konfiguration**: Standard-Gruppe und LuckPerms-Nutzung über config.yml anpassbar

- **Berechtigungssystem**: Zwei Berechtigungsstufen für normale User und Admins

[Features](#-features) • [Installation](#-installation) • [Konfiguration](#-konfiguration) • [Commands](#-commands) • [Download](#-download)- **Asynchrone Verarbeitung**: Keine Server-Lags durch blockierende Operationen

- **Broadcast-Nachrichten**: Benachrichtigungen an berechtigte Spieler

</div>

## Anforderungen

---

- **Minecraft**: 1.21.8 oder höher (kompatibel mit 1.21.x, inkl. 1.21.10)

## 📖 Über das Plugin- **Server**: Paper, Purpur oder ein anderer Paper-Fork

- **Java**: Version 21 oder höher

**BetterWhitelist** wurde entwickelt, um ein häufiges Problem auf privaten Minecraft-Servern zu lösen:- **Abhängigkeiten (optional)**: LuckPerms (falls LuckPerms-Integration gewünscht ist)



> **Das Problem:** Spieler müssen immer einen Admin kontaktieren, wenn sie einen Freund auf den Server einladen möchten. Das ist umständlich und zeitaufwendig.## Installation



> **Die Lösung:** BetterWhitelist gibt vertrauenswürdigen Spielern die Möglichkeit, selbst Freunde zur Whitelist hinzuzufügen – ohne Admin-Rechte zu benötigen.1. (Optional) LuckPerms auf deinem Server installieren, falls du die Gruppenzuweisung nutzen möchtest

2. Die `BetterWhitelist-<version>.jar` in den `plugins`-Ordner kopieren

### ✨ Warum BetterWhitelist?3. Server neu starten (erstellt automatisch `plugins/BetterWhitelist/config.yml`)

4. Konfiguration nach Bedarf anpassen (siehe unten)

- 🤝 **Spieler-Freiheit**: Vertrauenswürdige Spieler können selbstständig Freunde einladen5. Berechtigungen vergeben (siehe unten)

- 🔒 **Sicherheit**: Berechtigungssystem verhindert Missbrauch

- 🌍 **Mehrsprachig**: Unterstützt Deutsch und Englisch## Befehle

- ⚡ **Performant**: Asynchrone Verarbeitung, keine Server-Lags

- 🎯 **Flexibel**: Optionale LuckPerms-Integration für automatische Gruppenzuweisung### `/invite <Spielername>`

- 📝 **Einfach**: Intuitive Befehle und klare NachrichtenLädt einen Spieler auf den Server ein.



---**Was passiert:**

- Spieler wird zur Whitelist hinzugefügt

## 🚀 Features- Falls in der Config aktiviert: Spieler erhält automatisch die konfigurierte Gruppe in LuckPerms (entspricht `/lp user <Username> parent set <gruppe>`)



### Kern-Features**Berechtigung:** `invite.use`



| Feature | Beschreibung |**Beispiel:**

|---------|--------------|```

| **🎫 Whitelist-Management** | Einfaches Hinzufügen/Entfernen von Spielern zur Whitelist |/invite Steve

| **🔐 Berechtigungssystem** | Zwei Stufen: `invite.use` und `invite.admin` |```

| **🌐 Mehrsprachigkeit** | Deutsch und Englisch vollständig unterstützt |

| **⚙️ Konfigurierbar** | Alle Einstellungen über `config.yml` anpassbar |### `/uninvite <Spielername>`

Entfernt einen Spieler von der Whitelist.

### LuckPerms Integration

**Berechtigung:** `invite.admin`

| Feature | Beschreibung |

|---------|--------------|**Beispiel:**

| **👥 Automatische Gruppenzuweisung** | Neue Spieler erhalten automatisch eine konfigurierbare Gruppe |```

| **🔄 Optional** | LuckPerms kann komplett deaktiviert werden |/uninvite Steve

| **📦 Flexible Konfiguration** | Standard-Gruppe frei wählbar |```



### Technische Features## Berechtigungen



| Feature | Beschreibung |### `invite.use`

|---------|--------------|- **Standard**: `false` (muss explizit vergeben werden)

| **⚡ Async-Verarbeitung** | Keine Server-Lags durch blockierende Operationen |- **Beschreibung**: Erlaubt das Einladen von Spielern

| **📡 Mojang-API** | Offizielle UUID-Abfrage wie bei `/whitelist add` |- **Empfohlen für**: Trusted Players, Moderatoren, Admins

| **📢 Broadcast-System** | Benachrichtigung bei Spieler-Einladungen |

| **🎨 Adventure API** | Moderne Text-Komponenten für bessere Darstellung |**LuckPerms Befehl:**

```

---/lp group <gruppenname> permission set invite.use true

```

## 📥 Installationoder für einzelne Spieler:

```

### Schritt 1: Voraussetzungen prüfen/lp user <username> permission set invite.use true

```

- ✅ **Server**: Paper, Purpur oder ein anderer Paper-Fork (1.21.x)

- ✅ **Java**: Version 21 oder höher### `invite.admin`

- ⭕ **Optional**: LuckPerms (falls Gruppenzuweisung gewünscht)- **Standard**: `op` (nur für Operator)

- **Beschreibung**: Erlaubt das Entfernen von Spielern (uninvite)

### Schritt 2: Plugin installieren- **Empfohlen für**: Admins, Server-Owner



1. [**Download**](#-download) die neueste `BetterWhitelist-x.x.x.jar`**LuckPerms Befehl:**

2. Kopiere die JAR-Datei in den `plugins/`-Ordner deines Servers```

3. Starte den Server neu/lp group admin permission set invite.admin true

4. Die `config.yml` wird automatisch erstellt in `plugins/BetterWhitelist/````



### Schritt 3: Konfiguration anpassen## Konfiguration



Öffne `plugins/BetterWhitelist/config.yml` und passe die Einstellungen an:Nach dem ersten Start wird automatisch die Datei `plugins/BetterWhitelist/config.yml` erstellt:



```yaml```yaml

language: "de"  # oder "en" für Englisch# ============================================

# BetterWhitelist - Konfiguration

luckperms:# ============================================

  enabled: true              # LuckPerms-Integration aktivieren

  default-group: "default"   # Standard-Gruppe für neue Spieler# LuckPerms Integration

```luckperms:

  # Sollen Spieler automatisch zu einer LuckPerms-Gruppe hinzugefügt werden?

### Schritt 4: Berechtigungen vergeben  # true = Aktiviert | false = Deaktiviert

  enabled: true

Weise Spielern oder Gruppen die benötigten Berechtigungen zu:  

  # Name der Standard-Gruppe, die neue Spieler erhalten sollen

```bash  # Diese Gruppe muss in LuckPerms existieren!

# Spieler dürfen Freunde einladen  # Beispiele: default, member, spieler, newbie

/lp group trusted permission set invite.use true  default-group: "default"

```

# Admins dürfen Spieler entfernen

/lp group admin permission set invite.admin true### Konfigurationsoptionen

```

#### `luckperms.enabled`

✅ **Fertig!** Das Plugin ist einsatzbereit.- **Standard**: `true`

- **Beschreibung**: Aktiviert oder deaktiviert die automatische LuckPerms-Gruppenzuweisung

---- **Hinweis**: Wenn auf `false` gesetzt, werden Spieler nur zur Whitelist hinzugefügt, ohne Gruppenänderungen



## ⚙️ Konfiguration#### `luckperms.default-group`

- **Standard**: `"default"`

### Automatische Config-Erstellung- **Beschreibung**: Name der Gruppe, die eingeladene Spieler erhalten sollen

- **Wichtig**: Diese Gruppe muss in LuckPerms existieren! Überprüfe mit `/lp listgroups`

Die `config.yml` wird beim ersten Start **automatisch erstellt**. Du musst nichts manuell anlegen!

### Gruppe in LuckPerms erstellen

Speicherort: `plugins/BetterWhitelist/config.yml`

Falls die konfigurierte Gruppe noch nicht existiert:

### Vollständige Config-Datei

```bash

```yaml/lp creategroup <gruppenname>

# ============================================/lp group <gruppenname> permission set <permissions>

# BetterWhitelist - Konfiguration```

# ============================================

## Kompilierung

# ============================================

# Sprache / Language**Hinweis**: Für die Kompilierung wird Java 21 benötigt, da Paper 1.21.x dies erfordert.

# ============================================

```bash

# Wähle die Sprache für alle Plugin-Nachrichtenmvn clean package

# Choose the language for all plugin messages```

# 

# Verfügbare Sprachen / Available languages:Die fertige JAR-Datei findest du dann im `target/`-Ordner.

#   - de (Deutsch)

#   - en (English)### Kompilierung mit Docker (wenn Java 21 nicht verfügbar ist)

language: "de"

```bash

# ============================================docker run --rm -v "$(pwd)":/app -w /app maven:3.9-eclipse-temurin-21 mvn clean package

# LuckPerms Integration```

# ============================================

## Verwendete APIs

# Sollen Spieler automatisch zu einer LuckPerms-Gruppe hinzugefügt werden?

# Should players be automatically added to a LuckPerms group?- **Paper API**: 1.21.8-R0.1-SNAPSHOT (aktuelle stabile Version, kompatibel mit 1.21.x)

# true = Aktiviert / Enabled | false = Deaktiviert / Disabled- **LuckPerms API**: 5.4 (aktuelle stabile Version)

luckperms:- **Java**: 21 (erforderlich für Paper 1.21.x)

  enabled: true- **Maven Compiler Plugin**: 3.13.0

  - **Maven Shade Plugin**: 3.6.0

  # Name der Standard-Gruppe, die neue Spieler erhalten sollen

  # Name of the default group that new players should receive## Support

  # Diese Gruppe muss in LuckPerms existieren!

  # This group must exist in LuckPerms!Bei Problemen oder Fragen:

  # Beispiele / Examples: default, member, spieler, newbie1. Überprüfe die Server-Logs (`logs/latest.log`)

  default-group: "default"2. Stelle sicher, dass LuckPerms korrekt installiert ist

```3. Überprüfe die Berechtigungen mit `/lp user <username> permission check invite.use`



### Konfigurationsoptionen## Lizenz



#### 🌐 `language`Dieses Plugin wurde für private Zwecke erstellt. Frei verwendbar und anpassbar.



- **Typ**: String## Changelog

- **Standard**: `"de"`

- **Optionen**: `"de"` (Deutsch), `"en"` (English)### Version 0.0.2

- **Beschreibung**: Legt die Sprache für alle Plugin-Nachrichten fest- Plugin umbenannt zu BetterWhitelist

- Konfigurationsdatei hinzugefügt (`config.yml`)

#### ⚙️ `luckperms.enabled`- LuckPerms-Integration ist jetzt optional konfigurierbar

- Standard-Gruppe ist jetzt konfigurierbar

- **Typ**: Boolean- LuckPerms ist jetzt eine optionale Abhängigkeit (softdepend statt depend)

- **Standard**: `true`

- **Beschreibung**: Aktiviert/Deaktiviert die automatische LuckPerms-Gruppenzuweisung### Version 0.0.1

- **Hinweis**: Bei `false` werden Spieler nur zur Whitelist hinzugefügt, ohne Gruppenänderungen- Initiales Release als InvitePlugin

- `/invite` Command mit Whitelist-Funktion

#### 👥 `luckperms.default-group`- `/uninvite` Command zum Entfernen

- Automatische LuckPerms-Gruppenzuweisung

- **Typ**: String- Berechtigungssystem

- **Standard**: `"default"`- Broadcast-Nachrichten

- **Beschreibung**: Name der Gruppe, die eingeladene Spieler erhalten
- **Wichtig**: Diese Gruppe muss in LuckPerms existieren!

### Gruppe in LuckPerms erstellen

Falls die konfigurierte Gruppe noch nicht existiert:

```bash
# Gruppe erstellen
/lp creategroup meingruppe

# Berechtigungen setzen
/lp group meingruppe permission set minecraft.command.help true
```

Überprüfe verfügbare Gruppen mit:

```bash
/lp listgroups
```

---

## 🎮 Commands

### `/invite <Spielername>`

**Beschreibung**: Lädt einen Spieler auf den Server ein

**Was passiert:**
- ✅ Spieler wird zur Whitelist hinzugefügt
- ✅ (Optional) Spieler erhält die konfigurierte LuckPerms-Gruppe
- ✅ Broadcast an alle Spieler mit `invite.use` Berechtigung

**Berechtigung**: `invite.use`

**Beispiele:**
```bash
/invite Steve
/invite Alex
/invite Notch
```

**Ausgabe:**
```
✓ Spieler Steve wurde erfolgreich eingeladen!
→ Whitelist: Aktiviert
→ LuckPerms-Gruppe: default
```

---

### `/uninvite <Spielername>`

**Beschreibung**: Entfernt einen Spieler von der Whitelist

**Was passiert:**
- ❌ Spieler wird von der Whitelist entfernt
- 📢 Broadcast an alle Spieler mit `invite.admin` Berechtigung

**Berechtigung**: `invite.admin`

**Beispiele:**
```bash
/uninvite Steve
/uninvite Alex
```

**Ausgabe:**
```
✓ Spieler Steve wurde von der Whitelist entfernt!
```

---

## 🔐 Berechtigungen

### Übersicht

| Berechtigung | Standard | Beschreibung |
|-------------|----------|--------------|
| `invite.use` | `false` | Erlaubt das Einladen von Spielern |
| `invite.admin` | `false` | Erlaubt das Entfernen von Spielern |

### Empfohlene Konfiguration

#### Für vertrauenswürdige Spieler

```bash
# Einzelne Spieler
/lp user Steve permission set invite.use true

# Ganze Gruppe
/lp group trusted permission set invite.use true
/lp user Steve parent add trusted
```

#### Für Administratoren

```bash
# Admins erhalten beide Berechtigungen
/lp group admin permission set invite.use true
/lp group admin permission set invite.admin true
```

#### Für Moderatoren

```bash
# Moderatoren nur einladen, nicht entfernen
/lp group moderator permission set invite.use true
```

---

## 📦 Download

### Neueste Version

**Version**: 0.0.2

**Download**: [GitHub Releases](https://github.com/2g4y1/better-whitelist/releases)

### Changelog

#### Version 0.0.2
- ➕ Mehrsprachigkeit hinzugefügt (Deutsch/Englisch)
- ➕ Konfigurationsdatei mit Sprachauswahl
- ➕ LuckPerms-Integration ist jetzt optional konfigurierbar
- ➕ Standard-Gruppe ist jetzt konfigurierbar
- 🔄 LuckPerms ist jetzt eine optionale Abhängigkeit (`softdepend` statt `depend`)
- 📝 README komplett überarbeitet

#### Version 0.0.1
- 🎉 Initiales Release
- ✨ `/invite` Command mit Whitelist-Funktion
- ✨ `/uninvite` Command zum Entfernen
- ✨ Automatische LuckPerms-Gruppenzuweisung
- ✨ Berechtigungssystem
- ✨ Broadcast-Nachrichten

---

## 🛠️ Kompilierung

### Voraussetzungen

- **Java 21** (erforderlich für Paper 1.21.x)
- **Maven 3.6+**

### Mit Maven

```bash
mvn clean package
```

Die fertige JAR-Datei findest du dann im `target/`-Ordner.

### Mit Docker (wenn Java 21 nicht verfügbar)

```bash
docker run --rm -v "$(pwd)":/app -w /app maven:3.9-eclipse-temurin-21 mvn clean package
```

---

## 🐛 Troubleshooting

### Problem: "LuckPerms wurde nicht gefunden"

**Lösung**:
1. Überprüfe, ob LuckPerms installiert ist: `/plugins`
2. Wenn nicht benötigt, setze in `config.yml`: `luckperms.enabled: false`

### Problem: "Gruppe existiert nicht in LuckPerms"

**Lösung**:
1. Überprüfe verfügbare Gruppen: `/lp listgroups`
2. Erstelle die Gruppe: `/lp creategroup <name>`
3. Oder ändere `luckperms.default-group` in der `config.yml`

### Problem: "Spieler wurde nicht gefunden"

**Lösung**:
- Stelle sicher, dass der Spielername korrekt geschrieben ist
- Der Spieler muss ein gültiger Minecraft-Account sein
- Überprüfe die Mojang-API-Erreichbarkeit

### Problem: Berechtigungen funktionieren nicht

**Lösung**:
```bash
# Berechtigung prüfen
/lp user <username> permission check invite.use

# Berechtigung setzen
/lp user <username> permission set invite.use true
```

---

## 📞 Support

Bei Problemen oder Fragen:

1. 📖 Überprüfe die [Troubleshooting](#-troubleshooting)-Sektion
2. 🔍 Schaue in die Server-Logs (`logs/latest.log`)
3. 🐛 Öffne ein [GitHub Issue](https://github.com/2g4y1/better-whitelist/issues)

---

## 📄 Lizenz

Dieses Plugin wurde für private Zwecke erstellt. Frei verwendbar und anpassbar.

---

## 🙏 Verwendete APIs

- **Paper API**: 1.21.10-R0.1-SNAPSHOT
- **LuckPerms API**: 5.4
- **Adventure API**: Für moderne Text-Komponenten
- **Mojang API**: Für UUID-Abfragen

---

<div align="center">

**Entwickelt mit ❤️ für die Minecraft-Community**

[⬆ Nach oben](#-betterwhitelist)

</div>
