package com.example.patriot.s7racing.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.patriot.s7racing.R;
import com.example.patriot.s7racing.client.Client;
import com.example.patriot.s7racing.client.RepeatReceive;
import com.example.patriot.s7racing.game.GameStart;

public class DefaultLobbyUI extends AppCompatActivity {

    private Client c = new Client("THIS SHOULDN'T WORK", null, this);
    private TextView clientList;

    private Button b_go;
    private Button b_refresh_list;
    private TextView readyMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_default_lobby);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        b_go = (Button) findViewById(R.id.bGo);
        b_refresh_list = (Button) findViewById(R.id.bRefreshList);
        readyMsg = (TextView) findViewById(R.id.readyMsg);
        clientList = (TextView) findViewById(R.id.clientList);

        c = new Client(username, getApplicationContext(), this);
        new Thread(c).start();

        b_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.sendMsg("~1");
                readyMsg.setVisibility(View.VISIBLE);
                b_go.setEnabled(false);
            }
        });

        b_refresh_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.sendMsg("*");
            }
        });
    }

    @Override
    protected void onStart() {
        b_go.setEnabled(true);
        readyMsg.setVisibility(View.INVISIBLE);
        super.onStart();
        RepeatReceive r = new RepeatReceive(c);
        r.start();
    }

    public void startGame(){
        Store.driver = c;
        Intent i = new Intent(DefaultLobbyUI.this, GameStart.class);
        startActivity(i);
        //finish();
    }

    /*
    public void endGame(String winner, String result){
        Intent intent = new Intent(DefaultLobbyUI.this, VictoryScreen.class);
        intent.putExtra("winner", winner);
        intent.putExtra("result", result);
        startActivity(intent);
        finish();
    }
    */

    public void updateClients(final String updatedClients){
        final String newList = updatedClients.replaceAll(" ", "\n");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clientList.setText(newList);
            }
        });
    }
}