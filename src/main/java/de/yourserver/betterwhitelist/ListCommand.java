package de.yourserver.betterwhitelist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListCommand implements CommandExecutor {

    private final BetterWhitelist plugin;
    private static final DateTimeFormatter DATE_FORMAT = 
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.systemDefault());

    public ListCommand(BetterWhitelist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {

        // Permission check
        if (!(sender instanceof org.bukkit.command.ConsoleCommandSender) && !sender.hasPermission("invite.use")) {
            sender.sendMessage(plugin.createMessage(
                plugin.getMessages().get("no_permission"),
                NamedTextColor.RED
            ));
            return true;
        }

        // Wenn Argument gegeben, zeige Invites von einem anderen Spieler (nur für Admins)
        if (args.length > 0) {
            if (!sender.hasPermission("invite.admin")) {
                sender.sendMessage(plugin.createMessage(
                    plugin.getMessages().get("no_permission"),
                    NamedTextColor.RED
                ));
                return true;
            }
            
            // Zeige Invites von einem bestimmten Spieler
            String targetName = args[0];
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                showInvitesForPlayer(sender, targetName);
            });
            return true;
        }

        // Zeige eigene Invites
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                showOwnInvites(player);
            });
        } else {
            // Console zeigt alle Invites
            showAllInvites(sender);
        }

        return true;
    }

    private void showOwnInvites(Player player) {
        List<InviteData.InviteRecord> invites = plugin.getInviteData().getInvites(player.getUniqueId());
        int limit = plugin.getMaxInvites();
        int remaining = limit - invites.size();

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            player.sendMessage(plugin.createMessage(
                plugin.getMessages().get("list.header"),
                NamedTextColor.GOLD
            ));
            
            player.sendMessage(plugin.createMessage(
                plugin.getMessages().get("list.own.count", 
                    "count", invites.size(),
                    "limit", limit),
                NamedTextColor.YELLOW
            ));
            
            player.sendMessage(plugin.createMessage(
                plugin.getMessages().get("list.own.remaining", "remaining", remaining),
                remaining > 0 ? NamedTextColor.GREEN : NamedTextColor.RED
            ));

            if (!invites.isEmpty()) {
                player.sendMessage(plugin.createMessage(
                    plugin.getMessages().get("list.own.invited"),
                    NamedTextColor.GRAY
                ));
                
                for (InviteData.InviteRecord record : invites) {
                    String date = DATE_FORMAT.format(Instant.ofEpochSecond(record.getTimestamp()));
                    player.sendMessage(plugin.createMessage(
                        "  §7- §e" + record.getInvitedName() + " §7(" + date + ")",
                        NamedTextColor.GRAY
                    ));
                }
            } else {
                player.sendMessage(plugin.createMessage(
                    plugin.getMessages().get("list.own.none"),
                    NamedTextColor.GRAY
                ));
            }
            
            player.sendMessage(plugin.createMessage(
                plugin.getMessages().get("list.footer"),
                NamedTextColor.GOLD
            ));
        });
    }

    private void showInvitesForPlayer(CommandSender sender, String targetName) {
        // UUID des Zielspielers holen
        java.util.UUID targetUuid = plugin.getServer().getOfflinePlayer(targetName).getUniqueId();
        List<InviteData.InviteRecord> invites = plugin.getInviteData().getInvites(targetUuid);
        int limit = plugin.getMaxInvites();

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            sender.sendMessage(plugin.createMessage(
                plugin.getMessages().get("list.other.header", "player", targetName),
                NamedTextColor.GOLD
            ));
            
            sender.sendMessage(plugin.createMessage(
                plugin.getMessages().get("list.other.count",
                    "player", targetName,
                    "count", invites.size(),
                    "limit", limit),
                NamedTextColor.YELLOW
            ));

            if (!invites.isEmpty()) {
                for (InviteData.InviteRecord record : invites) {
                    String date = DATE_FORMAT.format(Instant.ofEpochSecond(record.getTimestamp()));
                    sender.sendMessage(plugin.createMessage(
                        "  §7- §e" + record.getInvitedName() + " §7(" + date + ")",
                        NamedTextColor.GRAY
                    ));
                }
            } else {
                sender.sendMessage(plugin.createMessage(
                    plugin.getMessages().get("list.other.none", "player", targetName),
                    NamedTextColor.GRAY
                ));
            }
            
            sender.sendMessage(plugin.createMessage(
                plugin.getMessages().get("list.footer"),
                NamedTextColor.GOLD
            ));
        });
    }

    private void showAllInvites(CommandSender sender) {
        int total = plugin.getInviteData().getTotalInvites();
        List<java.util.Map.Entry<String, Integer>> topInviters = 
            plugin.getInviteData().getTopInviters(10);

        sender.sendMessage(plugin.createMessage(
            plugin.getMessages().get("list.all.header"),
            NamedTextColor.GOLD
        ));
        
        sender.sendMessage(plugin.createMessage(
            plugin.getMessages().get("list.all.total", "total", total),
            NamedTextColor.YELLOW
        ));

        if (!topInviters.isEmpty()) {
            sender.sendMessage(plugin.createMessage(
                plugin.getMessages().get("list.all.top"),
                NamedTextColor.GRAY
            ));
            
            int rank = 1;
            for (java.util.Map.Entry<String, Integer> entry : topInviters) {
                sender.sendMessage(plugin.createMessage(
                    "  §7" + rank + ". §e" + entry.getKey() + " §7- §f" + entry.getValue() + " Invites",
                    NamedTextColor.GRAY
                ));
                rank++;
            }
        }
        
        sender.sendMessage(plugin.createMessage(
            plugin.getMessages().get("list.footer"),
            NamedTextColor.GOLD
        ));
    }
}
