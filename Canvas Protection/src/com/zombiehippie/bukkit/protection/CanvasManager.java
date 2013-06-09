package com.zombiehippie.bukkit.protection;

import java.util.HashMap;

import org.bukkit.block.Block;

public class CanvasManager {
	private final CanvasProtection plugin;

	private HashMap<Integer,Integer> codeToOwnerId = new HashMap<Integer,Integer>();
	private HashMap<Integer,String> ownerIdToOwner = new HashMap<Integer,String>();
	
	public CanvasManager(CanvasProtection thisPlugin){
		plugin = thisPlugin;
	}

	public String getCanvasFromBlock(Block block){
		int code = getCodeFromBlock(block);
		
		if(!codeToOwnerId.containsKey(code))
			return null;
		
		int ownerId = codeToOwnerId.get(code);
		
		return ownerIdToOwner.get(ownerId);
	}

	private int getCodeFromBlock(Block block){
		int x = block.getX();
		int z = block.getZ();

		x = (x - x%8)/8;
		z = (z - z%8)/8;
		x*=10000;
		return x+z;
	}

	public boolean canBuild(String playerName, Block block) {
		return playerName.equals(getCanvasFromBlock(block));
	}

	public void register(String name, Block clickedBlock) {
		int code = getCodeFromBlock(clickedBlock);
		
		if(codeToOwnerId.containsKey(code)){
			System.out.println("Occuppied!");
		} else {
			int id = getUniqueId();
			codeToOwnerId.put(code,id);
			ownerIdToOwner.put(id, name);
		}
		
	}
	private int getUniqueId(){
		// Find a unique id
		int new_id = (int)(Math.random() * 99999);
		
		while(this.ownerIdToOwner.containsKey(new_id)) {
			new_id = (int)(Math.random() * 99999);
		}
		
		return new_id;
	}
}
