package com.zombiehippie.bukkit.claims.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.Claim;

public class WorldListener implements Listener {
	@EventHandler
	// Check spreading liquid
    public void onBlockFromTo(BlockFromToEvent event) {
		Claim ToClaim = CanvasClaims.instance.getClaimAt(event.getToBlock());
		Claim FromClaim = CanvasClaims.instance.getClaimAt(event.getBlock());
		
		// if the liquid is flowing into or out of a claim
        if(ToClaim != FromClaim){
        	event.setCancelled(true);
        }
    }
}
