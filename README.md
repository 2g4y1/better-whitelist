# 🎮 BetterWhitelist

<div align="center">

**Finally, your players can invite friends themselves!**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-green.svg)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

[Features](#-features) • [Quick Start](#-quick-start) • [Commands](#-commands) • [Configuration](#%EF%B8%8F-configuration) • [Download](#-download)

</div>

---

## 📖 The Problem

On most whitelisted servers, players always have to contact an admin when they want to invite a friend. That's annoying and time-consuming.

## 💡 The Solution

BetterWhitelist gives trusted players the ability to independently add friends to the whitelist – with a single command!

---

## ✨ Features

- 🎫 **Simple Whitelist Management** - `/invite <player>` is all you need
- 🚫 **Auto-Kick on Removal** - Players are kicked when uninvited
- 📊 **Invite Tracking & Limits** - Track who invited whom, configurable max invites (default: 5)
- 📋 **Statistics & Management** - View invites, remaining slots, admin tools
- ⚡ **Mutual Boost System** - XP boost when mutually invited players are nearby
- 🔄 **Hot Reload** - Update config without restart
- 🔐 **Permission System** - Secure role-based access control
- 👥 **LuckPerms Integration** - Optional automatic group assignment
- 🌍 **Multilingual** - German and English support
- ⚙️ **High Performance** - Async operations, persistent JSON storage

---

## 🚀 Quick Start

1. Download from [Releases](https://github.com/2g4y1/better-whitelist/releases)
2. Copy plugin to `plugins/` folder
3. Start server (config is created automatically)
4. Grant permissions: `/lp group trusted permission set invite.use true`
5. Done! Players can now use `/invite <name>`

---

## 📋 Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/invite <player>` | `invite.use` | Invites a player to the whitelist |
| `/invite <player> bedrock` | `invite.use` | Invites a floodgate player to the whitelist (requires floodgate to be anbled in config) |
| `/uninvite <player>` | `invite.admin` | Removes a player (kicks if online) |
| `/invitelist [player]` | `invite.use` | Shows your invites and remaining slots |
| `/invitelist <player>` | `invite.admin` | View another player's invites |
| `/betterwhitelist reload` | `invite.admin` | Reloads config and invite data |

**Aliases:** `/invitelist` → `/ilist`, `/invites` • `/betterwhitelist` → `/bwl`, `/bwhitelist`

---

## 🔐 Permissions

| Permission | Description | Default |
|-----------|-------------|---------|
| `invite.use` | Invite and view own invites | OP |
| `invite.admin` | Unlimited invites, manage all players | OP |

---

## ⚙️ Configuration

```yaml
language: "en"         # "en" or "de"
max-invites: 5         # Max invites per player (admins unlimited)

luckperms:
  enabled: false
  default-group: "default"

# Mutual Boost System - XP bonus when invited friends play together
mutual-invite-boost:
  enabled: true
  xp-multiplier: 1.5   # 50% XP bonus
  xp-share: 0.25       # Share 25% XP with nearby partner
  radius:
    overworld: 50      # Block radius per dimension
    nether: 75
    end: 100
```

---

## 📊 Invite System

Players can invite up to 5 friends (configurable). Use `/invitelist` to see your invites and remaining slots. When you uninvite someone, the slot becomes available again. All invites are tracked with timestamps in `invites.json`.

**Admin Features:** View any player's invites, unlimited invites, see top inviters from console.

### ⚡ Mutual Boost System

When two players have invited each other (mutual invites) and are within range:
- 🎯 **XP Boost** - Configurable XP multiplier (default: 1.5x)
- 🤝 **XP Sharing** - Share part of your XP gains with your partner
- ✨ **Visual Effects** - Particle effects and actionbar notifications
- 🌍 **Per-Dimension Radius** - Different ranges for Overworld, Nether, and End

---

## 📥 Download

[**GitHub Releases**](https://github.com/2g4y1/better-whitelist/releases)

---

## 🛠️ Build from Source

```bash
mvn clean package
```

---

## 📄 License

MIT License - Free to use and modify.

---

<div align="center">

**Made with ❤️ for the Minecraft Community**

[⬆ Back to Top](#-betterwhitelist)

</div>
