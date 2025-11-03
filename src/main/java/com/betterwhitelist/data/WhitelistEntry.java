package com.betterwhitelist.data;

import java.util.Date;
import java.util.UUID;

public class WhitelistEntry {
    
    private final UUID playerId;
    private final String playerName;
    private final String addedBy;
    private final Date addedDate;
    private final String reason;
    private Date expirationDate;
    
    public WhitelistEntry(UUID playerId, String playerName, String addedBy, Date addedDate, String reason) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.addedBy = addedBy;
        this.addedDate = addedDate;
        this.reason = reason;
        this.expirationDate = null;
    }
    
    public WhitelistEntry(UUID playerId, String playerName, String addedBy, Date addedDate, String reason, Date expirationDate) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.addedBy = addedBy;
        this.addedDate = addedDate;
        this.reason = reason;
        this.expirationDate = expirationDate;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public String getAddedBy() {
        return addedBy;
    }
    
    public Date getAddedDate() {
        return addedDate;
    }
    
    public String getReason() {
        return reason;
    }
    
    public Date getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public boolean isTemporary() {
        return expirationDate != null;
    }
    
    public boolean isExpired() {
        if (expirationDate == null) {
            return false;
        }
        return new Date().after(expirationDate);
    }
}
