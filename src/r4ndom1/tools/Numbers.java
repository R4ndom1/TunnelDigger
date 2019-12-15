package r4ndom1.tools;

import java.util.Random;

public class Numbers {
	
	public static int RandomNumber(int min, int max) {
		Random r = new Random();
		
		return r.nextInt((max - min) + 1) + min;
	}

}
