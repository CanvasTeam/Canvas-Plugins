package com.zombiehippie.bukkit.protection.visuals;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class VisualManager {
	private HashMap<String, PlayerVisual> playersWithVisuals = new HashMap<String, PlayerVisual>();

	public void resetPlayerVisuals(String thePlayerName) {
		if (playersWithVisuals.containsKey(thePlayerName)) {
			playersWithVisuals.get(thePlayerName).resetVisuals();
			playersWithVisuals.remove(thePlayerName);
		}
	}
	
	public void sendPlayerVisuals(Player thePlayerSeeing, VisualType theType, Integer[] canvasLocationCodes) {
		resetPlayerVisuals(thePlayerSeeing.getName());
		
		PlayerVisual newVisual = new PlayerVisual(thePlayerSeeing, theType, canvasLocationCodes);
				
		playersWithVisuals.put(thePlayerSeeing.getName(), newVisual);
	}
}