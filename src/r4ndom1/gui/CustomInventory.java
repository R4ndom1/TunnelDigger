package r4ndom1.gui;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import r4ndom1.DB.cart.*;
import r4ndom1.tunnelDigger.TunnelDigger;

public class CustomInventory implements Listener
{
	private static Plugin plugin = TunnelDigger.getPlugin(TunnelDigger.class);
	
	public static void toggleInvDir(Inventory inv, String uuid) {
		if(inv.getItem(12) != null) {
			String encString = CustomItemData.encodeString("" + uuid);
			ArrayList<String> lore = (ArrayList<String>) inv.getItem(12).getItemMeta().getLore();
			
			String NewDir = CartConfiguration.GetNextDir(lore);
			
			//Bukkit.broadcastMessage("newdir" + NewDir);
			
			ItemStack compass = Compass(ChatColor.GRAY + NewDir + encString);
			inv.setItem(12, compass);
		}
	}
	
	public static Inventory getCartsDirInventory(Entity ent)
	{
		String direction = RegisterCart.getDBdirection("" + ent.getEntityId());
		String count = "inf";
		if(direction == null) {
			direction = "North";
		}
		
		Inventory in  = plugin.getServer().createInventory(null, 27, "Path");
		
		String lore = CustomItemData.encodeString("" + ent.getEntityId());
		ItemStack border = Border(lore),
				  door = Door(lore),
				  compas = Compass(lore + ChatColor.GRAY + direction),
				  plus = Plus(lore + ChatColor.GRAY + count),
				  min = Min(lore + ChatColor.GRAY + count);
		
		//row 1
		
		in.setItem(0, door);
		
		in.setItem(1, border);
		in.setItem(2, border);
		in.setItem(3, border);
		in.setItem(4, border);
		in.setItem(5, border);
		in.setItem(6, border);
		in.setItem(7, border);
		in.setItem(8, border);
		
		//row 2
		
		in.setItem(9, border);
		in.setItem(10, border);
		in.setItem(11, border);
		
		in.setItem(12, compas);
		
		in.setItem(13, min);
		in.setItem(14, plus);
		
		in.setItem(15, border);
		in.setItem(16, border);
		in.setItem(17, border);
		
		//row 3
		
		in.setItem(18, border);
		in.setItem(19, border);
		in.setItem(20, border);
		in.setItem(21, border);
		in.setItem(22, border);
		in.setItem(23, border);
		in.setItem(24, border);
		in.setItem(25, border);
		in.setItem(26, border);
		
		return in;
	}
	
	
	public static Inventory getCartsFuelInventory(Entity ent, String type, String name)
	{
		Inventory in = plugin.getServer().createInventory(null, 45, name);
		in = CartInventory.DBgetFuelInvItems(type, ent, in);
		return in;
	}
	
	
	public static Inventory getCartsMainInventory(Entity ent)
	{
		Inventory in = plugin.getServer().createInventory(null, 45, "Tunnel digger menu");

		String lore = CustomItemData.encodeString("" + ent.getEntityId());
		
		ItemStack border = Border(lore),
				  rail = Rail(lore),
				  fire = FirePlace(lore),
				  chest = Chest(lore),
				  bookShelf = BookShelf(lore),
				  cartography = CartographyTable(lore),
				  eChest = EnderChest(lore);
		
		in.setItem(0, border);
		in.setItem(1, border);
		in.setItem(2, border);
		in.setItem(3, border);
		in.setItem(4, border);
		in.setItem(5, border);
		in.setItem(6, border);
		in.setItem(7, border);
		in.setItem(8, border);
		
		in.setItem(9, border);
		in.setItem(10, rail);
		in.setItem(11, border);
		in.setItem(12, fire);
		in.setItem(13, border);
		in.setItem(14, chest);
		in.setItem(15, border);
		in.setItem(16, bookShelf);
		in.setItem(17, border);
		
		in.setItem(18, border);
		in.setItem(19, border);
		in.setItem(20, border);
		in.setItem(21, border);
		in.setItem(22, border);
		in.setItem(23, border);
		in.setItem(24, border);
		in.setItem(25, border);
		in.setItem(26, border);
		
		in.setItem(27, border);
		in.setItem(28, eChest);
		in.setItem(29, border);
		in.setItem(30, border);
		in.setItem(31, border);
		in.setItem(32, border);
		in.setItem(33, border);
		in.setItem(34, cartography);
		in.setItem(35, border);
		
		in.setItem(36, border);
		in.setItem(37, border);
		in.setItem(38, border);
		in.setItem(39, border);
		in.setItem(40, border);
		in.setItem(41, border);
		in.setItem(42, border);
		in.setItem(43, border);
		in.setItem(44, border);
		
		return in;
	}
	
	
	public static boolean setupCartInventory(Entity ent, Player pl) 
	{
		Inventory in = plugin.getServer().createInventory(null, 45, "Tunnel digger menu");
		
		String lore = CustomItemData.encodeString("" + ent.getEntityId());
		ItemStack door = Door(lore);
		
		/* Block Storage */
		in = plugin.getServer().createInventory(null, 45, "Block storage");
		in.setItem(0, door);
		CartInventory.DBnewInv(in, "block", ent);
		
		/* Fuel Storage */
		in = plugin.getServer().createInventory(null, 45, "Fuel storage");
		in.setItem(0, door);
		CartInventory.DBnewInv(in, "fuel", ent);
		
		/* Mined Block and Item Storage */
		in = plugin.getServer().createInventory(null, 45, "Mined storage");
		in.setItem(0, door);
		CartInventory.DBnewInv(in, "mined", ent);
		
		/* Block Storage */
		in = plugin.getServer().createInventory(null, 45, "Rail storage");
		in.setItem(0, door);
		CartInventory.DBnewInv(in, "rail", ent);
		
		return true;
	}
	
