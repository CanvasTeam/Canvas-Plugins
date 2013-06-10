package com.zombiehippie.bukkit.claims.visuals;

import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.zombiehippie.bukkit.claims.Claim;

public class Visuallization {
	private static HashMap<String, Visuallization> playersWithVisuals = new HashMap<String, Visuallization>();
	private final LinkedList<VisualLocation> blocks;
	private final Player player;
	private final String worldName;
	private final VisualType visualType;

	public Visuallization(Player thePlayerSeeing, VisualType theType,
			LinkedList<VisualLocation> theBlocks, String theWorldName, String ownerName) {
		player = thePlayerSeeing;
		worldName = theWorldName;
		blocks = theBlocks;
		visualType = theType;

		resetPlayerVisuals(thePlayerSeeing.getName());
		playersWithVisuals.put(thePlayerSeeing.getName(), this);

		sendVisuals();
		
		thePlayerSeeing.sendMessage(theType.getMessageToPlayer().replaceFirst("$ownerName", ownerName));
	}

	public static void applyClaimVisualsToPlayer(Player thePlayer,
			Claim[] theClaims, VisualType theTypeOfVisual) {
		
		new Visuallization(thePlayer, theTypeOfVisual, getOutlines(theClaims),
				theClaims[0].getWorldName(), theClaims[0].getOwnerName());

	}

	private static LinkedList<VisualLocation> getOutlines(Claim[] claims) {
		LinkedList<VisualLocation> locations = new LinkedList<VisualLocation>();

		LinkedList<XZLocation> claimOutlineLocations = new LinkedList<XZLocation>();

		LinkedList<XZLocation> excludeLocations = new LinkedList<XZLocation>();

		// Perhaps necessary to check owner names for different outlining methods
		
		for (Claim claimToGetOutlines : claims) {

			int N = claimToGetOutlines.getNorthBoundary();
			int S = claimToGetOutlines.getSouthBoundary();
			int W = claimToGetOutlines.getWestBoundary();
			int E = claimToGetOutlines.getEastBoundary();

			for (int dx = 0; dx <= E - W; dx++) {
				int x = W + dx;
				claimOutlineLocations.add(new XZLocation(x, N));
				claimOutlineLocations.add(new XZLocation(x, S));
				if (dx != 0 && dx != E - W) {
					excludeLocations.add(new XZLocation(x, N + 1));
					excludeLocations.add(new XZLocation(x, S - 1));
				}
			}
			for (int dz = 0; dz <= N - S; dz++) {
				int z = S + dz;
				claimOutlineLocations.add(new XZLocation(E, z));
				claimOutlineLocations.add(new XZLocation(W, z));
				if (dz != 0 && dz != N - S) {
					excludeLocations.add(new XZLocation(E + 1, z));
					excludeLocations.add(new XZLocation(W - 1, z));
				}
			}
		}

		claimOutlineLocations.removeAll(excludeLocations);

		World theClaimWorld = Bukkit.getWorld(claims[0].getWorldName());

		for (XZLocation XZLoc : claimOutlineLocations) {
			Block theBlock = theClaimWorld.getHighestBlockAt(XZLoc.x, XZLoc.z);

			while (!theBlock.getType().isOccluding()) {
				theBlock = theBlock.getRelative(0, -1, 0);
			}

			VisualLocation theVisualLocation = new VisualLocation(theBlock);
			locations.add(theVisualLocation);
		}

		return locations;
	}

	public static void resetPlayerVisuals(String thePlayerName) {
		if (playersWithVisuals.containsKey(thePlayerName)) {
			playersWithVisuals.get(thePlayerName).resetVisuals();
			playersWithVisuals.remove(thePlayerName);
		}
	}

	private void resetVisuals() {
		World theVisualWorld = Bukkit.getWorld(worldName);

		if (!player.getWorld().equals(theVisualWorld))
			return;

		for (VisualLocation location : blocks)
			location.resetVisual(player, theVisualWorld);
	}

	private void sendVisuals() {
		World theVisualWorld = Bukkit.getWorld(worldName);

		if (!player.getWorld().equals(theVisualWorld))
			return;

		for (VisualLocation location : blocks)
			location.sendVisual(player, visualType.getOutlineMaterial());
	}

}
