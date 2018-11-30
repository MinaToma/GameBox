package com.example.mina.gamebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
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
    ArrayList<String> cardType;

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
        cardType = new ArrayList<String>();

        initializeCardType();
        initializeCards();
        initializeDrawStack();
        initializeDrawnStackPosition();
        initializePlayCardPosition();
        initializeFinishedCardPosition();
    }

    private void initializeCardType() {
        cardType.add(new String("hearts"));
        cardType.add(new String("diamonds"));
        cardType.add(new String("clubs"));
        cardType.add(new String("spades"));
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
    }

    private Card cardForDrawn(Card card)
    {
        card.setOnClickListener(cardOnClickListner);


        //card.setOnDragListener();
        card.setPosition(drawnCardPosition);
        Log.i("min" ,  "inside " + Float.toString(card.getX()) + " " + Float.toString(card.getY()));
        card.setImageResource(card.getPictureId());

        return card;
    }

    /*----------------------------------------------------------------------------------------------------*/



    /*---------------------------------------------- Draw Stack ------------------------------------------*/

    private void initializeDrawStack() {
        RelativeLayout drawRelativeLayout = (RelativeLayout) findViewById(R.id.drawRelativeLayout);

        drawCardPosition = new Pair<Float , Float>(drawRelativeLayout.getX() +  cardWidth , drawRelativeLayout.getY());
        Card emptyCard = new Card(getBaseContext() ,  R.drawable.diamonds2 , baseImageButton.getLayoutParams());
        emptyCard = cardForDraw(emptyCard);
        emptyCard.setImageResource(emptyCard.getPictureId());
        drawRelativeLayout.addView(emptyCard);
        drawCardStack.add(emptyCard);

        for(int i = 0 ; i < 4 ; i++)
        {
            Card card = cards.get(i);
            card = cardForDraw(card);
            drawCardStack.push(card);
            drawRelativeLayout.addView(drawCardStack.peek());
        }


        drawCardPosition = drawCardStack.peek().getPosition();

        //test
        Card card = new Card(getBaseContext() , R.drawable.clubs1 , baseImageButton.getLayoutParams());
        card.setPosition(drawRelativeLayout.getX() , drawRelativeLayout.getY());
        drawnCardStack.push(card);
        drawnCardPosition = drawnCardStack.peek().getPosition();
        drawRelativeLayout.addView(card);
        //test
    }

    private Card cardForDraw(Card card)
    {
        card.setOnClickListener(drawCardOnClickListener);
        card.setOnDragListener(null);
        card.setPosition(drawCardPosition);
        card.setImageResource(R.drawable.cardcover);

        return  card;
    }

    private View.OnClickListener drawCardOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(drawCardStack.size() == 1)
            {
                refillDrawCardStack();
            }
            else {
                RelativeLayout drawRelativeLayout = (RelativeLayout) findViewById(R.id.drawRelativeLayout);

                Log.i("min" , "ana hna");
                Card card = drawCardStack.peek();
                drawRelativeLayout.removeView(card);
                drawCardStack.pop();
                Log.i("min" ,  "before " + Float.toString(card.getX()) + " " + Float.toString(card.getY()));
                card = cardForDrawn(card);
                drawRelativeLayout.addView(card);
                Log.i("min" ,  "after " + Float.toString(card.getX()) + " " + Float.toString(card.getY()));
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

        Log.i("min", "CARASDKASJFKLASHK");
        for(int i = 0 ; i < 7 ; i++)
        {
            playCardPostion.add(new Pair<Float, Float>(x , y));

            String cardName = cardType.get(i % 4);
            cardName += Integer.toString(i%4 + 1);

            int pictureId = getBaseContext().getResources().getIdentifier(cardName , "drawable" , getBaseContext().getPackageName());
            Card card = new Card(getBaseContext() , pictureId , baseImageButton.getLayoutParams());

            card.setPosition(playCardPostion.get(i));
            card.setPlay(true);
            card.setPlayPosition(i);
            card.setOnClickListener(cardOnClickListner);
            playRelativeLayout.addView(card);
            x += cardWidth;
        }
    }

    /*----------------------------------------------------------------------------------------------------*/



    /*--------------------------------------------  Finished Card ----------------------------------------------*/


    private void initializeFinishedCardPosition()
    {
        RelativeLayout finisehedCardRelativeView = (RelativeLayout) findViewById(R.id.finishedCardsRelativeLayout);
        Float x = finisehedCardRelativeView.getX() , y = finisehedCardRelativeView.getY();
        finishedCardPosition = new ArrayList<Pair<Float, Float>>();

        for(int i = 0 ; i < 4 ; i++)
        {
            finishedCard.add(new Stack<Card>());
            Log.i("min" ,Integer.toString(i));
            finishedCardPosition.add(new Pair<Float, Float>(x , y));
            //Card card = new Card(getBaseContext() , R.drawable.clubs1 , baseImageButton.getLayoutParams());
            //card.setPosition(playCardPostion.get(i));
            //finisehedCardRelativeView.addView(card);
            Log.i("min" , "ana " + Float.toString(finishedCardPosition.get(i).first) + " " + Float.toString(finishedCardPosition.get(i).second));
            x += cardWidth;
        }
    }

    private Card cardForFinished(int pos , Card card){
        Log.i("min" , card.getName() + " " + Integer.toString(card.getNumber()) + " " + Integer.toString(pos));
        Log.i("min" , Float.toString(finishedCardPosition.get(pos).first) + Float.toString(finishedCardPosition.get(pos).second));
        card.setPosition(finishedCardPosition.get(pos));
        finishedCard.get(pos).push(card);

        return card;
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
            Card card = ((Card) v);

            RelativeLayout finishedRelativeLayout = (RelativeLayout) findViewById(R.id.finishedCardsRelativeLayout);
            int lastNumber = 0 , pos = -1;
            Log.i("min" , card.getName() + " " + Integer.toString(card.getNumber()));
            switch(card.getName())
            {
                case "hearts":
                    if(finishedCard.get(1).size() != 0){
                        lastNumber = finishedCard.get(1).peek().getNumber();
                    }
                    if(lastNumber == card.getNumber() - 1){
                        pos = 1;
                    }
                    break;
                case"diamonds":
                    if(finishedCard.get(2).size() != 0){
                        lastNumber = finishedCard.get(2).peek().getNumber();
                    }
                    if(lastNumber == card.getNumber() - 1) {
                        pos = 2;
                    }
                    break;
                case "clubs":
                    if(finishedCard.get(3).size() != 0){
                        lastNumber = finishedCard.get(3).peek().getNumber();
                        Log.i("min" , Integer.toString(lastNumber));
                    }

                    Log.i("min" , Integer.toString(lastNumber) + " "  + Integer.toString(card.getNumber()));
                    if(lastNumber == card.getNumber() - 1) {
                    Log.i("min" , Integer.toString(lastNumber));

                        pos = 3;
                    }
                    break;
                case "spades":
                    if(finishedCard.get(0).size() != 0){
                        lastNumber = finishedCard.get(0).peek().getNumber();
                    }
                    if(lastNumber == card.getNumber() - 1) {
                        pos = 0;
                    }
                    break;
            }

            if(pos != -1){
                RelativeLayout parentRelativeLayout = (RelativeLayout) card.getParent();
                parentRelativeLayout.removeView(card);

                if (parentRelativeLayout.getId() == R.id.drawRelativeLayout) {
                    drawnCardStack.pop();
                }
                else {
                    playCardStack.get(card.getPlayPosition()).pop();
                    card.setPlay(false);
                }
                card.setFinished(true);
                card.setFinishedPosition(pos);
                finishedRelativeLayout.addView(card);
                card = cardForFinished(pos , card);
            }
        }
    };

    /*----------------------------------------------------------------------------------------------------*/

}
