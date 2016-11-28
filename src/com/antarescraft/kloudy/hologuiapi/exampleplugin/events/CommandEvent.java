package com.antarescraft.kloudy.hologuiapi.exampleplugin.events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.exampleplugin.ExamplePlugin;
import com.antarescraft.kloudy.hologuiapi.plugincore.command.CommandHandler;
import com.antarescraft.kloudy.plugincore.command.CommandParser;

public class CommandEvent implements CommandExecutor
{
	ExamplePlugin plugin;
	
	public CommandEvent(ExamplePlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		return CommandParser.parseCommand(plugin, this, "example", command.getName(), sender, args);
	}
	
	@CommandHandler(description = "Reloads GUI Pages from config",
			mustBePlayer = false, permission = "", subcommands = "reload")
	public void reload(CommandSender sender, String[] args)
	{
		plugin.getHoloGUIApi().destroyGUIPages(plugin);//remove all gui displays players are looking at
		plugin.loadGUIPages();//load the gui pages from config
	}
	
	@CommandHandler(description = "Opens the specified gui page for the ExmaplePlugin", 
			mustBePlayer = true, permission = "", subcommands = "open <gui_page_id>")
	public void openGUIPage(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		
		plugin.getHoloGUIApi().openGUIPage(plugin, player, args[1]);//open guipage with id retrieved from command argument
	}
}