package com.zombiehippie.bukkit.claimfacets;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.zombiehippie.bukkit.claimfacets.Facet.Quadrant;
import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.Claim;
import com.zombiehippie.bukkit.claims.events.ClaimAfterAddEvent;
import com.zombiehippie.bukkit.claims.events.ClaimBeforeAddEvent;

public class FacetListeners implements Listener{
	private static int BORDERS = ClaimFacets.BORDERS;
	
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onClaimBeforeAdd(ClaimBeforeAddEvent event) {
		System.out.println("ClaimFacets BeforeAdd");
		BorderedClaim newBorderedClaim = new BorderedClaim(event.getClaim());
		event.setClaim(newBorderedClaim);
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onClaimAfterAdd(ClaimAfterAddEvent event) {
		System.out.println("ClaimFacets AfterAdd");
		BorderedClaim theBorderedClaim = (BorderedClaim) CanvasClaims
				.getClaim(event.getClaimId());
		ClaimFacets.registerClaimFacets(theBorderedClaim);
	}
	@EventHandler
	// Try to join claims together
	public void onFacetChange(FacetChangeEvent event) {
		Facet changedFacet = event.getFacet();
		int X = changedFacet.getX();
		int Z = changedFacet.getZ();

		Claim NEClaim = null;
		if (!changedFacet.isQuadrantAvailable(Quadrant.NE))
			NEClaim = CanvasClaims.getClaimAt(X + BORDERS, Z + BORDERS);

		Claim SEClaim = null;
		if (!changedFacet.isQuadrantAvailable(Quadrant.SE))
			SEClaim = CanvasClaims.getClaimAt(X + BORDERS, Z - 1 - BORDERS);

		Claim SWClaim = null;
		if (!changedFacet.isQuadrantAvailable(Quadrant.SW))
			SWClaim = CanvasClaims.getClaimAt(X - 1 - BORDERS, Z - 1 - BORDERS);

		Claim NWClaim = null;
		if (!changedFacet.isQuadrantAvailable(Quadrant.NW))
			NWClaim = CanvasClaims.getClaimAt(X - 1 - BORDERS, Z + BORDERS);

		if (NEClaim != null && SEClaim != null
				&& NEClaim.getOwnerName().equals(SEClaim.getOwnerName())) {
			((BorderedClaim) NEClaim).setSouthBorder(BORDERS);
			((BorderedClaim) SEClaim).setNorthBorder(BORDERS);
			System.out.println("NE + SE");
		}

		if (SEClaim != null && SWClaim != null
				&& SEClaim.getOwnerName().equals(SWClaim.getOwnerName())) {
			((BorderedClaim) SEClaim).setWestBorder(BORDERS);
			((BorderedClaim) SWClaim).setEastBorder(BORDERS);
			System.out.println("SE + SW");
		}

		if (SWClaim != null && NWClaim != null
				&& SWClaim.getOwnerName().equals(NWClaim.getOwnerName())) {
			((BorderedClaim) SWClaim).setNorthBorder(BORDERS);
			((BorderedClaim) NWClaim).setSouthBorder(BORDERS);
			System.out.println("SW + NW");
		}

		if (NWClaim != null && NEClaim != null
				&& NWClaim.getOwnerName().equals(NEClaim.getOwnerName())) {
			((BorderedClaim) NWClaim).setEastBorder(BORDERS);
			((BorderedClaim) NEClaim).setWestBorder(BORDERS);
			System.out.println("NW + NE");
		}

	}
}
