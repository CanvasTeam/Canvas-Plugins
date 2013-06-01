package com.zombiehippie.bukkit.painter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zombiehippie.bukkit.painter.listeners.PlayerListeners;

public class CanvasPainter extends JavaPlugin {
	@Override
	public void onEnable(){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerListeners(), this);
	}
	@Override
	public void onDisable() {
		
	}

}
