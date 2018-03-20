package com.example.patriot.s7racing.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Tuple {
    int x, y;
    int r = 10;
    int color;
    private int relative;
    //private Paint paint = new Paint();

    public Tuple(int x, int y){
        this.x = x;
        this.y = y;
        //this.color = Color.BLACK;

        //paint.setColor(color);
        //paint.setStyle(Paint.Style.FILL);
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setAbsoluteRelative(int relative){
        this.relative = relative;
    }

    public void draw(Canvas canvas, Bitmap b){
        //canvas.drawCircle(x, relative, r, paint);
        try{
            canvas.drawBitmap(b, x-b.getWidth(), relative-b.getHeight(), null);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
