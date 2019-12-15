package r4ndom1.DB.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;

import r4ndom1.DB.DB;

public class CartConfiguration {
	
	private static String NewDir = "";
	private static String CurDir = "";
	private static Connection con = null;
	
	public static void DirSwitch(ArrayList<String> lore) {
		for(String dir: lore) {
			if(dir.contains("North")) {
				NewDir = "East";
				CurDir = "North";
			} else if(dir.contains("East")) {
				NewDir = "South";
				CurDir = "East";
			} else if(dir.contains("South")) {
				NewDir = "West";
				CurDir = "South";
			} else if(dir.contains("West")) {
				NewDir = "Up";
				CurDir = "West";
			} else if(dir.contains("Up")) {
				NewDir = "Down";
				CurDir = "Up";
			} else if(dir.contains("Down")) {
				NewDir = "North";
				CurDir = "Down";
			} else {
				NewDir = "North";
				CurDir = "North";
			}
		}
	}
	
	public static String GetDir(ArrayList<String> lore) {
		DirSwitch(lore);
		return CurDir;
	}
	
	public static String GetNextDir(ArrayList<String> lore) {
		DirSwitch(lore);
		return NewDir;
	}
	
	public static void MoveCart(Entity ent, Location toLoc) {
		con = DB.DBConnection();
		
		ent.teleport(toLoc);
		
		Location loc = ent.getLocation();
		double locX = loc.getBlockX(),
		        locZ = loc.getBlockZ(),
		        locY = loc.getBlockY();
		 
		String sql = " UPDATE carts \n" +
				     " SET locX = '" + locX + "', \n" +
				     " locY = '" + locY + "', \n" +
				     " locZ = '" + locZ + "', \n" +
				     " world = '" + ent.getWorld().getName() + "' \n" +
			  	     " WHERE " +
			         " uuid = '" + ent.getEntityId() + "' ;";
        	
        PreparedStatement pstmt;
        
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (Exception e) {
			Bukkit.broadcastMessage("MoveCartError:" + e.getMessage());
		}
	    DB.DBCloseConnection(con);
	}
	
	public static void SetDirection(Inventory in, String uuid) {
		if(in.getItem(12) != null) {
			Connection con = DB.DBConnection();
			
			ArrayList<String> lore = (ArrayList<String>) in.getItem(12).getItemMeta().getLore(); 
			String dir = GetDir(lore);
			
			try {
			 String sql = " UPDATE carts \n" +
    				  	  " SET dir = '" + dir + "' \n" +
    				  	  " WHERE uuid = " + uuid + " ;";
			
			
    		PreparedStatement pstmt = con.prepareStatement(sql);
			//Bukkit.broadcastMessage("UPDATE/INSERT OKE");
	        pstmt.executeUpdate();
			} catch(Exception E) {
				Bukkit.broadcastMessage("SetDirection" + E.getMessage());
			}
			
			DB.DBCloseConnection(con);
		}
	}
}
