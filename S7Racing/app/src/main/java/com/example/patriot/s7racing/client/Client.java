package com.example.patriot.s7racing.client;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.patriot.s7racing.lobby.DefaultLobbyUI;
import com.example.patriot.s7racing.lobby.VictoryScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread implements Serializable {
    public static Socket socket;
    public static PrintWriter out;
    public static BufferedReader serverIn;
    private static String username;
    private Context c;
    private ArrayList<String> locations;
    private String winners;
    private DefaultLobbyUI defaultLobbyUI;

    public Client(String username, Context c, DefaultLobbyUI d) {
        this.username = username;
        this.c = c;
        defaultLobbyUI = d;
    }

    public void initializeConnection() {
        if (socket == null) {
            try {
                socket = new Socket("proj-309-gp-b-4.cs.iastate.edu", 27031);
                Log.d("LOG", "Client connected to server");
                out = new PrintWriter(socket.getOutputStream(), true);
                serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        initializeConnection();
        sendMsg(username);
        try {
            sleep(10);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        sendMsg("*");
    }

    public void sendMsg(final String message) {
        new Thread() {
            public void run() {
                out.println(message);
            }
        }.start();
    }


    public void receiveMsg() {
        try {
            if (serverIn != null) {
                String msg = serverIn.readLine();
                if (msg != null) {
                    handleMessage(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(String msg) {
        if (msg.contains("%")){
            //winners = msg.replace("%", "\n");
            winners = msg;
        } if (msg.contains("Loc")) {
            locations = parseLocations();
        } else if (msg.contains("*")){
            msg = msg.replace("*", "");
            defaultLobbyUI.updateClients(msg);
        } else if (msg.contains("&")) {
            defaultLobbyUI.startGame();
        }

        else {
            Log.d("MSG", msg);
        }
    }

    public ArrayList<String> getUpdatedLocations() {
        return locations;
    }

    private ArrayList<String> parseLocations() {
        ArrayList<String> toReturn = new ArrayList<String>();
        try {
            String line = serverIn.readLine();
            while(line != null && !(line.equals(""))){
                toReturn.add(line);
                line = serverIn.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public String getUsername(){
        return username;
    }

    public String getWinners(){
        return winners;
    }
}