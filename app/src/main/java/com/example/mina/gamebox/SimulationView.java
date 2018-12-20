package com.example.mina.gamebox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.display.DisplayManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Stack;

public class SimulationView extends View {

    ArrayList<Node> BBST;
    Paint mPaint;
    float width , height , startHorizontal , startVertical , bottomVertical , radius , prevX , prevY;
    Stack<Integer> st;
    boolean isStack , isBBST;
    
    public SimulationView(Context context) {
        super(context);
    }

    public SimulationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void initialize(Display display){
        BBST = new ArrayList<>();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setColor(Color.RED);

        this.setOnTouchListener(onTouchListener);
        prevX = prevY = 0f;
        Log.i("MIN" , Float.toString(width));
        width = display.getWidth();
        height = display.getHeight();
        Log.i("MIN" , Float.toString(width));
        startHorizontal = width / 2;
        startVertical = height * 0.05f;
        bottomVertical = height - startHorizontal;
        radius = width * 0.05f;
    }

    public void simulateBBST(ArrayList<Node> BBST)
    {
        this.BBST = BBST;
        invalidate();
    }

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_MOVE:
                    float currX = event.getRawX();
                    float currY = event.getRawY();

                    if(Math.abs(currX - prevX) < width * 0.1f && Math.abs(currY - prevY) < width * 0.1f){
                        startHorizontal += currX - prevX;
                        startVertical += currY - prevY;
                    }

                    prevX = currX;
                    prevY = currY;

                    postInvalidate();

                    break;
            }

            return true;
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(isBBST){
            for (Node node : BBST) {
                canvas.drawCircle(node.x + startHorizontal , node.y + startVertical , radius , mPaint);
            }
        }

        Log.i("MIN" , "hori " + Float.toString(startHorizontal));
        for(int i = 0 ; i < 10 ; i++)
        {
            canvas.drawCircle(startHorizontal + ( (i % 2 == 1) ?  i * 50 : i * 50 * -1 ) , startVertical  + i * 50  , radius, mPaint);
        }
    }
}