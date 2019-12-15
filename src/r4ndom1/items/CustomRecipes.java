package r4ndom1.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import r4ndom1.gui.CustomItemData;
import r4ndom1.tunnelDigger.TunnelDigger;

public class CustomRecipes {
	
	private static Plugin plugin = TunnelDigger.getPlugin(TunnelDigger.class);
	
	public CustomRecipes() {
		// Create the custom ItemStack
		ItemStack cart = new ItemStack(Material.MINECART);
				
		//item meta of item
		ItemMeta cartMeta = cart.getItemMeta();
		
		String encodedString = CustomItemData.encodeString("TDK 58937");
		
		cartMeta.setDisplayName("Tunnel Digger" + encodedString);
		cart.setItemMeta(cartMeta);
		
	    NamespacedKey cartKey = new NamespacedKey(plugin, "cart_key");
	    ShapedRecipe cartRecipe = new ShapedRecipe(cartKey, cart);
	  
	    cartRecipe.shape("P P", "DCF", "MMM");
	    cartRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
	    cartRecipe.setIngredient('P', Material.PISTON);
	    cartRecipe.setIngredient('C', Material.CHEST);
	    cartRecipe.setIngredient('F', Material.BLAST_FURNACE);
	    cartRecipe.setIngredient('M', Material.MINECART);

	    Bukkit.addRecipe(cartRecipe);
	}
}