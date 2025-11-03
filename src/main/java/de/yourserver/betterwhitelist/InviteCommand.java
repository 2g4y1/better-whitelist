package de.yourserver.betterwhitelist;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class InviteCommand implements CommandExecutor {

    private final BetterWhitelist plugin;

    public InviteCommand(BetterWhitelist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {

        // Argument-Check
        if (args.length != 1) {
            sender.sendMessage(plugin.createMessage(
                plugin.getMessages().get("invite.usage"),
                NamedTextColor.YELLOW
            ));
            return true;
        }

        String playerName = args[0];

        // Nachricht, dass es lädt
        sender.sendMessage(plugin.createMessage(
            plugin.getMessages().get("invite.loading"),
            NamedTextColor.GRAY
        ));

        // UUID-Abfrage async, dann Whitelist auf Main-Thread
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.invitePlayer(playerName, sender);
        });

        return true;
    }
}
