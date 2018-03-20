package com.example.patriot.s7racing.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patriot.s7racing.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        final EditText et_username = (EditText) findViewById(R.id.etUsername);
        final EditText et_password = (EditText) findViewById(R.id.etPassword);

        final TextView tv_username_error = (TextView) findViewById(R.id.tvUsernameError);
        final TextView tv_password_error = (TextView) findViewById(R.id.tvPasswordError);

        final Button b_register = (Button) findViewById(R.id.bRegister);

        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_username_error.setText("");
                tv_password_error.setText("");

                final String username = et_username.getText().toString();
                final String password = et_password.getText().toString();

                Response.Listener<String> response_listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json_response = new JSONObject(response);

                            boolean success = json_response.getBoolean("success");

                            if (success) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            } else {
                                boolean username_err = json_response.getBoolean("username_err");
                                boolean password_err = json_response.getBoolean("password_err");
                                boolean username_empty_err = json_response.getBoolean("username_empty_err");
                                boolean password_empty_err = json_response.getBoolean("password_empty_err");
                                boolean duplicate_username_err = json_response.getBoolean("duplicate_username_err");

                                Toast.makeText(RegisterActivity.this.getApplication(), "Register Failed", Toast.LENGTH_SHORT).show();

                                if(username_empty_err){
                                    tv_username_error.setText("Username cannot be blank.");
                                } else if (username_err) {
                                    tv_username_error.setText("Enter username less than 15 characters.");
                                }

                                if(password_empty_err){
                                    tv_password_error.setText("Password cannot be empty.");
                                } else if (password_err) {
                                    tv_password_error.setText("Enter password less than 15 characters.");
                                }

                                if (duplicate_username_err) {
                                    tv_username_error.setText("This username has already been taken.");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest register_request = new RegisterRequest(username, password, response_listener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(register_request);
            }
        });
    }
}