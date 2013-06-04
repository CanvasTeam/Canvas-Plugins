package com.zombiehippie.bukkit.claims.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.zombiehippie.bukkit.claims.Claim;

public class PlayerClaimEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Claim claim;
    private String playerName;
	public PlayerClaimEvent(String thePlayerName, Claim theClaim) {
		this.playerName = thePlayerName;
		this.claim = theClaim;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;		
	}
	
	@Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public String getPlayerName() {
		return playerName;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(playerName);
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
