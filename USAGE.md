# BetterWhitelist - Usage Guide

## Getting Started

### First Time Setup

1. **Install the Plugin**
   - Place `BetterWhitelist-1.0.0.jar` in your `plugins` folder
   - Restart your server
   - Plugin will generate default configuration files

2. **Initial Configuration**
   - Navigate to `plugins/BetterWhitelist/`
   - Edit `config.yml` to customize messages and settings
   - Use `/bwhitelist reload` to apply changes without restart

3. **Enable Whitelist**
   ```
   /bwhitelist on
   ```

## Common Use Cases

### Managing Regular Players

#### Adding a Player
```
/bwhitelist add Steve
/bwhitelist add Steve Good builder and active player
```

#### Removing a Player
```
/bwhitelist remove Steve
```

#### Viewing Player Information
```
/bwhitelist info Steve
```
Output example:
```
[BetterWhitelist] Player Information:
Player: Steve
Added by: Admin
Added on: 2025-11-03 12:00:00
Reason: Good builder and active player
```

### Managing Temporary Access

#### Give Someone Trial Access
```
/bwhitelist temp Alex 24h Trial period
```

#### Guest Access for a Few Hours
```
/bwhitelist temp Guest123 3h Visiting friend
```

#### Event Participant Access
```
/bwhitelist temp EventPlayer 7d Building competition participant
```

### Checking Whitelist Status

#### View All Whitelisted Players
```
/bwhitelist list
```
Output example:
```
[BetterWhitelist] Whitelisted players (5):
- Steve
- Alex [TEMP]
- Builder1
- ModeratorJohn
- Guest123 [TEMP]
```

#### Check if Whitelist is Active
```
/bwhitelist check
```
Output: `[BetterWhitelist] Whitelist is currently: ENABLED`

### Whitelist Control

#### Turn Whitelist On
```
/bwhitelist on
```

#### Turn Whitelist Off (Allow Everyone)
```
/bwhitelist off
```

## Advanced Usage

### Duration Format Examples

| Format | Duration | Use Case |
|--------|----------|----------|
| `30m` | 30 minutes | Quick test access |
| `1h` | 1 hour | Short visit |
| `2h` | 2 hours | Showing the server to a friend |
| `6h` | 6 hours | Extended trial |
| `12h` | 12 hours | Half-day access |
| `1d` | 1 day | Daily trial |
| `3d` | 3 days | Weekend access |
| `7d` | 7 days | Week trial |
| `30d` | 30 days | Month access |

### Permission Configuration

#### For LuckPerms
```
# Give player basic whitelist info viewing
lp user <player> permission set betterwhitelist.check true
lp user <player> permission set betterwhitelist.info true

# Give moderator whitelist management
lp user <moderator> permission set betterwhitelist.add true
lp user <moderator> permission set betterwhitelist.remove true
lp user <moderator> permission set betterwhitelist.list true
lp user <moderator> permission set betterwhitelist.temp true

# Give admin all permissions
lp user <admin> permission set betterwhitelist.* true
```

#### For PermissionsEx
```
pex user <player> add betterwhitelist.check
pex user <player> add betterwhitelist.info

pex user <moderator> add betterwhitelist.add
pex user <moderator> add betterwhitelist.remove
pex user <moderator> add betterwhitelist.list
pex user <moderator> add betterwhitelist.temp

pex user <admin> add betterwhitelist.*
```

### Configuration Customization

#### Custom Kick Message
Edit `config.yml`:
```yaml
kick-message: "&c&lACCESS DENIED\n&7Please apply for whitelist at:\n&ewww.yourserver.com/apply"
```

#### Custom Plugin Prefix
```yaml
prefix: "&8[&b&lWL&8]&r "
```

#### Adjust Auto-Save
```yaml
# Save every 10 minutes
auto-save-interval: 10

# Disable auto-save (only saves on plugin disable and after changes)
auto-save-interval: 0
```

