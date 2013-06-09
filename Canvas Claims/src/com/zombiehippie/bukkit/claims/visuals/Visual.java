package com.zombiehippie.bukkit.claims.visuals;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.zombiehippie.bukkit.claims.Claim;

public class Visual {
	protected Claim[] claims;
	
	public Visual(Claim[] _areas){
		claims = _areas;
	}

	public void own(Player sendToPlayer){
		visual(sendToPlayer,Material.GLOWSTONE.getId(), Material.GOLD_BLOCK.getId());
	}
	public void error(Player sendToPlayer){
		visual(sendToPlayer,Material.NETHER_BRICK.getId(), Material.LAVA.getId());
	}
	public void info(Player sendToPlayer){
		visual(sendToPlayer,Material.DIAMOND_BLOCK.getId(), Material.IRON_BLOCK.getId());
	}
	public void reset(Player sendToPlayer){
		// Setting materials to -1 will trigger reset in visual
		visual(sendToPlayer,-1,-1);
	}
	public void visual(Player player, int corner, int dots){
		for(int index=0;index<claims.length;index++){
			Claim claim = claims[index];
			
			int height = claim.getHeight();
			int width = claim.getWidth();
			for(int i=2; i<width; i+=3){
				sendChange(player,claim.getWestBoundary()+i, claim.getNorthBoundary(), dots);
				sendChange(player,claim.getEastBoundary()-i, claim.getSouthBoundary(), dots);
			}
			for(int k=2; k<height; k+=3){
				sendChange(player,claim.getWestBoundary(), claim.getSouthBoundary()+k, dots);
				sendChange(player,claim.getEastBoundary(), claim.getNorthBoundary()-k, dots);
			}
			sendChange(player,claim.getEastBoundary(),claim.getNorthBoundary(),corner);
			sendChange(player,claim.getEastBoundary(),claim.getSouthBoundary(),corner);
			sendChange(player,claim.getWestBoundary(),claim.getSouthBoundary(),corner);
			sendChange(player,claim.getWestBoundary(),claim.getNorthBoundary(),corner);
		}
	}
	
	private void sendChange(Player player, int X, int Z, int mat){
		Block b = player.getWorld().getHighestBlockAt(X, Z);
		while(!b.getType().isOccluding()){
			b = b.getRelative(0, -1, 0);
		}
		if(mat==-1){
			// Send actual block material
			mat = b.getTypeId();
		}
		player.sendBlockChange(b.getLocation(), mat, (byte)0);
	}
}
