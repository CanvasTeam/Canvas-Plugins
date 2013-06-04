package com.zombiehippie.bukkit.claimfacets;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zombiehippie.bukkit.claimfacets.listeners.ClaimListener;

public class ClaimFacets extends JavaPlugin {
	private LinkedList<Facet> facets = new LinkedList<Facet>();
	
	@Override
	public void onEnable(){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new ClaimListener(), this);
		
		
	}
	@Override
	public void onDisable(){
		// This is how we PUSH to github
	}
}
