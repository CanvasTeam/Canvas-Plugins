package com.zombiehippie.bukkit.claimfacets;

import java.util.LinkedList;

import com.zombiehippie.bukkit.claimfacets.Facet.Quadrant;

public class FacetRegion {
	private LinkedList<Facet> facets = new LinkedList<Facet>();

	public LinkedList<Facet> getFacets() {
		return facets;
	}
	
	public Facet lookupFacet(int ptX, int ptZ){
		for(Facet facet : facets){
			Quadrant q = facet.getFacetQuadrant(ptX, ptZ);
			if(q != null){
				return facet;
			}
		}
		
		return null;
	}
	public Quadrant lookupFacetQuadrant(int ptX, int ptZ){
		for(Facet facet : facets){
			Quadrant q = facet.getFacetQuadrant(ptX, ptZ);
			if(q != null){
				return q;
			}
		}
		
		return null;
	}

	public void registerFacet(Facet facet) {
		Facet looked = lookupFacet(facet.getX(),facet.getZ());
		
		if(looked != null) {
			looked.useQuadrants(facet);
		} else {
			this.facets.add(facet);
		}
		
	}
	
}
