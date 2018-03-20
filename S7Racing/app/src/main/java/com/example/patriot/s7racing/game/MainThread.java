package com.example.patriot.s7racing.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.LruCache;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private int MAXFPS = 30;
    private double averageFPS;
    private SurfaceHolder mSurfaceHolder;
    private GamePanel mGamePanel;
    private boolean running;
    private static Canvas mCanvas;

    private LruCache<String, Bitmap> mMemoryCache;

    public MainThread(SurfaceHolder sh, GamePanel gp){
        super();
        this.mSurfaceHolder = sh;
        this.mGamePanel = gp;
    }

    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/MAXFPS;

        while(running){
            startTime = System.nanoTime();
            mCanvas = null;

            try{
                mCanvas = this.mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder){
                    this.mGamePanel.update();
                    this.mGamePanel.draw(mCanvas);
                }
            } catch(Exception e){
                e.printStackTrace();
            } finally {
                if(mCanvas != null){
                    try{
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime()-startTime)/1000000;
            waitTime = targetTime - timeMillis;

            if(waitTime > 0) {
                try {
                    this.sleep(waitTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            totalTime += System.nanoTime()-startTime;
            frameCount++;

            if(frameCount == MAXFPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                totalTime = 0;
                frameCount = 0;

                //System.out.println(averageFPS);
            }
        }
    }

    public void setRunning(boolean tf){
        this.running = tf;
    }

    public int getFps(){
        return (int)averageFPS;
    }
}
