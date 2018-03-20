package com.example.patriot.s7racing.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patriot.s7racing.R;
import com.example.patriot.s7racing.client.Client;
import com.example.patriot.s7racing.login.LoginActivity;
import com.example.patriot.s7racing.login.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class VictoryScreen extends AppCompatActivity {
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_victory_screen);

        Client client = Store.driver;

        final Button bReturnToLobby = (Button) findViewById(R.id.b_lobby_return);
        final TextView tvResult = (TextView) findViewById(R.id.tv_result);
        final TextView tvWinner = (TextView) findViewById(R.id.tv_winner);
        final TextView tvWins = (TextView) findViewById(R.id.tv_wins);

        final ImageView image1 = (ImageView) findViewById(R.id.imageView3);
        final ImageView image2 = (ImageView) findViewById(R.id.imageView4);

        image1.setVisibility(View.INVISIBLE);
        image2.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        final String username = client.getUsername();

        bReturnToLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(VictoryScreen.this, DefaultLobbyUI.class);
                //intent.putExtra("username", username);
                //VictoryScreen.this.startActivity(intent);
                finish();
            }
        });

        String recievingMessage = client.getWinners();
        String splited[] = recievingMessage.split("%");

        if(splited[0].equals(username)){
            tvResult.setText("Congratulations!");
            tvWinner.setText("You have won the game!");

            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);

            Response.Listener<String> response_listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject json_response = new JSONObject(response);
                        boolean success = json_response.getBoolean("success");
                        int wins = json_response.getInt("wins");

                        if(wins == 0) {
                            tvWins.setText("Don't worry, you'll get your first win next game.");
                        } else if(wins == 1){
                            tvWins.setText("Awesome! Congrats on your first win.");
                        } else {
                            tvWins.setText("You now have " + wins + " total wins!");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            VictoryRequest victory_request = new VictoryRequest(username, "1", response_listener);
            RequestQueue queue = Volley.newRequestQueue(VictoryScreen.this);
            queue.add(victory_request);
        } else{
            tvResult.setText("Better luck next time!");
            tvWinner.setText(splited[0] + " has won the game.");
        }
    }
}