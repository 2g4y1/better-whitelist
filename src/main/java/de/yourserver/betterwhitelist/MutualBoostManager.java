package de.yourserver.betterwhitelist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Verwaltet XP-Boosts für gegenseitig eingeladene Spieler
 * Manages XP boosts for mutually invited players
 */
public class MutualBoostManager implements Listener {
    
    private final BetterWhitelist plugin;
    private final InviteData inviteData;
    private BukkitTask boostTask;
    
    // Config values
    private boolean enabled;
    private int radius;
    private double xpMultiplier;
    private int checkInterval;
    private boolean showActionbar;
    private boolean showParticles;
    private int maxBoostPartners;  // Maximale Anzahl Partner die gleichzeitig boosten
    private double maxMultiplier;   // Maximaler Multiplikator
    private int minXpForBoost;      // Minimum XP um Boost zu aktivieren (verhindert Spam)
    
    // Tracking
    private final Map<UUID, Set<UUID>> activeBoosts = new HashMap<>();
    private final Map<UUID, Long> lastActionbarSent = new HashMap<>();
    private final Map<UUID, Integer> xpGainedThisPeriod = new HashMap<>();  // XP-Tracking für Anti-Exploit
    
    public MutualBoostManager(BetterWhitelist plugin, InviteData inviteData) {
        this.plugin = plugin;
        this.inviteData = inviteData;
        loadConfig();
        
        if (enabled) {
            startBoostTask();
            Bukkit.getPluginManager().registerEvents(this, plugin);
            plugin.getLogger().info("✓ Mutual Invite Boost System aktiviert");
        }
    }
    
    /**
     * Lädt die Config-Werte
     */
    public void loadConfig() {
        enabled = plugin.getConfig().getBoolean("mutual-invite-boost.enabled", true);
        radius = plugin.getConfig().getInt("mutual-invite-boost.radius", 50);
        xpMultiplier = plugin.getConfig().getDouble("mutual-invite-boost.xp-multiplier", 1.5);
        checkInterval = plugin.getConfig().getInt("mutual-invite-boost.check-interval", 5);
        showActionbar = plugin.getConfig().getBoolean("mutual-invite-boost.show-actionbar", true);
        showParticles = plugin.getConfig().getBoolean("mutual-invite-boost.particle-effects", true);
        
        // Neue Sicherheits-Optionen
        maxBoostPartners = plugin.getConfig().getInt("mutual-invite-boost.max-boost-partners", 3);
        maxMultiplier = plugin.getConfig().getDouble("mutual-invite-boost.max-multiplier", 2.5);
        minXpForBoost = plugin.getConfig().getInt("mutual-invite-boost.min-xp-for-boost", 1);
        
        // Sicherstellen dass Werte in vernünftigen Grenzen sind
        xpMultiplier = Math.min(xpMultiplier, maxMultiplier);
        xpMultiplier = Math.max(xpMultiplier, 1.0);
        maxBoostPartners = Math.max(1, Math.min(maxBoostPartners, 10));
    }
    
