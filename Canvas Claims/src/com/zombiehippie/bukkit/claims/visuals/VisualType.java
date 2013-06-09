package com.zombiehippie.bukkit.claims.visuals;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum VisualType {
	INTERSECTING( Material.LAVA,
			ChatColor.RED+"The claim is intersecting at least one other claim!"),
	CREATED(Material.GOLD_BLOCK,
			ChatColor.DARK_GREEN+"You've claimed land!"),
	INFORMSELFOWNED(Material.GOLD_BLOCK,
			ChatColor.BLUE+"You own this claim!"),
	INFORMELSEOWNED(Material.IRON_BLOCK,
			ChatColor.AQUA+"$else "+ChatColor.BLUE+"owns this land.");
	
	
	
	private final Material outlineMaterial;
	private final String messageToPlayer;
	VisualType(Material theOutlineMaterial, String theMessageToPlayer){
		this.outlineMaterial = theOutlineMaterial;
		this.messageToPlayer = theMessageToPlayer;
	}
	public Material getOutlineMaterial() {
		return outlineMaterial;
	}
	public String getMessageToPlayer() {
		return messageToPlayer;
	}
}
