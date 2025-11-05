# 🎮 BetterWhitelist

<div align="center">

**Let players invite friends to your whitelisted Minecraft server**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-green.svg)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

[🇩🇪 Deutsche Version](README.md)

</div>

---

## 📖 About

**BetterWhitelist** allows trusted players to invite friends without admin intervention.

### ✨ Features

- 🤝 Players can invite friends independently
- 🚫 Players are automatically kicked when removed from whitelist
- 🔒 Permission-based security
- 🌍 German and English support
- ⚡ Async processing (no lag)
- 🎯 Optional LuckPerms group assignment

---

## 📥 Installation

### Prerequisites

- Paper/Purpur/Paper fork (1.21.x)
- Java 21+
- Optional: LuckPerms

### Setup

1. Download `BetterWhitelist-x.x.x.jar` from [Releases](https://github.com/2g4y1/better-whitelist/releases)
2. Copy to `plugins/` folder
3. Restart server
4. Configure `plugins/BetterWhitelist/config.yml`
5. Set permissions:

```bash
/lp group trusted permission set invite.use true
/lp group admin permission set invite.admin true
```

---

## ⚙️ Configuration

Config file: `plugins/BetterWhitelist/config.yml` (auto-created)

```yaml
language: "en"  # de or en

luckperms:
  enabled: false              # Auto-assign groups?
  default-group: "default"    # Group name for new players
```

**Note**: If `luckperms.enabled: true`, the group must exist in LuckPerms!

---

## 🎮 Commands

| Command | Permission | Description |
|---------|-----------|-------------|
| `/invite <player>` | `invite.use` | Add player to whitelist |
| `/uninvite <player>` | `invite.admin` | Remove player from whitelist (kicks if online) |

**Tab Completion**: Both commands support tab completion for player names.

---

## 🔐 Permissions

| Permission | Description |
|-----------|-------------|
| `invite.use` | Invite players |
| `invite.admin` | Remove players |

---

## 📦 Download

**Latest Version**: [GitHub Releases](https://github.com/2g4y1/better-whitelist/releases)

---

## 🛠️ Build from Source

```bash
mvn clean package
```

Or with Docker:

```bash
docker run --rm -v "$(pwd)":/app -w /app maven:3.9-eclipse-temurin-21 mvn clean package
```

---

## 📄 License

MIT License - Free to use and modify.

---

<div align="center">

**Made with ❤️ for the Minecraft Community**

[⬆ Back to Top](#-betterwhitelist)

</div>
