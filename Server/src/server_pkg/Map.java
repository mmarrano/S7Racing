package server_pkg; 

import java.util.ArrayList;

public class Map {
	public ArrayList<Client> players = new ArrayList<Client>();
	//private Scanner scanner;
	private String loc;
	

	public Map(ArrayList<Client> clients) {
		players = clients;
		
	}

	public void update(){
		loc = "Loc\n";
		Client c;
		for(int i = 0; i < players.size(); i++) {
			c = players.get(i);
			c.sinceUpdate++;
			if(c.sinceUpdate > 100) {
				c.ThisLobby.removeClient(c);
				c.stopClient();
			} else {
			loc = loc + c.getClientName() + " " + c.getLocation() + "\n";}
		}


	}
	
	
	public String getLocations(){
		update();
		return loc;

	}
	

}
