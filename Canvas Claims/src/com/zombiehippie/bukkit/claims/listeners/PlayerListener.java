package com.zombiehippie.bukkit.claims.listeners;

import java.util.HashMap;
import java.util.List;

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
			if(event.getMaterial().getId() == Material.GOLD_SPADE.getId()){
				List<Block> blocks = player.getLastTwoTargetBlocks(null, 150);
				if(blocks.isEmpty() || blocks.get(1).isEmpty()) {
					player.sendMessage(ChatColor.GOLD+"You clicked too far away!");
					return;
				}
				if(lastClicked.containsKey(player.getName())){
					Location last = lastClicked.get(player.getName());
					// Try to create the claim, get a result back
					CanvasClaims.instance.createClaim(player, last, blocks.get(1).getLocation());
				} else {
					player.sendBlockChange(blocks.get(1).getLocation(), Material.GLOWSTONE, (byte) 0);
					lastClicked.put(player.getName(), blocks.get(1).getLocation());
					player.sendMessage(ChatColor.AQUA+"First Block selected");
				}
			} else if (event.getMaterial().getId() == Material.BOOK.getId()){
				List<Block> blocks = player.getLastTwoTargetBlocks(null, 150);
				if(blocks.isEmpty() || blocks.get(1).isEmpty()) {
					player.sendMessage(ChatColor.GOLD+"You clicked too far away!");
					return;
				}
				Claim clicked_claim = CanvasClaims.instance.getClaimAt(blocks.get(1));
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
