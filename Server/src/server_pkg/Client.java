package server_pkg;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import GameEntity.Player;

public class Client extends Thread {

	Socket s; // connects to client
	String name; // client name from input
	private BufferedReader in; // reads input from client
	PrintWriter out; // sends string to clients
	Player player;
	Lobby ThisLobby;
	int sinceUpdate;
	Scanner scanner;
	
	int X = 0;
	int Y = 0;
	
	Client(Socket s) {
		this.s = s;
	}



	public void run() { //could make this synchronized with and sendMessage
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
			

			// Request name from client
			//out.println("msg" + "Enter your Name:");
			name = in.readLine();
			player = new Player(name);
			//Server.gui.update_textbox(name + " Joined!");			
			sendMessage();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage() throws IOException, ClassNotFoundException {
		try {
			String receiveMsg;
			while (true) {
				if ((receiveMsg = in.readLine()) != null) {
					//System.out.println(name +" in: " + receiveMsg);
					
					
					if(receiveMsg.charAt(0) == '$') {
						scanner = new Scanner(receiveMsg);
						scanner.next();
						X = scanner.nextInt();
						Y = scanner.nextInt();
						sinceUpdate = 0;
						String s = ThisLobby.map.getLocations();
						ThisLobby.inGame = true;
						System.out.println(s);
						this.out.println(s);	
					}
					//Player finishes
					else if (receiveMsg.equals("#")) {
						ThisLobby.finalrank = ThisLobby.finalrank + name + "%";
						String r= ThisLobby.finalrank;
						System.out.println("%\n" + r);
						this.out.println("%\n" + r);	
						try {
							sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ThisLobby.removeClient(this);
						Server.lobbys.get(0).addClient(this);
					}
					//should never run
						else if (receiveMsg.equals("~")) {
						if(!ThisLobby.equals(Server.lobbys.get(0))) {
						this.out.println("msg" + "Moving to Menu");
						ThisLobby.removeClient(this);
						Server.lobbys.get(0).addClient(this);
						}
					}
						else if(receiveMsg.equals("*")) {
							System.out.println("hello "+  name);
							this.out.println( "* " +  ThisLobby.playerList());	
						}
					//Player ready ups
					else if(receiveMsg.equals("~1")) {
						//this.out.println("msg " + "Moving to lobby 1");
						ThisLobby.readyUps++;
						for(int i = 1 ; i < Server.lobbys.size(); i++) {
							if(Server.lobbys.get(i).inGame == false) {
								ThisLobby.dumpPlayers(Server.lobbys.get(i));
								break;
								}
						}
						
						//ThisLobby.removeClient(this);
						//Server.lobbys.get(1).addClient(this);
						//Server.lobbys.get(0).sendMessage("msg" + name + " joined!");
						
					}
					//should never run
					else if(receiveMsg.equals("~2")) {
						this.out.println("msg" + "Moving to lobby 2");
						ThisLobby.removeClient(this);
						Server.lobbys.get(2).addClient(this);
						//Server.lobbys.get(0).sendMessage("msg" + name + " joined!");
						
					}
					else {
						ThisLobby.log.add(name + ": " + receiveMsg);
					for (Client client : ThisLobby.clients) {
						if (!client.equals(this)) {
							client.out.println(name + ": " + receiveMsg);
						}
					}
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getClientName() {
		return name;
	}

	public String getLocation() {
		return "" + X + " " + Y;
	}

	
	public void reset() {
		setLocation(0, 0);
	}
	
	public void setLocation(int x, int y) {
		X = x;
		Y = y;
		
	}
	
	public void stopClient() {
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}
}



