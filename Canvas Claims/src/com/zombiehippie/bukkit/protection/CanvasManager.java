package com.zombiehippie.bukkit.protection;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.zombiehippie.bukkit.protection.visuals.XZLocation;

public class CanvasManager {
	private HashMap<Integer, Integer> canvasLocationCodeToOwnerId = new HashMap<Integer, Integer>();
	private HashMap<Integer, String> ownerIdToOwner = new HashMap<Integer, String>();

	private CanvasProtection plugin;
	
	public CanvasManager(CanvasProtection thePlugin){
		this.plugin = thePlugin;
	}
	
	
	public String getCanvasFromBlock(Block block) {
		XZLocation blockXZLocation = new XZLocation(block.getX(), block.getZ());

		int code = K.getCodeFromBlockXZLocation(blockXZLocation);

		return getCanvasFromCode(code);
	}

	public String getCanvasFromCanvasLocation(XZLocation canvasLocation) {
		XZLocation canvasXZLocation = new XZLocation(canvasLocation.x,
				canvasLocation.z);

		int code = K.getCodeFromCanvasXZLocation(canvasXZLocation);

		return getCanvasFromCode(code);
	}

	private String getCanvasFromCode(int code) {
		if (!canvasLocationCodeToOwnerId.containsKey(code))
			return null;

		int ownerId = canvasLocationCodeToOwnerId.get(code);

		return ownerIdToOwner.get(ownerId);
	}

	public boolean canBuild(String playerName, Block block) {
		return playerName.equals(getCanvasFromBlock(block));
	}

	public Integer[] getAdjacentCanvasLocationCodes(int locationCode) {
		Set<Integer> locations = new HashSet<Integer>();
		Set<Integer> checkCanvasLocations = new HashSet<Integer>();
		Set<Integer> checkedCanvasLocations = new HashSet<Integer>();

		XZLocation canvasLocation = K.getCanvasXZLocationFromCode(locationCode);

		String canvasOwner = getCanvasFromCanvasLocation(canvasLocation);

		if (canvasOwner == null) {
			return locations.toArray(new Integer[] {});
		}

		checkCanvasLocations.add(locationCode);

		while (!checkCanvasLocations.isEmpty()) {
			Set<Integer> checkNextCanvasLocations = new HashSet<Integer>();

			for (int checkLocation : checkCanvasLocations) {
				if (canvasOwner.equals(getCanvasFromCode(checkLocation))) {
					locations.add(checkLocation);

					checkNextCanvasLocations.add(checkLocation + 10000);
					checkNextCanvasLocations.add(checkLocation - 10000);
					checkNextCanvasLocations.add(checkLocation + 1);
					checkNextCanvasLocations.add(checkLocation - 1);
				}
				checkedCanvasLocations.add(checkLocation);
				System.out.println("Check Next Size: "
						+ checkNextCanvasLocations.size()
						+ getCanvasFromCode(checkLocation));
				System.out.println("Checked Size: "
						+ checkedCanvasLocations.size() + canvasOwner);
			}

			checkCanvasLocations.addAll(checkNextCanvasLocations);
			checkCanvasLocations.removeAll(checkedCanvasLocations);
		}
		System.out.println(locations.size());
		return locations.toArray(new Integer[] {});
	}

	public void register(String name, Block clickedBlock) {
		XZLocation blockXZLocation = new XZLocation(clickedBlock.getX(),
				clickedBlock.getZ());

		int code = K.getCodeFromBlockXZLocation(blockXZLocation);
		
		register(name, code);
	}

	public void register(String name, int code) {
		if (!canvasLocationCodeToOwnerId.containsKey(code)) {
			int id = getUniqueId();
			canvasLocationCodeToOwnerId.put(code, id);
			ownerIdToOwner.put(id, name);
			saveAll();
		}
	}

	private int getUniqueId() {
		// Find a unique id
		int new_id = (int) (Math.random() * 99999);

		while (this.ownerIdToOwner.containsKey(new_id)) {
			new_id = (int) (Math.random() * 99999);
		}

		return new_id;
	}

	private void saveAll() {
		File allClaimsConfigFile = new File(plugin.getDataFolder(), "save.yml");
		FileConfiguration allClaims = new YamlConfiguration();
		ConfigurationSection csect = allClaims.createSection("canvii");
		int id = 0;
		for (int locationCode : this.canvasLocationCodeToOwnerId.keySet()) {
			id++;
			ConfigurationSection sect = csect.createSection("" + id);
			sect.set("code", locationCode);
			int ownerId = canvasLocationCodeToOwnerId.get(locationCode);
			sect.set("player", ownerIdToOwner.get(ownerId));
		}
		try {
			allClaims.save(allClaimsConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadAll() {
		File allClaimsConfigFile = new File(plugin.getDataFolder(), "save.yml");
		FileConfiguration allClaims = YamlConfiguration
				.loadConfiguration(allClaimsConfigFile);

		String pre = "canvii.";

		for (int id = 1; allClaims.contains(pre + id); id++) {
			int code = ((int) allClaims.get(pre + id + ".code"));
			String name = ((String) allClaims.get(pre + id + ".player"));

			register(name, code);
		}
		System.out.println("[Canvas Claims] "
				+ this.canvasLocationCodeToOwnerId.size()
				+ " claims loaded from file.");
	}
}
