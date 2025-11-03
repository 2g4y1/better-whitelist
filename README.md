# BetterWhitelist

A comprehensive whitelist management plugin for Minecraft Paper 1.21.x servers with player-friendly features.

## Features

### 🎯 Core Features
- **Enhanced Whitelist Management**: Add, remove, and manage whitelisted players with ease
- **Temporary Whitelist**: Set time-based whitelist entries that automatically expire
- **Player Information**: Store and view reasons for whitelisting and who added each player
- **Multiple Commands**: Full-featured command system with aliases (`/bwhitelist`, `/bwl`, `/bw`)
- **Permission System**: Granular permissions for different operations
- **Auto-Save**: Configurable automatic saving of whitelist data
- **Player-Friendly**: Easy-to-use commands for both admins and players

### 📋 Commands

All commands can be accessed via `/bwhitelist`, `/bwl`, or `/bw`:

| Command | Description | Permission |
|---------|-------------|------------|
| `/bwhitelist add <player> [reason]` | Add a player to the whitelist | `betterwhitelist.add` |
| `/bwhitelist remove <player>` | Remove a player from the whitelist | `betterwhitelist.remove` |
| `/bwhitelist list` | List all whitelisted players | `betterwhitelist.list` |
| `/bwhitelist on` | Enable the whitelist | `betterwhitelist.on` |
| `/bwhitelist off` | Disable the whitelist | `betterwhitelist.off` |
| `/bwhitelist check` | Check if whitelist is enabled | `betterwhitelist.check` |
| `/bwhitelist temp <player> <duration> [reason]` | Add temporary whitelist entry | `betterwhitelist.temp` |
| `/bwhitelist info <player>` | View detailed info about a whitelisted player | `betterwhitelist.info` |
| `/bwhitelist reload` | Reload the configuration | `betterwhitelist.reload` |
| `/bwhitelist help` | Show help message | `betterwhitelist.use` |

### ⏱️ Duration Format

For temporary whitelist entries, use these formats:
- `30m` - 30 minutes
- `2h` - 2 hours
- `1d` - 1 day
- `7d` - 7 days

### 🔐 Permissions

- `betterwhitelist.*` - All permissions (default: op)
- `betterwhitelist.use` - Basic command usage (default: true)
- `betterwhitelist.add` - Add players to whitelist (default: op)
- `betterwhitelist.remove` - Remove players from whitelist (default: op)
- `betterwhitelist.list` - List whitelisted players (default: op)
- `betterwhitelist.on` - Enable whitelist (default: op)
- `betterwhitelist.off` - Disable whitelist (default: op)
- `betterwhitelist.check` - Check whitelist status (default: true)
- `betterwhitelist.temp` - Add temporary entries (default: op)
- `betterwhitelist.info` - View player info (default: op)
- `betterwhitelist.reload` - Reload config (default: op)

## Installation

1. Download the latest release from the [Releases](https://github.com/2g4y1/better-whitelist/releases) page
2. Place the `BetterWhitelist-1.0.0.jar` file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin by editing `plugins/BetterWhitelist/config.yml`

## Configuration

The plugin generates a `config.yml` file with the following options:

```yaml
# Enable or disable the whitelist
whitelist-enabled: true

# Message shown to non-whitelisted players when they try to join
kick-message: "&cYou are not whitelisted on this server!"

# Prefix for all plugin messages
prefix: "&8[&6BetterWhitelist&8]&r "

# Auto-save interval in minutes (set to 0 to disable)
auto-save-interval: 5

# Date format for timestamps
date-format: "yyyy-MM-dd HH:mm:ss"
```

All messages can be customized in the configuration file.

## Building from Source

See [BUILD.md](BUILD.md) for detailed build instructions.

### Quick Build

```bash
mvn clean package
```

The compiled JAR will be in the `target` directory.

## Examples

### Add a player to the whitelist
```
/bwhitelist add Steve Because he's a good builder
```

### Add a temporary whitelist for 2 hours
```
/bwhitelist temp Alex 2h Testing the server
```

### Check who added a player and why
```
/bwhitelist info Steve
```

### List all whitelisted players
```
/bwhitelist list
```

### Toggle whitelist on/off
```
/bwhitelist on
/bwhitelist off
```

## Data Storage

Player whitelist data is stored in `plugins/BetterWhitelist/whitelist.yml` with the following information:
- Player UUID and name
- Who added them
- When they were added
- Reason for whitelisting
- Expiration date (for temporary entries)

## Requirements

- Minecraft Server: Paper 1.21.x (or compatible forks)
- Java: 21 or higher

## License

This project is open source. Feel free to use, modify, and distribute.

## Support

For issues, feature requests, or questions, please open an issue on the [GitHub repository](https://github.com/2g4y1/better-whitelist/issues).

## Credits

Developed by 2g4y1