package com.zombiehippie.bukkit.claimfacets.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.zombiehippie.bukkit.claims.events.PlayerClaimEvent;

public class ClaimListener implements Listener {
	@EventHandler
	public void onClaimCreation(PlayerClaimEvent event) {
		//create facets for claim.
	}
}
