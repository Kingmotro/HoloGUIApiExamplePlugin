package com.antarescraft.kloudy.hologuiapi.exampleplugin.datamodels;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.guicomponents.*;
import com.antarescraft.kloudy.hologuiapi.handlers.ClickHandler;
import com.antarescraft.kloudy.hologuiapi.handlers.GUIPageLoadHandler;
import com.antarescraft.kloudy.hologuiapi.playerguicomponents.PlayerGUIPage;
import com.antarescraft.kloudy.hologuiapi.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;

import com.antarescraft.kloudy.hologuiapi.scrollvalues.ListScrollValue;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.Calendar;

/**
 * StopwatchPageModel class
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

public class StopwatchPageModel extends PlayerGUIPageModel // extend your data model class from PlayerGUIPageModel
{
	private PlayerGUIPage playerGUIPage;

	//references to gui components in stopwatch.yml
	private ImageComponent stopwatch;//non-moving stopwatch image
	private ImageComponent stopwatchRunning;//moving stopwatch image. This component was set to 'hidden' so it will not render on the page on pageLoad
	private ButtonComponent startButton;
	private ButtonComponent closeButton;
	private ButtonComponent stopButton;
	private ButtonComponent resetButton;
	final private ToggleSwitchComponent soundToggle;
	final private ValueScrollerComponent tickSoundType;

	private BukkitRunnable stopWatch;
	private Duration time = Duration.ZERO;//the amount of time remaining on the stopwatch
	
	public StopwatchPageModel(final HoloGUIPlugin plugin, GUIPage guiPage, final Player player)
	{
		super(plugin, guiPage, player);
		
		//references to GUICoponents can be retrieved from the GUIPage object through the 'getComponent(String guiPageId)' method
		//pass in the id of the component you want and cast it to the appropriate type
		stopwatch = (ImageComponent)guiPage.getComponent("stopwatch");
		stopwatchRunning = (ImageComponent)guiPage.getComponent("stopwatch-running");
		closeButton = (ButtonComponent)guiPage.getComponent("close-btn");
		startButton = (ButtonComponent)guiPage.getComponent("start-btn");
		stopButton = (ButtonComponent)guiPage.getComponent("stop-btn");
		resetButton = (ButtonComponent)guiPage.getComponent("reset-btn");
		soundToggle = (ToggleSwitchComponent)guiPage.getComponent("sound-toggle");
		tickSoundType = (ValueScrollerComponent)guiPage.getComponent("tick-sound-scroller");

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
		closeButton.registerClickHandler(player, new ClickHandler()
		{
			@Override
			public void onClick()
			{
				//when the player clicks the close button

				stop();//stop the stopwatch and close the page

				plugin.getHoloGUIApi().closeGUIPage(player);//close the page
			}
		});

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
		playerGUIPage.removeComponent("stopwatch");//remove the non-moving stopwatch image
		playerGUIPage.renderComponent(stopwatchRunning);//render the moving stopwatch image

		stopWatch = new BukkitRunnable()
		{
			private Calendar prevTime = Calendar.getInstance();
			
			@Override
			public void run()
			{
				if(soundToggle.getPlayerToggleSwitchState(player))//sound toggle is switched on, play tick sound
				{
					//Retrieve the current Sound value from the tick type scroller
					ListScrollValue listValue = (ListScrollValue)tickSoundType.getPlayerScrollValue(player);
					try
					{
						player.playSound(player.getLocation(), Sound.valueOf(listValue.toString()), 0.5f, 1);
					}
					catch(Exception e){}
				}

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
		playerGUIPage.removeComponent("stopwatch-running");//remove the moving stopwatch image
		playerGUIPage.renderComponent(stopwatch);//render the non-moving stopwatch image

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