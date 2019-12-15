package r4ndom1.DB.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import r4ndom1.DB.*;
import r4ndom1.gui.CustomItemData;

public class RegisterCart {
	private static Connection con = null;
	
	public static void regigisterCart(Entity ent, Player pl) {
		con = DB.DBConnection();
		
        try {
        	String sql = "INSERT INTO carts (uuid,owner,world,LocX,LocY,LocZ) \n " +
        				 "VALUES ('" + ent.getEntityId() + "','" + 
        				 			   pl.getName() + "' , '" + 
        				 			   ent.getWorld().getName() + "' , " +
        				 			   ent.getLocation().getBlockX() + " , " +
        				 			   ent.getLocation().getBlockY() + " , " +
        				 			   ent.getLocation().getBlockZ() + " ); ";
			

    		PreparedStatement pstmt = con.prepareStatement(sql);
	        pstmt.executeUpdate();
			//Bukkit.broadcastMessage("INSERT OKE");
		} catch (SQLException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "regigisterCart: " + e.getMessage());
		}
        
		DB.DBCloseConnection(con);
	}
	
	public static boolean getRegisterd(Entity ent) {
		if(ent.getEntityId() != 0) {
			con = DB.DBConnection();
	        String sql = "SELECT COUNT(*) FROM carts WHERE uuid = " + ent.getEntityId();
	        
             try {
            	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				if(rs.getInt(1) > 0 ) {
					DB.DBCloseConnection(con);
					return true;
				}
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "getRegisterd: " + e.getMessage());
			}
		}
		DB.DBCloseConnection(con);
		return false;
	}
	
	public static void setBurnTime(Entity ent, int ticks) {
		try {
			con = DB.DBConnection();
			String sql = " UPDATE carts \n" +
						 " SET fuellevel = '" + ticks + "' \n" +
						 " WHERE uuid = '" + ent.getEntityId() + "';";
        	
        	PreparedStatement pstmt = con.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        DB.DBCloseConnection(con);
		}
		catch (Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "(UPDATE) fuellevel: " + e.getMessage());
		}
	}
	
	public static int getBurnTime(Entity ent) {
		if(ent.getEntityId() != 0) {
			con = DB.DBConnection();
	        String sql = "SELECT fuellevel FROM carts WHERE uuid = " + ent.getEntityId() + " ;";
	        int fuellevel = 0;
	        
             try {
            	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				while(rs.next()) {
					fuellevel = rs.getInt("fuellevel");
					DB.DBCloseConnection(con);
					return fuellevel;
				}
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "getBurnTime: " + e.getMessage());
			}
		}
		
		DB.DBCloseConnection(con);
		return 0;
	}
	
	public static String getDBdirection(String uuid) {
		String dir = "";
		if(uuid != null) {
			con = DB.DBConnection();
	        String sql = "SELECT dir FROM carts WHERE uuid = " + uuid + " ;";
	        
             try {
            	Statement stmt  = con.createStatement();
				ResultSet rs    = stmt.executeQuery(sql);
				while(rs.next()) {
					dir = rs.getString("dir");
				}
				
			} catch (Exception e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "getBurnTime: " + e.getMessage());
			}
		}
		DB.DBCloseConnection(con);
		return dir;
	}
	
	public static void LoadCarts() {
		int c = 0;
		con = DB.DBConnection();
        	
		try 
		{	
	    	String sql = "SELECT * FROM carts ;";
	    	Statement stmt  = con.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			
			ArrayList<Location> cartLocs = new ArrayList<Location>();
			ArrayList<String> uuids = new ArrayList<String>();
			
			while(rs.next()) {
				
				String cartWorldName = rs.getString("world");
				
				if(cartWorldName == null) {
					continue;
				}
				
				Location TempLoc = new Location(Bukkit.getServer().getWorld(cartWorldName), 
								     			rs.getFloat("LocX"),  
								     			rs.getFloat("LocY"),  
								     			rs.getFloat("LocZ"));
				
				cartLocs.add(TempLoc);
				uuids.add(rs.getString("uuid"));
			}
		
			DB.DBCloseConnection(con);
			
			for(int i = 0; i < cartLocs.size(); i ++) {
				Location loc = cartLocs.get(i);
				
				loc.getChunk().load();
				
				World w = Bukkit.getWorld(loc.getWorld().getName());
				
				for(Entity ent : w.getEntities()) {
					
					if(ent.getType() == EntityType.MINECART && 
					   ent.getCustomName().contains("Tunnel Digger") &&
					   CustomItemData.extractHiddenString(ent.getCustomName().toString()) != null)
					{
						con = DB.DBConnection();
						
						Location entLoc = ent.getLocation();
					
						if( entLoc.getBlockX() == loc.getBlockX() && 
						    entLoc.getBlockY() == loc.getBlockY() && 
						    entLoc.getBlockZ() == loc.getBlockZ()) 
						{
							String Olduuid = uuids.get(i);
							
							// Replace Old UUID with new UUID based on saved location (carts) 
							try {
								String PstSql = " UPDATE carts \n" +
											    " SET uuid = '" + ent.getEntityId() + "' \n" +
											    " WHERE " +
											    " LocX = '" + loc.getBlockX() + "' AND " +
											    " LocY = '" + loc.getBlockY() + "' AND " +
											    " LocZ = '" + loc.getBlockZ() + "' AND " +
											    " world = '" + loc.getWorld().getName() + "' ;";
					        	
					        	PreparedStatement pstmt = con.prepareStatement(PstSql);
						        pstmt.executeUpdate();
								c ++;
							}
							catch (Exception e) {
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "ChangeMainUUID: " + e.getMessage());
							}
							
							
							// Replace Old UUID with new UUID based on saved location (blocks)
							try {
								String PstSql = " UPDATE block \n" +
											    " SET uuid = '" + ent.getEntityId() + "' \n" +
											    " WHERE " +
											    " uuid = '" + Olduuid + "' ;";
								
					        	PreparedStatement pstmt = con.prepareStatement(PstSql);
						        pstmt.executeUpdate();
						        
							}
							catch (Exception e) {
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "ChangeblocksUUID: " + e.getMessage());
							}
							
							
							// Replace Old UUID with new UUID based on saved location (fuel)
							try {
								String PstSql = " UPDATE fuel \n" +
											    " SET uuid = '" + ent.getEntityId() + "' \n" +
											    " WHERE " +
											    " uuid = '" + Olduuid + "' ;";
								
					        	PreparedStatement pstmt = con.prepareStatement(PstSql);
						        pstmt.executeUpdate();
						        
							}
							catch (Exception e) {
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "ChangeFuelUUID: " + e.getMessage());
							}
							
							
							// Replace Old UUID with new UUID based on saved location (mined)
							try {
								String PstSql = " UPDATE mined \n" +
											    " SET uuid = '" + ent.getEntityId() + "' \n" +
											    " WHERE " +
											    " uuid = '" + Olduuid + "' ;";
								
					        	PreparedStatement pstmt = con.prepareStatement(PstSql);
						        pstmt.executeUpdate();
						        
							}
							catch (Exception e) {
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "ChangeMinedUUID: " + e.getMessage());
							}
							
							
							// Replace Old UUID with new UUID based on saved location (path)
							try {
								String PstSql = " UPDATE path \n" +
											    " SET uuid = '" + ent.getEntityId() + "' \n" +
											    " WHERE " +
											    " uuid = '" + Olduuid + "' ;";
								
					        	PreparedStatement pstmt = con.prepareStatement(PstSql);
						        pstmt.executeUpdate();
						        
							}
							catch (Exception e) {
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "ChangePathUUID: " + e.getMessage());
							}
							
							
							// Replace Old UUID with new UUID based on saved location (rail)
							try {
								String PstSql = " UPDATE rail \n" +
											    " SET uuid = '" + ent.getEntityId() + "' \n" +
											    " WHERE " +
											    " uuid = '" + Olduuid + "' ;";
								
					        	PreparedStatement pstmt = con.prepareStatement(PstSql);
						        pstmt.executeUpdate();
						        
							}
							catch (Exception e) {
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "ChangeRialsUUID: " + e.getMessage());
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "LoadCarts: " + e.getMessage());
			e.printStackTrace();
		}
		
		DB.DBCloseConnection(con);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "loaded: " + c + " Tunnel Diggers");
	}
	
	public static void SaveCarts() {
		int c = 0;
		con = DB.DBConnection();
		try 
		{	
	    	String sql = "SELECT COUNT(*) FROM carts ;";
	    	Statement stmt  = con.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			
			if(rs.getInt(1) > 0) {
				c = rs.getInt(1);
			}
			
		} catch (Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "SaveCarts: " + e.getMessage());
			e.printStackTrace();
		}
		
		DB.DBCloseConnection(con);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Saved: " + c + " Tunnel Diggers");
	}
}














