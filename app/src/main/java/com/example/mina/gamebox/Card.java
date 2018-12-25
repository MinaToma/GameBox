package com.example.mina.gamebox;

import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.Pair;
import android.view.ViewGroup;

public class Card extends android.support.v7.widget.AppCompatImageButton {

    private int pictureID , coverCardID , playIdx , finishedIdx , number , inPlayIdx;
    private Pair<Float , Float> lastPosition;
    private String name;
    private Boolean isPlay , isFinished , isDeck , isHand , isFaceUp , isRed;
    ConstraintLayout constraintLayout;
    private String newState;

    public Card(Context context) {
        super(context);
    }

    public Card(Context context , int pictureID, int coverCardID , ViewGroup.LayoutParams layoutParams ,
                OnTouchListener onTouchListener , final ConstraintLayout constraintLayout ) {

        super(context);

        this.pictureID = pictureID;
        this.coverCardID = coverCardID;
        this.constraintLayout = constraintLayout;
        setImageResource(pictureID);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setPadding(7 , 7 ,7, 7);
        }
        name = context.getResources().getResourceName(pictureID);

        if(Character.isDigit(name.codePointAt(name.length()-2))){
            number = name.charAt(name.length()-2) - '0';
            number *= 10;
            number += name.charAt(name.length()-1) - '0';
        }
        else{
            number = name.charAt(name.length()-1) - '0';
        }

        if(name.contains("clubs")) {
            name = "clubs";
            isRed = false;
        }
        else if(name.contains("diamonds")) {
            name = "diamonds";
            isRed = true;
        }
        else if(name.contains("hearts")) {
            name = "hearts";
            isRed = true;
        }
        else if(name.contains("spades")) {
            name = "spades";
            isRed = false;
        }

        playIdx = finishedIdx = 0;
        isDeck = isFaceUp = true;
        isPlay = isFinished = isHand = false;
        newState = "Deck";
        lastPosition = new Pair<Float, Float>(0f, 0f);

        ViewGroup.LayoutParams newLayout = new ViewGroup.LayoutParams(layoutParams);
        setLayoutParams(newLayout);
        setOnTouchListener(onTouchListener);
    }

    public Boolean getFaceUp() {
        return isFaceUp;
    }
    public void reSize(ViewGroup.LayoutParams layoutParams)
    {
        ViewGroup.LayoutParams newLayout = new ViewGroup.LayoutParams(layoutParams);
        setLayoutParams(newLayout);
    }

    public void setFaceUp(Boolean faceUp) {
        this.isFaceUp = faceUp;
    }

    public int getPictureID() {
        return pictureID;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setLastPosition(Pair<Float, Float> lastPosition) {
        this.lastPosition = lastPosition;
    }

    public int getPlayIdx() {
        return playIdx;
    }

    public void setPlayIdx(int playIdx) {
        this.playIdx = playIdx;
    }

    public void setFinishedIdx(int finishedIdx) {
        this.finishedIdx = finishedIdx;
    }

    public void setInPlayIdx(int inPlayIdx) {
        this.inPlayIdx = inPlayIdx;
    }

    public int getFinishedIdx() {

        return finishedIdx;
    }

    public Boolean getRed() {
        return isRed;
    }

    public int getInPlayIdx() {
        return inPlayIdx;
    }

    public Boolean getPlay() {
        return isPlay;
    }

    public Boolean getDeck() {
        return isDeck;
    }

    public Boolean getHand() {
        return isHand;
    }

    public void hideCard() {
        setFaceUp(false);
        setImageResource(coverCardID);
    }

    public void showCard(){
        setFaceUp(true);
        setImageResource(pictureID);
    }

    public void toHand(Pair<Float , Float> newPos){
        isDeck = isPlay = isFinished = false;
        isHand = isFaceUp = true;
        setImageResource(pictureID);
        setPosition(newPos);
        lastPosition = getPosition();
        newState = "Hand";
        reAddToConstraint();
    }

    public void toUnDeck()
    {
        isDeck=false;
    }

    public void toDeck(Pair<Float , Float> newPos){
        isHand = isFaceUp = isPlay = isFinished = false;
        newState = "Deck";
        isDeck = true;
        setImageResource(coverCardID);
        setPosition(newPos);
        lastPosition = getPosition();
        reAddToConstraint();
    }

    public void toPlayArea(int playIdx , int inPlayIdx , Pair<Float , Float> newPos){
        isDeck = isHand = isFinished = false;
        isPlay = true;
        newState = "Play";
        this.playIdx = playIdx;
        this.inPlayIdx = inPlayIdx;
        setPosition(newPos);
        lastPosition = getPosition();
        reAddToConstraint();
    }

    public void reAddToConstraint(){
        constraintLayout.removeView(this);
        constraintLayout.addView(this);
    }

    public void toSuits(int finishedIdx , Pair<Float , Float> newPos){
        isDeck = isPlay = isHand = false;
        isFinished = isFaceUp = true;
        newState = "Suit";
        this.finishedIdx = finishedIdx;
        setPosition(newPos);
        lastPosition = getPosition();
        reAddToConstraint();
    }

    public Pair<Float, Float> getLastPosition() {
        return lastPosition;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setPosition(Float x , Float y)
    {
        setX(x);
        setY(y);
    }

    public Pair<Float , Float> getPosition()
    {
        return  new     Pair<Float,Float>(getX() , getY());
    }

    public void setPosition(Pair<Float, Float> position)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(0f);
        }
        setX(position.first);
        setY(position.second);
    }
}
