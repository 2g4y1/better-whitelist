# BetterWhitelist - Feature Overview

## Plugin Architecture

### Core Components

1. **BetterWhitelistPlugin** (Main Class)
   - Entry point for the plugin
   - Handles plugin lifecycle (enable/disable)
   - Manages auto-save scheduler
   - Provides utility methods for message formatting

2. **WhitelistManager** (Data Management)
   - Manages whitelist entries in memory
   - Handles data persistence (load/save to YAML)
   - Provides API for whitelist operations
   - Auto-cleanup of expired temporary entries

3. **WhitelistCommand** (Command Handler)
   - Processes all whitelist commands
   - Implements tab completion
   - Permission checking
   - User-friendly command interface

4. **PlayerJoinListener** (Event Handler)
   - Intercepts player login events
   - Enforces whitelist restrictions
   - Shows custom kick messages

5. **WhitelistEntry** (Data Model)
   - Stores whitelist entry information
   - Handles temporary whitelist logic

## Features in Detail

### 1. Basic Whitelist Management
- **Add Players**: `/bwhitelist add <player> [reason]`
  - Add players with optional reason
  - Tracks who added the player
  - Records timestamp
  
- **Remove Players**: `/bwhitelist remove <player>`
  - Remove players from whitelist
  - Instant effect

- **List Players**: `/bwhitelist list`
  - Shows all whitelisted players
  - Indicates temporary entries
  - Shows total count

### 2. Temporary Whitelist
- **Duration Formats**: 30m, 2h, 1d, 7d, etc.
- **Auto-Expiration**: Entries automatically expire
- **Background Cleanup**: Runs every minute
- **Visual Indicators**: Temporary entries marked in list

### 3. Player Information
- **Detailed Info**: `/bwhitelist info <player>`
  - Player name and UUID
  - Who added them
  - When they were added
  - Reason for whitelisting
  - Expiration time (for temporary entries)
  - Time remaining

### 4. Whitelist Toggle
- **Enable**: `/bwhitelist on`
- **Disable**: `/bwhitelist off`
- **Check Status**: `/bwhitelist check`
- **Persistent**: Setting saved to config

### 5. Configuration System
- **Customizable Messages**: All messages configurable
- **Kick Message**: Custom message for non-whitelisted players
- **Message Prefix**: Customizable plugin prefix
- **Date Format**: Configurable timestamp format
- **Auto-Save**: Configurable interval or disable

### 6. Permission System
- **Granular Permissions**: Separate permission for each action
- **Default Values**: Sensible defaults (ops get all, players get check)
- **Wildcard Support**: `betterwhitelist.*` for all permissions

### 7. Data Persistence
- **YAML Storage**: Human-readable whitelist.yml
- **Automatic Saving**: Configurable auto-save
- **Safe Operations**: Always saves after changes
- **UUID-Based**: Uses player UUIDs for reliability

### 8. User Experience
- **Tab Completion**: 
  - Command suggestions
  - Player name suggestions
  - Duration format suggestions
- **Color Support**: Minecraft color codes (& format)
- **Command Aliases**: /bwhitelist, /bwl, /bw
- **Help Command**: Built-in help system
- **Error Messages**: Clear, helpful error messages

## Technical Details

### Performance
- **Async Operations**: Auto-save runs asynchronously
- **Efficient Storage**: Map-based in-memory storage
- **Minimal Impact**: Lightweight event handling

### Compatibility
- **Paper 1.21.x**: Built for Paper API
- **Java 21**: Modern Java features
- **Maven Build**: Standard build system

### Data Format

**whitelist.yml Structure:**
```yaml
entries:
  '<player-uuid>':
    name: "PlayerName"
    addedBy: "AdminName"
    addedDate: 1234567890123
    reason: "Good builder"
    expirationDate: 1234567890123  # Optional, for temporary entries
```

### Command Flow

1. Player executes command
2. Permission check
3. Argument validation
4. Operation execution
5. WhitelistManager updates data
6. Automatic save
7. Feedback message to player

### Whitelist Enforcement

1. Player attempts to join
2. PlayerLoginEvent fired
3. Plugin checks if whitelist enabled
4. Plugin checks if player whitelisted
5. If temporary, checks expiration
6. Allows or denies login
7. Shows custom kick message if denied

## Future Enhancement Possibilities

While not currently implemented, the architecture supports:
- Database storage (MySQL, PostgreSQL)
- Web API integration
- Discord integration
- Import from vanilla whitelist
- Export to vanilla whitelist
- Group-based whitelist
- Whitelist requests system
- Approval workflow
- Audit logging
- Statistics tracking

## Code Quality

- **Clean Architecture**: Separation of concerns
- **Type Safety**: Strong typing throughout
- **Error Handling**: Proper exception handling
- **Documentation**: Comments where needed
- **Maintainability**: Easy to extend and modify
