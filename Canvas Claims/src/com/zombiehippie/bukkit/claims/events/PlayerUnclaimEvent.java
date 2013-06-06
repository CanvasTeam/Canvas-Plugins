package com.zombiehippie.bukkit.claims.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.zombiehippie.bukkit.claims.Claim;

public class PlayerUnclaimEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
    private Claim claim;
    
	public PlayerUnclaimEvent(Claim theClaim) {
		this.claim = theClaim;
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
}
