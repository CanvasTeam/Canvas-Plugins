package com.zombiehippie.bukkit.protection.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.zombiehippie.bukkit.protection.CanvasManager;

public class WorldListener implements Listener {

	private final CanvasManager canvasManager;

	public WorldListener(CanvasManager manager) {
		canvasManager = manager;
	}

	@EventHandler
	// Check spreading liquid
	public void onBlockFromTo(BlockFromToEvent event) {
		String ToClaim = getOwnerAt(event.getToBlock());
		String FromClaim = getOwnerAt(event.getBlock());

		// if the liquid is flowing into or out of a claim
		if (!ToClaim.equals(FromClaim)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	// Check explosions
	public void onEntityExplode(EntityExplodeEvent event) {
		/*
		 * String owner =
		 * getClaimOwnerAt(event.getEntity().getLocation().getBlock());
		 * 
		 * for (Block b : event.blockList()) { if
		 * (getClaimOwnerAt(b).equals(owner)) b.setTypeId(0); }
		 */
		event.setCancelled(true);
	}

	@EventHandler
	// Check spreading fire and mushrooms
	public void onBlockSpread(BlockSpreadEvent event) {
		if (!getOwnerAt(event.getBlock()).equals(getOwnerAt(event.getSource()))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	// Check piston extensions
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		String owner = getOwnerAt(event.getBlock());
		for (Block b : event.getBlocks()) {
			if (!owner.equals(getOwnerAt(b.getRelative(event.getDirection()))))
				event.setCancelled(true);
		}
	}

	private String getOwnerAt(Block b) {
		String theOwner = canvasManager.getCanvasFromBlock(b);
		if (theOwner == null) {
			return "";
		} else {
			return theOwner;
		}
	}
}
