package com.example.mina.gamebox;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class flappyBirdGameView extends View {



    // this is our custom view class

    Handler handler ; // Handler is required to schedule  a  runnable after some delay
    Runnable runnable;
    Bitmap background ;
    Bitmap topTube,bottomTube ;

    final  int updateMilles=30;
    Display display ;
    Point point;
    int displayWidth,displayHeight ; //Devices width  and height respectively
    Rect rect ;
    // let's make bitmap array for birds
    Bitmap[] birds;
    // we need an integer variable to keep track of bird image / frame
    int birdFram = 0;
    int velocity=0 , gravity = 3 ; // let's play around with these values
    // we need to keep the track of bird postion
    int birdX , birdY;
    boolean gameState = false;

    int gap = 400 ; // gap between top tube and bottom tube
    int minTubeOffset , maxTubeOffset ;
    int numberOfTube=4;
    int distanceBetweenTubes ;
    int[] tubeX = new int[numberOfTube];
    int[] topTubeY = new int[numberOfTube];
    Random random;
    int tubeVelocity = 8;



    public flappyBirdGameView(Context context) {
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); // this will call onDraw()
            }
        };
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background_day);
        topTube = BitmapFactory.decodeResource(getResources(),R.drawable.top_bib);
        bottomTube = BitmapFactory.decodeResource(getResources(),R.drawable.bottom_bib);
        display= ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        displayWidth = point.x;
        displayHeight = point.y;
        rect = new Rect(0,0,displayWidth,displayHeight);
        birds = new Bitmap[3];
        birds[0]= BitmapFactory.decodeResource(getResources(),R.drawable.redbird1);
        birds[1]= BitmapFactory.decodeResource(getResources(),R.drawable.redbird2);
        birds[2]= BitmapFactory.decodeResource(getResources(),R.drawable.redbird3);
        birdX = displayWidth/2 - birds[0].getWidth()/2; // initially bird will be center
        birdY = displayHeight/2 - birds[0].getHeight()/2;
        distanceBetweenTubes =displayWidth*3/4 ; // Our assumption
        minTubeOffset =gap/2;
        maxTubeOffset =displayHeight -minTubeOffset - gap;
        random = new Random();
        for(int i= 0 ;i<numberOfTube  ;  i++)
        {

            tubeX[i] = displayWidth + i*distanceBetweenTubes;
            topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
        }



    }

    @Override
    protected  void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        // we'll draw our view inside onDraw()
        // draw the background on canvas
        //canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(background,null,rect,null);

        if(birdFram == 0)
        {
            birdFram =1;

        }
        else if(birdFram ==1)
        {
            birdFram =2;
        }
        else
        {
            birdFram =0;
        }
        if(gameState)
        {
            // the bird should be in the screen
            if (birdY < displayHeight - birds[0].getHeight() || velocity < 0) {
                velocity += gravity; // As the bird falls, it gets faster and faster as the velocity value increments  by gravity each time
                birdY += velocity;
            }
            for(int i= 0 ;i<numberOfTube  ;  i++) {

                tubeX[i] -= tubeVelocity;
                if(tubeX[i] < -topTube.getWidth())
                {
                    tubeX[i] += numberOfTube * distanceBetweenTubes;
                    topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
                }

                canvas.drawBitmap(topTube, tubeX[i], topTubeY[i]- topTube.getHeight(), null);
                canvas.drawBitmap(bottomTube, tubeX[i], topTubeY[i] + gap, null);
            }
        }
        // we want to dispaly the bird in the center of the screen
        // Both birds[0] and birds[1] has the same dimension
        canvas.drawBitmap(birds[birdFram], birdX ,birdY,null );
        handler.postDelayed(runnable,updateMilles);


    }

    // Get the touch event

    @Override
    public boolean  onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN) // this is tap is detected on screen
        {
            //Here we want the bird to move upward by some unit;
            velocity-=30; // let's say 30 units on upward direction
            gameState = true ;


        }
        return  true ;// By returning indicates that we've done with touch event and no further action is required by Android
    }
}
