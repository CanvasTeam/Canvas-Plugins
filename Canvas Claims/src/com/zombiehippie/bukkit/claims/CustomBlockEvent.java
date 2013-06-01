package com.zombiehippie.bukkit.claims;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomBlockEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final boolean claimed;
    private final String ownerName;
    private final String playerName;
	public CustomBlockEvent(String player, Block theBlock) {
		Claim theClaim = CanvasClaims.instance.getClaimAt(theBlock);
		claimed=(theClaim != null);
		ownerName=claimed?theClaim.getOwnerName():"";
		playerName = player;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isClaimed(){
		return claimed;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public boolean isOwner(){
		return playerName == ownerName;
	}
	
	public String getOwner(){
		return ownerName;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
