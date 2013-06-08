package com.zombiehippie.bukkit.claims.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.zombiehippie.bukkit.claims.Claim;

public class ClaimAddEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Claim claim;
    
	public ClaimAddEvent(Claim theClaim) {
		this.claim = theClaim;
	}
	
	@Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    /**
     * Get claim that will be added to the claim manager
     * @return theClaim
     */
	public Claim getClaim() {
		return claim;
	}
	
	/**
	 * Set's the claim to be added to the claim manager
	 * @param newClaim
	 */
	public void setClaim(Claim newClaim) {
		this.claim = newClaim;
	}
}
