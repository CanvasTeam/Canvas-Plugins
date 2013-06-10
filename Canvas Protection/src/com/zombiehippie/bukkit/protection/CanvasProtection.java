package com.zombiehippie.bukkit.protection;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.zombiehippie.bukkit.protection.listeners.PlayerListener;
import com.zombiehippie.bukkit.protection.listeners.WorldListener;
import com.zombiehippie.bukkit.protection.visuals.VisualManager;
import com.zombiehippie.bukkit.protection.visuals.VisualType;
import com.zombiehippie.bukkit.protection.visuals.XZLocation;

public class CanvasProtection extends JavaPlugin implements Listener {
	private HashMap<String, Long> playerVisualTimeouts = new HashMap<String, Long>();
	private CanvasManager canvasManager;
	private VisualManager visualManager;
	

	@Override
	public void onEnable() {
		canvasManager = new CanvasManager(this);
		visualManager = new VisualManager();

		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(
				new PlayerListener(canvasManager), this);
		Bukkit.getPluginManager().registerEvents(
				new WorldListener(canvasManager), this);
		
		canvasManager.loadAll();
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block clickedBlock = event.getClickedBlock();
			
			if (event.getMaterial() == Material.DIAMOND_SPADE) {
				canvasManager.register(event.getPlayer().getName(),
						clickedBlock);
			} else if (event.getMaterial() == Material.BOOK) {
				Player thePlayer = event.getPlayer();
				
				if(playerVisualTimeouts.containsKey(thePlayer.getName())){
					long lastVisual = playerVisualTimeouts.get(thePlayer.getName());
					if(System.currentTimeMillis() - lastVisual < K.visualTimeout){
						return;
					} else {
						playerVisualTimeouts.remove(thePlayer.getName());
					}
				}
				playerVisualTimeouts.put(thePlayer.getName(), System.currentTimeMillis());
				
				XZLocation blockXZLocation = new XZLocation(clickedBlock.getX(),clickedBlock.getZ());
				Integer[] canvasLocationCodes = canvasManager.getAdjacentCanvasLocationCodes(K.getCodeFromBlockXZLocation(blockXZLocation));
				
				visualManager.sendPlayerVisuals(thePlayer, VisualType.INFORMELSEOWNED, canvasLocationCodes);
				
				thePlayer.sendMessage("Claimed by "
						+ canvasManager.getCanvasFromBlock(clickedBlock));
			}
		}
	}
}
