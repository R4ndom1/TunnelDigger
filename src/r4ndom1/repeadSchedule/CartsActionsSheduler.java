package r4ndom1.repeadSchedule;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import r4ndom1.DB.cart.CartConfiguration;
import r4ndom1.DB.cart.CartInventory;
import r4ndom1.DB.cart.RegisterCart;
import r4ndom1.items.ItemData;
import r4ndom1.tools.GetLocation;

public class CartsActionsSheduler {
	
	public static void CartsActions() {
		for(World w : Bukkit.getWorlds()) {
			for(Entity ent : w.getEntities()) {
				if(RegisterCart.getRegisterd(ent)) {
					
					String uuid = "" + ent.getEntityId();
					String dir = RegisterCart.getDBdirection(uuid);
					
					if(dir != null) {
						
						Location Nloc = GetLocation.GetNextLocation(dir, ent.getLocation());
 						
						if(FuelCart(ent, uuid)) {
							
							Material rail = CartInventory.GetRailType(uuid);
							Material block = CartInventory.GetBlockType(uuid);
							
							if(rail != null && block != null) {
								
								if(Nloc.getBlock().getType() != rail) {
									CartInventory.SetMinedItem(ItemData.GetMinedDrops(Nloc.getBlock()), uuid);
									CartInventory.UseFirstRailItem(uuid);
									Nloc.getBlock().setType(rail);
								}
								
								Nloc.subtract(0,1,0);
								if(Nloc.getBlock().getType() != block) {
									CartInventory.SetMinedItem(ItemData.GetMinedDrops(Nloc.getBlock()), uuid);
									CartInventory.UseFirstBlockItem(uuid, 1);
									Nloc.getBlock().setType(block);
								}
								Nloc.add(0,1,0);
								
								Nloc.add(0,1,0);
								if(Nloc.getBlock().getType() != Material.AIR) {
									CartInventory.SetMinedItem(ItemData.GetMinedDrops(Nloc.getBlock()), uuid);
									Nloc.getBlock().setType(Material.AIR);
								}
								
								Nloc.subtract(0,1,0);
								
								CartConfiguration.MoveCart(ent, Nloc);
								
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean FuelCart(Entity ent, String uuid) {
		if(RegisterCart.getBurnTime(ent) != 0) {
			int dbticks = RegisterCart.getBurnTime(ent);
			RegisterCart.setBurnTime(ent, (dbticks - 100));
			return true;
			
		} else {
			int ticks = CartInventory.UseFirstFuelItem(uuid);
			if(ticks != 0) {
				RegisterCart.setBurnTime(ent, ticks);
				return true;
			}	
		}
		
		return false;
	}
}
	
