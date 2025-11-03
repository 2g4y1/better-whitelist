package com.betterwhitelist.commands;

import com.betterwhitelist.BetterWhitelistPlugin;
import com.betterwhitelist.data.WhitelistEntry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WhitelistCommand implements CommandExecutor, TabCompleter {
    
    private final BetterWhitelistPlugin plugin;
    
    public WhitelistCommand(BetterWhitelistPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "add":
                return handleAdd(sender, args);
            case "remove":
                return handleRemove(sender, args);
            case "list":
                return handleList(sender, args);
            case "on":
                return handleOn(sender, args);
            case "off":
                return handleOff(sender, args);
            case "check":
                return handleCheck(sender, args);
            case "temp":
                return handleTemp(sender, args);
            case "info":
                return handleInfo(sender, args);
            case "reload":
                return handleReload(sender, args);
            case "help":
                sendHelp(sender);
                return true;
            default:
                sender.sendMessage(plugin.getMessage("usage").replace("%usage%", "/bwhitelist help"));
                return true;
        }
    }
    
    private boolean handleAdd(CommandSender sender, String[] args) {
        if (!sender.hasPermission("betterwhitelist.add")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessage("usage").replace("%usage%", "/bwhitelist add <player> [reason]"));
            return true;
        }
        
        String playerName = args[1];
        String reason = "No reason provided";
        
        if (args.length > 2) {
            reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        }
        
        // Try to find player
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        String addedBy = sender instanceof Player ? sender.getName() : "Console";
        
        if (plugin.getWhitelistManager().addPlayer(target.getUniqueId(), target.getName(), addedBy, reason)) {
            sender.sendMessage(plugin.getMessage("player-added").replace("%player%", target.getName()));
        } else {
            sender.sendMessage(plugin.getMessage("player-already-whitelisted").replace("%player%", target.getName()));
        }
        
        return true;
    }
    
    private boolean handleRemove(CommandSender sender, String[] args) {
        if (!sender.hasPermission("betterwhitelist.remove")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessage("usage").replace("%usage%", "/bwhitelist remove <player>"));
            return true;
        }
        
        String playerName = args[1];
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        
        if (plugin.getWhitelistManager().removePlayer(target.getUniqueId())) {
            sender.sendMessage(plugin.getMessage("player-removed").replace("%player%", target.getName()));
        } else {
            sender.sendMessage(plugin.getMessage("player-not-whitelisted").replace("%player%", target.getName()));
        }
        
        return true;
    }
    
    private boolean handleList(CommandSender sender, String[] args) {
        if (!sender.hasPermission("betterwhitelist.list")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }
        
        List<WhitelistEntry> entries = plugin.getWhitelistManager().getAllEntries();
        
        if (entries.isEmpty()) {
            sender.sendMessage(plugin.colorize(plugin.getConfig().getString("prefix") + "&7No players are whitelisted."));
            return true;
        }
        
        sender.sendMessage(plugin.colorize(plugin.getConfig().getString("prefix") + "&7Whitelisted players (&e" + entries.size() + "&7):"));
        
        for (WhitelistEntry entry : entries) {
            String tempIndicator = entry.isTemporary() ? " &8[TEMP]" : "";
            sender.sendMessage(plugin.colorize("&7- &e" + entry.getPlayerName() + tempIndicator));
        }
        
        return true;
    }
    
    private boolean handleOn(CommandSender sender, String[] args) {
        if (!sender.hasPermission("betterwhitelist.on")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }
        
        plugin.getWhitelistManager().setWhitelistEnabled(true);
        sender.sendMessage(plugin.getMessage("whitelist-enabled"));
        
        return true;
    }
    
    private boolean handleOff(CommandSender sender, String[] args) {
        if (!sender.hasPermission("betterwhitelist.off")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }
        
        plugin.getWhitelistManager().setWhitelistEnabled(false);
        sender.sendMessage(plugin.getMessage("whitelist-disabled"));
        
        return true;
    }
    
    private boolean handleCheck(CommandSender sender, String[] args) {
        if (!sender.hasPermission("betterwhitelist.check")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }
        
        boolean enabled = plugin.getWhitelistManager().isWhitelistEnabled();
        String status = enabled ? "&aENABLED" : "&cDISABLED";
        sender.sendMessage(plugin.getMessage("whitelist-status").replace("%status%", status));
        
        return true;
    }
    
    private boolean handleTemp(CommandSender sender, String[] args) {
        if (!sender.hasPermission("betterwhitelist.temp")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }
        
        if (args.length < 3) {
            sender.sendMessage(plugin.getMessage("usage").replace("%usage%", "/bwhitelist temp <player> <duration> [reason]"));
            sender.sendMessage(plugin.colorize("&7Duration format: <number>m (minutes), <number>h (hours), <number>d (days)"));
            return true;
        }
        
        String playerName = args[1];
        String durationStr = args[2];
        String reason = "Temporary access";
        
        if (args.length > 3) {
            reason = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
        }
        
        // Parse duration
        long durationMinutes;
        try {
            char unit = durationStr.charAt(durationStr.length() - 1);
            int value = Integer.parseInt(durationStr.substring(0, durationStr.length() - 1));
            
            switch (unit) {
                case 'm':
                    durationMinutes = value;
                    break;
                case 'h':
                    durationMinutes = value * 60L;
                    break;
                case 'd':
                    durationMinutes = value * 60L * 24L;
                    break;
                default:
                    sender.sendMessage(plugin.colorize("&cInvalid duration format! Use: 30m, 2h, or 1d"));
                    return true;
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            sender.sendMessage(plugin.colorize("&cInvalid duration format! Use: 30m, 2h, or 1d"));
            return true;
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        String addedBy = sender instanceof Player ? sender.getName() : "Console";
        
        if (plugin.getWhitelistManager().addPlayerTemporary(target.getUniqueId(), target.getName(), addedBy, reason, durationMinutes)) {
            String duration = formatDuration(durationMinutes);
            sender.sendMessage(plugin.getMessage("temp-whitelist-added")
                .replace("%player%", target.getName())
                .replace("%duration%", duration));
        } else {
            sender.sendMessage(plugin.getMessage("player-already-whitelisted").replace("%player%", target.getName()));
        }
        
        return true;
    }
    
    private boolean handleInfo(CommandSender sender, String[] args) {
        if (!sender.hasPermission("betterwhitelist.info")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessage("usage").replace("%usage%", "/bwhitelist info <player>"));
            return true;
        }
        
        String playerName = args[1];
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        WhitelistEntry entry = plugin.getWhitelistManager().getEntry(target.getUniqueId());
        
        if (entry == null) {
            sender.sendMessage(plugin.getMessage("player-not-whitelisted").replace("%player%", target.getName()));
            return true;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(plugin.getConfig().getString("date-format", "yyyy-MM-dd HH:mm:ss"));
        String dateStr = dateFormat.format(entry.getAddedDate());
        
        String message = plugin.getMessageNoPrefix("player-info")
            .replace("%player%", entry.getPlayerName())
            .replace("%added_by%", entry.getAddedBy())
            .replace("%date%", dateStr)
            .replace("%reason%", entry.getReason());
        
        sender.sendMessage(plugin.colorize(plugin.getConfig().getString("prefix") + "&7Player Information:"));
        sender.sendMessage(message);
        
        if (entry.isTemporary()) {
            String expirationStr = dateFormat.format(entry.getExpirationDate());
            sender.sendMessage(plugin.colorize("&7Expiration: &e" + expirationStr));
            
            long remainingMinutes = (entry.getExpirationDate().getTime() - System.currentTimeMillis()) / (60 * 1000);
            sender.sendMessage(plugin.colorize("&7Time remaining: &e" + formatDuration(remainingMinutes)));
        }
        
        return true;
    }
    
    private boolean handleReload(CommandSender sender, String[] args) {
        if (!sender.hasPermission("betterwhitelist.reload")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }
        
        plugin.reloadConfig();
        sender.sendMessage(plugin.getMessage("config-reloaded"));
        
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(plugin.colorize("&8&m----------&r &6BetterWhitelist Help &8&m----------"));
        sender.sendMessage(plugin.colorize("&e/bwhitelist add <player> [reason] &7- Add a player to whitelist"));
        sender.sendMessage(plugin.colorize("&e/bwhitelist remove <player> &7- Remove a player from whitelist"));
        sender.sendMessage(plugin.colorize("&e/bwhitelist list &7- List all whitelisted players"));
        sender.sendMessage(plugin.colorize("&e/bwhitelist on &7- Enable whitelist"));
        sender.sendMessage(plugin.colorize("&e/bwhitelist off &7- Disable whitelist"));
        sender.sendMessage(plugin.colorize("&e/bwhitelist check &7- Check whitelist status"));
        sender.sendMessage(plugin.colorize("&e/bwhitelist temp <player> <duration> [reason] &7- Temporary whitelist"));
        sender.sendMessage(plugin.colorize("&e/bwhitelist info <player> &7- Get player whitelist info"));
        sender.sendMessage(plugin.colorize("&e/bwhitelist reload &7- Reload configuration"));
        sender.sendMessage(plugin.colorize("&8&m--------------------------------------"));
    }
    
    private String formatDuration(long minutes) {
        if (minutes < 60) {
            return minutes + " minute" + (minutes != 1 ? "s" : "");
        } else if (minutes < 1440) {
            long hours = minutes / 60;
            return hours + " hour" + (hours != 1 ? "s" : "");
        } else {
            long days = minutes / 1440;
            return days + " day" + (days != 1 ? "s" : "");
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("add", "remove", "list", "on", "off", "check", "temp", "info", "reload", "help");
            return subCommands.stream()
                .filter(cmd -> cmd.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (args.length == 2 && (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("info"))) {
            // Suggest whitelisted players
            return plugin.getWhitelistManager().getAllEntries().stream()
                .map(WhitelistEntry::getPlayerName)
                .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (args.length == 2 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("temp"))) {
            // Suggest online players
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (args.length == 3 && args[0].equalsIgnoreCase("temp")) {
            // Suggest duration formats
            return Arrays.asList("30m", "1h", "2h", "6h", "12h", "1d", "7d").stream()
                .filter(dur -> dur.startsWith(args[2].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return completions;
    }
}
