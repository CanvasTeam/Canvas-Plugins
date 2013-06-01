package com.zombiehippie.bukkit.claims;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zombiehippie.bukkit.claims.commands.AbandonCommand;
import com.zombiehippie.bukkit.claims.listeners.BlockListener;
import com.zombiehippie.bukkit.claims.listeners.PlayerListener;
import com.zombiehippie.bukkit.claims.listeners.WorldListener;
import com.zombiehippie.bukkit.claims.visuals.ClaimVisual;
import com.zombiehippie.bukkit.claims.visuals.ClaimVisual.ResultType;

public class CanvasClaims extends JavaPlugin {
	public static CanvasClaims instance;
	private static LinkedList<Claim> claims = new LinkedList<Claim>();
	private final BlockListener blockListener = new BlockListener();
	private final WorldListener worldListener = new WorldListener();
	private final PlayerListener playerListener = new PlayerListener();

	@Override
	public void onDisable() {
		saveAll();
		// ClaimVisual.resetAllVisuals();
	}

	@Override
	public void onEnable() {
		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(blockListener, this);
		pm.registerEvents(worldListener, this);
		pm.registerEvents(playerListener, this);

		// Register commands
		getCommand("AbandonClaim").setExecutor(new AbandonCommand());

		// Load
		loadAll();

		instance = this;
	}

	private void saveAll() {
		File allClaimsConfigFile = new File(getDataFolder() + File.separator
				+ "claims", "claims.yml");
		FileConfiguration allClaims = new YamlConfiguration();
		// Will eventually use the actual claim ids
		int id = 0;
		String pre = "claim.";

		for (Claim next : claims) {
			id++;
			allClaims.set(pre + id + ".own", next.getOwnerName());
			allClaims.set(pre + id + ".n", next.getNorthBoundary());
			allClaims.set(pre + id + ".e", next.getEastBoundary());
			allClaims.set(pre + id + ".s", next.getSouthBoundary());
			allClaims.set(pre + id + ".w", next.getWestBoundary());
		}
		try {
			allClaims.save(allClaimsConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadAll() {
		File allClaimsConfigFile = new File(getDataFolder() + File.separator
				+ "claims", "claims.yml");
		FileConfiguration allClaims = YamlConfiguration
				.loadConfiguration(allClaimsConfigFile);

		String pre = "claim.";

		for (int id = 1; allClaims.contains(pre + id); id++) {
			Claim next = new Claim();
			next.setOwnerName((String) allClaims.get(pre + id + ".own"));
			next.setNorthBoundary((int) allClaims.get(pre + id + ".n"));
			next.setEastBoundary((int) allClaims.get(pre + id + ".e"));
			next.setSouthBoundary((int) allClaims.get(pre + id + ".s"));
			next.setWestBoundary((int) allClaims.get(pre + id + ".w"));
			claims.add(next);
		}
		System.out.println("[Canvas Claims] " + claims.size()
				+ " claims loaded from file.");
	}

	/**
	 * Attempts to create the claim, also sends the results to player
	 * 
	 * @param User
	 *            Creator of claim
	 * @param first
	 *            First point selected
	 * @param second
	 *            Second point selected
	 */
	public void createClaim(Player User, Location first, Location second) {
		Claim new_claim = new Claim();

		// Setup the new claim with boundaries
		new_claim.setupClaim(User.getName(), (int) first.getX(),
				(int) first.getZ(), (int) second.getX(), (int) second.getZ());

		// Check that the claim meets minimum size requirements
		if (new_claim.getHeight() < 7 || new_claim.getWidth() < 7) {
			User.sendMessage(ChatColor.RED
					+ "Your claim needs to be at least 7x7 blocks!");
			new ClaimVisual(User, new_claim, ResultType.TOOSMALL);
			return;
		}

		// Look for intersecting claims
		List<Claim> intersecting = getClaimsIntersecting(new_claim);
		if (intersecting == null || intersecting.size() == 0) {
			// Found no intersecting claims
			
			//assign id
			new_claim.assignUniqueId();
			//add to list
			addClaim(new_claim);
			//reset shovel
			PlayerListener.resetShovel(User);
			new ClaimVisual(User, new Claim[] { new_claim }, ResultType.CREATE);
		} else {
			// Found at least one intersecting claims
			new ClaimVisual(User, intersecting.toArray(new Claim[0]),
					ResultType.INTERSECT);
		}
	}

	public boolean owns(String UserName, Block block) {
		Claim claim = getClaimAt(block);
		// User owns the claim
		return claim.ownsClaim(UserName);
	}

	/**
	 * Get a claim at specific X and Z coords
	 * 
	 * @param X
	 * @param Z
	 * @return claim at this location, returns null if no claim is present
	 */
	public Claim getClaimAt(Block block) {
		Iterator<Claim> it = claims.iterator();
		while (it.hasNext()) {
			// iterate through all claims
			Claim next = it.next();
			if (next.isInside(block.getX(), block.getZ())) {
				return next;
			}
		}
		// Did not find a claim
		return null;
	}

	/**
	 * Get all of a certain user's claims
	 * 
	 * @param userName
	 * @return claims owned by user
	 */
	public List<Claim> getUsersClaims(String theUserName) {
		Iterator<Claim> it = claims.iterator();
		LinkedList<Claim> claims = new LinkedList<Claim>();
		while (it.hasNext()) {
			Claim next = it.next();
			if (next.ownsClaim(theUserName)) {
				claims.add(next);
			}
		}

		return claims;
	}

	/**
	 * Gets the first claim to intersect the area provided
	 * 
	 * @param new_claim
	 * @return returns null if no claims intersect
	 */
	public List<Claim> getClaimsIntersecting(Claim new_claim) {
		Iterator<Claim> it = claims.iterator();
		LinkedList<Claim> intersectingClaims = new LinkedList<Claim>();
		while (it.hasNext()) {
			Claim next = it.next();
			// if the next claim intersects the area, add it to the intersection
			// list
			if (next.isIntersecting(new_claim)) {
				intersectingClaims.add(next);
			}
		}
		return intersectingClaims;
	}

	public void addClaim(Claim theClaim) {
		claims.add(theClaim);
		saveAll();
	}

	public void removeClaim(Claim theClaim) {
		claims.remove(theClaim);

	}

	public static LinkedList<Claim> getClaims() {
		return claims;
	}
}
