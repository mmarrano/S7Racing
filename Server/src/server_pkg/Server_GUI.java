package server_pkg;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;



public class Server_GUI {
	public JFrame frame;
	public JTextField MessageBox;
	public JTextPane playerChatText;
	public JTextPane playerListText;
	public JList lobbyList;
	
	public Server_GUI() {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Server_GUI window = new Server_GUI();
					initialize();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	
	//public Server_GUI() {
	//	initialize();
	//}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("S7 Server v0.5");
		//frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Server.class.getResource("/Server_GUIpkg/rack_server.png")));
		frame.setBounds(100, 100, 550, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane PlayerList = new JScrollPane();
		PlayerList.setBounds(200, 61, 334, 101);
		PlayerList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(PlayerList);
		
		playerListText = new JTextPane();
		PlayerList.setViewportView(playerListText);
		
		JScrollPane CurrentLobbys = new JScrollPane();
		CurrentLobbys.setBounds(16, 61, 172, 213);
		CurrentLobbys.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		CurrentLobbys.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(CurrentLobbys);
		
		lobbyList = new JList(Server.lobbys.toArray());
		lobbyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		CurrentLobbys.setViewportView(lobbyList);
		
		
		JScrollPane PlayerChat = new JScrollPane();
		PlayerChat.setBounds(200, 169, 334, 80);
		PlayerChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(PlayerChat);
		
		playerChatText = new JTextPane();
		playerChatText.setEditable(false);
		playerChatText.setText("Type to chat, or \"~help\" for help.\n");
		PlayerChat.setViewportView(playerChatText);
	
		Canvas canvas = new Canvas();
		canvas.setBounds(10, 280, 524, 138);
		canvas.setBackground(Color.GRAY);
		frame.getContentPane().add(canvas);
		
		MessageBox = new JTextField();
		MessageBox.setBounds(200, 248, 245, 26);
		frame.getContentPane().add(MessageBox);
		MessageBox.setColumns(10);
		MessageBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String message = MessageBox.getText();
				if(message.equals("~help")) {
					update_textbox(help());
				}
				else
				{String m  = message;
				update_textbox(m);

				for (Client c : Server.lobbys.get(getLobbyIndex()).clients) {
						c.out.println(m);
				}
				}

			}
		});
		
		
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Basic View");
		mntmNewMenuItem.setBounds(16, 16, 517, 19);
		frame.getContentPane().add(mntmNewMenuItem);
		
		JLabel lblCurrentGames = new JLabel("Current Games");
		lblCurrentGames.setBounds(16, 46, 172, 16);
		frame.getContentPane().add(lblCurrentGames);
		
		JLabel lblPlayerList = new JLabel("Player List");
		lblPlayerList.setBounds(200, 47, 334, 16);
		frame.getContentPane().add(lblPlayerList);
		
		JButton clearChatButton = new JButton("Clear");
		clearChatButton.setBounds(440, 248, 93, 29);
		frame.getContentPane().add(clearChatButton);
		clearChatButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				playerChatText.setText("");
			}
		});
	}
	
	private String help() {
		return    "===================\n" 
				+ "~kick <playername>\n"
				+ "~restMap\n"
				+ "~resetPlayerStats <playername>\n"
				+ "===================\n";
	}
	
	public void update_textbox(String message) {
		if(message.equals("~help")) {
			playerChatText.setText(help());
			}	
		else { 
			String logString = "";
			for(int i = 0; i < Server.lobbys.get(getLobbyIndex()).log.size(); i++) {
				logString = logString + Server.lobbys.get(getLobbyIndex()).log.get(i) + "\n";
			}
			
			playerChatText.setText(logString);
			}
	MessageBox.setText("");
	update_clientBox();
	
	}
	public void update_clientBox() {
		String text = "";
		ArrayList<Client> c = Server.lobbys.get(getLobbyIndex()).clients;
		for (int i = 0; i < c.size(); i++) {
			text = text + c.get(i).name + "\n";
		}
		playerListText.setText(text);
		
	}
	
	private int getLobbyIndex() {
		int index = lobbyList.getSelectedIndex();
		
		if(index < 0) {
			return 0;
		}
		return index;
	}
	
}