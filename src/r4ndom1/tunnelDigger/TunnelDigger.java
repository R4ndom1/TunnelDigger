package r4ndom1.tunnelDigger;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import r4ndom1.DB.DB;
import r4ndom1.DB.cart.RegisterCart;
import r4ndom1.gui.CustomInventory;
import r4ndom1.items.CustomRecipes;
import r4ndom1.repeadSchedule.*;
import r4ndom1.tunnelDigger.listeners.Events;

public class TunnelDigger extends JavaPlugin 
{
	private static Plugin plugin;

	@Override
	public void onEnable() 
	{	
		getServer().getPluginManager().registerEvents(new Events(), this);
		
		@SuppressWarnings("unused")
		DB db = new DB();
		
		@SuppressWarnings("unused")
		CustomRecipes rsp = new CustomRecipes();
		
		@SuppressWarnings("unused")
		CustomInventory Cinv = new CustomInventory();
		
		//Initialize uuid's with carts
		RegisterCart.LoadCarts();
		
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				CartsActionsSheduler.CartsActions();
			}
			
		} , 0L, 100L);
		
		
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "tunnel diggers are enabled");
	}
	
	@Override
	public void onDisable() 
	{
		//counts saved carts. (is already done in scheduler)
		RegisterCart.SaveCarts();
		
		getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "tunnel diggers are disabled");
	}
	
	public static Plugin getPlugin() 
	{
		return plugin;
	}
}
