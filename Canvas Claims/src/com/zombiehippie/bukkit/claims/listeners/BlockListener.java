package com.zombiehippie.bukkit.claims.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.events.CustomBlockEvent;

public class BlockListener implements Listener{
	@EventHandler
	public void onCustomBlock(CustomBlockEvent event){
		
		
		event.setCancelled(!event.isOwner());
		
	}
	@EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(!canModify(event.getPlayer(),event.getBlock())){
        	event.setCancelled(true);
        }
    }
	
	@EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!canModify(event.getPlayer(),event.getBlockPlaced())){
        	event.setCancelled(true);
        }
    }

	@EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		// Use relative block to face clicked because that is where the liquid is poured
        if(!canModify(event.getPlayer(),event.getBlockClicked().getRelative(event.getBlockFace()))){
        	event.setCancelled(true);
        }
    }
	@EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if(!canModify(event.getPlayer(),event.getBlockClicked())){
        	event.setCancelled(true);
        }
    }
	
	private boolean canModify(Player player, Block block){
		if(player.isOp())
			return true;
		String UserName = player.getName();
		return CanvasClaims.instance.owns(UserName, block);
	}
}
