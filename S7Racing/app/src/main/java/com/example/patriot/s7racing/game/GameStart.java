package com.example.patriot.s7racing.game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;
import android.view.Window;
import android.view.WindowManager;

import com.example.patriot.s7racing.client.Client;
import com.example.patriot.s7racing.lobby.DefaultLobbyUI;
import com.example.patriot.s7racing.lobby.Store;

import java.io.Serializable;


public class GameStart extends AppCompatActivity {
    private GamePanel GP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Client client = Store.driver;

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        GP = new GamePanel(this, client);
        setContentView(GP);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        GP = null;
    }
}
