package server_pkg;
import java.util.ArrayList;




public class Lobby {
	boolean inGame = false;
	String name;	
	int readyUps = 0;
	public ArrayList<Client> clients = new ArrayList<Client>();
	ArrayList<String> log = new ArrayList<String>();
	Map map = new Map(clients);
	String finalrank = "";
	
	public Lobby(String n) {
		name = n;
	}
	
	//public void movePlayer(Client c) {	
	//}
	
	public void sendMessage(String message){
		log.add(message);
		System.out.println("OUT: "+ message);
		for(int i = 0; i < clients.size(); i++) {
			clients.get(i).out.println(message);
		}
	}
	
	public void addToLog(String string) {
		log.add(string + "\n");
	}
	
	public void addClient(Client client){
		
		for(int i = 0; i < clients.size(); i++) {
		
			if(clients.get(i).getClientName().equals(client.getClientName())){
				clients.remove(i);
				clients.add(client);
				System.out.println(client.getClientName() + " " + "joined " + name + ".");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendMessage("* " + playerList());
				return;
				}
		
		
		}
		
		clients.add(client);
		client.ThisLobby = this;
		client.reset();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String n = client.getClientName();
		System.out.println(n + " " + "joined " + name + ".");
		sendMessage("* " + playerList());
	}
	
	
	
	
	public void removeClient(Client client) {
		clients.remove(client);	
		if(clients.size() == 0) {
			inGame = false;
		}
		}
	
	public String toString() {
		return name;	
	}
	
	public void dumpPlayers(Lobby toLobby) {
		if(readyUps >= clients.size() * (3.0/5.0)) {
			readyUps = 0;
		while(clients.size() > 0) {
			clients.get(0).reset();
			toLobby.addClient(clients.get(0));
			removeClient(clients.get(0));
		}
		}
		toLobby.finalrank = "";
		toLobby.sendMessage("&");
		toLobby.inGame = true;
		
	}
	
	public String playerList() {
		String list = "";
		for(int i = 0; i < clients.size(); i++) {
			list = list + clients.get(i).getClientName() + " ";
			
		}
		
		return list;
		
	}
	
	
	
	public void updateLobbyClients() {
		//sendMessage(map.getLocations());
		map.update();
	}
	/*
	 public void checkWin() {
		 for(int i = 0; i < clients.size(); i++) {
				if(clients.get(i).getY() > 1000) {
					sendMessage("%" + " " + clients.get(i).name);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					readyUps = 0;
					inGame = false;
					dumpPlayers(Server.lobbys.get(0));
				}
			}
	 }
	 */
	
}
