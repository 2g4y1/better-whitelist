# 🎮 BetterWhitelist# 🎮 BetterWhitelist# BetterWhitelist



<div align="center">



**Ein konfigurierbares Minecraft-Plugin, das Spielern ermöglicht, Freunde einzuladen**<div align="center">Ein konfigurierbares Paper/Spigot Plugin für Minecraft, das das Whitelisten von Spielern vereinfacht und optional automatisch LuckPerms-Gruppen zuweist.



[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-green.svg)](https://papermc.io/)

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)**Ein konfigurierbares Minecraft-Plugin, das Spielern ermöglicht, Freunde einzuladen**## Features



[Features](#-features) • [Installation](#-installation) • [Konfiguration](#-konfiguration) • [Commands](#-commands) • [Download](#-download)



[🇬🇧 English Version](README_EN.md)[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-green.svg)](https://papermc.io/)- **Einfaches Whitelisting**: Spieler können mit einem einzigen Befehl eingeladen werden



</div>[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)- **Konfigurierbare LuckPerms-Integration**: Optional können neue Spieler automatisch einer konfigurierbaren Gruppe zugewiesen werden



---[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)- **Flexible Konfiguration**: Standard-Gruppe und LuckPerms-Nutzung über config.yml anpassbar



## 📖 Über das Plugin- **Berechtigungssystem**: Zwei Berechtigungsstufen für normale User und Admins



**BetterWhitelist** wurde entwickelt, um ein häufiges Problem auf privaten Minecraft-Servern zu lösen:[Features](#-features) • [Installation](#-installation) • [Konfiguration](#-konfiguration) • [Commands](#-commands) • [Download](#-download)- **Asynchrone Verarbeitung**: Keine Server-Lags durch blockierende Operationen



> **Das Problem:** Spieler müssen immer einen Admin kontaktieren, wenn sie einen Freund auf den Server einladen möchten. Das ist umständlich und zeitaufwendig.- **Broadcast-Nachrichten**: Benachrichtigungen an berechtigte Spieler



> **Die Lösung:** BetterWhitelist gibt vertrauenswürdigen Spielern die Möglichkeit, selbst Freunde zur Whitelist hinzuzufügen – ohne Admin-Rechte zu benötigen.</div>



### ✨ Warum BetterWhitelist?## Anforderungen



- 🤝 **Spieler-Freiheit**: Vertrauenswürdige Spieler können selbstständig Freunde einladen---

- 🔒 **Sicherheit**: Berechtigungssystem verhindert Missbrauch

- 🌍 **Mehrsprachig**: Unterstützt Deutsch und Englisch- **Minecraft**: 1.21.8 oder höher (kompatibel mit 1.21.x, inkl. 1.21.10)

- ⚡ **Performant**: Asynchrone Verarbeitung, keine Server-Lags

- 🎯 **Flexibel**: Optionale LuckPerms-Integration für automatische Gruppenzuweisung## 📖 Über das Plugin- **Server**: Paper, Purpur oder ein anderer Paper-Fork

- 📝 **Einfach**: Intuitive Befehle und klare Nachrichten

- **Java**: Version 21 oder höher

---

**BetterWhitelist** wurde entwickelt, um ein häufiges Problem auf privaten Minecraft-Servern zu lösen:- **Abhängigkeiten (optional)**: LuckPerms (falls LuckPerms-Integration gewünscht ist)

## 📸 Screenshots



<div align="center">

> **Das Problem:** Spieler müssen immer einen Admin kontaktieren, wenn sie einen Freund auf den Server einladen möchten. Das ist umständlich und zeitaufwendig.## Installation

![Ingame Screenshot](images/ingame-screenshot.png)



*So sieht es aus, wenn ein Spieler eingeladen wird*

> **Die Lösung:** BetterWhitelist gibt vertrauenswürdigen Spielern die Möglichkeit, selbst Freunde zur Whitelist hinzuzufügen – ohne Admin-Rechte zu benötigen.1. (Optional) LuckPerms auf deinem Server installieren, falls du die Gruppenzuweisung nutzen möchtest

</div>

2. Die `BetterWhitelist-<version>.jar` in den `plugins`-Ordner kopieren

---

### ✨ Warum BetterWhitelist?3. Server neu starten (erstellt automatisch `plugins/BetterWhitelist/config.yml`)

## 🚀 Features

4. Konfiguration nach Bedarf anpassen (siehe unten)

### Kern-Features

- 🤝 **Spieler-Freiheit**: Vertrauenswürdige Spieler können selbstständig Freunde einladen5. Berechtigungen vergeben (siehe unten)

| Feature | Beschreibung |

|---------|--------------|- 🔒 **Sicherheit**: Berechtigungssystem verhindert Missbrauch

| **🎫 Whitelist-Management** | Einfaches Hinzufügen/Entfernen von Spielern zur Whitelist |

| **🔐 Berechtigungssystem** | Zwei Stufen: `invite.use` und `invite.admin` |- 🌍 **Mehrsprachig**: Unterstützt Deutsch und Englisch## Befehle

| **🌐 Mehrsprachigkeit** | Deutsch und Englisch vollständig unterstützt |

| **⚙️ Konfigurierbar** | Alle Einstellungen über `config.yml` anpassbar |- ⚡ **Performant**: Asynchrone Verarbeitung, keine Server-Lags



### LuckPerms Integration- 🎯 **Flexibel**: Optionale LuckPerms-Integration für automatische Gruppenzuweisung### `/invite <Spielername>`



| Feature | Beschreibung |- 📝 **Einfach**: Intuitive Befehle und klare NachrichtenLädt einen Spieler auf den Server ein.

|---------|--------------|

| **👥 Automatische Gruppenzuweisung** | Neue Spieler erhalten automatisch eine konfigurierbare Gruppe |

| **🔄 Optional** | LuckPerms kann komplett deaktiviert werden |

| **📦 Flexible Konfiguration** | Standard-Gruppe frei wählbar |---**Was passiert:**



### Technische Features- Spieler wird zur Whitelist hinzugefügt



| Feature | Beschreibung |## 🚀 Features- Falls in der Config aktiviert: Spieler erhält automatisch die konfigurierte Gruppe in LuckPerms (entspricht `/lp user <Username> parent set <gruppe>`)

|---------|--------------|

| **⚡ Async-Verarbeitung** | Keine Server-Lags durch blockierende Operationen |

| **📡 Mojang-API** | Offizielle UUID-Abfrage wie bei `/whitelist add` |

| **📢 Broadcast-System** | Benachrichtigung bei Spieler-Einladungen |### Kern-Features**Berechtigung:** `invite.use`

| **🎨 Adventure API** | Moderne Text-Komponenten für bessere Darstellung |



---

| Feature | Beschreibung |**Beispiel:**

## 📥 Installation

|---------|--------------|```

### Schritt 1: Voraussetzungen prüfen

| **🎫 Whitelist-Management** | Einfaches Hinzufügen/Entfernen von Spielern zur Whitelist |/invite Steve

- ✅ **Server**: Paper, Purpur oder ein anderer Paper-Fork (1.21.x)

- ✅ **Java**: Version 21 oder höher| **🔐 Berechtigungssystem** | Zwei Stufen: `invite.use` und `invite.admin` |```

- ⭕ **Optional**: LuckPerms (falls Gruppenzuweisung gewünscht ist)

| **🌐 Mehrsprachigkeit** | Deutsch und Englisch vollständig unterstützt |

### Schritt 2: Plugin installieren

| **⚙️ Konfigurierbar** | Alle Einstellungen über `config.yml` anpassbar |### `/uninvite <Spielername>`

1. [**Download**](#-download) die neueste `BetterWhitelist-x.x.x.jar`

2. Kopiere die JAR-Datei in den `plugins/`-Ordner deines ServersEntfernt einen Spieler von der Whitelist.

3. Starte den Server neu

4. Die `config.yml` wird automatisch in `plugins/BetterWhitelist/` erstellt### LuckPerms Integration



### Schritt 3: Konfiguration anpassen**Berechtigung:** `invite.admin`



Öffne `plugins/BetterWhitelist/config.yml` und passe die Einstellungen an:| Feature | Beschreibung |



```yaml|---------|--------------|**Beispiel:**

language: "de"  # oder "en" für Englisch

| **👥 Automatische Gruppenzuweisung** | Neue Spieler erhalten automatisch eine konfigurierbare Gruppe |```

luckperms:

  enabled: true              # LuckPerms-Integration aktivieren| **🔄 Optional** | LuckPerms kann komplett deaktiviert werden |/uninvite Steve

  default-group: "default"   # Standard-Gruppe für neue Spieler

```| **📦 Flexible Konfiguration** | Standard-Gruppe frei wählbar |```



### Schritt 4: Berechtigungen vergeben



Vergib den Spielern oder Gruppen die benötigten Berechtigungen:### Technische Features## Berechtigungen



```bash

# Spieler dürfen Freunde einladen

/lp group trusted permission set invite.use true| Feature | Beschreibung |### `invite.use`



# Admins dürfen Spieler entfernen|---------|--------------|- **Standard**: `false` (muss explizit vergeben werden)

/lp group admin permission set invite.admin true

```| **⚡ Async-Verarbeitung** | Keine Server-Lags durch blockierende Operationen |- **Beschreibung**: Erlaubt das Einladen von Spielern



✅ **Fertig!** Das Plugin ist einsatzbereit.| **📡 Mojang-API** | Offizielle UUID-Abfrage wie bei `/whitelist add` |- **Empfohlen für**: Trusted Players, Moderatoren, Admins



---| **📢 Broadcast-System** | Benachrichtigung bei Spieler-Einladungen |



## ⚙️ Konfiguration| **🎨 Adventure API** | Moderne Text-Komponenten für bessere Darstellung |**LuckPerms Befehl:**



### Automatische Config-Erstellung```



Die `config.yml` wird beim **ersten Start automatisch erstellt**. Du musst nichts manuell erstellen!---/lp group <gruppenname> permission set invite.use true



Speicherort: `plugins/BetterWhitelist/config.yml````



### Komplette Config-Datei## 📥 Installationoder für einzelne Spieler:



```yaml```

# ============================================

# BetterWhitelist - Konfiguration### Schritt 1: Voraussetzungen prüfen/lp user <username> permission set invite.use true

# ============================================

```

# ============================================

# Sprache / Language- ✅ **Server**: Paper, Purpur oder ein anderer Paper-Fork (1.21.x)

# ============================================

- ✅ **Java**: Version 21 oder höher### `invite.admin`

# Wähle die Sprache für alle Plugin-Nachrichten

# Choose the language for all plugin messages- ⭕ **Optional**: LuckPerms (falls Gruppenzuweisung gewünscht)- **Standard**: `op` (nur für Operator)

# 

# Verfügbare Sprachen / Available languages:- **Beschreibung**: Erlaubt das Entfernen von Spielern (uninvite)

#   - de (Deutsch)

#   - en (English)### Schritt 2: Plugin installieren- **Empfohlen für**: Admins, Server-Owner

language: "de"



# ============================================

# LuckPerms Integration1. [**Download**](#-download) die neueste `BetterWhitelist-x.x.x.jar`**LuckPerms Befehl:**

# ============================================

2. Kopiere die JAR-Datei in den `plugins/`-Ordner deines Servers```

# Sollen Spieler automatisch zu einer LuckPerms-Gruppe hinzugefügt werden?

# Should players be automatically added to a LuckPerms group?3. Starte den Server neu/lp group admin permission set invite.admin true

# true = Aktiviert / Enabled | false = Deaktiviert / Disabled

luckperms:4. Die `config.yml` wird automatisch erstellt in `plugins/BetterWhitelist/````

  enabled: true

  

  # Name der Standard-Gruppe, die neue Spieler erhalten sollen

  # Name of the default group that new players should receive### Schritt 3: Konfiguration anpassen## Konfiguration

  # Diese Gruppe muss in LuckPerms existieren!

  # This group must exist in LuckPerms!

  # Beispiele / Examples: default, member, player, newbie

  default-group: "default"Öffne `plugins/BetterWhitelist/config.yml` und passe die Einstellungen an:Nach dem ersten Start wird automatisch die Datei `plugins/BetterWhitelist/config.yml` erstellt:

```



### Konfigurations-Optionen

```yaml```yaml

#### 🌐 `language`

language: "de"  # oder "en" für Englisch# ============================================

- **Typ**: String

- **Standard**: `"de"`# BetterWhitelist - Konfiguration

- **Optionen**: `"de"` (Deutsch), `"en"` (Englisch)

- **Beschreibung**: Legt die Sprache für alle Plugin-Nachrichten festluckperms:# ============================================



#### ⚙️ `luckperms.enabled`  enabled: true              # LuckPerms-Integration aktivieren



- **Typ**: Boolean  default-group: "default"   # Standard-Gruppe für neue Spieler# LuckPerms Integration

- **Standard**: `true`

- **Beschreibung**: Aktiviert/Deaktiviert die automatische LuckPerms-Gruppenzuweisung```luckperms:

- **Hinweis**: Bei `false` werden Spieler nur zur Whitelist hinzugefügt, ohne Gruppenänderung

  # Sollen Spieler automatisch zu einer LuckPerms-Gruppe hinzugefügt werden?

#### 👥 `luckperms.default-group`

### Schritt 4: Berechtigungen vergeben  # true = Aktiviert | false = Deaktiviert

- **Typ**: String

- **Standard**: `"default"`  enabled: true

- **Beschreibung**: Name der Gruppe, die eingeladene Spieler erhalten

- **Wichtig**: Diese Gruppe muss in LuckPerms existieren!Weise Spielern oder Gruppen die benötigten Berechtigungen zu:  



### Gruppe in LuckPerms erstellen  # Name der Standard-Gruppe, die neue Spieler erhalten sollen



Falls die konfigurierte Gruppe noch nicht existiert:```bash  # Diese Gruppe muss in LuckPerms existieren!



```bash# Spieler dürfen Freunde einladen  # Beispiele: default, member, spieler, newbie

# Gruppe erstellen

/lp creategroup meinegruppe/lp group trusted permission set invite.use true  default-group: "default"



# Berechtigungen setzen```

/lp group meinegruppe permission set minecraft.command.help true

```# Admins dürfen Spieler entfernen



Verfügbare Gruppen prüfen mit:/lp group admin permission set invite.admin true### Konfigurationsoptionen



```bash```

/lp listgroups

```#### `luckperms.enabled`



---✅ **Fertig!** Das Plugin ist einsatzbereit.- **Standard**: `true`



## 🎮 Commands- **Beschreibung**: Aktiviert oder deaktiviert die automatische LuckPerms-Gruppenzuweisung



### `/invite <Spielername>`---- **Hinweis**: Wenn auf `false` gesetzt, werden Spieler nur zur Whitelist hinzugefügt, ohne Gruppenänderungen



**Beschreibung**: Lädt einen Spieler auf den Server ein



**Was passiert:**## ⚙️ Konfiguration#### `luckperms.default-group`

- ✅ Spieler wird zur Whitelist hinzugefügt

- ✅ (Optional) Spieler erhält die konfigurierte LuckPerms-Gruppe- **Standard**: `"default"`

- ✅ Broadcast an alle Spieler mit `invite.use`-Berechtigung

### Automatische Config-Erstellung- **Beschreibung**: Name der Gruppe, die eingeladene Spieler erhalten sollen

**Berechtigung**: `invite.use`

- **Wichtig**: Diese Gruppe muss in LuckPerms existieren! Überprüfe mit `/lp listgroups`

**Beispiele:**

```bashDie `config.yml` wird beim ersten Start **automatisch erstellt**. Du musst nichts manuell anlegen!

/invite Steve

/invite Alex### Gruppe in LuckPerms erstellen

/invite Notch

```Speicherort: `plugins/BetterWhitelist/config.yml`



**Ausgabe:**Falls die konfigurierte Gruppe noch nicht existiert:

```

✓ Spieler Steve wurde erfolgreich eingeladen!### Vollständige Config-Datei

→ Whitelist: Aktiviert

→ LuckPerms-Gruppe: default```bash

```

```yaml/lp creategroup <gruppenname>

---

# ============================================/lp group <gruppenname> permission set <permissions>

### `/uninvite <Spielername>`

# BetterWhitelist - Konfiguration```

**Beschreibung**: Entfernt einen Spieler von der Whitelist

# ============================================

**Was passiert:**

- ❌ Spieler wird von der Whitelist entfernt## Kompilierung

- 📢 Broadcast an alle Spieler mit `invite.admin`-Berechtigung

# ============================================

**Berechtigung**: `invite.admin`

# Sprache / Language**Hinweis**: Für die Kompilierung wird Java 21 benötigt, da Paper 1.21.x dies erfordert.

**Beispiele:**

```bash# ============================================

/uninvite Steve

/uninvite Alex```bash

```

# Wähle die Sprache für alle Plugin-Nachrichtenmvn clean package

**Ausgabe:**

```# Choose the language for all plugin messages```

✓ Spieler Steve wurde von der Whitelist entfernt!

```# 



---# Verfügbare Sprachen / Available languages:Die fertige JAR-Datei findest du dann im `target/`-Ordner.



## 🔐 Berechtigungen#   - de (Deutsch)



### Übersicht#   - en (English)### Kompilierung mit Docker (wenn Java 21 nicht verfügbar ist)



| Berechtigung | Standard | Beschreibung |language: "de"

|--------------|----------|--------------|

| `invite.use` | `false` | Erlaubt das Einladen von Spielern |```bash

| `invite.admin` | `false` | Erlaubt das Entfernen von Spielern |

# ============================================docker run --rm -v "$(pwd)":/app -w /app maven:3.9-eclipse-temurin-21 mvn clean package

### Empfohlene Konfiguration

# LuckPerms Integration```

#### Für Trusted Players

# ============================================

```bash

# Einzelne Spieler## Verwendete APIs

/lp user Steve permission set invite.use true

# Sollen Spieler automatisch zu einer LuckPerms-Gruppe hinzugefügt werden?

# Ganze Gruppe

/lp group trusted permission set invite.use true# Should players be automatically added to a LuckPerms group?- **Paper API**: 1.21.8-R0.1-SNAPSHOT (aktuelle stabile Version, kompatibel mit 1.21.x)

/lp user Steve parent add trusted

```# true = Aktiviert / Enabled | false = Deaktiviert / Disabled- **LuckPerms API**: 5.4 (aktuelle stabile Version)



#### Für Administratorenluckperms:- **Java**: 21 (erforderlich für Paper 1.21.x)



```bash  enabled: true- **Maven Compiler Plugin**: 3.13.0

# Admins bekommen beide Berechtigungen

/lp group admin permission set invite.use true  - **Maven Shade Plugin**: 3.6.0

/lp group admin permission set invite.admin true

```  # Name der Standard-Gruppe, die neue Spieler erhalten sollen



#### Für Moderatoren  # Name of the default group that new players should receive## Support



```bash  # Diese Gruppe muss in LuckPerms existieren!

# Moderatoren können nur einladen, nicht entfernen

/lp group moderator permission set invite.use true  # This group must exist in LuckPerms!Bei Problemen oder Fragen:

```

  # Beispiele / Examples: default, member, spieler, newbie1. Überprüfe die Server-Logs (`logs/latest.log`)

---

  default-group: "default"2. Stelle sicher, dass LuckPerms korrekt installiert ist

## 📦 Download

```3. Überprüfe die Berechtigungen mit `/lp user <username> permission check invite.use`

### Aktuelle Version



**Version**: 0.0.2

### Konfigurationsoptionen## Lizenz

**Download**: [GitHub Releases](https://github.com/2g4y1/better-whitelist/releases)



### Changelog

#### 🌐 `language`Dieses Plugin wurde für private Zwecke erstellt. Frei verwendbar und anpassbar.

#### Version 0.0.2

- ➕ Mehrsprachigkeit hinzugefügt (Deutsch/Englisch)

- ➕ Konfigurationsdatei mit Sprachauswahl

- ➕ LuckPerms-Integration ist nun optional konfigurierbar- **Typ**: String## Changelog

- ➕ Standard-Gruppe ist nun konfigurierbar

- 🔄 LuckPerms ist nun eine optionale Abhängigkeit (`softdepend` statt `depend`)- **Standard**: `"de"`

- 📝 README komplett überarbeitet

- **Optionen**: `"de"` (Deutsch), `"en"` (English)### Version 0.0.2

#### Version 0.0.1

- 🎉 Erstes Release- **Beschreibung**: Legt die Sprache für alle Plugin-Nachrichten fest- Plugin umbenannt zu BetterWhitelist

- ✨ `/invite`-Befehl mit Whitelist-Funktion

- ✨ `/uninvite`-Befehl zum Entfernen- Konfigurationsdatei hinzugefügt (`config.yml`)

- ✨ Automatische LuckPerms-Gruppenzuweisung

- ✨ Berechtigungssystem#### ⚙️ `luckperms.enabled`- LuckPerms-Integration ist jetzt optional konfigurierbar

- ✨ Broadcast-Nachrichten

- Standard-Gruppe ist jetzt konfigurierbar

---

- **Typ**: Boolean- LuckPerms ist jetzt eine optionale Abhängigkeit (softdepend statt depend)

## 🛠️ Kompilierung

- **Standard**: `true`

### Voraussetzungen

- **Beschreibung**: Aktiviert/Deaktiviert die automatische LuckPerms-Gruppenzuweisung### Version 0.0.1

- **Java 21** (erforderlich für Paper 1.21.x)

- **Maven 3.6+**- **Hinweis**: Bei `false` werden Spieler nur zur Whitelist hinzugefügt, ohne Gruppenänderungen- Initiales Release als InvitePlugin



### Mit Maven- `/invite` Command mit Whitelist-Funktion



```bash#### 👥 `luckperms.default-group`- `/uninvite` Command zum Entfernen

mvn clean package

```- Automatische LuckPerms-Gruppenzuweisung



Die fertige JAR-Datei findest du dann im `target/`-Ordner.- **Typ**: String- Berechtigungssystem



### Mit Docker (falls Java 21 nicht verfügbar)- **Standard**: `"default"`- Broadcast-Nachrichten



```bash- **Beschreibung**: Name der Gruppe, die eingeladene Spieler erhalten

docker run --rm -v "$(pwd)":/app -w /app maven:3.9-eclipse-temurin-21 mvn clean package- **Wichtig**: Diese Gruppe muss in LuckPerms existieren!

```

### Gruppe in LuckPerms erstellen

---

Falls die konfigurierte Gruppe noch nicht existiert:

## 🐛 Troubleshooting

```bash

### Problem: "LuckPerms nicht gefunden"# Gruppe erstellen

/lp creategroup meingruppe

**Lösung**:

1. Prüfe, ob LuckPerms installiert ist: `/plugins`# Berechtigungen setzen

2. Falls nicht benötigt, in `config.yml` setzen: `luckperms.enabled: false`/lp group meingruppe permission set minecraft.command.help true

```

### Problem: "Gruppe existiert nicht in LuckPerms"

Überprüfe verfügbare Gruppen mit:

**Lösung**:

1. Verfügbare Gruppen prüfen: `/lp listgroups````bash

2. Gruppe erstellen: `/lp creategroup <name>`/lp listgroups

3. Oder `luckperms.default-group` in `config.yml` ändern```



### Problem: "Spieler nicht gefunden"---



**Lösung**:## 🎮 Commands

- Stelle sicher, dass der Spielername korrekt geschrieben ist

- Der Spieler muss ein gültiger Minecraft-Account sein### `/invite <Spielername>`

- Prüfe die Mojang-API-Erreichbarkeit

**Beschreibung**: Lädt einen Spieler auf den Server ein

### Problem: Berechtigungen funktionieren nicht

**Was passiert:**

**Lösung**:- ✅ Spieler wird zur Whitelist hinzugefügt

```bash- ✅ (Optional) Spieler erhält die konfigurierte LuckPerms-Gruppe

# Berechtigung prüfen- ✅ Broadcast an alle Spieler mit `invite.use` Berechtigung

/lp user <username> permission check invite.use

**Berechtigung**: `invite.use`

# Berechtigung setzen

/lp user <username> permission set invite.use true**Beispiele:**

``````bash

/invite Steve

---/invite Alex

/invite Notch

## 📞 Support```



Bei Problemen oder Fragen:**Ausgabe:**

```

1. 📖 Schaue in den [Troubleshooting](#-troubleshooting)-Bereich✓ Spieler Steve wurde erfolgreich eingeladen!

2. 🔍 Sieh in die Server-Logs (`logs/latest.log`)→ Whitelist: Aktiviert

3. 🐛 Erstelle ein [GitHub Issue](https://github.com/2g4y1/better-whitelist/issues)→ LuckPerms-Gruppe: default

```

---

---

## 📄 Lizenz

### `/uninvite <Spielername>`

Dieses Plugin wurde für private Zwecke erstellt. Frei nutzbar und anpassbar.

**Beschreibung**: Entfernt einen Spieler von der Whitelist

---

**Was passiert:**

## 🙏 Verwendete APIs- ❌ Spieler wird von der Whitelist entfernt

- 📢 Broadcast an alle Spieler mit `invite.admin` Berechtigung

- **Paper API**: 1.21.10-R0.1-SNAPSHOT

- **LuckPerms API**: 5.4**Berechtigung**: `invite.admin`

- **Adventure API**: Für moderne Text-Komponenten

- **Mojang API**: Für UUID-Abfragen**Beispiele:**

```bash

---/uninvite Steve

/uninvite Alex

<div align="center">```



**Entwickelt mit ❤️ für die Minecraft-Community****Ausgabe:**

```

[⬆ Zurück nach oben](#-betterwhitelist)✓ Spieler Steve wurde von der Whitelist entfernt!

```

</div>

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
