package com.zombiehippie.bukkit.painter.brushes;

import org.bukkit.event.player.PlayerInteractEvent;

public abstract class Brush {
	protected final String name;
	public Brush(String brushName){
		this.name = brushName;
	}
	
	/**
	 * Let the brush handle the interact event
	 * @param event triggered
	 */
	public abstract void interact(PlayerInteractEvent event);
	
	/**
	 * Executes the brush's task
	 */
	public abstract void execute();
	
	/**
	 * Get the brush name
	 * @return brush name
	 */
	public String getName(){
		return name;
	}
}
