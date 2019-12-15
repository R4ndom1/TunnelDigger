package r4ndom1.DB.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import r4ndom1.DB.DB;
import r4ndom1.gui.CustomItemData;
import r4ndom1.items.ItemData;

public class CartInventory {
	private static Connection con = null;
	
	public static Inventory DBgetFuelInvItems(String type, Entity ent, Inventory in) {
		con = DB.DBConnection();
		
        String sql = "SELECT * FROM " + type + " WHERE uuid = " + ent.getEntityId();
        
        try {
        	Statement stmt  = con.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			
			while(rs.next()) {
				if(rs.getInt("place") == 0) {
			        String encodedString = CustomItemData.encodeString("" + ent.getEntityId());
					ItemStack item = new ItemStack(Material.IRON_DOOR);
					ItemMeta itemMeta = item.getItemMeta();
					ArrayList<String> itemLore = new ArrayList<String>();
					itemLore.add(encodedString);
					itemMeta.setLore(itemLore);
					itemMeta.setDisplayName("Back");
					item.setItemMeta(itemMeta);
					in.setItem(0, item);
					continue;
				}
				ItemStack item = new ItemStack(Material.getMaterial(rs.getString("material")) , rs.getInt("count"));
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.setDisplayName(rs.getString("name"));
				item.setItemMeta(itemMeta);
				in.setItem(rs.getInt("place"), item);
			}
			
		} catch (Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DBgetInvItems: " + e.getMessage());
		}
        
        DB.DBCloseConnection(con);
		return in;
	}
        
	
	public static void DBnewInv(Inventory inv, String type, Entity ent) {
		con = DB.DBConnection();
		
        try {
        	String sql = "";
        	if(inv.getContents().length > 0) {
        		
        	   sql += " INSERT INTO " + type + " (uuid,material,name,place,count) \n" +
       			      " SELECT '" + 
        			   ent.getEntityId() + "' AS 'uuid' , '" + 
       			       inv.getItem(0).getType() + "' AS 'material' , '" +
       			       inv.getItem(0).getItemMeta().getDisplayName() + "' AS 'name' , '" +
       			       0 + "' AS 'place' , '" +
       			       inv.getItem(0).getAmount() + "' AS 'count' \n";
        	}
        	if(inv.getContents().length >= 1) {
        		for(int i = 1; i < inv.getContents().length; i++) 
    			{
    				if(inv.getItem(i) == null) {
    					continue;
    				}
    				
    				sql += "UNION SELECT '" + 
    						ent.getEntityId() + "' , '" + 
    	       			    inv.getItem(i).getType() + "' , '" +
    	       			    inv.getItem(i).getItemMeta().getDisplayName() + " ' , '" +
    	       			    i + "' , '" +
    	       			    inv.getItem(i).getAmount() + "' \n";
    				
    			}
        	}
        	
   
			sql += ";";
    		PreparedStatement pstmt = con.prepareStatement(sql);
			//Bukkit.broadcastMessage("INSERT OKE");
	        pstmt.executeUpdate();
		} catch (SQLException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DBnewInv: " + e.getMessage());
		}
		DB.DBCloseConnection(con);
	}
	
