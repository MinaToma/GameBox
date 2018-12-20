package com.example.mina.gamebox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;

public class SimulationView extends View {

    ArrayList<Node> nodes;
    Paint mPaint;
    float width , height , startHorizontal , startVertical;

    public SimulationView(Context context) {
        super(context);

        nodes = new ArrayList<>();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setColor(Color.RED);

        width = this.getLayoutParams().width;
        height = this.getLayoutParams().height;
        startHorizontal = width / 2;
        startVertical = height * 0.5f;
    }

    public SimulationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        nodes = new ArrayList<>();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setColor(Color.RED);

        width = this.getLayoutParams().width;
        height = this.getLayoutParams().height;
        startHorizontal = width / 2;
        startVertical = height * 0.5f;
    }

    public void simulate(ArrayList<Node> nodes)
    {

        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(100 , 100 , 100 , mPaint);
        Log.i("tag", "ho");
        for (Node node : nodes) {
            canvas.drawCircle(node.x  , node.y , node.radius , mPaint);
        }

        for(int i = 0 ; i < 10 ; i++)
        {
            canvas.drawCircle(i * 100 , i * 200 , 100 , mPaint);
        }


        canvas.save();
        canvas.scale(100,100 , 0 , 0 );
        canvas.restore();
    }
}