package com.antarescraft.kloudy.hologuiapi.exampleplugin;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.exampleplugin.events.CommandEvent;


public class ExamplePlugin extends HoloGUIPlugin
{
	@Override
	public void onEnable()
	{
		setMinSupportedApiVersion("1.0.7");
		checkMinApiVersion();
		
		saveDefaultConfig();

		copyResourceConfigs(true);
		getHoloGUIApi().hookHoloGUIPlugin(this);//hook the plugin into HoloGUIApi
		loadGUIPages();//load the GUI Pages from cofig

		getCommand("stopwatch").setExecutor(new CommandEvent(this));
	}

	@Override
	public void onDisable()
	{
		//destorys all gui pages displayed through this plugin that players may have been looking at when the plugin got disabled
		getHoloGUIApi().destroyGUIPages(this);
	}
}