#### Change Date Format
```yaml
# US format
date-format: "MM/dd/yyyy hh:mm a"

# European format
date-format: "dd.MM.yyyy HH:mm"

# ISO format
date-format: "yyyy-MM-dd'T'HH:mm:ss"
```

### Command Aliases

All these commands are equivalent:
```
/bwhitelist list
/bwl list
/bw list
```

### Tab Completion

The plugin provides smart tab completion:

1. **Command Suggestions**: Type `/bwhitelist ` and press TAB
   - Shows: add, remove, list, on, off, check, temp, info, reload, help

2. **Player Name Suggestions**: 
   - For add/temp: Shows online players
   - For remove/info: Shows whitelisted players

3. **Duration Suggestions**: Type `/bwhitelist temp Steve ` and press TAB
   - Shows: 30m, 1h, 2h, 6h, 12h, 1d, 7d

## Troubleshooting

### Player Can't Join

1. **Check if whitelist is enabled**:
   ```
   /bwhitelist check
   ```

2. **Check if player is whitelisted**:
   ```
   /bwhitelist list
   ```

3. **Check player information**:
   ```
   /bwhitelist info PlayerName
   ```

4. **If temporary, check if expired**: Look at the info output

### Temporary Whitelist Not Expiring

- The cleanup task runs every minute
- Check server console for errors
- Verify the expiration date with `/bwhitelist info <player>`
- Restart the plugin with `/bwhitelist reload`

### Changes Not Saving

1. Check file permissions on `plugins/BetterWhitelist/` folder
2. Check console for save errors
3. Verify auto-save is enabled in config
4. Manually trigger save by running any whitelist command

### Command Not Working

1. **Check permissions**: Ensure you have the required permission
2. **Check syntax**: Use `/bwhitelist help` for correct usage
3. **Check console**: Look for error messages
4. **Try command aliases**: Use `/bwl` or `/bw` instead

## Best Practices

### 1. Regular Backups
Backup `plugins/BetterWhitelist/whitelist.yml` regularly:
```bash
cp plugins/BetterWhitelist/whitelist.yml plugins/BetterWhitelist/whitelist.yml.backup
```

### 2. Clear Reasons
Always provide clear reasons when adding players:
```
✓ Good: /bwhitelist add Steve Applied on website, verified by staff
✗ Bad:  /bwhitelist add Steve
```

### 3. Use Temporary Whitelist for Trials
Instead of permanently adding unknown players:
```
/bwhitelist temp NewPlayer 3d Trial member
```

### 4. Document Your Decisions
Use the info command to check why players were added:
```
/bwhitelist info PlayerName
```

### 5. Regular Cleanup
Periodically review your whitelist:
```
/bwhitelist list
```
Remove inactive players as needed.

### 6. Permission Delegation
Don't give everyone full whitelist permissions:
- Admins: `betterwhitelist.*`
- Moderators: `add`, `remove`, `temp`, `list`, `info`
- Regular players: `check`

## Integration Examples

### With Discord Bot
When player applies on Discord:
```
/bwhitelist add <player> Applied via Discord on YYYY-MM-DD
```

### With Website
When player completes application:
```
/bwhitelist add <player> Application approved by <reviewer> - Reference: APP-12345
```

### For Events
Before tournament starts:
```
/bwhitelist temp Player1 3d Tournament participant - Event ID: T2025-001
```

## Migration

### From Vanilla Whitelist
1. Note all players from vanilla whitelist
2. Add them to BetterWhitelist with appropriate reasons
3. Keep vanilla whitelist disabled
4. Let BetterWhitelist handle all whitelist functionality

### To Vanilla Whitelist
If you need to revert:
1. Use `/bwhitelist list` to get all players
2. Add them to vanilla whitelist with `/whitelist add <player>`
3. Remove the plugin

## Support

For issues or questions:
1. Check this guide
2. Review `FEATURES.md` for detailed feature information
3. Check `BUILD.md` for building from source
4. Open an issue on GitHub: https://github.com/2g4y1/better-whitelist/issues
