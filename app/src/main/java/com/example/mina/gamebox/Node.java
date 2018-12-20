package com.example.mina.gamebox;

import android.util.Pair;

public class Node {
    float x ,  y , radius;
    Pair<Float , Float> position;

    public Node(float x , float y , float radius)
    {
        this.x = x;
        this.y = y;
        position = new Pair<>( x , y);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {

        return radius;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(Pair<Float, Float> position) {
        this.position = position;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Pair<Float, Float> getPosition() {
        return position;
    }
}
