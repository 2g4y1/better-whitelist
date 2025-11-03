package de.yourserver.inviteplugin;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class InviteCommand implements CommandExecutor {

    private final InvitePlugin plugin;

    public InviteCommand(InvitePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {

        // Argument-Check
        if (args.length != 1) {
            sender.sendMessage(plugin.createMessage(
                "§eVerwendung: /invite <Spielername>",
                NamedTextColor.YELLOW
            ));
            return true;
        }

        String playerName = args[0];

        // Nachricht, dass es lädt
        sender.sendMessage(plugin.createMessage(
            "§7Lade Spielerdaten von Mojang...",
            NamedTextColor.GRAY
        ));

        // UUID-Abfrage async, dann Whitelist auf Main-Thread
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.invitePlayer(playerName, sender);
        });

        return true;
    }
}
