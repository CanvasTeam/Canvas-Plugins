package com.zombiehippie.bukkit.voteups.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.Claim;

public class CheckVotesCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if(!(sender instanceof Player)){
			sender.sendMessage("You're not a Player!");
			return false;
		}
		
		Player player = (Player) sender;
		
		Claim claim = CanvasClaims.instance.getClaimAt(player.getLocation().getBlock());
		
		if(claim == null){
			player.sendMessage(ChatColor.RED + "Unclaimed areas don't have votes!");
			return false;
		}
		
		int claimId = claim.getId();
		
		if(VoteUpCommand.claimIdVotes.containsKey(claimId)){
			int votes = VoteUpCommand.claimIdVotes.get(claimId);
			player.sendMessage(ChatColor.AQUA + claim.getOwnerName() +ChatColor.BLUE + "'s claim has " + ChatColor.RED + votes + ChatColor.BLUE +" votes");
			
		} else {
			player.sendMessage(ChatColor.AQUA + claim.getOwnerName() +ChatColor.BLUE + "'s claim has " + ChatColor.RED +"0" + ChatColor.BLUE +" votes");
		}
		return true;
	}
}
