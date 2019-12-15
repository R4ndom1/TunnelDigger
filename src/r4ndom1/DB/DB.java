package r4ndom1.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;


public class DB { 
	static Statement stmt = null;
	
	public DB() { /* Connect to SQLite DB */
		try {
			Connection con = DBConnection();
			String sql = "CREATE TABLE IF NOT EXISTS carts ( \n" +
						 "id 	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , \n" +
						 "uuid  TEXT NOT NULL , \n" +
						 "owner INTERGER NOT NULL , \n" +
						 "fuellevel INTEGER , \n" +
						 "step INTEGER , \n" +
						 "lenght INTEGER , \n" +
						 "dir TEXT , \n" +
						 "world TEXT , \n" +
						 "LocX  REAL , \n" + 
						 "LocY  REAL , \n" + 
						 "LocZ  REAL \n" +
						 "); ";
			
			stmt = con.createStatement();
	        stmt.execute(sql); 
	        
	        sql = "CREATE TABLE IF NOT EXISTS fuel ( \n" +
					 "id 	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , \n" +
					 "uuid  TEXT NOT NULL , \n" +
					 "material TEXT NOT NULL , \n" + 
					 "name TEXT , \n" +
					 "place INTEGER NOT NULL , \n" +
					 "count INTEGER NOT NULL , \n" +
					 "ticks" +
					 "); ";
		
	        stmt = con.createStatement();
	        stmt.execute(sql); 
	        
	        sql = "CREATE TABLE IF NOT EXISTS rail ( \n" +
					 "id 	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , \n" +
					 "uuid  TEXT NOT NULL , \n" +
					 "material TEXT NOT NULL , \n" + 
					 "name TEXT , \n" +
					 "place INTEGER NOT NULL , \n" +
					 "count INTEGER NOT NULL \n" +
					 "); ";
		
	        stmt = con.createStatement();
	        stmt.execute(sql); 
	        
	        sql = "CREATE TABLE IF NOT EXISTS block ( \n" +
					 "id 	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , \n" +
					 "uuid  TEXT NOT NULL , \n" +
					 "material TEXT NOT NULL , \n" + 
					 "name TEXT , \n" +
					 "place INTEGER NOT NULL , \n" +
					 "count INTEGER NOT NULL \n" +
					 "); ";

	        stmt = con.createStatement();
	        stmt.execute(sql); 
	        
	        sql = "CREATE TABLE IF NOT EXISTS mined ( \n" +
					 "id 	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , \n" +
					 "uuid  TEXT NOT NULL , \n" +
					 "material TEXT NOT NULL , \n" + 
					 "name TEXT , \n" +
					 "place INTEGER NOT NULL , \n" +
					 "count INTEGER NOT NULL \n" +
					 "); ";
		
	        stmt = con.createStatement();
	        stmt.execute(sql); 
	        
	        sql = "CREATE TABLE IF NOT EXISTS path ( \n" +
					 "id 	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , \n" +
					 "uuid  TEXT NOT NULL , \n" +
					 "direction INTEGER NOT NULL , \n" +
					 "lenght INTEGER NOT NULL , \n" +
					 "step INTEGER NOT NULL " +
					 "); ";
		
	        stmt = con.createStatement();
	        stmt.execute(sql); 
	        	        
	        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "DB Setup Succes :)");
	        
	        DBCloseConnection(con);
		} 
		catch ( Exception e ) 
		{
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DB Setup Failed :(" + e.getMessage());
		}
	}
	
	
	public static Connection DBConnection() {
		
        String url = "jdbc:sqlite:test.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "DB Connected :)");
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "DB Connection Failed :( " + e.getMessage());
        }
        return conn;
	}
	
	
	public static void DBCloseConnection(Connection con) {
		try {
			con.close();
			//Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "DB Closed ;)");
		}
		catch( Exception E ) 
		{
			
		}
	}
}  

