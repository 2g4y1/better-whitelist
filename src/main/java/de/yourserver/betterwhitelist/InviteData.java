package de.yourserver.betterwhitelist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Verwaltet die Invite-Daten (wer hat wen eingeladen)
 * Manages invite data (who invited whom)
 */
public class InviteData {
    
    private final File dataFile;
    private final Gson gson;
    private Map<String, List<InviteRecord>> invites; // Inviter UUID -> List of invited players
    
    public InviteData(File dataFolder) {
        this.dataFile = new File(dataFolder, "invites.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.invites = new HashMap<>();
        load();
    }
    
    /**
     * Lädt die Invite-Daten aus der JSON-Datei
     */
    public void load() {
        if (!dataFile.exists()) {
            invites = new HashMap<>();
            save();
            return;
        }
        
        try (Reader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<String, List<InviteRecord>>>(){}.getType();
            Map<String, List<InviteRecord>> loaded = gson.fromJson(reader, type);
            invites = loaded != null ? loaded : new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
            invites = new HashMap<>();
        }
    }
    
    /**
     * Speichert die Invite-Daten in die JSON-Datei
     */
    public void save() {
        try {
            if (!dataFile.getParentFile().exists()) {
                dataFile.getParentFile().mkdirs();
            }
            
            try (Writer writer = new FileWriter(dataFile)) {
                gson.toJson(invites, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Fügt einen Invite hinzu
     */
    public void addInvite(UUID inviter, String inviterName, UUID invited, String invitedName) {
        String inviterKey = inviter.toString();
        
        invites.computeIfAbsent(inviterKey, k -> new ArrayList<>());
        
        InviteRecord record = new InviteRecord(
            invited.toString(),
            invitedName,
            inviterName,
            Instant.now().getEpochSecond()
        );
        
        invites.get(inviterKey).add(record);
        save();
    }
    
    /**
     * Entfernt einen Invite (wenn jemand uninvited wird)
     */
    public void removeInvite(UUID invited) {
        String invitedStr = invited.toString();
        
        for (List<InviteRecord> records : invites.values()) {
            records.removeIf(record -> record.getInvitedUuid().equals(invitedStr));
        }
        
        save();
    }
    
    /**
     * Gibt die Anzahl der Invites eines Spielers zurück
     */
    public int getInviteCount(UUID inviter) {
        String key = inviter.toString();
        return invites.getOrDefault(key, Collections.emptyList()).size();
    }
    
    /**
     * Gibt alle Invites eines Spielers zurück
     */
    public List<InviteRecord> getInvites(UUID inviter) {
        String key = inviter.toString();
        return new ArrayList<>(invites.getOrDefault(key, Collections.emptyList()));
    }
    
    /**
     * Gibt alle Invites zurück (für Admin-Übersicht)
     */
    public Map<String, List<InviteRecord>> getAllInvites() {
        return new HashMap<>(invites);
    }
    
    /**
     * Findet heraus, wer einen bestimmten Spieler eingeladen hat
     */
    public Optional<String> getInviter(UUID invited) {
        String invitedStr = invited.toString();
        
        for (Map.Entry<String, List<InviteRecord>> entry : invites.entrySet()) {
            for (InviteRecord record : entry.getValue()) {
                if (record.getInvitedUuid().equals(invitedStr)) {
                    return Optional.of(record.getInviterName());
                }
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Gibt die Anzahl aller Invites zurück
     */
    public int getTotalInvites() {
        return invites.values().stream()
            .mapToInt(List::size)
            .sum();
    }
    
    /**
     * Gibt die Top-Inviter zurück (sortiert nach Anzahl)
     */
    public List<Map.Entry<String, Integer>> getTopInviters(int limit) {
        return invites.entrySet().stream()
            .map(entry -> {
                String inviterName = entry.getValue().isEmpty() 
                    ? "Unknown" 
                    : entry.getValue().get(0).getInviterName();
                return Map.entry(inviterName, entry.getValue().size());
            })
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Datenklasse für einen einzelnen Invite-Eintrag
     */
    public static class InviteRecord {
        private final String invitedUuid;
        private final String invitedName;
        private final String inviterName;
        private final long timestamp;
        
        public InviteRecord(String invitedUuid, String invitedName, String inviterName, long timestamp) {
            this.invitedUuid = invitedUuid;
            this.invitedName = invitedName;
            this.inviterName = inviterName;
            this.timestamp = timestamp;
        }
        
        public String getInvitedUuid() {
            return invitedUuid;
        }
        
        public String getInvitedName() {
            return invitedName;
        }
        
        public String getInviterName() {
            return inviterName;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
    }
}
