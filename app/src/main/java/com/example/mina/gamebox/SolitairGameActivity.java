package com.example.mina.gamebox;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class SolitairGameActivity extends AppCompatActivity {

    int remainingCards;
    Stack<Card> drawCardStack , drawnCardStack;
    ArrayList<Stack<Card>> playCardStack , finishedCard;
    Pair<Float,Float> drawCardPosition , drawnCardPosition;
    ArrayList<Pair<Float , Float>> playCardPostion , finishedCardPosition;
    Float cardWidth , cardHeight , myMargin;

    //ANIMATION

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitair_game);

        RelativeLayout drawRelativeView = (RelativeLayout) findViewById(R.id.drawRelativeLayout);

        remainingCards = 52;
        cardHeight = 100f;
        cardWidth = 55.0f;
        myMargin = 5.0f;

        playCardPostion = new ArrayList<Pair<Float, Float>>();
        finishedCardPosition = new ArrayList<Pair<Float, Float>>();
        drawnCardPosition = new Pair<Float, Float >(drawRelativeView.getX() , drawRelativeView.getY());
        drawCardPosition = new Pair<Float, Float>(drawnCardPosition.first + cardWidth + myMargin, drawnCardPosition.second);
        drawCardStack = new Stack<Card>();
        drawnCardStack = new Stack<Card>();
        playCardStack = new ArrayList<Stack<Card>>();
        finishedCard = new ArrayList<Stack<Card>>();

        initializePlayCardStack();
        initializeDrawStack();
    }

    private void initializePlayCardPosition()
    {

    }

    private void initializeFinishedCardPosition()
    {

    }

    private View.OnClickListener drawCardOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(drawCardStack.empty())
            {
                refillDrawCardStack();
            }
            else {
                Card card = drawCardStack.peek();
                drawnCardStack.pop();
                card = cardForDrawn(card);
                drawnCardStack.push(card);
            }
        }
    };

    private Card cardForDrawn(Card card)
    {
        if(!drawnCardStack.empty()) {
            Card temp = drawnCardStack.peek();
            drawnCardStack.pop();
            temp.setOnClickListener(null);
            temp.setOnDragListener(null);
            drawnCardStack.push(temp);
        }

        card.setOnClickListener(cardOnClickListner);
        //card.setOnDragListener();
        card.setPosition(drawnCardPosition);
        card.setImageResource(card.getPictureId());

        return card;
    }

    private Card cardForDraw(Card card)
    {
        card.setOnClickListener(drawCardOnClickListener);
        card.setOnDragListener(null);
        card.setPosition(drawCardPosition);
        //card.setImageResource(R.drawable.hiddenCard);

        return  card;
    }

    private Card cardForFinished(int idx , Card card)
    {
        if(!finishedCard.get(idx).empty())
        {
            Card temp = finishedCard.get(idx).peek();
            finishedCard.get(idx).pop();
            temp .setOnClickListener(null);
            temp.setOnDragListener(null);
            finishedCard.get(idx).push(temp);
        }

        remainingCards--;
        card.setPlay(false);
        card.setFinishedPosition(idx);
        card.setFinished(true);
        card.setPosition(new Pair<Float, Float>(finishedCardPosition.get(idx).first , finishedCardPosition.get(idx).second));

        if(isWin())
        {
            Toast.makeText(getBaseContext() , (String)"Al3b yla",
                    Toast.LENGTH_LONG).show();
        }

        return card;
    }

    private Boolean isWin()
    {
        return remainingCards == 0;
    }

    private Card cardForPlay(int idx , Card card)
    {
        card.setPlay(true);
        card.setPlayPosition(idx);
        card.setFinished(false);
        card.setPosition(new Pair<Float, Float>(playCardPostion.get(idx).first , playCardPostion.get(idx).second));

        return  card;
    }

    private void initializePlayCardStack()
    {
        RelativeLayout playRelativeView = (RelativeLayout) findViewById(R.id.playCardRelativeView);

        for(int i = 0 ; i < 7 ; i++) {
            playCardStack.add(new Stack<Card>());
            playCardStack.get(i).push(new Card(getBaseContext(), "heart", 55, 100));
            playCardStack.get(i).peek().setX(i * 100 + 50 * i);
            playCardStack.get(i).peek().setImageResource(R.drawable.ic_launcher_background);
            playRelativeView.addView(playCardStack.get(i).peek());
        }
    }

    private void initializeDrawStack()
    {

    }

    private void refillDrawCardStack()
    {
        while(!drawnCardStack.empty())
        {
            Card card = drawnCardStack.peek();
            drawnCardStack.pop();
            card = cardForDraw(card);
            drawCardStack.push(card);
        }
    }

    private View.OnClickListener cardOnClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };
}
