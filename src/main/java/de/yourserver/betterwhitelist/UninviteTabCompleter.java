package de.yourserver.betterwhitelist;

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

    private final BetterWhitelist plugin;

    public UninviteTabCompleter(BetterWhitelist plugin) {
        this.plugin = plugin;
    }

    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        
        if (args.length == 1) {
            // Zeige Online-Spieler als Vorschläge
            List<String> suggestions = Bukkit.getOnlinePlayers().stream()
                .map(player -> player.getName())
                .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
            
            return suggestions;
        }
        if (args.length == 2 && plugin.isFloodgateEnabled()) {
            // suggest "bedrock" for the second argument
            if ("bedrock".startsWith(args[1].toLowerCase())) {
                return Arrays.asList("bedrock");
            }
        }
        
        return new ArrayList<>();
    }
}
