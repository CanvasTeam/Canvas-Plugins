package com.zombiehippie.bukkit.claims.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;

import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.visuals.Visuallization;

public class ServerListener implements Listener {	
	
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		Visuallization.resetPlayerVisuals(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		if(event.getPlugin().getName().equals("Canvas Claim Facets")){
			CanvasClaims.instance.loadAll();
		}
	}

}
