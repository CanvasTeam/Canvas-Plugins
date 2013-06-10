package com.zombiehippie.bukkit.protection.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

import com.zombiehippie.bukkit.protection.CanvasManager;

public class PlayerListener implements Listener {
	// PROHIBITTED Materials to have in hand on other people's property
	private final List<Material> inhand_prohibits = Arrays.asList(
			Material.FIREBALL, Material.FLINT_AND_STEEL, Material.DRAGON_EGG,
			Material.MONSTER_EGG, Material.MONSTER_EGGS,
			Material.STORAGE_MINECART, Material.MINECART,
			Material.POWERED_MINECART, Material.MINECART,
			Material.EXPLOSIVE_MINECART, Material.HOPPER_MINECART);

	// PROHIBITTED Materials to click on other people's property
	private final List<Material> clicked_prohibits = Arrays.asList(
			Material.LEVER, Material.ANVIL, Material.FURNACE, Material.CHEST,
			Material.STONE_BUTTON, Material.JUKEBOX, Material.DISPENSER,
			Material.TRAPPED_CHEST, Material.REDSTONE_COMPARATOR_OFF,
			Material.REDSTONE_COMPARATOR_ON, Material.BREWING_STAND,
			Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON);

	private final CanvasManager canvasManager;

	public PlayerListener(CanvasManager manager) {
		canvasManager = manager;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block theBlockClicked = event.getClickedBlock();
			Material inhand_mat = event.getMaterial();
			if (inhand_prohibits.contains(inhand_mat)) {
				event.setCancelled(!canModify(event.getPlayer(),
						theBlockClicked.getRelative(event.getBlockFace())));
			}

			Material clicked_mat = theBlockClicked.getType();
			if (clicked_prohibits.contains(clicked_mat)) {
				event.setCancelled(!canModify(event.getPlayer(),
						theBlockClicked));
			}

		}
	}

	@EventHandler
	public void onVehicleDestroy(VehicleDestroyEvent event) {
		if (event.getAttacker() instanceof Player)
			if (!canModify((Player) event.getAttacker(), event.getVehicle()
					.getLocation().getBlock())) {
				event.setCancelled(true);
			}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!canModify(event.getPlayer(), event.getBlock())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!canModify(event.getPlayer(), event.getBlockPlaced())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		// Use relative block to face clicked because that is where the liquid
		// is poured
		if (!canModify(event.getPlayer(),
				event.getBlockClicked().getRelative(event.getBlockFace()))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		if (!canModify(event.getPlayer(), event.getBlockClicked())) {
			event.setCancelled(true);
		}
	}

	private boolean canModify(Player playerWhoModified, Block block) {
		if (playerWhoModified.isOp())
			return true;
		String playerName = playerWhoModified.getName();
		return canvasManager.canBuild(playerName, block);
	}
}
