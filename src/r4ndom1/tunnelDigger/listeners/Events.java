package r4ndom1.tunnelDigger.listeners;


import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import r4ndom1.DB.cart.CartConfiguration;
import r4ndom1.DB.cart.CartInventory;
import r4ndom1.DB.cart.RegisterCart;
import r4ndom1.DB.cart.RemoveCart;
import r4ndom1.gui.CustomInventory;
import r4ndom1.gui.CustomItemData;


public class Events implements Listener{
	//implement a function to prevent renaming !!!!!!!
	
	@EventHandler 
	public void VircleMove(VehicleMoveEvent E) {
		Entity ent = E.getVehicle();
		if(ent.getType() == EntityType.MINECART) {
			if(ent.getCustomName() != null) {
				if(ent.getCustomName().contains("Tunnel Digger")) {
					ent.setVelocity(new Vector(0,0,0));
				}
			}
		}
	}
	
	@EventHandler
	public void EntityRightclick(PlayerInteractEntityEvent E)
	{
		Entity ent = E.getRightClicked();
		if(ent.getType() == EntityType.MINECART && 
		   ent.getCustomName().contains("Tunnel Digger") &&
		   CustomItemData.extractHiddenString(ent.getCustomName().toString()) != null)
		{
			if(CustomItemData.extractHiddenString(ent.getCustomName().toString()).contains("TDK 58937")) // not named in a anvil etc. 
			{
				Player pl = E.getPlayer();
				E.setCancelled(true);
				if(RegisterCart.getRegisterd(ent) == false) 
				{
					RegisterCart.regigisterCart(ent, pl);
					if(CustomInventory.setupCartInventory(ent, pl)) {
						pl.openInventory(CustomInventory.getCartsMainInventory(ent));
					}
				} else {
					pl.openInventory(CustomInventory.getCartsMainInventory(ent));
				}	
			}
		}
	}
	
	@EventHandler
	public void EntityDeath(VehicleDestroyEvent E) {
		Entity ent = E.getVehicle();
		if(ent.getType() == EntityType.MINECART && ent.getCustomName().contains("Tunnel Digger"))
		{
			ArrayList<ItemStack> item = RemoveCart.KillCart(ent);
			for(int i = 0; i < item.size(); i++) {
				 ent.getWorld().dropItem(ent.getLocation(), item.get(i));
			}
		}
	}
	
	@EventHandler 
	public void OinvLeave(InventoryCloseEvent E) {

		if(E.getInventory().getType() == InventoryType.CHEST && 
		   CustomItemData.getUuidFromItem(E.getInventory().getItem(0)) != null 
		   )
		{
			String type;
			String uuid = CustomItemData.getUuidFromItem(E.getInventory().getItem(0));
			
			switch(E.getView().getTitle()) {
				case "Tunnel digger menu":
				return;
				case "Fuel Storage":
					CartInventory.DBsetFuelInvItem(uuid, E.getInventory(), E.getPlayer());
				return;
				case "Block Storage":
					type = "block";
				break;
				case "Rail Storage":
					CartInventory.DBsetRailInvItem(uuid, E.getInventory(), E.getPlayer());
				return;
				case "Mined Storage":
					type = "mined";
				break;
				case "Path":
					CartConfiguration.SetDirection(E.getInventory(), uuid);
				return;
				default:
					type = "main";
				break;
			}
			
			CartInventory.DBsetInvItem(uuid, type, E.getInventory());
		}
	}

	@EventHandler
	public void OnInvClick(InventoryClickEvent E) {
		String invTitle = E.getView().getTitle();
		if( invTitle == "Tunnel digger menu" 
			|| invTitle == "Fuel Storage" 
			|| invTitle == "Rail Storage" 
			|| invTitle == "Block Storage" 
			|| invTitle == "Mined Storage"
			|| invTitle == "Path") 
		{
			
			ItemStack item = E.getCurrentItem();
			
			if(item!=null) 
			{
				if(CustomItemData.getUuidFromItem(item) != null) 
					{
					int uuid = Integer.parseInt(CustomItemData.getUuidFromItem(item));
					Entity ent = null;
					Player pl = (Player) E.getWhoClicked();
					
					for(Entity e : E.getWhoClicked().getNearbyEntities(7, 7, 7)) {
						if(e.getEntityId() == uuid) {
							ent = e;
						}
					}
					
					if(ent != null) {
						if(item.getType() == Material.CAMPFIRE && item.getItemMeta().getDisplayName().contains("Fuel Storage"))
						{
							pl.openInventory(CustomInventory.getCartsFuelInventory(ent, "fuel", "Fuel Storage"));
						}
						else if(item.getType() == Material.RAIL && item.getItemMeta().getDisplayName().contains("Rail Storage")) 
						{
							pl.openInventory(CustomInventory.getCartsFuelInventory(ent, "rail", "Rail Storage"));
						}
						else if(item.getType() == Material.CHEST && item.getItemMeta().getDisplayName().contains("Block Storage")) 
						{
							pl.openInventory(CustomInventory.getCartsFuelInventory(ent, "block", "Block Storage"));
						}
						else if(item.getType() == Material.ENDER_CHEST && item.getItemMeta().getDisplayName().contains("Mined Storage")) 
						{
							pl.openInventory(CustomInventory.getCartsFuelInventory(ent, "mined", "Mined Storage"));
						}
						else if(item.getType() == Material.CARTOGRAPHY_TABLE && item.getItemMeta().getDisplayName().contains("Path")) 
						{
							pl.openInventory(CustomInventory.getCartsDirInventory(ent));
						}
						else if(item.getType() == Material.IRON_DOOR && item.getItemMeta().getDisplayName().contains("Back")) 
						{
							pl.openInventory(CustomInventory.getCartsMainInventory(ent));
						}
						else if(item.getType() == Material.COMPASS && item.getItemMeta().getDisplayName().contains("Set direction with clicks")) 
						{
							CustomInventory.toggleInvDir(E.getClickedInventory(),"" + uuid);
						}
						E.setCancelled(true);
					}
				} 
			}
		}
	}
}
