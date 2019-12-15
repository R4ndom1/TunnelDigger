package r4ndom1.items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import r4ndom1.tools.Numbers;

public class ItemData {
	
	public static ArrayList<ItemStack> GetMinedDrops(Block oldBlock) {
		
		Material mat = oldBlock.getType();
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		
		if(mat == Material.STONE) {
			drops.add(new ItemStack(Material.COBBLESTONE, 1));
			
		} else if(mat == Material.GRAVEL) {
			drops.add(new ItemStack(Material.COBBLESTONE, 1));
			
			if(Numbers.RandomNumber(0, 10) <= 8) {
				drops.add(new ItemStack(Material.FLINT, 1));
			}
		} else if(mat == Material.COAL_ORE) {
			drops.add(new ItemStack(Material.COAL, 1));
			
		} else if(mat == Material.LAPIS_ORE) {
			drops.add(new ItemStack(Material.LAPIS_LAZULI, Numbers.RandomNumber(4, 10)));
			
		} else if(mat == Material.GRASS) {
			drops.add(new ItemStack(Material.WHEAT_SEEDS, 1));
			
		} else if(mat == Material.DEAD_BUSH) {
			drops.add(new ItemStack(Material.STICK, 1));
			
		} else if(mat == Material.DIAMOND_ORE) {
			drops.add(new ItemStack(Material.DIAMOND, 1));
			
		} else if(mat == Material.REDSTONE_ORE) {
			drops.add(new ItemStack(Material.REDSTONE,  Numbers.RandomNumber(4, 15)));
			
		} else if(mat == Material.BOOKSHELF) {
			drops.add(new ItemStack(Material.BOOK, 3));
			
		} else if(mat == Material.EMERALD_ORE) {
			drops.add(new ItemStack(Material.EMERALD, 1));
			
		} else if(mat == Material.NETHER_QUARTZ_ORE) {
			drops.add(new ItemStack(Material.QUARTZ, 1));
			
		} else if(mat == Material.MUSHROOM_STEM) {
			drops.add(new ItemStack(Material.RED_MUSHROOM, 1));
			
		} else if(mat == Material.RED_MUSHROOM_BLOCK) {
			
			drops.add(new ItemStack(Material.RED_MUSHROOM, 1));
		} else if(mat == Material.BROWN_MUSHROOM_BLOCK) {
			
			drops.add(new ItemStack(Material.BROWN_MUSHROOM, 1));
		} else if(mat == Material.PODZOL) {

			drops.add(new ItemStack(Material.DIRT, 1));
		} else {
			String matString = mat.toString();
			if(matString.contains("GLASS")) { // everything made of glass 
				
			} else if(matString.contains("INFESTED")) { //monsterNest
				
			} else if(matString.contains("DIRT")) { //monsterNest
				drops.add(new ItemStack(Material.DIRT, 1));
				
			} else if(matString.contains("GRASS_")) { //monsterNest
				drops.add(new ItemStack(Material.DIRT, 1));
				
			} else {
				drops.add(new ItemStack(oldBlock.getType()));
			}
		}
		return drops;
	}
	
	public static boolean GetTrueRail(ItemStack item) {
		if(item.getType() != null) {
			if(item.getType().toString().endsWith("RAIL")) {
				//Bukkit.broadcastMessage("trueRail");
				return true;
			}
		}
		return false;
	}
	
	public static int GetItemFuelTime(ItemStack item) {
		
		if(item.getType() == null) {
			return 0;
		}
		
		Material mat = item.getType();
		int ticks = 0;
		switch(mat) {
			case COAL: // coal
				ticks = 1600;
				//Bukkit.broadcastMessage("Coal" + ticks);
			break;
			case CHARCOAL:
				ticks = 1600;
				//Bukkit.broadcastMessage("charcoal" + ticks);
			break;
			case COAL_BLOCK:
				ticks = 16000;
				//Bukkit.broadcastMessage("coalB" + ticks);
			break;
			case LAVA_BUCKET: // lava
				ticks = 20000;
				//Bukkit.broadcastMessage("Lava" + ticks);
			break;
			default:
				ticks = 0;
			break;
		}
		
		String matS = mat.toString();
		if(matS.endsWith("_LOG") || matS.endsWith("_PLANKS") || matS.endsWith("_FENCE"))
		{
			ticks = 300;
		}
		
		return ticks;
	}
}
























