package com.example.patriot.s7racing.game;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
    private Bitmap image;
    private int x, y, dy, dx;
    private int speed;

    public Background(Bitmap b){
        image = b;
        dy = GamePanel.BACKGROUNDMOVESPEED;
    }

    public void update(){
        y -= (dy*(speed));
        if(y > GamePanel.HEIGHT){
            y = 0;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
        if(y > 0){
            canvas.drawBitmap(image, x, y - GamePanel.HEIGHT, null);
        }
    }

    public void adjustSpped(int newSpeed){
        speed = newSpeed;
    }
}
