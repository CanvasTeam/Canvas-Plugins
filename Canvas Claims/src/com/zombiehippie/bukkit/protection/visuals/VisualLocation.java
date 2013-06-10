package com.zombiehippie.bukkit.protection.visuals;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class VisualLocation {
	private final int x;
	private final int y;
	private final int z;
	private final World world;
	private final Player player;

	public VisualLocation(Player playerSeeing, Block constrBlock) {
		this(	playerSeeing,
				constrBlock.getX(),
				constrBlock.getY(),
				constrBlock.getZ());
	}

	public VisualLocation(Player playerSeeing, int _x, int _y, int _z) {
		this.x = _x;
		this.y = _y;
		this.z = _z;
		this.world = playerSeeing.getWorld();
		this.player = playerSeeing;
	}

	public void sendVisual(Material theMaterial) {
		sendVisual(theMaterial, (byte) 0);
	}

	public void sendVisual(Material theMaterial, byte theData) {
		player.sendBlockChange(world.getBlockAt(x, y, z).getLocation(),
				theMaterial, theData);
	}

	public void resetVisual() {
		Block theWorldBlock = world.getBlockAt(x, y, z);
		sendVisual(theWorldBlock.getType(),theWorldBlock.getData());
	}
}
