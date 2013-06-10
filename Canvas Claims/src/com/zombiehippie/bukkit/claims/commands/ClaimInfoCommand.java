package com.zombiehippie.bukkit.claims.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.Claim;
import com.zombiehippie.bukkit.claims.visuals.Visuallization;

public class ClaimInfoCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(!(sender instanceof Player)){
			return false;
		}
		Player thePlayer = (Player) sender;
		
		
		Claim theClaim = CanvasClaims.getClaimAt(thePlayer.getLocation().getBlock());
		
		if(theClaim != null && theClaim.ownsClaim(thePlayer.getName())){
			CanvasClaims.removeClaim(theClaim.getId());
			Visuallization.resetPlayerVisuals(thePlayer.getName());
		}
		return true;
	}
}
