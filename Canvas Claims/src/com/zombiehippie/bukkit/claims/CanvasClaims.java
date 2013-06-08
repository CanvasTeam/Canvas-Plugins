package com.zombiehippie.bukkit.claims;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zombiehippie.bukkit.claims.commands.AbandonCommand;
import com.zombiehippie.bukkit.claims.events.ClaimAddEvent;
import com.zombiehippie.bukkit.claims.events.PlayerClaimEvent;
import com.zombiehippie.bukkit.claims.listeners.PlayerListener;
import com.zombiehippie.bukkit.claims.listeners.WorldListener;
import com.zombiehippie.bukkit.claims.visuals.ClaimVisual;
import com.zombiehippie.bukkit.claims.visuals.ClaimVisual.ResultType;

public class CanvasClaims extends JavaPlugin {
	public static CanvasClaims instance;
	private static TreeMap<Integer,Claim> idToClaim = new TreeMap<Integer,Claim>();

	@Override
	public void onDisable() {
		// saveAll();
		ClaimVisual.resetAllVisuals();
	}

	@Override
	public void onEnable() {
		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new WorldListener(), this);
		pm.registerEvents(new PlayerListener(), this);

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
		ConfigurationSection csect = allClaims.createSection("claims");
		int id = 0;
		for (Claim next : idToClaim.values()) {
			id++;
			ConfigurationSection sect = csect.createSection(""+id);
			sect.set("id", next.getId());
			sect.set("own", next.getOwnerName());
			sect.set("n", next.getNorthBoundary());
			sect.set("e", next.getEastBoundary());
			sect.set("s", next.getSouthBoundary());
			sect.set("w", next.getWestBoundary());
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
		
		String pre = "claims.";

		for (int id = 1; allClaims.contains(pre + id); id++) {
			Claim next = new Claim();
			next.setId((int) allClaims.get(pre + id + ".id"));
			next.setOwnerName((String) allClaims.get(pre + id + ".own"));
			next.setNorthBoundary((int) allClaims.get(pre + id + ".n"));
			next.setEastBoundary((int) allClaims.get(pre + id + ".e"));
			next.setSouthBoundary((int) allClaims.get(pre + id + ".s"));
			next.setWestBoundary((int) allClaims.get(pre + id + ".w"));
			
			addClaim(next);
		}
		System.out.println("[Canvas Claims] " + idToClaim.size()
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
			// Call event
			PlayerClaimEvent claimevt = new PlayerClaimEvent(User.getName(),
					new_claim);

			this.getServer().getPluginManager().callEvent(claimevt);

			if(claimevt.isCancelled())
				return; // if the event was cancelled
			
			// assign id
			new_claim.assignUniqueId();
			// add to list
			addClaim(new_claim);
			
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
		return claim!=null?claim.ownsClaim(UserName):false;
	}

	/**
	 * Get a claim at specific block
	 * 
	 * @param block the block
	 * @return claim at this location, returns null if no claim is present
	 */
	public static Claim getClaimAt(Block block) {
		return getClaimAt(block.getX(), block.getZ());
	}
	/**
	 * Get a claim at specific X and Z coords
	 * 
	 * @param X
	 * @param Z
	 * @return claim at this location, returns null if no claim is present
	 */
	public static Claim getClaimAt(int X, int Z) {
		for(Claim checkClaim : idToClaim.values()){
			if (checkClaim.isInside(X, Z)) {
				return checkClaim;
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
		LinkedList<Claim> usersClaims = new LinkedList<Claim>();
		
		for(Claim claim : idToClaim.values()) {
			if (claim.ownsClaim(theUserName)) {
				usersClaims.add(claim);
			}
		}

		return usersClaims;
	}

	/**
	 * Gets the first claim to intersect the area provided
	 * 
	 * @param new_claim
	 * @return returns null if no claims intersect
	 */
	public List<Claim> getClaimsIntersecting(Claim new_claim) {
		LinkedList<Claim> intersectingClaims = new LinkedList<Claim>();
		
		for(Claim claim : idToClaim.values()) {
			if (claim.isIntersecting(new_claim)) {
				intersectingClaims.add(claim);
			}
		}
		return intersectingClaims;
	}

	public void addClaim(Claim theClaim) {
		ClaimAddEvent evt = new ClaimAddEvent(theClaim);

		Bukkit.getPluginManager().callEvent(evt);
		
		idToClaim.put(evt.getClaim().getId(), evt.getClaim());
		
		saveAll();
	}

	public void removeClaim(int theClaimId) {
		idToClaim.remove(theClaimId);
	}

	public static Collection<Claim> getClaims() {
		return idToClaim.values();
	}
	public static Set<Integer> getClaimIds() {
		return idToClaim.keySet();
	}
}
