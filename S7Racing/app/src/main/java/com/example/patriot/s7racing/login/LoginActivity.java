package com.example.patriot.s7racing.login;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.example.patriot.s7racing.R;
import com.example.patriot.s7racing.lobby.DefaultLobbyUI;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        final EditText et_username = (EditText) findViewById(R.id.etUsername);
        final EditText et_password = (EditText) findViewById(R.id.etPassword);

        final Button b_login = (Button) findViewById(R.id.bLogin);
        final Button b_register = (Button) findViewById(R.id.bRegister);

        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(register_intent);
            }
        });

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = et_username.getText().toString();
                final String password = et_password.getText().toString();

                Response.Listener<String> response_listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json_response = new JSONObject(response);
                            boolean success = json_response.getBoolean("success");

                            if (success) {
                                String username = json_response.getString("username");

                                Intent intent = new Intent(LoginActivity.this, DefaultLobbyUI.class);
                                intent.putExtra("username", username);
                                LoginActivity.this.startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this.getApplication(), "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest login_request = new LoginRequest(username, password, response_listener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(login_request);
            }
        });
    }
}