	public static void DBsetInvItem(String uuid, String type, Inventory inv) {
		
		for(int i = 1; i < inv.getSize(); i++) {
			ItemStack Item = inv.getItem(i);
			con = DB.DBConnection();
			
			if(inv.getItem(i) == null) {
				String psql = "DELETE FROM " + type + " WHERE uuid = " + uuid + " AND place =" + i + " ;";
	             try {
	            	 PreparedStatement pstmt = con.prepareStatement(psql);
	            	 pstmt.executeUpdate();
	             } catch(Exception E) {
	 				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DeleteRailItem: " + E.getMessage());
	             }
				continue;
			}
	        
	         try {
	 	        String sql = "SELECT COUNT(*) FROM " + type + " WHERE uuid = " + uuid + " AND place = " + i;
	        	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				
				if(rs.getInt(1) > 0 ) {
					//Bukkit.broadcastMessage("true");
		        	sql = " UPDATE " + type + "\n" +
        				  " SET material = '" + Item.getType().toString() + "' , \n" +
        		          " name = '" + Item.getItemMeta().getDisplayName().toString() + "' , \n" + 
        		          " place = '" + i + "' , \n" +
        				  " count = '" + Item.getAmount() + "' \n" +
        				  " WHERE uuid = " + uuid + " AND place = " + i + ";";
				} else {
					//Bukkit.broadcastMessage("false");
					sql = " INSERT INTO " + type + " (uuid,material,name,place,count) \n" +
		       			  " SELECT '" + 
	        			   uuid + "' AS 'uuid' , '" + 
	       			       Item.getType() + "' AS 'material' , '" +
	       			       Item.getItemMeta().getDisplayName() + "' AS 'name' , '" +
	       			       i + "' AS 'place' , '" +
	       			       Item.getAmount() + "' AS 'count' \n";	
				}
				try {
		    		PreparedStatement pstmt = con.prepareStatement(sql);
					//Bukkit.broadcastMessage("UPDATE/INSERT OKE");
			        pstmt.executeUpdate();
				}
				catch(Exception E) {
					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "SetInvItem: " + E.getMessage());
				}
				
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "getRegisterdBlockItemSlot: " + e.getMessage());
			}
	 	DB.DBCloseConnection(con);    
		}
	}
	
	public static void DBsetFuelInvItem(String uuid, Inventory inv, HumanEntity pl) {
		
		for(int i = 1; i < inv.getSize(); i++) {
			con = DB.DBConnection();
			ItemStack Item = inv.getItem(i);
			
			if(inv.getItem(i) == null) {
				String psql = "DELETE FROM fuel WHERE uuid = " + uuid + " AND place =" + i + " ;";
	             try {
	            	 PreparedStatement pstmt = con.prepareStatement(psql);
	            	 pstmt.executeUpdate();
	             } catch(Exception E) {
	 				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DeleteRailItem: " + E.getMessage());
	             }
				continue;
			}

			int fuellevel = ItemData.GetItemFuelTime(inv.getItem(i));
			
			if(fuellevel == 0) {
				pl.getInventory().addItem(Item);
				pl.sendMessage(ChatColor.GOLD + "Only log, planks, (char)coal (block) and lava can be used as fuel");
				continue;
			}
	        
	         try {
	 	        String sql = "SELECT COUNT(*) FROM fuel WHERE uuid = " + uuid + " AND place = " + i;
	        	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				
				if(rs.getInt(1) > 0 ) {
		        	sql = " UPDATE fuel \n" +
        				  " SET material = '" + Item.getType().toString() + "' , \n" +
        		          " name = '" + Item.getItemMeta().getDisplayName().toString() + "' , \n" +
        				  " count = '" + Item.getAmount() + "' , \n" +
        		          " ticks = '" + fuellevel + "' \n" +
        				  " WHERE uuid = " + uuid + " AND place = " + i + ";";
				} else {
					sql = " INSERT INTO fuel (uuid,material,name,place,count,ticks) \n" +
		       			  " SELECT '" + 
	        			   uuid + "' AS 'uuid' , '" + 
	       			       Item.getType() + "' AS 'material' , '" +
	       			       Item.getItemMeta().getDisplayName() + "' AS 'name' , '" +
	       			       i + "' AS 'place' , '" +
	       			       Item.getAmount() + "' AS 'count' , " +
	       			       fuellevel + " AS 'ticks';";
				}
				try {
		    		PreparedStatement pstmt = con.prepareStatement(sql);
			        pstmt.executeUpdate();
				}
				catch(Exception E) {
					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "SetFuelInvItem: " + E.getMessage());
				}
				
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "getRegisterdFuelItemSlot: " + e.getMessage());
			}
	 	DB.DBCloseConnection(con);    
		}
	}
	
