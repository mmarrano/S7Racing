package GameEntity;

public class GameEntity {

	protected String type = "Enitity";
	protected final int STATIC = 0;
	protected final int MOVABLE = -1;
	
	//All Entities have an id, Players have an ID starting with 11###, Immovables 10###, Movables 12###
	protected int id;
	
	
	protected int onCollision;	
	
	protected int x = -1;
	protected int y = -1;

	
	public GameEntity(){
		onCollision = STATIC;
		
	}
	
	public GameEntity(int id){
		this.id = id;
		onCollision = STATIC;
		
	}
	 public int getX() {
		 return x;
	 }
	 
	 public int getY() {
		 return y;
	 }
	 
	public int getCollisionType() {
		 return onCollision;
	 }
	
	 public String getType() {
			return type;
		 }
	
}
