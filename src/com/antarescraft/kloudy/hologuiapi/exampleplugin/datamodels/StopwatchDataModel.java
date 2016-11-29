package com.antarescraft.kloudy.hologuiapi.exampleplugin.datamodels;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologuiapi.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologuiapi.handlers.ClickHandler;
import com.antarescraft.kloudy.hologuiapi.handlers.GUIPageLoadHandler;
import com.antarescraft.kloudy.hologuiapi.playerguicomponents.PlayerGUIPage;
import com.antarescraft.kloudy.hologuiapi.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.Calendar;

/**
 * StopwatchDataModel class
 * 
 * This is the data model class that is bound to the stopwatch.yml gui page that is defined in resources/yamls/stopwatch.yml
 * All public methods of this class are available to be called from the stopwatch.yml config file
 * Reference resources/yamls/stopwatch.yml to see how the GUIComponents used in this class were configured
 *
 * Methods can be called from the config file by using syntax: $model.myFunctionName();
 * Example of this usage can be found in stopwatch.yml
 *
 * If the method returns a value, then that value will be replaced in the string in the config file
 * This allows you to display dynamic data in your gui pages
 */

public class StopwatchDataModel extends PlayerGUIPageModel
{
	private PlayerGUIPage playerGUIPage;

	//references to gui components in stopwatch.yml
	private ButtonComponent startButton;
	private ButtonComponent stopButton;
	private ButtonComponent resetButton;

	private BukkitRunnable stopWatch;
	private Duration time = Duration.ZERO;//the amount of time remaining on the stopwatch
	
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
			public void onPageLoad(PlayerGUIPage _playerGUIPage)
			{
				//This code runs on page load

				playerGUIPage = _playerGUIPage;
			
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

				time = Duration.ZERO;
			}
		});
		
		//# end region
	}
	
	/**
	 * Starts the stopwatch
	 */
	private void start()
	{
		//The PlayerGUIPage object contains methods to render / remove components on a player's gui page
		playerGUIPage.removeComponent("start-btn");//remove the start button
		playerGUIPage.renderComponent(stopButton);//render the stop button

		stopWatch = new BukkitRunnable()
		{
			private Calendar prevTime = Calendar.getInstance();
			
			@Override
			public void run()
			{
				Calendar now = Calendar.getInstance();
				time = time.plusMillis(now.getTimeInMillis() - prevTime.getTimeInMillis());
				prevTime = now;
			}
		};

		stopWatch.runTaskTimer(plugin, 0, 20);//update the time every second
	}
	
	/**
	 * Stops the stopwatch
	 */
	private void stop()
	{
		//The PlayerGUIPage object contains methods to render / remove components on a player's gui page
		playerGUIPage.removeComponent("stop-btn");//remove the stop button
		playerGUIPage.renderComponent(startButton);//render the start button

		if(stopWatch != null)
		{
			stopWatch.cancel();
			stopWatch = null;
		}
	}
	
	/**
	 * Returns the amount of time left on the stopwatch
	 */
	public String time()
	{
		return TimeFormat.getDurationFormatString(time);
	}
}