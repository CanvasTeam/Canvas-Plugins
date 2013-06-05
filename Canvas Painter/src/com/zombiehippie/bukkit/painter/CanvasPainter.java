package com.zombiehippie.bukkit.painter;

import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zombiehippie.bukkit.painter.brushes.Brush;

public class CanvasPainter extends JavaPlugin implements Listener{
	// PlayerName>Brush
	TreeMap<String, Brush> brushes = new TreeMap<String, Brush>();
	
	@Override
	public void onEnable(){
		PluginManager pm = Bukkit.getPluginManager();
		
		// Register the EventHandler here, onPlayerInteractEvent
		pm.registerEvents(this, this);
	}
	@Override
	public void onDisable() {
		
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Action action = event.getAction();
		
		
	}

}
