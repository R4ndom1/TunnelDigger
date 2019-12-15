package r4ndom1.tools;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class GetLocation {
	
	private static Location Nloc = null;
	private static Vector vec = null;

	public static void DirectionSwitch(String dir, Location Cloc){
		
		if(dir.contains("North")) {
			Nloc = Cloc.subtract(0,0,1);
			
			vec = new Vector(0,0,-0.04);
			
		} else if(dir.contains("East")) {
			Nloc = Cloc.add(1,0,0);
			
			vec = new Vector(0.04,0,0);
			
		} else if(dir.contains("South")) {
			Nloc = Cloc.add(0,0,1);
			
			vec = new Vector(0,0,0.04);
			
		} else if(dir.contains("West")) {
			Nloc = Cloc.subtract(1,0,0);
			
			vec = new Vector(-0.04,0,0);
			
			
		} else if(dir.contains("Up")) {
			Nloc = Cloc.add(0,0,0);
		} else if(dir.contains("Down")) {
			Nloc = Cloc.add(0,0,0);
		} else {
			Nloc = Cloc;
		}
	}
	
	public static Vector GetVelocityVector(String dir, Location loc) {
		DirectionSwitch(dir, loc);
		return vec;
	}
	
	public static Location GetNextLocation(String dir, Location loc) {
		DirectionSwitch(dir, loc);
		return Nloc;
	}
}
