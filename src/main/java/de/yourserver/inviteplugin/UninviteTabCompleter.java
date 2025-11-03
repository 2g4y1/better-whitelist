package de.yourserver.inviteplugin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UninviteTabCompleter implements TabCompleter {

    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        
        if (args.length == 1) {
            // Zeige gewhitelistete Spieler als Vorschläge
            List<String> suggestions = Arrays.stream(Bukkit.getWhitelistedPlayers().toArray(new OfflinePlayer[0]))
                .map(OfflinePlayer::getName)
                .filter(name -> name != null && name.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
            
            return suggestions;
        }
        
        return new ArrayList<>();
    }
}
