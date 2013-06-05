package com.zombiehippie.bukkit.claimfacets;
/**
 * A facet is a "snapping point" that can help users establish
 * their land claims, facets are 2x2 squares that establish the
 * claim corners thus resulting in closer together flush claims
 * with less dead space.
 * 
 * @author Cole
 *
 */
public class Facet {
	public enum Quadrant { 
		NW(true,true,true),
		NE(true,true,false),
		SW(true,false,true),
		SE(true,false,false),
		UNW(false,true,true),
		UNE(false,true,false),
		USW(false,false,true),
		USE(false,false,false);
		final boolean available;
		final boolean NS;
		final boolean WE;
		Quadrant(boolean isAvailable, boolean isNnotS, boolean isWnotE){
			available = isAvailable;
			NS = isNnotS;
			WE = isWnotE;
		}
	};
	
	// These are the open areas of the facet
	private boolean NW = true;
	private boolean NE = true;
	private boolean SW = true;
	private boolean SE = true;
	
	// The upper left position of the facet
	private final int NEptX;
	private final int NEptZ;
	
	/**
	 * Establish a new facet
	 * 
	 * @param cornerX the X location of the claim's corner
	 * @param cornerZ the Z location of the claim's corner
	 * @param quad
	 */
	public Facet (int cornerX, int cornerZ, Quadrant quad) {
		int neptX = cornerX;
		int neptZ = cornerZ;
		
		// Set the availability of certain positions on the facet to false
		// Then calculate the NWpt location based on the relative location
		//    of the claim's corner
		switch(quad){
		case NW:
			SE = false;
			// N
			neptZ++;
			break;
		case NE:
			SW = false;
			// E
			neptX++;
			// N
			neptZ++;
			break;
		case SW:
			break;
		case SE:
			NW = false;
			// E
			neptX++;
			// No changes to the Facet's corner location
			break;
		}
		NEptX = neptX;
		NEptZ = neptZ;
	}
	
	public Quadrant getFacetQuadrant(int ptX, int ptZ){
		int quad=0;
		switch(NEptZ-ptZ){
		case 1: // SOUTH
			quad+=1;
		case 0: // NORTH
			break;
			default:
				return null;
		}
		switch(NEptX-ptX){
		case 1: // WEST
			quad+=10;
		case 0: // EAST
			break;
			default:
				return null;
			
		}
		
		switch(quad){
		case 0: // 0 0 NE
			return NE?Quadrant.NE:Quadrant.UNE;
		case 1: // 1 0 SE
			return SE?Quadrant.SE:Quadrant.USE;
		case 10:// 0 1 NW
			return NW?Quadrant.NW:Quadrant.UNW;
		case 11:// 1 1 SW
			return SW?Quadrant.SW:Quadrant.USW;
		}
		
		
		// return null if facet unavailable
		return null;
	}
	
	public void useQuadrants(Facet facet){
		NW = facet.NW && this.NW;
		NE = facet.NE && this.NE;
		SW = facet.SW && this.SW;
		SE = facet.SE && this.SE;
	}
	
	public int getX(){
		return NEptX;
	}	
	public int getZ(){
		return NEptZ;
	}
}
