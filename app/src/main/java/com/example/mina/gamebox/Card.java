package com.example.mina.gamebox;

import android.content.Context;

public class Card extends android.support.v7.widget.AppCompatImageButton {

    private String type , number , name;

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public Card(Context context , String type , String number) {
        super(context);
        name = type + number;
        int pictureId = getResources().getIdentifier(name , "drawable" , context.getPackageName());
        setImageResource(pictureId);
    }
}
