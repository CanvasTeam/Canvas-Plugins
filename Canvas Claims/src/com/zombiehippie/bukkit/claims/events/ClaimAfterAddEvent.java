package com.zombiehippie.bukkit.claims.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClaimAfterAddEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private int claim_id;
    
	public ClaimAfterAddEvent(int theClaimId) {
		this.claim_id = theClaimId;
	}
	
	@Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    /**
     * Get claim that was added to the claim manager
     * @return theClaim
     */
	public int getClaimId() {
		return claim_id;
	}
}
