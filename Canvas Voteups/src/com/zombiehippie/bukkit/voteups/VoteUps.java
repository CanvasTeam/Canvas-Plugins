package com.zombiehippie.bukkit.voteups;

import org.bukkit.plugin.java.JavaPlugin;

import com.zombiehippie.bukkit.voteups.commands.CheckVotesCommand;
import com.zombiehippie.bukkit.voteups.commands.VoteUpCommand;

public class VoteUps extends JavaPlugin{
	@Override
	public void onEnable(){
		//PluginManager pm = Bukkit.getPluginManager();

		this.getCommand("voteup").setExecutor(new VoteUpCommand());
		this.getCommand("checkvotes").setExecutor(new CheckVotesCommand());
	}
	@Override
	public void onDisable() {
		
	}

}
