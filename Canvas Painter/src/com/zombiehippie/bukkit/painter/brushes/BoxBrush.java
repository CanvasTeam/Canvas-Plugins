package com.zombiehippie.bukkit.painter.brushes;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;

public class BoxBrush extends Brush {
	Location leftClick;
	Location rightClick;
	public BoxBrush() {
		super("boxbrush");
		
	}

	@Override
	public void interact(PlayerInteractEvent event) {
		boolean leftClick = false;
		Location set;
		switch(event.getAction()){
		case LEFT_CLICK_AIR:
			leftClick = true;
		case RIGHT_CLICK_AIR:
			set = event.getPlayer().getTargetBlock(null, 150).getLocation();
			break;
		case LEFT_CLICK_BLOCK:
			leftClick = true;
		case RIGHT_CLICK_BLOCK:
			set = event.getClickedBlock().getLocation();
			break;
			default:
				return;
		}
		if(leftClick){
			this.leftClick = set;
		} else {
			this.rightClick = set;
		}
		
		if(this.leftClick != null && this.rightClick != null){
			execute();
		}
	}

	@Override
	public void execute() {
		// draw box
		
	}

}
