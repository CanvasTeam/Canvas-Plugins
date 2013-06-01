package com.zombiehippie.bukkit.voteups.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zombiehippie.bukkit.claims.CanvasClaims;
import com.zombiehippie.bukkit.claims.Claim;

public class VoteUpCommand implements CommandExecutor {
	public static HashMap<Integer,Integer> claimIdVotes = new HashMap<Integer,Integer>();
	
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
			player.sendMessage(ChatColor.RED + "You can't voteup unclaimed areas!");
			return false;
		}
		
		int claimId = claim.getId();
		
		if(claimIdVotes.containsKey(claimId)){
			int prev = claimIdVotes.get(claimId);
			claimIdVotes.remove(claimId);
			claimIdVotes.put(claimId,prev+1);
		} else {
			claimIdVotes.put(claimId,1);
		}
		player.sendMessage(ChatColor.AQUA + claim.getOwnerName() +ChatColor.GREEN + " thanks you for the vote!");
		return true;
	}
	
}
