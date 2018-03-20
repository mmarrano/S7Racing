package com.example.patriot.s7racing.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class Player extends GameObject {
    private int score;
    private boolean left;
    private boolean right;
    private boolean playing;
    private long startTime;
    private long startTimeS;
    private int r;
    private Paint paint = new Paint();

    private int speed;
    private final int MAXPLAYERMOVESPEED = 8;

    private int absoluteY;

    public Player(int radius, int speed){
        x = GamePanel.WIDTH/2;
        //y = 200;
        dx = 0;
        score = 0;
        r = radius;
        height = radius;
        width = radius;

        this.speed = speed;
        absoluteY = GamePanel.HEIGHT - 150;

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setLeft(boolean b){
        this.left = b;
    }

    public void setRight(boolean b){
        this.right = b;
    }

    public void update(){
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed > 100){
            score += speed;
            startTime = System.nanoTime();
        }

        //SPEED STUFF
        long speedElapsed = (System.nanoTime()-startTimeS)/1000000;
        if(speedElapsed > 500){
            speed += 1;
            if(speed > MAXPLAYERMOVESPEED){
                speed = MAXPLAYERMOVESPEED;
            }
            startTimeS = System.nanoTime();
        }

        //LEFT RIGHT STUFF
        if(left){
            dx -= 1;
        } else if(right){
            dx += 1;
        } else
            dx = 0;

        if(dx > 8){
            dx = 8;
        }
        if(dx < -8){
            dx = -8;
        }

        //BOUNDRIES
        if(x < (0 + height)){
            x = 0 + height;
            dx = 0;
        } else if(x > (GamePanel.WIDTH - height)){
            x = GamePanel.WIDTH - height;
            dx = 0;
        }

        x += dx*2;
        absoluteY += speed;
    }

    public void draw(Canvas canvas, Bitmap b){
        //just draw a paint blip
        canvas.drawBitmap(b, x-b.getWidth(), y-(b.getHeight()/2), null);
        //canvas.drawCircle(x, y, r, paint);
    }

    public int getScore() {
        return score;
    }

    public void adjustScore(int adjustingScore) {
        this.score += adjustingScore;
    }

    public boolean getPlaying(){
        return playing;
    }

    public void setPlaying(Boolean b){
        this.playing = b;
    }

    public void resetDX() {
        this.dx = 0;
    }

    public void resetScore() {
        score = 0;
    }

    public int getSpeed(){
        return this.speed;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public void adjustSpeed(int adjustment){
        this.speed = this.speed + adjustment;
    }

    public int getAbsoluteY(){
        return this.absoluteY;
    }
}
