package com.example.patriot.s7racing.game;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.Random;

public class Tree extends GameObject{
    private int score;
    private int speed;
    private int playerSpeed;
    private Random rand = new Random();
    private Bitmap treepic;
    private int treeSize = 60;
    private boolean hittable;

    public Tree(){

    }

    public Tree(Bitmap res, int x, int y, boolean hittable){
        super.x = x;
        super.y = y;
        treepic = getResizedBitmap(res, treeSize, treeSize);

        speed = 5;

        height = treeSize;
        width = treeSize;

        this.hittable = hittable;
    }

    public void update(){
        y+= (speed*playerSpeed);
        int xdist = 0;

        if(x >= (GamePanel.WIDTH/2)){
            x+=xdist;
        } else
            x-=xdist;
    }

    public void draw(Canvas c){
        try{
            c.drawBitmap(treepic, x-(treepic.getWidth()/2), y-(treepic.getHeight()/2), null);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth(){
        return width - 10;
    }

    //Taken from a helpful member of the stack
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        //bm.recycle();
        return resizedBitmap;
    }

    public void clean(){
        if (treepic != null) {
            treepic.recycle();
            treepic = null;
        }
    }

    public int getTreeSize(){
        return treeSize;
    }

    public void adjustPlayerSpeed(int ps){
        playerSpeed = ps;
    }

    public void hit(){
        hittable = false;
    }

    public boolean isHittable(){
        return hittable;
    }
}