	private static ItemStack Rail(String lore) {
		String encodedString = lore;
		ItemStack rail = new ItemStack(Material.RAIL, 1);
		ItemMeta railMeta = rail.getItemMeta();
		ArrayList<String> railLore = new ArrayList<String>();
		railLore.add(encodedString);
		railMeta.setLore(railLore);
		railMeta.setDisplayName("Rail Storage");
		rail.setItemMeta(railMeta);
		return rail;
	}
	
	private static ItemStack FirePlace(String lore) {
		String encodedString = lore;
		ItemStack fire = new ItemStack(Material.CAMPFIRE, 1);
		ItemMeta fireMeta = fire.getItemMeta();
		ArrayList<String> fireLore = new ArrayList<String>();
		fireLore.add(encodedString);
		fireMeta.setLore(fireLore);
		fireMeta.setDisplayName("Fuel Storage");
		fire.setItemMeta(fireMeta);
		return fire;
	}
	
	private static ItemStack Chest(String lore) {
		String encodedString = lore;
		ItemStack chest = new ItemStack(Material.CHEST, 1);
		ItemMeta chestMeta = chest.getItemMeta();
		ArrayList<String> chestLore = new ArrayList<String>();
		chestLore.add(encodedString);
		chestMeta.setLore(chestLore);
		chestMeta.setDisplayName("Block Storage");
		chest.setItemMeta(chestMeta);
		return chest;
	}
	
	private static ItemStack EnderChest(String lore) {
		String encodedString = lore;
		ItemStack eChest = new ItemStack(Material.ENDER_CHEST, 1);
		ItemMeta eChestMeta = eChest.getItemMeta();
		ArrayList<String> eChestLore = new ArrayList<String>();
		eChestLore.add(encodedString);
		eChestMeta.setLore(eChestLore);
		eChestMeta.setDisplayName("Mined Storage");
		eChest.setItemMeta(eChestMeta);
		return eChest;
	}
	
	private static ItemStack Compass(String lore) {
		String encodedString = lore;
		ItemStack compas = new ItemStack(Material.COMPASS, 1);
		ItemMeta compasMeta = compas.getItemMeta();
		ArrayList<String> compasLore = new ArrayList<String>();
		compasLore.add(encodedString);
		compasMeta.setLore(compasLore);
		compasMeta.setDisplayName("Set direction with clicks");
		compas.setItemMeta(compasMeta);
		return compas;
	}
	
