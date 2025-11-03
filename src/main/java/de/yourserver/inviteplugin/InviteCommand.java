package de.yourserver.inviteplugin;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InviteCommand implements CommandExecutor {

    private final InvitePlugin plugin;

    public InviteCommand(InvitePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {

        // Permission-Check
        if (!sender.hasPermission("invite.use")) {
            sender.sendMessage(plugin.createMessage(
                "§cDu hast keine Berechtigung, diesen Befehl zu verwenden!",
                NamedTextColor.RED
            ));
            return true;
        }

        // Argument-Check
        if (args.length != 1) {
            sender.sendMessage(plugin.createMessage(
                "§eVerwendung: /invite <Spielername>",
                NamedTextColor.YELLOW
            ));
            return true;
        }

        String playerName = args[0];

        // Spieler einladen - Whitelist auf Main-Thread, Rest async
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            boolean success = plugin.invitePlayer(playerName);

            // Rückmeldung an den Sender
            if (success) {
                    sender.sendMessage(plugin.createMessage(
                        "§a✓ Spieler §e" + playerName + "§a wurde erfolgreich eingeladen!",
                        NamedTextColor.GREEN
                    ));
                    sender.sendMessage(plugin.createMessage(
                        "§7→ Whitelist: §aAktiviert",
                        NamedTextColor.GRAY
                    ));
                    sender.sendMessage(plugin.createMessage(
                        "§7→ LuckPerms-Gruppe: §edefault",
                        NamedTextColor.GRAY
                    ));

                    // Broadcast an alle Online-Spieler mit der Permission
                    plugin.getServer().getOnlinePlayers().stream()
                        .filter(p -> p.hasPermission("invite.use"))
                        .forEach(p -> {
                            if (!p.equals(sender)) {
                                p.sendMessage(plugin.createMessage(
                                    "§7[§aInvite§7] §e" + sender.getName() +
                                    " §7hat §e" + playerName + " §7eingeladen.",
                                    NamedTextColor.GRAY
                                ));
                            }
                        });
                } else {
                    sender.sendMessage(plugin.createMessage(
                        "§c✗ Fehler beim Einladen von §e" + playerName + "§c!",
                        NamedTextColor.RED
                    ));
                    sender.sendMessage(plugin.createMessage(
                        "§7Bitte überprüfe die Logs für weitere Informationen.",
                        NamedTextColor.GRAY
                    ));
                }
            });
        });

        return true;
    }
}
