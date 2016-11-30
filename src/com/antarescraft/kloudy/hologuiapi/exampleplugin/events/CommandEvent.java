package com.antarescraft.kloudy.hologuiapi.exampleplugin.events;

import com.antarescraft.kloudy.hologuiapi.exampleplugin.ExamplePlugin;
import com.antarescraft.kloudy.hologuiapi.exampleplugin.datamodels.StopwatchDataModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.plugincore.command.CommandHandler;
import com.antarescraft.kloudy.hologuiapi.plugincore.command.CommandParser;

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
		return CommandParser.parseCommand(plugin, this, "stopwatch", command.getName(), sender, args);
	}
	
	@CommandHandler(description = "Reloads GUI Pages from config",
			mustBePlayer = false, permission = "", subcommands = "reload")
	public void reload(CommandSender sender, String[] args)
	{
		plugin.getHoloGUIApi().destroyGUIPages(plugin);//remove all gui displays players are looking at
		plugin.loadGUIPages();//load the gui pages from config
	}
	
	@CommandHandler(description = "Opens the stopwatch gui",
			mustBePlayer = true, permission = "", subcommands = "open")
	public void openStopwatch(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;

		//create a new StopwatchDataModel and pass in the 'stopwatch' gui page
		StopwatchDataModel stopwatchModel = new StopwatchDataModel(plugin, plugin.getGUIPage("stopwatch"), player);

		plugin.getHoloGUIApi().openGUIPage(plugin, player, stopwatchModel);//opens the gui page and binds the dataModel to the guiPage
	}
}