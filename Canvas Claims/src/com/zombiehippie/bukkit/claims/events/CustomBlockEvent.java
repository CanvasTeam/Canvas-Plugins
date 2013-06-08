package com.zombiehippie.bukkit.claims.events;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.Claim;

public class CustomBlockEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final boolean claimed;
    private final String ownerName;
    private final String playerName;
    private boolean cancelled = false;
	public CustomBlockEvent(String thePlayerName, Block theBlock) {
		Claim theClaim = CanvasClaims.getClaimAt(theBlock);
		
		// isClaimed If claim isn't null
		claimed=(theClaim != null);
		
		// ownerName may = "" if iClaimed == false
		ownerName=claimed?theClaim.getOwnerName():"";
		
		// 
		playerName = thePlayerName;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	public boolean isClaimed(){
		return claimed;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public boolean isOwner(){
		// Check if player's Name and Owner's name match
		return playerName.equals(ownerName);
	}
	
	public String getOwner(){
		return ownerName;
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
}
