package com.example.mina.gamebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class SolitairGameActivity extends AppCompatActivity {

    int remainingCards , cardWidth , cardHeight;
    ImageButton baseImageButton;
    ArrayList<Card> cards;
    Stack<Card> drawCardStack , drawnCardStack;
    ArrayList<Stack<Card>> playCardStack , finishedCard;
    Pair<Float,Float> drawCardPosition , drawnCardPosition;
    ArrayList<Pair<Float , Float>> playCardPostion , finishedCardPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitair_game);

        RelativeLayout drawRelativeView = (RelativeLayout) findViewById(R.id.drawRelativeLayout);


        baseImageButton = (ImageButton) findViewById(R.id.baseImageButton);
        cardWidth = baseImageButton.getLayoutParams().width;
        cardHeight = baseImageButton.getLayoutParams().height;
        cards = new ArrayList<Card>();
        drawCardStack = new Stack<Card>();
        drawnCardStack = new Stack<Card>();
        playCardStack = new ArrayList<Stack<Card>>();
        finishedCard = new ArrayList<Stack<Card>>();

        initializeCards();
        initializeDrawStack();
        initializeDrawnStackPosition();
        initializePlayCardPosition();
        initializeFinishedCardPosition();
    }


    private void initializeCards() {
        for(int i = 0 ; i < 52 ; i++)
        {
            Card card = new Card(getBaseContext() , R.drawable.spades5 , baseImageButton.getLayoutParams());
            cards.add(card);
        }
    }


    /*---------------------------------------------- Drawn Stack ------------------------------------------*/

    private void initializeDrawnStackPosition() {
        RelativeLayout drawRelativeLayout = (RelativeLayout) findViewById(R.id.drawRelativeLayout);
        drawnCardPosition = new Pair<Float, Float>(drawRelativeLayout.getX() , drawRelativeLayout.getY());
        Log.i("min" ,  "Gzra " + Float.toString(drawCardPosition.first) + " " + Float.toString(drawnCardPosition.second));

    }

    private Card cardForDrawn(Card card)
    {
        /*if(!drawnCardStack.empty()) {
            Card temp = drawnCardStack.peek();
            drawnCardStack.pop();
            temp.setOnClickListener(null);
            temp.setOnDragListener(null);
            drawnCardStack.push(temp);
        }*/

        //card.setOnClickListener(cardOnClickListner);


        //card.setOnDragListener();
        Log.i("min" ,  "Gzra2 " + Float.toString(drawCardPosition.first) + " " + Float.toString(drawnCardPosition.second));
        card.setPosition(drawnCardPosition);
        card.setImageResource(card.getPictureId());

        return card;
    }

    /*----------------------------------------------------------------------------------------------------*/



    /*---------------------------------------------- Draw Stack ------------------------------------------*/

    private void initializeDrawStack() {
        RelativeLayout drawRelativeLayout = (RelativeLayout) findViewById(R.id.drawRelativeLayout);

        drawCardPosition = new Pair<Float , Float>(drawRelativeLayout.getX() +  cardWidth , drawRelativeLayout.getY());

        //disable below cards' clickListener

        for(int i = 0 ; i < 4 ; i++)
        {
            Card card = cards.get(i);
            card = cardForDraw(card);
            drawCardStack.push(card);
            drawRelativeLayout.addView(drawCardStack.peek());
        }

        //test
        Card card = new Card(getBaseContext() , R.drawable.clubs1 , baseImageButton.getLayoutParams());
        card.setPosition(drawRelativeLayout.getX() , drawRelativeLayout.getY());
        drawnCardStack.push(card);
        drawRelativeLayout.addView(card);
        //test
    }

    private Card cardForDraw(Card card)
    {
        /*if(!drawCardStack.empty()) {
            Card temp = drawCardStack.peek();
            drawCardStack.pop();
            temp.setOnClickListener(null);
            temp.setOnDragListener(null);
            drawCardStack.push(temp);
        }*/

        card.setOnClickListener(drawCardOnClickListener);
        card.setOnDragListener(null);
        card.setPosition(drawCardPosition);
        card.setImageResource(R.drawable.cardcover);

        return  card;
    }

    private View.OnClickListener drawCardOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(drawCardStack.empty())
            {
                refillDrawCardStack();
            }
            else {
                Log.i("min" , "ana hna");
                Card card = drawCardStack.peek();
                drawCardStack.pop();
                card = cardForDrawn(card);
                drawnCardStack.push(card);
            }
        }
    };

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


    /*----------------------------------------------------------------------------------------------------*/




    /*--------------------------------------------  Play Card ----------------------------------------------*/

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
            playCardStack.get(i).peek().setX(i * 100 + 50 * i);
            playCardStack.get(i).peek().setImageResource(R.drawable.ic_launcher_background);
            playRelativeView.addView(playCardStack.get(i).peek());
        }
    }

    private void initializePlayCardPosition()
    {
        RelativeLayout playRelativeLayout = (RelativeLayout) findViewById(R.id.playCardRelativeView);
        Float x = playRelativeLayout.getX() , y = playRelativeLayout.getY();
        playCardPostion = new ArrayList<Pair<Float, Float>>();

        for(int i = 0 ; i < 7 ; i++)
        {
            Log.i("min" ,Integer.toString(i));
            playCardPostion.add(new Pair<Float, Float>(x , y));
            Card card = new Card(getBaseContext() , R.drawable.clubs1 , baseImageButton.getLayoutParams());
            card.setPosition(playCardPostion.get(i));
            playRelativeLayout.addView(card);
            x += cardWidth;
        }
    }

    /*----------------------------------------------------------------------------------------------------*/



    /*--------------------------------------------  Finished Card ----------------------------------------------*/

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

    private void initializeFinishedCardPosition()
    {
        RelativeLayout finisehedCardRelativeView = (RelativeLayout) findViewById(R.id.finishedCardsRelativeLayout);
        Float x = finisehedCardRelativeView.getX() , y = finisehedCardRelativeView.getY();
        finishedCardPosition = new ArrayList<Pair<Float, Float>>();

        for(int i = 0 ; i < 4 ; i++)
        {
            Log.i("min" ,Integer.toString(i));
            finishedCardPosition.add(new Pair<Float, Float>(x , y));
            Card card = new Card(getBaseContext() , R.drawable.clubs1 , baseImageButton.getLayoutParams());
            card.setPosition(playCardPostion.get(i));
            finisehedCardRelativeView.addView(card);
            x += cardWidth;
        }
    }


    /*----------------------------------------------------------------------------------------------------*/




    /*--------------------------------------------  General ----------------------------------------------*/


    private Boolean isWin()
    {
        return remainingCards == 0;
    }


    private View.OnClickListener cardOnClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };

    /*----------------------------------------------------------------------------------------------------*/

}
