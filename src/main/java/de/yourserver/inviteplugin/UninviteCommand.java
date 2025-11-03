package de.yourserver.inviteplugin;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UninviteCommand implements CommandExecutor {

    private final InvitePlugin plugin;

    public UninviteCommand(InvitePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {

        // Permission-Check (Konsole hat immer Zugriff)
        if (sender instanceof Player && !sender.hasPermission("invite.admin")) {
            sender.sendMessage(plugin.createMessage(
                "§cDu hast keine Berechtigung, diesen Befehl zu verwenden!",
                NamedTextColor.RED
            ));
            return true;
        }

        // Argument-Check
        if (args.length != 1) {
            sender.sendMessage(plugin.createMessage(
                "§eVerwendung: /uninvite <Spielername>",
                NamedTextColor.YELLOW
            ));
            return true;
        }

        String playerName = args[0];

        // Spieler entfernen (auf Main-Thread, da Whitelist-Operation)
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            boolean success = plugin.uninvitePlayer(playerName);

            // Rückmeldung an den Sender
            if (success) {
                sender.sendMessage(plugin.createMessage(
                    "§a✓ Spieler §e" + playerName + "§a wurde von der Whitelist entfernt!",
                    NamedTextColor.GREEN
                ));

                // Broadcast an alle Admins
                plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission("invite.admin"))
                    .forEach(p -> {
                        if (!p.equals(sender)) {
                            p.sendMessage(plugin.createMessage(
                                "§7[§cUninvite§7] §e" + sender.getName() +
                                " §7hat §e" + playerName + " §7von der Whitelist entfernt.",
                                NamedTextColor.GRAY
                            ));
                        }
                    });
            } else {
                sender.sendMessage(plugin.createMessage(
                    "§c✗ Fehler beim Entfernen von §e" + playerName + "§c!",
                    NamedTextColor.RED
                ));
                sender.sendMessage(plugin.createMessage(
                    "§7Bitte überprüfe die Logs für weitere Informationen.",
                    NamedTextColor.GRAY
                ));
            }
        });

        return true;
    }
}
