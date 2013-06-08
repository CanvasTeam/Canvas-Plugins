package com.zombiehippie.bukkit.claims.visuals;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.zombiehippie.bukkit.claims.Claim;

public class ClaimVisual extends Visual{
	// We store all the active visualizations here so we can reset them later
	private static HashMap<String, ClaimVisual> activeVisuals = new HashMap<String, ClaimVisual>();
	
	public static enum ResultType {
		CREATE,
		INTERSECT,
		RESIZE,
		INFO,
		TOOSMALL
	}
	private final ResultType type;

	/**
	 * Create a ClaimVisual that can notify the player where a claim is
	 * @param thePlayer The sender
	 * @param theClaim claim to have visualization
	 * @param theType type of visualization
	 */
	public ClaimVisual(Player thePlayer, Claim theClaim, ResultType theType){
		this(thePlayer, new Claim[]{theClaim}, theType);
	}
	
	/**
	 * Create a claim visual with multiple claims
	 * @param thePlayer	the sender
	 * @param theClaims list of claims to have visuals applied
	 * @param theType type of visualization
	 */
	public ClaimVisual(Player thePlayer, Claim[] theClaims, ResultType theType){
		super(theClaims);
		
		type = theType;
		
		// Notify Player upon construction of visual
		notify(thePlayer);
	}

	public Claim getClaim(){
		return claims[0];
	}
	
	public Claim[] getClaims(){
		return claims;
	}
	/**
	 * Send block updates to the client outlining the area
	 * @param player Player to be notified
	 * @param collective whether the visuals build on top of one another, multiple claims highlighted
	 */
	public void notify(Player player){
		resetPlayersVisuals(player.getName());
		// Reset the player's current visual
		
		switch(type){
		case CREATE:
			// If the claim was successfully created
			own(player);
			player.sendMessage(ChatColor.GREEN+"Claim successfully created.");
			break;
		case INTERSECT:
			// If there is a claim already there
			error(player);
			player.sendMessage(ChatColor.RED+"Claim intersects at least one nearby claim!");
			break;
		case RESIZE:
			// If we resized our claim
			own(player);
			player.sendMessage(ChatColor.GOLD+"Claim has been resized!");
			break;
		case INFO:
			// If we are requesting a visual and information about a claim
			info(player);
			String prefix=claims.length>1?"These claims are owned by ":"This claim is owned by ";
			player.sendMessage(ChatColor.BLUE+prefix+ChatColor.AQUA+getClaim().getOwnerName());
			break;
		case TOOSMALL:
			player.sendMessage(ChatColor.RED+"Claim is too small!");
			break;
		}
			
		
		// Add this visual to the active visuals
		activeVisuals.put(player.getName(), this);
	}
	
	/**
	 * Reset players visuals, maybe they logged off or requested a different visual
	 * @param playerName
	 */
	public static void resetPlayersVisuals(String playerName){
		if(activeVisuals.containsKey(playerName)){
			
			ClaimVisual theVisual = activeVisuals.get(playerName);
			
			// Check if player is online
			Player p = Bukkit.getPlayerExact(playerName);
			if(p != null && p.isOnline())
				theVisual.reset(p);
			
			activeVisuals.remove(playerName);
		}
		
	}
	/**
	 * Reset all active visuals
	 */
	
	public static void resetAllVisuals(){
		if(activeVisuals.isEmpty()){
			return;
		}
		for(String playerWithActiveVisualsName : activeVisuals.keySet())
			resetPlayersVisuals(playerWithActiveVisualsName);
		activeVisuals.clear();
	}
}