public static void DBsetRailInvItem(String uuid, Inventory inv, HumanEntity pl) {
		
		for(int i = 1; i < inv.getSize(); i++) {
			con = DB.DBConnection();
			if(inv.getItem(i) == null) {
				String psql = "DELETE FROM rail WHERE uuid = " + uuid + " AND place =" + i + " ;";
	             try {
	            	 PreparedStatement pstmt = con.prepareStatement(psql);
	            	 pstmt.executeUpdate();
	             } catch(Exception E) {
	 				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DeleteRailItem: " + E.getMessage());
	             }
				continue;
			}
			
			ItemStack Item = inv.getItem(i);
			
			if(ItemData.GetTrueRail(Item) == false) {
				pl.getInventory().addItem(Item);
				pl.sendMessage(ChatColor.GOLD + "Only Rails are Allowed");
				continue;
			}
	        
	         try {
	 	        String sql = "SELECT COUNT(*) FROM rail WHERE uuid = " + uuid + " AND place = " + i;
	        	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				
				if(rs.getInt(1) > 0 ) {
		        	sql = " UPDATE rail \n" +
        				  " SET material = '" + Item.getType().toString() + "' , \n" +
        		          " name = '" + Item.getItemMeta().getDisplayName().toString() + "' , \n" +
        				  " count = '" + Item.getAmount() + "' \n" +
        				  " WHERE uuid = " + uuid + " AND place = " + i + ";";
				} else {
					sql = " INSERT INTO rail (uuid,material,name,place,count) \n" +
		       			  " SELECT '" + 
	        			   uuid + "' AS 'uuid' , '" + 
	       			       Item.getType() + "' AS 'material' , '" +
	       			       Item.getItemMeta().getDisplayName() + "' AS 'name' , '" +
	       			       i + "' AS 'place' , '" +
	       			       Item.getAmount() + "' AS 'count' ;";
				}
				try {
		    		PreparedStatement pstmt = con.prepareStatement(sql);
			        pstmt.executeUpdate();
				}
				catch(Exception E) {
					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "SetRailInvItem: " + E.getMessage());
				}
				
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "getRegisterdRailItemSlot: " + e.getMessage());
			}
	 	DB.DBCloseConnection(con);    
		}
	}
	
	public static Material GetRailType(String uuid) {
		con = DB.DBConnection();
		Material mat = null;
		
        String sql = "SELECT * FROM rail WHERE uuid =" + uuid + " ;";
        
        try {
        	Statement stmt  = con.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			while(rs.next()) {
				if(rs.getInt("place") == 0) {
					continue;
				}
				mat = Material.getMaterial(rs.getString("material"));
				 DB.DBCloseConnection(con);
			     return mat;
			}
			
        } catch (Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "CheckRailItem: " + e.getMessage());
		}
        
        DB.DBCloseConnection(con);
        return mat;
	}
	
	public static Material GetBlockType(String uuid) {
		con = DB.DBConnection();
		Material mat = null;
		
        String sql = "SELECT * FROM block WHERE uuid =" + uuid + " ;";
        
        try {
        	Statement stmt  = con.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			while(rs.next()) {
				if(rs.getInt("place") == 0) {
					continue;
				}
				
				mat = Material.getMaterial(rs.getString("material"));
				DB.DBCloseConnection(con);
		        return mat;
			}
			
        } catch (Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "CheckRailItem: " + e.getMessage());
		}
        
        DB.DBCloseConnection(con);
        return mat;
	}
	
	public static int UseFirstFuelItem(String uuid) {
		
		int ticks = 0;
		
		con = DB.DBConnection();
		
        String sql = "SELECT * FROM fuel WHERE uuid =" + uuid + " \n" +
        		     "ORDER BY ticks ASC";
        
        try {
        	Statement stmt  = con.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			try { 
				while(rs.next()) {
					if(rs.getInt("place") == 0) {
						continue;
					}
					ticks = rs.getInt("ticks");
					
					if(rs.getInt("count") <= 1) {
						String psql = "DELETE FROM fuel WHERE uuid = " + uuid + " AND place = " + rs.getInt("place") + ";";
						PreparedStatement pstmt = con.prepareStatement(psql);
						pstmt.executeUpdate();
						
						DB.DBCloseConnection(con);
						return ticks;
					}
					
		        	sql = " UPDATE fuel \n" +
	    				  " SET material = '" + rs.getString("material") + "' , \n" +
	    		          " name = '" + rs.getString("name") + "' , \n" + 
	    		          " place = '" + rs.getInt("place") + "' , \n" +
	    				  " count = '" + (rs.getInt("count") - 1 ) + "' , \n" +
	    		          " ticks = '" + rs.getInt("ticks") + "' \n" +
	    				  " WHERE uuid = " + uuid + " AND place = " + rs.getInt("place") + ";";
		        	
		        	PreparedStatement pstmt = con.prepareStatement(sql);
			        pstmt.executeUpdate();
			        
			        DB.DBCloseConnection(con);
			        return ticks;
				}
			}
			catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(UPDATE)DBUseFirstFuelItem: " + e.getMessage());
			}
			
		} catch (Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(SELECT)DBUseFirstFuelItem: " + e.getMessage());
		}
        
        DB.DBCloseConnection(con);
		return ticks;
	}
	
	public static ItemStack UseFirstRailItem(String uuid) {
		
		con = DB.DBConnection();
		
        String sql = "SELECT * FROM rail WHERE uuid =" + uuid + " ;";
        
        ItemStack item = null;
        
        try {
        	Statement stmt  = con.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			try { 
				while(rs.next()) {
					
					if(rs.getInt("place") == 0) {
						continue;
					}
					
					item = new ItemStack(Material.getMaterial(rs.getString("material")) , 1);
					ItemMeta itemMeta = item.getItemMeta();
					itemMeta.setDisplayName(rs.getString("name"));
					item.setItemMeta(itemMeta);
					
					if(rs.getInt("count") <= 1) {
						String psql = "DELETE FROM rail WHERE uuid = " + uuid + " AND place = " + rs.getInt("place") + ";";
						PreparedStatement pstmt = con.prepareStatement(psql);
						pstmt.executeUpdate();
					}
					
		        	sql = " UPDATE rail \n" +
	    				  " SET material = '" + rs.getString("material") + "' , \n" +
	    		          " name = '" + rs.getString("name") + "' , \n" + 
	    		          " place = '" + rs.getInt("place") + "' , \n" +
	    				  " count = '" + (rs.getInt("count") - 1 ) + "' \n" +
	    				  " WHERE uuid = " + uuid + " AND place = " + rs.getInt("place") + ";";
		        	
		        	PreparedStatement pstmt = con.prepareStatement(sql);
			        pstmt.executeUpdate();
			        
			        DB.DBCloseConnection(con);
			        return item;
				}
			}
			catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(UPDATE)DBUseFirstRailItem: " + e.getMessage());
			}
			
		} catch (Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(SELECT)DBUseFirstRailItem: " + e.getMessage());
		}       
        DB.DBCloseConnection(con);
        return item;
	}
	
	public static ItemStack UseFirstBlockItem(String uuid, int minCount) {
		
		con = DB.DBConnection();
		
        String sql = "SELECT * FROM block WHERE uuid =" + uuid + " ;";
        ItemStack item = null;
        
        try {
        	Statement stmt  = con.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			try { 
				while(rs.next()) {
					
					if(rs.getInt("place") == 0 || rs.getInt("count") < minCount || item != null) {
						continue;
					}
					
					
					item = new ItemStack(Material.getMaterial(rs.getString("material")) , 1);
					ItemMeta itemMeta = item.getItemMeta();
					itemMeta.setDisplayName(rs.getString("name"));
					item.setItemMeta(itemMeta);
					
					if(rs.getInt("count") <= minCount) {
						sql = "DELETE FROM block WHERE uuid =" + uuid + " AND place =" + rs.getInt("place") + ";";
						
					} else {
						sql = " UPDATE block \n" +
		    				  " SET material = '" + rs.getString("material") + "' , \n" +
		    		          " name = '" + rs.getString("name") + "' , \n" + 
		    				  " count = '" + (rs.getInt("count") - minCount ) + "' \n" +
		    				  " WHERE uuid = " + uuid + " AND place = " + rs.getInt("place") + ";";
					}
		        	PreparedStatement pstmt = con.prepareStatement(sql);
			        pstmt.executeUpdate();
				}
			}
			catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(UPDATE)DBUseFirstBlockItems: " + e.getMessage());
			}
			
		} catch (Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(SELECT)DBUseFirstBlockItems: " + e.getMessage());
		}       
        DB.DBCloseConnection(con);
        return item;
	}
	
	public static void SetMinedItem(ArrayList<ItemStack> collection, String uuid) {
		con = DB.DBConnection();
		
		for(ItemStack Item : collection) {
			if(Item == null) {
				continue;
			}
			if(Item.getType() == Material.AIR) {
				continue;
			}
		     try {
		    	 
		        String sql = "SELECT COUNT(*) FROM mined WHERE uuid = '" + uuid + 
		        									 "' AND material = '" + Item.getType() + "' ;";
		        //Bukkit.broadcastMessage(sql);
		    	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				
				String name = "";
				if(Item.getItemMeta().hasDisplayName()) {
					name = Item.getItemMeta().getDisplayName();
				} else {
					name = "";
				}
				
				if(rs.getInt(1) > 0) {
					
					sql = "  SELECT * FROM mined WHERE uuid = '" + uuid + 
					      "' AND material = '" + Item.getType() + "' ;";
					stmt  = con.createStatement();
					rs    = stmt.executeQuery(sql);
					
		        	sql = " UPDATE mined \n" +
	    				  " SET material = '" + Item.getType() + "' , \n" +
	    		          " name = '" + name + "' , \n" + 
	    		          " place = '" + rs.getInt("place") + "' , \n" +
	    				  " count = '" + (rs.getInt("count") + Item.getAmount()) + "' \n" +
	    				  " WHERE uuid = " + uuid + " AND place = " + rs.getInt("place") + ";";
				} else {
					
					/* Check for empty slot */
					int place = 0;
					try {
						for(int p = 0; p < 45; p ++) {
							sql = "SELECT COUNT(*) FROM mined WHERE uuid = '" + uuid + 
									  "' AND place = '" + p + "' ;";
							stmt  = con.createStatement();
							rs    = stmt.executeQuery(sql);
							if(rs.getInt(1) == 0) {
								place = p;
								break; 
							}
						}
					} catch (Exception e) {
						Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "SetMinedItemPlace: " + e.getMessage());
					}
					
					sql = " INSERT INTO mined (uuid,material,name,place,count) \n" +
		       			  " SELECT '" + 
		        			uuid + "' AS 'uuid' , '" + 
		       			    Item.getType() + "' AS 'material' , '" +
		       			    name + "' AS 'name' , '" +
		       			    place + "' AS 'place' , '" +
		       			    Item.getAmount() + "' AS 'count';";
				}
				try {
					
					PreparedStatement pstmt = con.prepareStatement(sql);
			        pstmt.executeUpdate();
			        
				} catch (Exception e) {
					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "SetMinedItem: " + e.getMessage());
				}
				
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "getRegisterdMinedItemSlot: " + e.getMessage());
			}
		}
 	DB.DBCloseConnection(con);  
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
