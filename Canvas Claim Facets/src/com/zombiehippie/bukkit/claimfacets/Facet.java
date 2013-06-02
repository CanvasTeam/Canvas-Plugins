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
	public enum Quadrant { NW, NE, SW, SE };
	
	// These are the open areas of the facet
	private boolean NW = true;
	private boolean NE = true;
	private boolean SW = true;
	private boolean SE = true;
	
	// The upper left position of the facet
	private final int NWptX;
	private final int NWptZ;
	
	/**
	 * Establish a new facet
	 * 
	 * @param cornerX the X location of the claim's corner
	 * @param cornerZ the Z location of the claim's corner
	 * @param quad
	 */
	public Facet (Number cornerX, Number cornerZ, Quadrant quad) {
		int nwptX = cornerX.intValue();
		int nwptZ = cornerZ.intValue();
		
		// Set the availability of certain positions on the facet to false
		// Then calculate the NWpt location based on the relative location
		//    of the claim's corner
		switch(quad){
		case NW:
			NW = false;
			nwptX++;
			nwptZ++;
			break;
		case NE:
			NE = false;
			nwptX++;
			break;
		case SW:
			SW = false;
			nwptZ++;
			break;
		case SE:
			SE = false;
			// No changes to the Facet's corner location
			break;
		}
		NWptX = nwptX;
		NWptZ = nwptZ;
	}
	
	public Quadrant getAvailableFacet(int ptX, int ptZ){
		// TODO check if the pt is in the facet
		// and return which quadrant the available facet is in
		
		// return null if facet unavailable
		return null;
	}
}
