package com.zombiehippie.bukkit.claims.listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.Claim;
import com.zombiehippie.bukkit.claims.visuals.ClaimVisual;
import com.zombiehippie.bukkit.claims.visuals.ClaimVisual.ResultType;

public class PlayerListener implements Listener{
	private static HashMap<String,Location> lastClicked = new HashMap<String,Location>();
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
		// When the player Right click interacts with air or a block
		Player player = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
			// Get the block we are pointing at from at least 150 blocks away
			Block block = player.getTargetBlock(null, 150);
			
			if(block.isEmpty()) {
				// If block returns as air, we are clicking too far away
				player.sendMessage(ChatColor.GOLD+"You clicked too far away!");
				return;
			}
			
			// Get the clicked claim
			Claim clicked_claim = CanvasClaims.instance.getClaimAt(block);
			
			if(event.getMaterial().getId() == Material.GOLD_SPADE.getId()){
				// Try to create a claim
				if(clicked_claim != null){
					// Clicked a claim while trying to create a claim
					new ClaimVisual(player, clicked_claim, ResultType.INTERSECT);
				} else {
					// Did not click a claim
					
					// If the player has already clicked a block
					if(lastClicked.containsKey(player.getName())){
						// Get the last clicked block
						Location last = lastClicked.get(player.getName());
						// notify
						player.sendMessage(ChatColor.AQUA+"Second Block selected");
						// Try to create the claim, get a result back
						CanvasClaims.instance.createClaim(player, last, block.getLocation());
					} else {
						player.sendBlockChange(block.getLocation(), Material.GLOWSTONE, (byte) 0);
						lastClicked.put(player.getName(), block.getLocation());
						player.sendMessage(ChatColor.AQUA+"First Block selected");
					}
				}
			} else if (event.getMaterial().getId() == Material.BOOK.getId()){
				if(clicked_claim != null){
					// Claim info needed
					new ClaimVisual(player, clicked_claim, ResultType.INFO);
				} else {
					ClaimVisual.resetPlayersVisuals(player.getName());
				}
				
			}
		}
	}
	@EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		String name = event.getPlayer().getName();
		if(lastClicked.containsKey(name)){
			event.getPlayer().sendMessage(ChatColor.GOLD+"Claiming action reset.");
			resetShovel(event.getPlayer());
		}
		
		// On item switch, reset visuals
		// ClaimVisual.resetPlayersVisuals(name);
	}

	public static void resetShovel(Player player){
		String playerName = player.getName();
		if(lastClicked.containsKey(playerName)){
			player.sendBlockChange(lastClicked.get(playerName), lastClicked.get(playerName).getBlock().getTypeId(), (byte)0);
			lastClicked.remove(playerName);
		}
	}
	
}
