package GameEntity;


public class Player extends Movable {
	String username;
	
	public Player(int i, int xi, int yi, String user) {
		type = "Player";
		username = user;
		y = yi;
		x = xi;
		
	}
	public Player(String user) {
		type = "Player";
		username = user;
		
	}
	
	public void reset() {
		x = 0;
		y = 0;
	}
	
	
	public String getName() {
		return username;	
	} 
	
	public void Move(int xi, int yi) {
		if(xi < 0) {
			x--;}
		else if(xi > 0){
			x++;
		}
		if(yi < 0) {
			y--;}
		else if(yi > 0){
			y++;
		}
	}
}
