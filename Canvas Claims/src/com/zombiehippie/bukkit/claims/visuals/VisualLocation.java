package com.zombiehippie.bukkit.claims.visuals;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class VisualLocation {
	private final int x;
	private final int y;
	private final int z;

	public VisualLocation(int _x, int _y, int _z){
		this.x = _x;
		this.y = _y;
		this.z = _z;
	}
	
	public VisualLocation(Block constructorBlock){
		this.x = constructorBlock.getX();
		this.y = constructorBlock.getY();
		this.z = constructorBlock.getZ();
	}

	public void sendVisual(Player thePlayer, Material theMaterial){
		sendVisual(thePlayer, thePlayer.getWorld(), theMaterial, (byte) 0);
	}

	public void sendVisual(Player thePlayer, World theWorld, Material theMaterial){
		sendVisual(thePlayer, theWorld, theMaterial, (byte) 0);
	}

	public void sendVisual(Player thePlayer, World theWorld, Material theMaterial, byte theData){
		thePlayer.sendBlockChange(theWorld.getBlockAt(x, y, z).getLocation(), theMaterial, theData);
	}
	
	public void resetVisual(Player thePlayer, World theWorld){
		Block theWorldBlock = theWorld.getBlockAt(x, y, z);
		sendVisual(thePlayer, theWorld, theWorldBlock.getType(), theWorldBlock.getData());
	}
}
