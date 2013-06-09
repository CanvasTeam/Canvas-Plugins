package com.zombiehippie.bukkit.protection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.zombiehippie.bukkit.protection.listeners.PlayerListener;
import com.zombiehippie.bukkit.protection.listeners.WorldListener;

public class CanvasProtection extends JavaPlugin implements Listener {
	private CanvasManager canvasManager;

	@Override
	public void onEnable() {
		canvasManager = new CanvasManager(this);

		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(
				new PlayerListener(canvasManager), this);
		Bukkit.getPluginManager().registerEvents(
				new WorldListener(canvasManager), this);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getMaterial() == Material.DIAMOND_SPADE) {
				canvasManager.register(event.getPlayer().getName(),
						event.getClickedBlock());
			} else if (event.getMaterial() == Material.BOOK) {
				Player thePlayer = event.getPlayer();
				thePlayer.sendMessage("Claimed by "
						+ canvasManager.getCanvasFromBlock(event
								.getClickedBlock()));
			}
		}

	}

}
