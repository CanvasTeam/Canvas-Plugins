package com.zombiehippie.bukkit.claimfacets;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FacetChangeEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Facet facet;

	public FacetChangeEvent(Facet theFacet) {
		this.facet = theFacet;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Facet getFacet() {
		return facet;
	}
}
