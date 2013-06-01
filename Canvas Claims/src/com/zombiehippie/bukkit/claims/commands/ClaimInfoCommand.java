package com.zombiehippie.bukkit.claims.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.Claim;
import com.zombiehippie.bukkit.claims.visuals.ClaimVisual;

public class ClaimInfoCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(!(sender instanceof Player)){
			return false;
		}
		Player thePlayer = (Player) sender;
		
		
		Claim theClaim = CanvasClaims.instance.getClaimAt(thePlayer.getLocation().getBlock());
		
		if(theClaim != null && theClaim.ownsClaim(thePlayer.getName())){
			CanvasClaims.instance.removeClaim(theClaim);
			ClaimVisual.resetPlayersVisuals(thePlayer.getName());
		}
		return true;
	}
}