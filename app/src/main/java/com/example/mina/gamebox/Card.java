package com.example.mina.gamebox;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;

public class Card extends android.support.v7.widget.AppCompatImageButton {

    private int pictureId;

    public int getPictureId() {
        return pictureId;
    }

    public Card(Context context , String name , int width , int height) {
        super(context);
        pictureId = getResources().getIdentifier(name , "drawable" , context.getPackageName());
        setImageResource(pictureId);

        setLayoutParams(new ViewGroup.LayoutParams((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                width,
                context.getResources().getDisplayMetrics()
        ) , (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                height ,
                context.getResources().getDisplayMetrics()
        ) ) );
    }

    public void setPosition(int x , int y)
    {
        setX(x);
        setY(y);
    }
}
