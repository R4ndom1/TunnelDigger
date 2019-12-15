package r4ndom1.DB.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import r4ndom1.DB.*;

public class RemoveCart {
	
	public static ArrayList<ItemStack> KillCart(Entity ent) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if(ent.getEntityId() != 0) {
			Connection con = DB.DBConnection();
			
	        String sql = "SELECT COUNT(*) FROM carts WHERE uuid = " + ent.getEntityId(); /* Delete The Cart Identifier Table */
            try {
            	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				if(rs.getInt(1) > 0 ) {
					 String psql = "DELETE FROM carts WHERE uuid = " + ent.getEntityId();
		             try {
		            	 PreparedStatement pstmt = con.prepareStatement(psql);
		            	 pstmt.executeUpdate();
		            	 
						//Bukkit.broadcastMessage("Delete from Carts Oke");
		             } catch(Exception E) {
		 				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DeleteCart: " + E.getMessage());
		             }
				}	
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(del)getRegisterd: " + e.getMessage());
			}
             
            sql = "SELECT * FROM fuel WHERE uuid = " + ent.getEntityId(); /* Delete The Cart fuel Table */
            try {
            	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				int rowcount = 0;
				
				while(rs.next()) {
					if(Material.getMaterial(rs.getString("material")) == Material.IRON_DOOR &&
					   rs.getString("name").contains("Back")) 
					{
						rowcount++;
						continue;
					} 
					if(Material.getMaterial(rs.getString("material")) == Material.AIR) {
						continue;
					}
					ItemStack item = new ItemStack(Material.getMaterial(rs.getString("material")), rs.getInt("count"));
					ItemMeta itemMeta = item.getItemMeta();
					itemMeta.setDisplayName(rs.getString("name"));
					item.setItemMeta(itemMeta);
					items.add(item);
					rowcount++;
				}
				if(rowcount > 0) {
					 String psql = "DELETE FROM fuel WHERE uuid = " + ent.getEntityId();
		             try {
		            	 PreparedStatement pstmt = con.prepareStatement(psql);
		            	 pstmt.executeUpdate();
		            	 
						//Bukkit.broadcastMessage("Delete fuel from Carts Oke");
		             } catch(Exception E) {
		 				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DeleteCart: " + E.getMessage());
		             }
				}	
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(del)getFuel: " + e.getMessage());
			}
            
            sql = "SELECT * FROM block WHERE uuid = " + ent.getEntityId(); /* Delete The Cart Block Table */
            try {
            	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				int rowcount = 0;
				while(rs.next()) {
					if(Material.getMaterial(rs.getString("material")) == Material.IRON_DOOR &&
					   rs.getString("name").contains("Back")) 
					{
						rowcount++;
						continue;
					} 
					if(Material.getMaterial(rs.getString("material")) == Material.AIR) {
						continue;
					}
					ItemStack item = new ItemStack(Material.getMaterial(rs.getString("material")), rs.getInt("count"));
					ItemMeta itemMeta = item.getItemMeta();
					itemMeta.setDisplayName(rs.getString("name"));
					item.setItemMeta(itemMeta);
					items.add(item);
					rowcount++;
				}
				if(rowcount > 0) {
					 String psql = "DELETE FROM block WHERE uuid = " + ent.getEntityId();
		             try {
		            	 PreparedStatement pstmt = con.prepareStatement(psql);
		            	 pstmt.executeUpdate();
		            	 
						//Bukkit.broadcastMessage("Delete block from Carts Oke");
		             } catch(Exception E) {
		 				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DeleteBlockCart: " + E.getMessage());
		             }
				}	
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(del)getBlock: " + e.getMessage());
			}

            sql = "SELECT * FROM rail WHERE uuid = " + ent.getEntityId(); /* Delete The Cart rail Table */
            try {
            	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				int rowcount = 0;
				while(rs.next()) {
					if(Material.getMaterial(rs.getString("material")) == Material.IRON_DOOR &&
					   rs.getString("name").contains("Back")) 
					{
						rowcount++;
						continue;
					} 
					if(Material.getMaterial(rs.getString("material")) == Material.AIR) {
						continue;
					}
					ItemStack item = new ItemStack(Material.getMaterial(rs.getString("material")), rs.getInt("count"));
					ItemMeta itemMeta = item.getItemMeta();
					itemMeta.setDisplayName(rs.getString("name"));
					item.setItemMeta(itemMeta);
					items.add(item);
					rowcount++;
				}
				if(rowcount > 0) {
					 String psql = "DELETE FROM rail WHERE uuid = " + ent.getEntityId();
		             try {
		            	 PreparedStatement pstmt = con.prepareStatement(psql);
		            	 pstmt.executeUpdate();
		            	 
						//Bukkit.broadcastMessage("Delete Rails from Carts Oke");
		             } catch(Exception E) {
		 				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DeleteRailCart: " + E.getMessage());
		             }
				}	
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(del)getRail: " + e.getMessage());
			}

            sql = "SELECT * FROM mined WHERE uuid = " + ent.getEntityId(); /* Delete The Cart Mined Table */
            try {
            	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				int rowcount = 0;
				while(rs.next()) {
					
					if(Material.getMaterial(rs.getString("material")) == Material.IRON_DOOR &&
					   rs.getString("name").contains("Back")) 
					{
						rowcount++;
						continue;
					} 
					if(Material.getMaterial(rs.getString("material")) == Material.AIR) {
						continue;
					}
					ItemStack item = new ItemStack(Material.getMaterial(rs.getString("material")), rs.getInt("count"));
					ItemMeta itemMeta = item.getItemMeta();
					itemMeta.setDisplayName(rs.getString("name"));
					item.setItemMeta(itemMeta);
					items.add(item);
					rowcount++;
				}
				
				if(rowcount > 0) {
					 String psql = "DELETE FROM mined WHERE uuid = " + ent.getEntityId();
		             try {
		            	 PreparedStatement pstmt = con.prepareStatement(psql);
		            	 pstmt.executeUpdate();
		            	 
						//Bukkit.broadcastMessage("Delete mined from Carts Oke");
		             } catch(Exception E) {
		 				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DeleteMinedCart: " + E.getMessage());
		             }
				}	
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(del)getMined: " + e.getMessage());
			}
	         DB.DBCloseConnection(con);
		}
		return items;
	}
}
