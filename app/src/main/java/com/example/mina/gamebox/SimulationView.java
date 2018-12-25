package com.example.mina.gamebox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Stack;

public class SimulationView extends View {

    private ArrayList<Pair<Node, Node>> drawArray;
    private Node toDraw, parent, root;
    private Paint circlePaint, textPaint, linePaint;
    private float width, height, startHorizontal, startVertical, bottomVertical, radius, prevX, prevY, vShift, hShift;
    private Stack<Integer> st;

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

    public void initialize(Display display) {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.RED);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLUE);

        drawArray = new ArrayList<>();
        this.setOnTouchListener(onTouchListener);
        prevX = prevY = 0f;
        width = display.getWidth();
        height = display.getHeight();
        startHorizontal = width / 2;
        startVertical = height * 0.05f;
        bottomVertical = height - startHorizontal;
        radius = width * 0.05f;

        textPaint.setTextSize(radius / 2);

        vShift = radius * 2 + width * 0.01f;
        hShift = radius * 2 + width * 0.01f;
    }

    public void simulateBST(Node root) {
        drawArray.clear();
        this.root = root;
        drawBST(root, null);
        postInvalidate();
    }

    private void drawBST(Node currNode, Node parent) {
        if (currNode == null) return;

        toDraw = currNode;
        this.parent = parent;
        drawArray.add(new Pair<Node, Node>(toDraw, this.parent));

        drawBST(currNode.right, currNode);
        drawBST(currNode.left, currNode);
    }

    public void simulateAVL(Node root) {
        drawArray.clear();
        this.root = root;
        drawAVL(root, null);
        postInvalidate();
    }

    private void drawAVL(Node currNode, Node parent) {
        if (currNode == null) return;

        toDraw = currNode;
        this.parent = parent;
        drawArray.add(new Pair<Node, Node>(toDraw, this.parent));

        drawAVL(currNode.right, currNode);
        drawAVL
                (currNode.left, currNode);
    }

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float currX = event.getRawX();
                    float currY = event.getRawY();

                    if (Math.abs(currX - prevX) < width * 0.1f && Math.abs(currY - prevY) < width * 0.1f) {
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

    public void clear() {
        drawArray.clear();
        postInvalidate();
    }

    public void drawStack(ArrayList<Node> nodes) {
        drawArray.clear();
        Node parent = null;

        for (Node i : nodes) {
            drawArray.add(new Pair<Node, Node>(i, parent));
            parent = i;
        }
        postInvalidate();
    }

    public void drawQueue(ArrayList<Node> nodes) {
        drawArray.clear();
        Node parent = null;

        for (Node i : nodes) {
            drawArray.add(new Pair<Node, Node>(i, parent));
            parent = i;
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawArray != null) {

            for (Pair<Node, Node> p : drawArray) {
                toDraw = p.first;
                parent = p.second;

                if (parent == null) {
                    canvas.drawCircle(startHorizontal + toDraw.HOrder * hShift, startVertical + toDraw.VOrder * vShift, radius, circlePaint);

                    canvas.drawText(Integer.toString(toDraw.value), startHorizontal + toDraw.HOrder * hShift,
                            startVertical + toDraw.VOrder * vShift, textPaint);

                    continue;
                }

                canvas.drawLine(startHorizontal + parent.HOrder * hShift, startVertical + parent.VOrder * vShift,
                        startHorizontal + toDraw.HOrder * hShift, startVertical + toDraw.VOrder * vShift, linePaint);

                canvas.drawCircle(startHorizontal + parent.HOrder * hShift, startVertical + parent.VOrder * vShift, radius, circlePaint);

                canvas.drawCircle(startHorizontal + toDraw.HOrder * hShift, startVertical + toDraw.VOrder * vShift, radius, circlePaint);

                canvas.drawText(Integer.toString(parent.value), startHorizontal + parent.HOrder * hShift,
                        startVertical + parent.VOrder * vShift, textPaint);

                canvas.drawText(Integer.toString(toDraw.value), startHorizontal + toDraw.HOrder * hShift,
                        startVertical + toDraw.VOrder * vShift, textPaint);
            }
        }
    }
}
