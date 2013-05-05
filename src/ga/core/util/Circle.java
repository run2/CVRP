package ga.core.util;

public class Circle {
	public static void main(String args[]){
		double angleInDegrees;
		int points = 12;
		for(int i = 0 ; i < points ; i ++){
			angleInDegrees = (360/points)*i;
	        // Convert from degrees to radians via multiplication by PI/180        
	        float x = (float)(100 * Math.cos(angleInDegrees * Math.PI / 180F)) + 200;
	        float y = (float)(100 * Math.sin(angleInDegrees * Math.PI / 180F)) + 200;
	        System.out.println(x + " " + y);
		}
	}
}
