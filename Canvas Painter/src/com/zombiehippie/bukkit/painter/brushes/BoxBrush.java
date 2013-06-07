package com.zombiehippie.bukkit.painter.brushes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.player.PlayerInteractEvent;

public class BoxBrush extends Brush {
	Location leftClick;
	Location rightClick;
	public BoxBrush() {
		super("boxbrush");
	}

	@Override
	public void interact(PlayerInteractEvent event) {
		boolean isLeftClick = false;
		Location set;
		switch(event.getAction()){
		case LEFT_CLICK_AIR:
			isLeftClick = true;
		case RIGHT_CLICK_AIR:
			set = event.getPlayer().getTargetBlock(null, 150).getLocation();
			break;
		case LEFT_CLICK_BLOCK:
			isLeftClick = true;
		case RIGHT_CLICK_BLOCK:
			set = event.getClickedBlock().getLocation();
			break;
		default:
			return;
		}
		if(isLeftClick){
			this.leftClick = set;
			event.getPlayer().sendMessage("Pt 1 for BoxBrush");
		} else {
			this.rightClick = set;
			event.getPlayer().sendMessage("Pt 2 for BoxBrush");
		}
		
		if(this.leftClick != null && this.rightClick != null){
			execute();
			event.getPlayer().sendMessage("Execute BoxBrush");
			this.leftClick = null;
			this.rightClick = null;
		}
	}

	@Override
	public void execute() {
		World world = leftClick.getWorld();
		int x1 = leftClick.getBlockX();
		int y1 = leftClick.getBlockY();
		int z1 = leftClick.getBlockZ();
		int x2 = rightClick.getBlockX();
		int y2 = rightClick.getBlockY();
		int z2 = rightClick.getBlockZ();
		
		int i = x1<x2?x1:x2;
		int i2 = x1>x2?x1:x2;
		
		int j = y1<y2?y1:y2;
		int j2 = y1>y2?y1:y2;
		
		int k = z1<z2?z1:z2;
		int k2 = z1>z2?z1:z2;
		
		System.out.println("i,i2:j,j2:k,k2;"+i+","+i2+":"+j+","+j2+":"+k+","+k2);
		
		for(int x = i;x<=i2;x++){
			for(int y = j;y<=j2;y++){
				for(int z=k;z<=k2;z++){
					world.getBlockAt(x, y, z).setType(Material.STONE);
				}
			}
		}
		
	}

}
