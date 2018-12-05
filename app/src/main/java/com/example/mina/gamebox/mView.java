package com.example.mina.gamebox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class mView extends View {

    Bitmap bitmap  , b2;
    float x = 0;
    float y = 0;
    float x2 = 100, y2 = 100;

    public mView(Context context) {
        super(context);

        bitmap = BitmapFactory.decodeResource(context.getResources() , R.drawable.clubs1);
        b2 = BitmapFactory.decodeResource(context.getResources() , R.drawable.clubs1);
    }

    public void setBitmap(Bitmap b1 , Bitmap b2)
    {
        bitmap = b1;
        this.b2 = b2;
    }

    public  void hoba(Context context)
    {
        bitmap = BitmapFactory.decodeResource(context.getResources() , R.drawable.clubs1);
        b2 = BitmapFactory.decodeResource(context.getResources() , R.drawable.clubs1);
    }

    public void move1()
    {


    }

    public void move2()
    {


    }
    /*
        public boolean onTouchEvent(MotionEvent event) {
            // TODO Auto-generated method stub

            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    x = event.getX();
                    y = event.getY();
                    invalidate();
                    break;
            }
            return true;
        }
    */
    public void setMe(float xx , float yy , float x22 , float y22)
    {
        x = xx;
        y = yy;
        x2 = x22;
        y2 =y22;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint p = new Paint(Paint.DITHER_FLAG);
        canvas.drawRect( x , y , x2 , y2 , p);
      //  canvas.drawBitmap(bitmap , x , y, null);
        //canvas.drawBitmap(b2 , x2 , y2 , null);
    }

}
