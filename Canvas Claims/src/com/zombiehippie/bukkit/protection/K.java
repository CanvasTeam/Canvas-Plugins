package com.zombiehippie.bukkit.protection;

import com.zombiehippie.bukkit.protection.visuals.XZLocation;

public class K {
	public final static int canvasSide = 6;
	public final static int visualTimeout = 600;

	public static Integer getCodeFromBlockXZLocation(XZLocation blockXZ) {
		blockXZ.x = (blockXZ.x - blockXZ.x % K.canvasSide) / K.canvasSide;
		blockXZ.z = (blockXZ.z - blockXZ.z % K.canvasSide) / K.canvasSide;

		return getCodeFromCanvasXZLocation(blockXZ);
	}

	public static int getCodeFromCanvasXZLocation(XZLocation canvasXZ) {
		return canvasXZ.x * 10000 + canvasXZ.z;
	}

	public static XZLocation getBlockXZLocationFromCode(int locationCode) {
		XZLocation returnXZ = getCanvasXZLocationFromCode(locationCode);
		returnXZ.x *= K.canvasSide;
		returnXZ.z *= K.canvasSide;

		return returnXZ;
	}

	public static XZLocation getCanvasXZLocationFromCode(int locationCode) {
		int locationX = (locationCode - locationCode % 10000) / 10000;
		int locationZ = locationCode % 10000;

		return new XZLocation(locationX, locationZ);
	}
}
