package com.zombiehippie.bukkit.claims.listeners;


import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.Claim;

public class WorldListener implements Listener {
	
	@EventHandler
	// Check spreading liquid
	public void onBlockFromTo(BlockFromToEvent event) {
		String ToClaim = getClaimOwnerAt(event.getToBlock());
		String FromClaim = getClaimOwnerAt(event.getBlock());

		// if the liquid is flowing into or out of a claim
		if (!ToClaim.equals(FromClaim)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	// Check explosions
	public void onEntityExplode(EntityExplodeEvent event) {
		/*String owner = getClaimOwnerAt(event.getEntity().getLocation().getBlock());

		for (Block b : event.blockList()) {
			if (getClaimOwnerAt(b).equals(owner))
				b.setTypeId(0);
		}*/
		event.setCancelled(true);
	}
	
	@EventHandler
	// Check spreading fire and mushrooms
	public void onBlockSpread(BlockSpreadEvent event) {
		if (!getClaimOwnerAt(event.getBlock()).equals(
				getClaimOwnerAt(event.getSource()))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	// Check piston extensions
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		String owner = getClaimOwnerAt(event.getBlock());
		for (Block b : event.getBlocks()) {
			if (!owner.equals(getClaimOwnerAt(b.getRelative(event
					.getDirection()))))
				event.setCancelled(true);
		}
	}

	private String getClaimOwnerAt(Block b) {
		Claim theClaim = CanvasClaims.getClaimAt(b);
		if (theClaim == null) {
			return "";
		} else {
			return theClaim.getOwnerName();
		}
	}

}
