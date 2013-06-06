package com.zombiehippie.bukkit.claimfacets;

import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zombiehippie.bukkit.claimfacets.Facet.Quadrant;
import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.Claim;
import com.zombiehippie.bukkit.claims.events.ClaimLoadEvent;
import com.zombiehippie.bukkit.claims.events.PlayerClaimEvent;

public class ClaimFacets extends JavaPlugin implements Listener {
	// Quick lookup of FacetRegions
	TreeMap<Integer, FacetRegion> lookupRegion = new TreeMap<Integer, FacetRegion>();
	
	
	@Override
	public void onEnable(){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(this, this);
		
		
	}
	@Override
	public void onDisable(){
		
	}
	
	public Integer blockToRegionCoord(int blockCoord){
		blockCoord = (blockCoord - blockCoord%500)/500;
		return blockCoord;
	}
	private int regionCode(int x, int z){
		x = blockToRegionCoord(x);
		z = blockToRegionCoord(z);
		x*=10000;
		x+=z;
		return x;
	}
	
	private FacetRegion lookupRegion(int X, int Z){
		return lookupRegion(regionCode(X,Z));
	}
	private FacetRegion lookupRegion(int code){
		if(lookupRegion.containsKey(code)) {
			return lookupRegion.get(code);
		} else {
			FacetRegion new_region = new FacetRegion();
			lookupRegion.put(code, new_region);
			return lookupRegion(code);
		}
	}
	public Quadrant lookupFacet(int X, int Z){
		FacetRegion fr = lookupRegion(X,Z);
		
		for(Facet facet : fr.getFacets()){
			Quadrant facetQuad = facet.getFacetQuadrant(X,Z);
			if(facetQuad!=null)
				return facetQuad;
		}
		
		return null;
	}
	
	public void registerFacet(Facet facet){
		int c = regionCode(facet.getX(),facet.getZ());
		FacetRegion fr = lookupRegion(c);
		fr.registerFacet(facet);
		int x = facet.getX();
		int z = facet.getZ();

		getWorld()
		.getHighestBlockAt(x, z).getRelative(0,-1,0)
		.setType(facet.isQuadrantAvailable(Quadrant.NE)?Material.DIAMOND_BLOCK:Material.IRON_BLOCK);
		getWorld()
		.getHighestBlockAt(x-1,z).getRelative(0,-1,0)
		.setType(facet.isQuadrantAvailable(Quadrant.NW)?Material.DIAMOND_BLOCK:Material.IRON_BLOCK);
		getWorld()
		.getHighestBlockAt(x-1, z-1).getRelative(0,-1,0)
		.setType(facet.isQuadrantAvailable(Quadrant.SW)?Material.DIAMOND_BLOCK:Material.IRON_BLOCK);
		getWorld()
		.getHighestBlockAt(x, z-1).getRelative(0,-1,0)
		.setType(facet.isQuadrantAvailable(Quadrant.SE)?Material.DIAMOND_BLOCK:Material.IRON_BLOCK);
	}
	
	public static World getWorld(){
		return Bukkit.getWorlds().get(0);
	}
	
	@EventHandler
	public void onClaimCreation(PlayerClaimEvent event) {
		if(event.isCancelled())
			return;
		registerClaimFacets(event.getClaim());
	}
	
	@EventHandler
	public void onClaimLoad(ClaimLoadEvent event) {
		registerClaimFacets(event.getClaim());
	}
	
	private void registerClaimFacets(Claim theClaim) {
				
		int n = theClaim.getNorthBoundary();
		int s = theClaim.getSouthBoundary();
		int w = theClaim.getWestBoundary();
		int e = theClaim.getEastBoundary();

		System.out.println("N:S:W:E;"+n+":"+s+":"+w+":"+e);
		
		registerFacet(new Facet(w,n,Quadrant.NW));
		registerFacet(new Facet(e,n,Quadrant.NE));
		registerFacet(new Facet(w,s,Quadrant.SW));
		registerFacet(new Facet(e,s,Quadrant.SE));
		
	}
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		if(event.getAction()==Action.RIGHT_CLICK_BLOCK){
			if(event.getMaterial()==Material.DIAMOND_SPADE) {
				int x = event.getClickedBlock().getX();
				int z = event.getClickedBlock().getZ();
				Quadrant quad = lookupFacet(x,z);
				if(quad == null){
					if(event.getPlayer().isOp()){
						registerFacet(new Facet(x,z,Quadrant.NE,true));
						event.getPlayer().sendMessage(ChatColor.GREEN + "New Facet established!");
					} else {
						event.getPlayer().sendMessage("Click a facet to make a claim!");
					}
					return;
				}
				if(quad.available){
					int x2 = x;
					int z2 = z;
					switch(quad){
					case NE:
						x2+=7;
						z2+=7;
						break;
					case NW:
						x2-=7;
						z2+=7;
						break;
					case SE:
						x2+=7;
						z2-=7;
						break;
					case SW:
						x2-=7;
						z2-=7;
						break;
					}
					
					CanvasClaims.instance.createClaim(event.getPlayer()
							, event.getClickedBlock().getLocation()
							, new Location(event.getPlayer().getWorld(),x2,0,z2));
				}
				String msg = "Facet: ";
				msg+= quad!=null?quad.toString():"NULL";
				event.getPlayer().sendMessage(msg);
			} else if(event.getMaterial()==Material.BOOK){
				
			}
		}
	}
}
