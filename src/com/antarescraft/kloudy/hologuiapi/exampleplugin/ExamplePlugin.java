package com.antarescraft.kloudy.hologuiapi.exampleplugin;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;

public class ExamplePlugin extends HoloGUIPlugin
{
	@Override
	public void onEnable()
	{
		saveDefaultConfig();
		
		getHoloGUIApi().hookHoloGUIPlugin(this);//hook the plugin into HoloGUIApi
		loadGUIPages();//load the GUI Pages from the resources/yamls/ directory in the plugin .jar
	}
	
	@Override
	public void onDisable()
	{
		//destorys all gui pages displayed through this plugin that players may have been looking at when the plugin got disabled
		getHoloGUIApi().destroyGUIPages(this);
	}
}