	private static ItemStack BookShelf(String lore) {
		String encodedString = lore;
		ItemStack bookShelf = new ItemStack(Material.BOOKSHELF, 1);
		ItemMeta bookShelfMeta = bookShelf.getItemMeta();
		ArrayList<String> bookShelfLore = new ArrayList<String>();
		bookShelfLore.add(encodedString);
		bookShelfMeta.setLore(bookShelfLore);
		bookShelfMeta.setDisplayName("Block Layers");
		bookShelf.setItemMeta(bookShelfMeta);
		return bookShelf;
	}
	
	private static ItemStack CartographyTable(String lore) {
		String encodedString = lore;
		ItemStack cartography = new ItemStack(Material.CARTOGRAPHY_TABLE, 1);
		ItemMeta cartographyMeta = cartography.getItemMeta();
		ArrayList<String> cartographyLore = new ArrayList<String>();
		cartographyLore.add(encodedString);
		cartographyMeta.setLore(cartographyLore);
		cartographyMeta.setDisplayName("Path");
		cartography.setItemMeta(cartographyMeta);
		return cartography;
	}
	
	private static ItemStack Door(String lore) {
		String encodedString = lore;
		ItemStack door = new ItemStack(Material.IRON_DOOR, 1);
		ItemMeta doorMeta = door.getItemMeta();
		ArrayList<String> doorLore = new ArrayList<String>();
		doorLore.add(encodedString);
		doorMeta.setLore(doorLore);
		doorMeta.setDisplayName("Back");
		door.setItemMeta(doorMeta);
		return door;
	}
	
	@SuppressWarnings("unused")
	private static ItemStack Emap(String lore) {
		String encodedString = lore;
		ItemStack newMap = new ItemStack(Material.MAP, 1);
		ItemMeta newMapMeta = newMap.getItemMeta();
		ArrayList<String> newMapLore = new ArrayList<String>();
		newMapLore.add(encodedString);
		newMapMeta.setLore(newMapLore);
		newMapMeta.setDisplayName("New");
		newMap.setItemMeta(newMapMeta);
		return newMap;
	}
	
	@SuppressWarnings("unused")
	private static ItemStack Map(String lore) {
		String encodedString = lore;
		ItemStack map = new ItemStack(Material.FILLED_MAP, 1);
		ItemMeta mapMeta = map.getItemMeta();
		ArrayList<String> mapLore = new ArrayList<String>();
		mapLore.add(encodedString);
		mapMeta.setLore(mapLore);
		mapMeta.setDisplayName("Previus");
		map.setItemMeta(mapMeta);
		return map;
	}
	
	
	private static ItemStack Border(String lore) {
		String encodedString = lore;
		ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta borderMeta = border.getItemMeta();
		borderMeta.setDisplayName(" ");
		ArrayList<String> borderLore = new ArrayList<String>();
		borderLore.add(encodedString);
		borderMeta.setLore(borderLore);
		border.setItemMeta(borderMeta);
		return border;
	}
	
	@SuppressWarnings("unused")
	private static ItemStack Barrier(String lore) {
		String encodedString = lore;
		ItemStack border = new ItemStack(Material.BARRIER, 1);
		ItemMeta borderMeta = border.getItemMeta();
		ArrayList<String> borderLore = new ArrayList<String>();
		borderLore.add(encodedString);
		borderMeta.setLore(borderLore);
		borderMeta.setDisplayName("Delete");
		border.setItemMeta(borderMeta);
		return border;
	}
	
	private static ItemStack Plus(String lore) {
		String encodedString = lore;
		ItemStack border = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
		ItemMeta borderMeta = border.getItemMeta();
		ArrayList<String> borderLore = new ArrayList<String>();
		borderLore.add(encodedString);
		borderMeta.setLore(borderLore);
		borderMeta.setDisplayName("Plus");
		border.setItemMeta(borderMeta);
		return border;
	}
	
	private static ItemStack Min(String lore) {
		String encodedString = lore;
		ItemStack border = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		ItemMeta borderMeta = border.getItemMeta();
		ArrayList<String> borderLore = new ArrayList<String>();
		borderLore.add(encodedString);
		borderMeta.setLore(borderLore);
		borderMeta.setDisplayName("Min");
		border.setItemMeta(borderMeta);
		return border;
	}
	
}
