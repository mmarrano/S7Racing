package server_pkg;


import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;


	

public class Server {
	//static ArrayList<Client> clients = new ArrayList<Client>();
	static ArrayList<Lobby> lobbys = new ArrayList<Lobby>();
	static int messageNum = 0;
	static int currentLobbyIndex = 0;
	/////static Server_GUI gui;
	public static int PORT = 27031;
	
	public static void main(String[] args) throws InterruptedException{
		
		if (args.length != 0){
		PORT = Integer.valueOf(args[0]);
	}
		lobbys.add(new Lobby("Menu"));
		lobbys.add(new Lobby("Lobby 1"));
		lobbys.add(new Lobby("Lobby 2"));
		lobbys.add(new Lobby("Lobby 3"));
		lobbys.add(new Lobby("Lobby 4"));
		lobbys.add(new Lobby("Lobby 5"));
		lobbys.add(new Lobby("Lobby 5"));
		//------gui = new Server_GUI();
		Thread.sleep(500);
		try {
			run();
			
			//-------gui.update_textbox("it Works!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Server is running");
			//------gui.update_textbox("Server is running");
			while (true) {
				
				try {
					Socket clientSocket = serverSocket.accept();
					Client client = new Client(clientSocket);
					client.start();
					//clients.add(client);
					lobbys.get(0).addClient(client);
					//lobbys.get(0).sendMessage("Im here");
				} catch (IOException e) {

					for(int j = 0; j < lobbys.size(); j++) {
						lobbys.get(j).updateLobbyClients();
						if(lobbys.get(j).inGame == false) {
							lobbys.get(j).sendMessage("* " + lobbys.get(j).playerList());
						}
					}
					
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
