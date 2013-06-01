package com.zombiehippie.bukkit.painter.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.zombiehippie.bukkit.claims.CustomBlockEvent;

public class PlayerListeners implements Listener{

	@EventHandler
	public void onPlayerInteraction(PlayerInteractEvent event){
		Block theBlock = event.getPlayer().getLocation().getBlock();
		boolean leftClicked = true;
		
		
		// Figure out target block
		switch(event.getAction()){
		case RIGHT_CLICK_AIR:
			leftClicked=false;
		case LEFT_CLICK_AIR:
			theBlock=event.getPlayer().getTargetBlock(null, 150);
			if (theBlock.isEmpty()){
				event.getPlayer().sendMessage(ChatColor.RED + "The block is too far away!");
				return;
			}
			break;
		case RIGHT_CLICK_BLOCK:
			leftClicked=false;
		case LEFT_CLICK_BLOCK:
			theBlock=event.getClickedBlock();
			break;
		default:
			return;
		}
		
		if(!event.getItem().getType().isBlock() && !leftClicked){
			// If item in hand is not a block and right click, then we cannot snipe-place the block
			return;
		}
		
		// We have the target block!
		CustomBlockEvent evt = new CustomBlockEvent(event.getPlayer().getName(), theBlock);
		
		Bukkit.getPluginManager().callEvent(evt);
		
		if(!evt.isCancelled()){
			// Snipe-Place block in-hand or Snipe-Break
			theBlock.setType(leftClicked?Material.AIR:event.getItem().getType());
		} else {
			event.getPlayer().sendMessage(ChatColor.RED + "You can only \"snipe\" on your own claim!");
		}
	}
}
