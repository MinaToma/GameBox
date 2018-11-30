package com.example.mina.gamebox;

import android.content.Context;
import android.util.Pair;
import android.util.TypedValue;
import android.view.ViewGroup;

public class Card extends android.support.v7.widget.AppCompatImageButton {

    private int pictureId , playPosition , finishedPosition;
    private Boolean isPlay , isFinished;

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

    public int getPictureId() {
        return pictureId;
    }

    public int getPlayPosition() {
        return playPosition;
    }

    public int getFinishedPosition() {
        return finishedPosition;
    }

    public Boolean getPlay() {
        return isPlay;
    }

    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }

    public void setFinishedPosition(int finishedPosition) {
        this.finishedPosition = finishedPosition;
    }

    public void setPlay(Boolean play) {
        isPlay = play;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setPosition(Pair<Float, Float> position)
    {
        setX(position.first);
        setY(position.second);
    }
}
