package GameEntity;

public class Movable extends GameEntity {
	private int speed = 0; // 1800 is recommended minimum speed
	private int direction = 0; // 0 is straight, -180 is left, 180 is right
	private int velocity = 0;
	private boolean gravity = false; //if true Acceleration will effect this movable, therefore speed up down the down hill
	
	protected Movable() {
		
	}
	
	public Movable(int i) {
		type = "Movable";
		id = i;
	}
	public Movable(int i, int xi, int yi, boolean gravity) {
		id = id;
		x = xi;
		y = yi;
		gravity = gravity;
	}
	
	public void setVelocity(int s, int dir) {
		speed = s;
		if(dir < -180 )
		direction = -180;
		else if(dir > 180 )
			direction = 180;
		else {
			direction =  dir;
		}
	}
	
	
}
