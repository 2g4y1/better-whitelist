package de.yourserver.betterwhitelist;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final BetterWhitelist plugin;

    public ReloadCommand(BetterWhitelist plugin) {
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

        // Reload configuration
        plugin.reloadConfiguration();
        
        sender.sendMessage(plugin.createMessage(
            plugin.getMessages().get("reload.success"),
            NamedTextColor.GREEN
        ));
        
        return true;
    }
}
