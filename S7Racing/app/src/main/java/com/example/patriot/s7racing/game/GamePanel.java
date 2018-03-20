package com.example.patriot.s7racing.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.LruCache;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.patriot.s7racing.R;
import com.example.patriot.s7racing.client.Client;
import com.example.patriot.s7racing.lobby.VictoryScreen;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener{
    private Background background;
    private MainThread thread;
    private Player player;
    private ArrayList<Tree> trees;
    private ArrayList<Tuple> otherPlayers;
    private Random r = new Random();

    private SensorManager mSensorManager;
    private Sensor mSensor;
    public float z;

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    private static final float TOLERANCE = .5f;
    public static final int BACKGROUNDMOVESPEED = -5;
    public static final int FINISHLINE = 15000;

    private long treeStartTime;
    private int treeSize;
    private boolean newGameCreated;
    private long startReset;
    private boolean reset;
    private boolean disappear;
    private boolean started;
    private int best;
    private boolean playerPastFinishLine;
    private int christmasCounter = 0;
    private boolean sentPound = false;

    Client client;
    private int setNumPlayersAlready = 0;

    private LruCache<String, Bitmap> mMemoryCache;
    private Paint paintdir = new Paint();
    private Paint paint = new Paint();
    private Paint paint1 = new Paint();
    private Paint paintFinishLine = new Paint();

    Timer timer = new Timer();
    Timer timer2 = new Timer();
    private int interval = 3;
    private int stall = 1;

    private String[] splited;
    private ArrayList<String> recievingMessage;
    private Context aboveContext;
    public GamePanel(Context context, Client client) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }

        //Trying new stuff - saving bitmaps to clear up native memory
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap){
                return bitmap.getByteCount()/1024;
            }
        };

        aboveContext = context;
        this.client = client;
        Log.d("LOG", "Game Panel created");

    }

    //------------------------------------ SURFACEVIEW CRAP ----------------------------------------

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        player = new Player(10, 0);
        playerPastFinishLine = false;
        trees = new ArrayList<>();
        otherPlayers = new ArrayList<>();

        treeStartTime = System.nanoTime();
        best = 0;
        disappear = false;

        Tree justForGettingTreeSize = new Tree();
        treeSize = justForGettingTreeSize.getTreeSize();

        thread = new MainThread(getHolder(), this);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //Adding bitmap to memoryjustForGettingTreeSize = null;
        addBitmapToMemoryCache("tree", BitmapFactory.decodeResource(getResources(), R.drawable.simpletree));
        addBitmapToMemoryCache("christmasTree", BitmapFactory.decodeResource(getResources(), R.drawable.cmassimpletree));
        addBitmapToMemoryCache("player", getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.playerpenny), 30, 46));
        addBitmapToMemoryCache("other", getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.otherpenny), 30, 46));
        addBitmapToMemoryCache("background", BitmapFactory.decodeResource(getResources(), R.drawable.snowysnowyground));

        background = new Background(getBitmapFromMemCache("background"));

        //Create paint objects
        paintdir.setColor(Color.BLACK);
        paintdir.setTextSize(20);
        paintdir.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        paint1.setColor(Color.BLACK);
        paint1.setTextSize(40);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        paintFinishLine.setColor(Color.GREEN);
        paintFinishLine.setStrokeWidth(10f);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        int counter = 0;
        while(retry && (counter < 1000)){
            try{
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            counter++;
        }
    }

    //------------------------------------- SENSOR CRAP --------------------------------------------
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            z = event.values[2];

            String gyroDirection;
            if(z < (0 - TOLERANCE)){
                player.setLeft(false);
                player.setRight(true);
                gyroDirection = "RIGHT";
            } else if(z > (0 + TOLERANCE)){
                player.setRight(false);
                player.setLeft(true);
                gyroDirection = "LEFT";
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    //------------------------------------------ UPDATE --------------------------------------------

    public void update() {
        if(player.getPlaying()){
            background.adjustSpped(Math.round((player.getSpeed()*.5f)));
            background.update();
            player.update();

            if(player.getAbsoluteY() > FINISHLINE){
                playerPastFinishLine = true;
                player.setPlaying(false);
            }

            client.sendMsg("$ " + player.getX() + " " + player.getAbsoluteY());
            recievingMessage = client.getUpdatedLocations();

            if(recievingMessage != null) {
                if(setNumPlayersAlready != 1){
                    setNumPLayers(recievingMessage.size());
                }

                for (int i = 0; i < recievingMessage.size(); i++) {
                    String str = recievingMessage.get(i);
                    splited = str.split("\\s+");

                    if(!(splited[0].equals(client.getUsername()))){
                        Tuple addingTuple = new Tuple(Integer.valueOf(splited[1]), Integer.valueOf(splited[2]));
                        otherPlayers.set(i, addingTuple);
                    }
                }
            }

            long treeElapsed = (System.nanoTime() - treeStartTime)/1000000;
            if(treeElapsed > (1000 - (player.getSpeed()*69))){
                int s = (int)((r.nextDouble()*(WIDTH)));

                if(christmasCounter == 7){
                    trees.add(new Tree(getBitmapFromMemCache("christmasTree"), s, -10, true));
                    christmasCounter = 0;
                } else {
                    trees.add(new Tree(getBitmapFromMemCache("tree"), s, -10, true));
                    christmasCounter++;
                }
                treeStartTime = System.nanoTime();
            }

            for(int i = 0; i < trees.size(); i++){
                trees.get(i).adjustPlayerSpeed(Math.round((player.getSpeed()*.5f)));
                trees.get(i).update();

                if((collision(trees.get(i), player) && trees.get(i).isHittable())){
                    player.setSpeed(0);
                    player.adjustScore(-100);
                    trees.get(i).hit();
                    //MAYBE CREATE TEXT THAT SAYS OUCH
                }

                if(trees.get(i).getY() > GamePanel.HEIGHT + treeSize){
                    trees.get(i).clean();
                    trees.remove(i);
                }
            }

        } else {
            if(playerPastFinishLine == false) {
                if (!reset) {
                    newGameCreated = false;
                    startReset = System.nanoTime();
                    reset = true;
                    disappear = true;
                }

                long resetElapsed = (System.nanoTime() - startReset) / 1000000;
                if (resetElapsed > 250 && !newGameCreated) {
                    newGame();
                }
            } else {
                if(!sentPound){
                    client.sendMsg("#");
                    sentPound = true;

                    timer2.scheduleAtFixedRate(new TimerTask() {
                        public void run() {
                            setInterval2();
                        }
                    }, 1000, 1000);
                }
            }
        }

    }

    public boolean collision(GameObject a, GameObject b){
        return (Rect.intersects(a.getRectangle(), b.getRectangle()));
    }

    public void newGame(){
        if(playerPastFinishLine == false) {
            disappear = false;
            player.setY(GamePanel.HEIGHT - 150);
            //player.setY(440);
            player.resetDX();
            newGameCreated = true;

            timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    setInterval();
                }
            }, 1000, 1000);

        } else{
            disappear = false;
            player.resetDX();

            if (player.getScore() > best) {
                best = player.getScore();
            }
            player.resetScore();
            newGameCreated = false;
        }
    }

    private int setInterval() {
        if (interval == 0) {
            timer.cancel();
            player.setPlaying(true);
            started = true;
            reset = false;
        }
        return --interval;
    }

    private int setInterval2() {
        if (stall == 0) {
            timer.cancel();
            Intent i = new Intent(aboveContext, VictoryScreen.class);
            aboveContext.startActivity(i);
            ((Activity)aboveContext).finish();
        }
        return --stall;
    }

    @Override
    public void draw(Canvas c){
        //Atempting to help GC by freeing up the canvas from other paint objects
        if(c != null){
            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);
        if(c != null){
            final int savedState = c.save();

            c.scale(scaleFactorX, scaleFactorY);
            background.draw(c);

            for(Tuple tup: otherPlayers){
                if(!(tup.getY() >= FINISHLINE)){
                    tup.setAbsoluteRelative(player.getAbsoluteY() - tup.getY() + 330);
                    tup.draw(c, getBitmapFromMemCache("other"));
                }
            }

            for(Tree t: trees){
                t.draw(c);
            }

            if(!disappear){
                player.draw(c, getBitmapFromMemCache("player"));
            }

            int finishLine = (player.getAbsoluteY() - FINISHLINE)*8+300;
            c.drawLine(0, finishLine, WIDTH, finishLine, paintFinishLine);

            drawText(c);

            c.restoreToCount(savedState);
        }
    }

    public void drawText(Canvas c){
        c.drawText("SCORE: " + (player.getScore()), 10, HEIGHT-10, paint);
        c.drawText("ABSOLUTE DISTANCE: " + (player.getAbsoluteY()-330), 10, HEIGHT-35, paint);
        c.drawText("BEST: " + best, WIDTH - 215, HEIGHT - 10, paint);

        //c.drawText("DIRECTION: " + gyroDirection, WIDTH/2-50, 30, paintdir);
        c.drawText("FRAMES: " + thread.getFps(), WIDTH/2-50, 30, paintdir);
        c.drawText("SPEED: " + player.getSpeed(), WIDTH/2-50, 60, paintdir);

        if(!player.getPlaying() && newGameCreated && reset){
            if(interval > 0){
                c.drawText("START IN: " + interval, WIDTH/2-75, HEIGHT/2, paint1);
            } else
                c.drawText("GO!!!", WIDTH/2-75, HEIGHT/2, paint1);

            paint1.setTextSize(20);
            c.drawText("MOVE LEFT TO RIGHT USING YOUR PHONE LIKE A STEERING WHEEL", WIDTH/2-300, HEIGHT/2+20, paint1);
        }
    }

    private void setNumPLayers(int x){
        for(int j = 0; j < x; j++){
            otherPlayers.add(new Tuple(0, 0));
        }
        setNumPlayersAlready = 1;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if(getBitmapFromMemCache(key) == null){
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