    /**
     * Startet den Boost-Check Task
     */
    public void startBoostTask() {
        if (boostTask != null) {
            boostTask.cancel();
        }
        
        long intervalTicks = checkInterval * 20L; // Sekunden zu Ticks
        
        boostTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            checkAndApplyBoosts();
        }, 0L, intervalTicks);
        
        // Cleanup-Task für XP-Tracking (alle 60 Sekunden zurücksetzen)
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            xpGainedThisPeriod.clear();
        }, 1200L, 1200L); // Alle 60 Sekunden
    }
    
    /**
     * Stoppt den Boost-Task
     */
    public void stop() {
        if (boostTask != null) {
            boostTask.cancel();
            boostTask = null;
        }
        activeBoosts.clear();
        lastActionbarSent.clear();
    }
    
    /**
     * Prüft alle Spieler und wendet Boosts an
     */
    private void checkAndApplyBoosts() {
        if (!enabled) return;
        
        // Clear old boosts
        activeBoosts.clear();
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            Set<UUID> nearbyMutuals = findNearbyMutualInvites(player);
            
            if (!nearbyMutuals.isEmpty()) {
                activeBoosts.put(player.getUniqueId(), nearbyMutuals);
                
                // Actionbar anzeigen
                if (showActionbar) {
                    showBoostActionbar(player, nearbyMutuals);
                }
                
                // Partikel anzeigen
                if (showParticles) {
                    for (UUID mutualUuid : nearbyMutuals) {
                        Player mutual = Bukkit.getPlayer(mutualUuid);
                        if (mutual != null) {
                            showBoostParticles(player, mutual);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Findet alle mutual invites in der Nähe
     */
    private Set<UUID> findNearbyMutualInvites(Player player) {
        Set<UUID> mutuals = new HashSet<>();
        UUID playerUuid = player.getUniqueId();
        
        // Finde Spieler in Reichweite
        Collection<Entity> nearby = player.getWorld()
            .getNearbyEntities(player.getLocation(), radius, radius, radius);
        
        for (Entity entity : nearby) {
            if (!(entity instanceof Player nearbyPlayer)) continue;
            if (nearbyPlayer.equals(player)) continue;
            
            UUID nearbyUuid = nearbyPlayer.getUniqueId();
            
            // Prüfe ob mutual invite
            if (areMutualInvites(playerUuid, nearbyUuid)) {
                mutuals.add(nearbyUuid);
                
                // Limit auf maxBoostPartners
                if (mutuals.size() >= maxBoostPartners) {
                    break;
                }
            }
        }
        
        return mutuals;
    }
    
    /**
     * Prüft ob zwei Spieler sich gegenseitig eingeladen haben
     */
    private boolean areMutualInvites(UUID player1, UUID player2) {
        List<InviteData.InviteRecord> invitesBy1 = inviteData.getInvites(player1);
        List<InviteData.InviteRecord> invitesBy2 = inviteData.getInvites(player2);
        
        boolean player1InvitedPlayer2 = invitesBy1.stream()
            .anyMatch(r -> r.getInvitedUuid().equals(player2.toString()));
        boolean player2InvitedPlayer1 = invitesBy2.stream()
            .anyMatch(r -> r.getInvitedUuid().equals(player1.toString()));
        
        return player1InvitedPlayer2 && player2InvitedPlayer1;
    }
    
    /**
     * Zeigt Actionbar mit Boost-Info
     */
    private void showBoostActionbar(Player player, Set<UUID> mutuals) {
        long now = System.currentTimeMillis();
        Long last = lastActionbarSent.get(player.getUniqueId());
        
        // Nur alle 3 Sekunden senden (um Spam zu vermeiden)
        if (last != null && now - last < 3000) {
            return;
        }
        
        lastActionbarSent.put(player.getUniqueId(), now);
        
        StringBuilder names = new StringBuilder();
        int count = 0;
        for (UUID uuid : mutuals) {
            Player mutual = Bukkit.getPlayer(uuid);
            if (mutual != null) {
                if (count > 0) names.append(", ");
                names.append(mutual.getName());
                count++;
                if (count >= 3) {
                    if (mutuals.size() > 3) {
                        names.append(" +").append(mutuals.size() - 3);
                    }
                    break;
                }
            }
        }
        
        String message = plugin.getMessages().get("boost.active",
            "players", names.toString(),
            "multiplier", String.format("%.0f%%", (xpMultiplier - 1) * 100));
        
        player.sendActionBar(Component.text(message).color(NamedTextColor.GOLD));
    }
    
    /**
     * Zeigt Partikel zwischen Spielern
     */
    private void showBoostParticles(Player player1, Player player2) {
        Location loc1 = player1.getLocation().add(0, 1, 0);
        Location loc2 = player2.getLocation().add(0, 1, 0);
        
        // Mittelpunkt zwischen beiden Spielern
        Location mid = loc1.clone().add(loc2).multiply(0.5);
        
        player1.getWorld().spawnParticle(
            Particle.HAPPY_VILLAGER,
            mid,
            3,
            0.5, 0.5, 0.5,
            0.01
        );
    }
    
    /**
     * XP Event Handler - Wendet Multiplikator an
     */
    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (!enabled) return;
        
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        Set<UUID> mutuals = activeBoosts.get(playerUuid);
        
        if (mutuals != null && !mutuals.isEmpty()) {
            int originalXP = event.getAmount();
            
            // Nur boosten wenn mindestens minXpForBoost XP
            if (originalXP < minXpForBoost) {
                return;
            }
            
            // Berechne Multiplikator (begrenzt auf maxMultiplier)
            double actualMultiplier = Math.min(xpMultiplier, maxMultiplier);
            
            // Optional: Progressiver Multiplikator basierend auf Anzahl Partner
            // Aber nur bis zu maxMultiplier
            // double partnerBonus = 1.0 + (mutuals.size() * 0.1); // +10% pro Partner
            // actualMultiplier = Math.min(actualMultiplier * partnerBonus, maxMultiplier);
            
            int boostedXP = (int)(originalXP * actualMultiplier);
            
            // Hard Cap: Maximale XP pro Event (verhindert Exploits)
            int maxXpPerEvent = plugin.getConfig().getInt("mutual-invite-boost.max-xp-per-event", 1000);
            boostedXP = Math.min(boostedXP, maxXpPerEvent);
            
            event.setAmount(boostedXP);
            
            // Anti-Exploit: Tracking
            int gained = xpGainedThisPeriod.getOrDefault(playerUuid, 0);
            xpGainedThisPeriod.put(playerUuid, gained + boostedXP);
        }
    }
    
    /**
     * Prüft ob ein Spieler aktiv einen Boost hat
     */
    public boolean hasActiveBoost(UUID playerUuid) {
        Set<UUID> mutuals = activeBoosts.get(playerUuid);
        return mutuals != null && !mutuals.isEmpty();
    }
    
    /**
     * Gibt die Anzahl aktiver Boost-Partner zurück
     */
    public int getBoostPartnerCount(UUID playerUuid) {
        Set<UUID> mutuals = activeBoosts.get(playerUuid);
        return mutuals != null ? mutuals.size() : 0;
    }
    
    /**
     * Prüft ob das System aktiviert ist
     */
    public boolean isEnabled() {
        return enabled;
    }
}
