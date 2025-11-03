package de.yourserver.betterwhitelist;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UninviteCommand implements CommandExecutor {

    private final BetterWhitelist plugin;

    public UninviteCommand(BetterWhitelist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {

        // Permission check: Console always allowed, players need invite.admin
        if (!(sender instanceof org.bukkit.command.ConsoleCommandSender) && !sender.hasPermission("invite.admin")) {
            sender.sendMessage(plugin.createMessage(
                plugin.getMessages().get("no_permission"),
                NamedTextColor.RED
            ));
            return true;
        }

        // Argument-Check
        if (args.length != 1) {
            sender.sendMessage(plugin.createMessage(
                plugin.getMessages().get("uninvite.usage"),
                NamedTextColor.YELLOW
            ));
            return true;
        }

        String playerName = args[0];

        // Nachricht, dass es lädt
        sender.sendMessage(plugin.createMessage(
            plugin.getMessages().get("uninvite.loading"),
            NamedTextColor.GRAY
        ));

        // UUID-Abfrage async, dann Whitelist auf Main-Thread
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.uninvitePlayer(playerName, sender);
        });

        return true;
    }
}
