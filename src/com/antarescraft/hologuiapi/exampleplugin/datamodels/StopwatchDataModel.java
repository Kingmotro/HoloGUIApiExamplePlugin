package com.antarescraft.hologuiapi.exampleplugin.datamodels;

import java.time.Duration;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologuiapi.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologuiapi.guicomponents.LabelComponent;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ValueScrollerComponent;
import com.antarescraft.kloudy.hologuiapi.handlers.ClickHandler;
import com.antarescraft.kloudy.hologuiapi.handlers.GUIPageLoadHandler;
import com.antarescraft.kloudy.hologuiapi.handlers.ScrollHandler;
import com.antarescraft.kloudy.hologuiapi.playerguicomponents.PlayerGUIPage;
import com.antarescraft.kloudy.hologuiapi.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.hologuiapi.scrollvalues.AbstractScrollValue;
import com.antarescraft.kloudy.hologuiapi.scrollvalues.DurationScrollValue;

/**
 * StopwatchDataModel class
 * 
 * This is the data model class that is bound to the stopwatch.yml gui page that is defined in resources/yamls/stopwatch.yml
 * All public methods of this class are available to be called from the stopwatch.yml config file
 * 
 * Methods can be called from the config file by using syntax: $model.myFunctionName();
 * Example of this usage can be found in stopwatch.yml
 *
 * If the method returns a value, then that value will be replaced in the string in the config file
 * This allows you to display dynamic data in your gui pages
 */
public class StopwatchDataModel extends PlayerGUIPageModel
{
	//references to gui components in stopwatch.yml
	private ButtonComponent startButton;
	private ButtonComponent stopButton;
	private ButtonComponent resetButton;
	
	private boolean isRunning = false;
	private Duration time;//the amount of time remaining on the stopwatch
	
	public StopwatchDataModel(HoloGUIPlugin plugin, GUIPage guiPage, final Player player)
	{
		super(plugin, guiPage, player);
		
		//references to GUICoponents can be retrieved from the GUIPage object through the 'getComponent(String guiPageId)' method
		//pass in the id of the component you want and cast it to the appropriate type
		startButton = (ButtonComponent)guiPage.getComponent("start-btn");
		stopButton = (ButtonComponent)guiPage.getComponent("stop-btn");
		resetButton = (ButtonComponent)guiPage.getComponent("reset-btn");
		
		//You can register a callback function to run when the guiPage is first loaded and displayed to the player
		guiPage.registerPageLoadHandler(new GUIPageLoadHandler()
		{
			@Override
			public void onPageLoad(PlayerGUIPage playerGUIPage)
			{
				//This code runs on page load
			
				player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1);
			}
		});
		
		//# region register click handlers for the ButtonComponents
		
		//you can register click handlers for ButtonComponents that will run when a player clicks the button
		startButton.registerClickHandler(player, new ClickHandler()
		{
			@Override
			public void onClick()
			{
				//when the player clicks the start button, start the stopwatch countdown
				start();
			}
		});
		
		stopButton.registerClickHandler(player, new ClickHandler()
		{
			@Override
			public void onClick()
			{
				//when the player clicks the stop button, stop the stopwatch countdown
				stop();
			}
		});
		
		resetButton.registerClickHandler(player, new ClickHandler()
		{
			@Override
			public void onClick()
			{
				//when the player clicks the reset button, reset the stopwatch to the value contained in the time ValueScroller component
				stop();
			}
		});
		
		//# end region
	}
	
	/**
	 * Starts the stopwatch
	 */
	private void start()
	{
		isRunning = true;
	}
	
	/**
	 * Stops the stopwatch
	 */
	private void stop()
	{
		isRunning = false;
	}
	
	/**
	 * Returns the amount of time left on the stopwatch
	 */
	public String time()
	{
		return TimeFormat.getDurationFormatString(time);
	}
}