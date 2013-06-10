package com.zombiehippie.bukkit.protection.visuals;

import java.util.LinkedList;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.zombiehippie.bukkit.protection.K;

public class PlayerVisual {
	private final LinkedList<VisualLocation> blocks;
	private final Player player;
	private final World world;
	private final VisualType visualType;

	public PlayerVisual(Player thePlayerSeeing, VisualType theType,
			Integer[] canvasLocationCodes) {
		player = thePlayerSeeing;
		world = thePlayerSeeing.getWorld();
		visualType = theType;

		blocks = getOutlines(canvasLocationCodes);

		resetVisuals();

		sendVisuals();
	}

	private LinkedList<VisualLocation> getOutlines(Integer[] canvasLocationCodes) {
		LinkedList<VisualLocation> locations = new LinkedList<VisualLocation>();

		LinkedList<XZLocation> canvasOutlineLocations = new LinkedList<XZLocation>();

		LinkedList<XZLocation> excludeLocations = new LinkedList<XZLocation>();

		LinkedList<XZLocation> cornerExcludeLocations = new LinkedList<XZLocation>();

		// Perhaps necessary to check owner names for different outlining
		// methods

		for (int locationCode : canvasLocationCodes) {
			XZLocation location = K.getBlockXZLocationFromCode(locationCode);

			int E = location.x + K.canvasSide-1;
			int W = location.x;
			int N = location.z + K.canvasSide-1;
			int S = location.z;

			for (int dx = 0; dx <= E - W; dx++) {
				int x = W + dx;
				canvasOutlineLocations.add(new XZLocation(x, N));
				canvasOutlineLocations.add(new XZLocation(x, S));

				XZLocation exclusionNorth = new XZLocation(x, N + 1);
				XZLocation exclusionSouth = new XZLocation(x, S - 1);

				if (dx != 0 && dx != E - W) {
					excludeLocations.add(exclusionNorth);
					excludeLocations.add(exclusionSouth);
				} else {
					if (cornerExcludeLocations.contains(exclusionNorth)) {
						excludeLocations.add(exclusionNorth);
					} else {
						cornerExcludeLocations.add(exclusionNorth);
					}

					if (cornerExcludeLocations.contains(exclusionSouth)) {
						excludeLocations.add(exclusionSouth);
					} else {
						cornerExcludeLocations.add(exclusionSouth);
					}
				}
			}

			for (int dz = 0; dz <= N - S; dz++) {
				int z = S + dz;
				canvasOutlineLocations.add(new XZLocation(E, z));
				canvasOutlineLocations.add(new XZLocation(W, z));

				XZLocation exclusionEast = new XZLocation(E + 1, z);
				XZLocation exclusionWest = new XZLocation(W - 1, z);

				if (dz != 0 && dz != N - S) {
					excludeLocations.add(exclusionEast);
					excludeLocations.add(exclusionWest);
				} else {

					if (cornerExcludeLocations.contains(exclusionEast)) {
						excludeLocations.add(exclusionEast);
					} else {
						cornerExcludeLocations.add(exclusionEast);
					}

					if (cornerExcludeLocations.contains(exclusionWest)) {
						excludeLocations.add(exclusionWest);
					} else {
						cornerExcludeLocations.add(exclusionWest);
					}
				}
			}
		}

		canvasOutlineLocations.removeAll(excludeLocations);

		for (XZLocation XZLoc : canvasOutlineLocations) {
			Block theBlock = world.getHighestBlockAt(XZLoc.x, XZLoc.z);

			while (!theBlock.getType().isOccluding()) {
				theBlock = theBlock.getRelative(0, -1, 0);
			}

			VisualLocation theVisualLocation = new VisualLocation(player,
					theBlock);
			locations.add(theVisualLocation);
		}

		return locations;
	}

	void resetVisuals() {
		if (!player.getWorld().equals(world))
			return;

		for (VisualLocation location : blocks)
			location.resetVisual();
	}

	private void sendVisuals() {
		if (!player.getWorld().equals(world))
			return;

		for (VisualLocation location : blocks)
			location.sendVisual(visualType.getOutlineMaterial());
	}
}
