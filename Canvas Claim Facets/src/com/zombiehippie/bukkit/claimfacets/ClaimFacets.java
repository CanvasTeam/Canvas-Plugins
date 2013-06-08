package com.zombiehippie.bukkit.claimfacets;

import java.util.ArrayList;
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
import com.zombiehippie.bukkit.claims.visuals.ClaimVisual;
import com.zombiehippie.bukkit.claims.visuals.ClaimVisual.ResultType;

public class ClaimFacets extends JavaPlugin implements Listener {
	// Quick lookup of FacetRegions
	private static TreeMap<Integer, FacetRegion> lookupRegion = new TreeMap<Integer, FacetRegion>();
	public static final int BORDERS = 1; // Modifiable
	public static final int SIDE = 9; // Modifiable

	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(this, this);
		pm.registerEvents(new FacetListeners(), this);

	}

	@Override
	public void onDisable() {

	}

	public static Integer blockToRegionCoord(int blockCoord) {
		blockCoord = (blockCoord - blockCoord % 500) / 500;
		return blockCoord;
	}

	private static int regionCode(int x, int z) {
		x = blockToRegionCoord(x);
		z = blockToRegionCoord(z);
		x *= 10000;
		x += z;
		return x;
	}

	private FacetRegion lookupRegion(int X, int Z) {
		return lookupRegion(regionCode(X, Z));
	}

	private static FacetRegion lookupRegion(int code) {
		if (lookupRegion.containsKey(code)) {
			return lookupRegion.get(code);
		} else {
			FacetRegion new_region = new FacetRegion();
			lookupRegion.put(code, new_region);
			return lookupRegion(code);
		}
	}

	public Quadrant lookupFacetQuadrant(int X, int Z) {
		FacetRegion fr = lookupRegion(X, Z);

		for (Facet facet : fr.getFacets()) {
			Quadrant facetQuad = facet.getFacetQuadrant(X, Z);
			if (facetQuad != null)
				return facetQuad;
		}

		return null;
	}

	public Facet lookupFacet(int X, int Z) {
		FacetRegion fr = lookupRegion(X, Z);

		for (Facet facet : fr.getFacets()) {
			Quadrant facetQuad = facet.getFacetQuadrant(X, Z);
			if (facetQuad != null)
				return facet;
		}

		return null;
	}

	public static void registerFacet(Facet facet) {
		int X = facet.getX();
		int Z = facet.getZ();

		// Store regions this facet is included in
		ArrayList<Integer> regionCodes = new ArrayList<Integer>();
		regionCodes.add(regionCode(X, Z));

		if (!regionCodes.contains(regionCode(X - 1, Z - 1)))
			regionCodes.add(regionCode(X - 1, Z - 1));

		if (!regionCodes.contains(regionCode(X - 1, Z)))
			regionCodes.add(regionCode(X - 1, Z));

		if (!regionCodes.contains(regionCode(X, Z - 1)))
			regionCodes.add(regionCode(X, Z - 1));

		for (int code : regionCodes)
			lookupRegion(code).registerFacet(facet);

		getWorld()
				.getHighestBlockAt(X, Z)
				.getRelative(0, -1, 0)
				.setType(
						facet.isQuadrantAvailable(Quadrant.NE) ? Material.DIAMOND_BLOCK
								: Material.IRON_BLOCK);
		getWorld()
				.getHighestBlockAt(X - 1, Z)
				.getRelative(0, -1, 0)
				.setType(
						facet.isQuadrantAvailable(Quadrant.NW) ? Material.DIAMOND_BLOCK
								: Material.IRON_BLOCK);
		getWorld()
				.getHighestBlockAt(X - 1, Z - 1)
				.getRelative(0, -1, 0)
				.setType(
						facet.isQuadrantAvailable(Quadrant.SW) ? Material.DIAMOND_BLOCK
								: Material.IRON_BLOCK);
		getWorld()
				.getHighestBlockAt(X, Z - 1)
				.getRelative(0, -1, 0)
				.setType(
						facet.isQuadrantAvailable(Quadrant.SE) ? Material.DIAMOND_BLOCK
								: Material.IRON_BLOCK);
	}

	public static World getWorld() {
		return Bukkit.getWorlds().get(0);
	}
/*
	private void unregisterClaimFacets(int theClaimId) {
		BorderedClaim borderedClaim = (BorderedClaim) CanvasClaims
				.getClaim(theClaimId);
		int n = borderedClaim.getN() + BORDERS;
		int s = borderedClaim.getS() - BORDERS;
		int w = borderedClaim.getW() - BORDERS;
		int e = borderedClaim.getE() + BORDERS;

		lookupFacet(w, n).unuseQuadrant(Quadrant.SE);
		lookupFacet(e, n).unuseQuadrant(Quadrant.SW);
		lookupFacet(w, s).unuseQuadrant(Quadrant.NE);
		lookupFacet(e, s).unuseQuadrant(Quadrant.NW);
	}
*/

	static void registerClaimFacets(BorderedClaim theBorderedClaim) {

		// Create the Facets as to spread the boundaries out

		int n = theBorderedClaim.getN() + BORDERS;
		int s = theBorderedClaim.getS() - BORDERS;
		int w = theBorderedClaim.getW() - BORDERS;
		int e = theBorderedClaim.getE() + BORDERS;

		System.out.println("N:S:W:E;" + n + ":" + s + ":" + w + ":" + e);

		registerFacet(new Facet(w, n, Quadrant.NW));
		registerFacet(new Facet(e, n, Quadrant.NE));
		registerFacet(new Facet(w, s, Quadrant.SW));
		registerFacet(new Facet(e, s, Quadrant.SE));

	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getMaterial() == Material.DIAMOND_SPADE) {
				int x = event.getClickedBlock().getX();
				int z = event.getClickedBlock().getZ();
				Quadrant quad = lookupFacetQuadrant(x, z);
				if (quad == null) {
					if (event.getPlayer().isOp()) {
						registerFacet(new Facet(x, z, Quadrant.NE, true));
						event.getPlayer().sendMessage(
								ChatColor.GREEN + "New Facet established!");
					} else {
						event.getPlayer().sendMessage(
								"Click a Diamond Block to make a claim!");
					}
					return;
				}
				if (quad.available) {
					int x2 = x;
					int z2 = z;
					switch (quad) {
					case NE:
						x2 += SIDE;
						z2 += SIDE;
						x += BORDERS;
						z += BORDERS;
						break;
					case NW:
						x2 -= SIDE;
						z2 += SIDE;
						x -= BORDERS;
						z += BORDERS;
						break;
					case SE:
						x2 += SIDE;
						z2 -= SIDE;
						x += BORDERS;
						z -= BORDERS;
						break;
					case SW:
						x2 -= SIDE;
						z2 -= SIDE;
						x -= BORDERS;
						z -= BORDERS;
						break;
					}

					CanvasClaims.instance.createClaim(event.getPlayer(),
							event.getClickedBlock().getWorld()
									.getHighestBlockAt(x, z).getLocation(),
							new Location(event.getPlayer().getWorld(), x2, 0,
									z2));
				}
				String msg = "Facet: ";
				msg += quad != null ? quad.toString() : "NULL";
				event.getPlayer().sendMessage(msg);
			} else if (event.getMaterial() == Material.BOOK) {
				Claim theClaimClicked = CanvasClaims.getClaimAt(event
						.getClickedBlock());
				if (theClaimClicked != null) {
					new ClaimVisual(event.getPlayer(), theClaimClicked,
							ResultType.INFO);
				} else {
					ClaimVisual
							.resetPlayersVisuals(event.getPlayer().getName());
				}
			}
		}
	}
}
