package com.example.mina.gamebox;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class SolitaireGameActivity extends AppCompatActivity {

    int remainingCards, cardWidth, cardHeight;
    ImageButton baseImageButton;
    ArrayList<Card> cards;
    Stack<Card> drawCardStack, drawnCardStack;
    ArrayList<Stack<Card>> finishedCard;
    ArrayList<ArrayList<Card>> playCardArrayList;
    ArrayList<RelativeLayout> playRelativeArray;
    Pair<Float, Float> drawCardPosition, drawnCardPosition;
    ArrayList<Pair<Float, Float>> playCardPostion, finishedCardPosition;
    ArrayList<String> cardType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitaire_game);

        initializeAll();
    }

    private void initializeAll() {
        RelativeLayout drawRelativeView = (RelativeLayout) findViewById(R.id.drawRelativeLayout);

        baseImageButton = (ImageButton) findViewById(R.id.baseImageButton);
        cardWidth = baseImageButton.getLayoutParams().width;
        cardHeight = baseImageButton.getLayoutParams().height;
        cards = new ArrayList<Card>();
        drawCardStack = new Stack<Card>();
        playRelativeArray  = new ArrayList<RelativeLayout>();
        drawnCardStack = new Stack<Card>();
        playCardArrayList = new ArrayList<ArrayList<Card>>();
        finishedCard = new ArrayList<Stack<Card>>();
        cardType = new ArrayList<String>();

        initializeCardType();
        initializeCards();
        initializeDrawStack();
        initializeDrawnStackPosition();
        initializePlayCardPosition();
        initializeFinishedCardPosition();
        testing(6);
        testing(5);
        testing(4);
        testing(3);
        testing(2);
        testing(1);

    }

    private void testing(int x) {

        RelativeLayout playRelativeLayout = (RelativeLayout) findViewById(R.id.playCardRelativeView);

        for(int j = 0 ; j <7 ; j++){
            String cardName = cardType.get(j%4);
            cardName += Integer.toString(x);
            int pictureId = getApplicationContext().getResources().getIdentifier(cardName, "drawable", getApplicationContext().getPackageName());
            Card card = new Card(getApplicationContext(), pictureId , baseImageButton.getLayoutParams());

            card.setPosition(playCardPostion.get(j).first , playCardPostion.get(j).second + ((6 - x) * 70) );
            card.setPlay(true);
            card.setPlayPosition(j);
            card.setOnTouchListener(onCardTouch);
            card.setInPlayPosittion(playCardArrayList.get(j).size());
            card.setOnClickListener(cardOnClickListner);
            playCardArrayList.get(j).add(card);
            playRelativeLayout.addView(card);
        }
    }

    private void initializeCardType() {
        cardType.add(new String("hearts"));
        cardType.add(new String("diamonds"));
        cardType.add(new String("clubs"));
        cardType.add(new String("spades"));
    }

    private void initializeCards() {
        for (int i = 0; i < 52; i++) {
            Card card = new Card(getApplicationContext(), R.drawable.spades5, baseImageButton.getLayoutParams());
            cards.add(card);
        }
    }


    /*---------------------------------------------- Drawn Stack ------------------------------------------*/

    private void initializeDrawnStackPosition() {
        RelativeLayout drawRelativeLayout = (RelativeLayout) findViewById(R.id.drawRelativeLayout);
        drawnCardPosition = new Pair<Float, Float>(drawRelativeLayout.getX(), drawRelativeLayout.getY());
    }

    private Card cardForDrawn(Card card) {
        card.setOnClickListener(cardOnClickListner);


        //card.setOnDragListener();
        card.setPosition(drawnCardPosition);
        Log.i("min", "inside " + Float.toString(card.getX()) + " " + Float.toString(card.getY()));
        card.setImageResource(card.getPictureId());

        return card;
    }

    /*----------------------------------------------------------------------------------------------------*/



    /*---------------------------------------------- Draw Stack ------------------------------------------*/

    private void initializeDrawStack() {
        RelativeLayout drawRelativeLayout = (RelativeLayout) findViewById(R.id.drawRelativeLayout);

        drawCardPosition = new Pair<Float, Float>(drawRelativeLayout.getX() + cardWidth, drawRelativeLayout.getY());
        Card emptyCard = new Card(getApplicationContext(), R.drawable.diamonds2, baseImageButton.getLayoutParams());
        emptyCard = cardForDraw(emptyCard);
        emptyCard.setImageResource(emptyCard.getPictureId());
        drawRelativeLayout.addView(emptyCard);
        drawCardStack.add(emptyCard);

        for (int i = 0; i < 4; i++) {
            Card card = cards.get(i);
            card = cardForDraw(card);
            drawCardStack.push(card);
            drawRelativeLayout.addView(drawCardStack.peek());
        }


        drawCardPosition = drawCardStack.peek().getPosition();

        //test
        Card card = new Card(getApplicationContext(), R.drawable.clubs1, baseImageButton.getLayoutParams());
        card.setPosition(drawRelativeLayout.getX(), drawRelativeLayout.getY());
        drawnCardStack.push(card);
        drawnCardPosition = drawnCardStack.peek().getPosition();
        drawRelativeLayout.addView(card);
        //test
    }

    private Card cardForDraw(Card card) {
        card.setOnClickListener(drawCardOnClickListener);
        card.setOnDragListener(null);
        card.setPosition(drawCardPosition);
        card.setImageResource(R.drawable.cardcover);

        return card;
    }

    private View.OnClickListener drawCardOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (drawCardStack.size() == 1) {
                refillDrawCardStack();
            } else {
                RelativeLayout drawRelativeLayout = (RelativeLayout) findViewById(R.id.drawRelativeLayout);

                Log.i("min", "ana hna");
                Card card = drawCardStack.peek();
                drawRelativeLayout.removeView(card);
                drawCardStack.pop();
                Log.i("min", "before " + Float.toString(card.getX()) + " " + Float.toString(card.getY()));
                card = cardForDrawn(card);
                drawRelativeLayout.addView(card);
                Log.i("min", "after " + Float.toString(card.getX()) + " " + Float.toString(card.getY()));
                drawnCardStack.push(card);
            }
        }
    };

    private void refillDrawCardStack() {
        while (!drawnCardStack.empty()) {
            Card card = drawnCardStack.peek();
            drawnCardStack.pop();
            card = cardForDraw(card);
            drawCardStack.push(card);
        }
    }


    /*----------------------------------------------------------------------------------------------------*/




    /*--------------------------------------------  Play Card ----------------------------------------------*/

    private Card cardForPlay(int idx, Card card) {
        card.setPlay(true);
        card.setPlayPosition(idx);
        card.setInPlayPosittion(playCardArrayList.get(idx).size()-1);
        card.setFinished(false);
        card.setPosition(new Pair<Float, Float>(playCardPostion.get(idx).first, playCardPostion.get(idx).second));

        return card;
    }

    private void initializePlayCardStack() {
        RelativeLayout playRelativeView = (RelativeLayout) findViewById(R.id.playCardRelativeView);

        for (int i = 0; i < 7; i++) {
            playCardArrayList.add(new ArrayList<Card>());
            playCardArrayList.get(i).get(playCardArrayList.size()-1).setX(i * 100 + 50 * i);
            playCardArrayList.get(i).get(playCardArrayList.size()-1).setImageResource(R.drawable.ic_launcher_background);
            playRelativeView.addView(playCardArrayList.get(i).get(playCardArrayList.get(i).size()-1));
        }
    }

    private void initializePlayCardPosition() {
        RelativeLayout playRelativeLayout = (RelativeLayout) findViewById(R.id.playCardRelativeView);
        Float x = playRelativeLayout.getX(), y = playRelativeLayout.getY();
        playCardPostion = new ArrayList<Pair<Float, Float>>();

        Log.i("min", "CARASDKASJFKLASHK");
        for (int i = 0; i < 7; i++) {
            playCardPostion.add(new Pair<Float, Float>(x, y));

            RelativeLayout tempRelativeLayout = new RelativeLayout(getBaseContext());
            tempRelativeLayout.setLayoutParams(new ViewGroup.LayoutParams(baseImageButton.getLayoutParams().width,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            tempRelativeLayout.setX(playCardPostion.get(i).first);
            tempRelativeLayout.setY(playCardPostion.get(i).second);
            /*if(i % 2 == 1){

                tempRelativeLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
            }
            else {
                tempRelativeLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorAccent));

            }*/
            playRelativeArray.add(tempRelativeLayout);
            playRelativeLayout.addView(tempRelativeLayout);

            playCardArrayList.add(new ArrayList<Card>());

            /*String cardName = cardType.get(i % 4);
            cardName += Integer.toString(0 % 4 + 1);

            int pictureId = getApplicationContext().getResources().getIdentifier(cardName, "drawable", getApplicationContext().getPackageName());
            Card card = new Card(getApplicationContext(), pictureId , baseImageButton.getLayoutParams());

            card.setPosition(playCardPostion.get(i));
            card.setPlay(true);
            card.setPlayPosition(i);
            card.setOnClickListener(cardOnClickListner);
            playCardStack.get(i).push(card);
            playRelativeLayout.addView(card);
            */
            x += cardWidth;
        }
    }

    /*----------------------------------------------------------------------------------------------------*/



    /*--------------------------------------------  Finished Card ----------------------------------------------*/


    private void initializeFinishedCardPosition() {
        RelativeLayout finisehedCardRelativeView = (RelativeLayout) findViewById(R.id.finishedCardsRelativeLayout);
        Float x = finisehedCardRelativeView.getX(), y = finisehedCardRelativeView.getY();
        finishedCardPosition = new ArrayList<Pair<Float, Float>>();

        for (int i = 0; i < 4; i++) {
            finishedCard.add(new Stack<Card>());
            Log.i("min", Integer.toString(i));
            finishedCardPosition.add(new Pair<Float, Float>(x, y));
            //Card card = new Card(getApplicationContext() , R.drawable.clubs1 , baseImageButton.getLayoutParams());
            //card.setPosition(playCardPostion.get(i));
            //finisehedCardRelativeView.addView(card);
            Log.i("min", "ana " + Float.toString(finishedCardPosition.get(i).first) + " " + Float.toString(finishedCardPosition.get(i).second));
            x += cardWidth;
        }
    }

    private Card cardForFinished(int pos, Card card) {
        Log.i("min", card.getName() + " " + Integer.toString(card.getNumber()) + " " + Integer.toString(pos));
        Log.i("min", Float.toString(finishedCardPosition.get(pos).first) + Float.toString(finishedCardPosition.get(pos).second));
        card.setPosition(finishedCardPosition.get(pos));
        finishedCard.get(pos).push(card);

        return card;
    }


    /*----------------------------------------------------------------------------------------------------*/




    /*--------------------------------------------  General ----------------------------------------------*/


    private Boolean isWin() {
        return remainingCards == 0;
    }


    private View.OnClickListener cardOnClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Card card = ((Card) v);

            RelativeLayout finishedRelativeLayout = (RelativeLayout) findViewById(R.id.finishedCardsRelativeLayout);
            int lastNumber = 0, pos = -1;
            Log.i("min", card.getName() + " " + Integer.toString(card.getNumber()));
            switch (card.getName()) {
                case "hearts":
                    if (finishedCard.get(1).size() != 0) {
                        lastNumber = finishedCard.get(1).peek().getNumber();
                    }
                    if (lastNumber == card.getNumber() - 1) {
                        pos = 1;
                    }
                    break;
                case "diamonds":
                    if (finishedCard.get(2).size() != 0) {
                        lastNumber = finishedCard.get(2).peek().getNumber();
                    }
                    if (lastNumber == card.getNumber() - 1) {
                        pos = 2;
                    }
                    break;
                case "clubs":
                    if (finishedCard.get(3).size() != 0) {
                        lastNumber = finishedCard.get(3).peek().getNumber();
                        Log.i("min", Integer.toString(lastNumber));
                    }

                    Log.i("min", Integer.toString(lastNumber) + " " + Integer.toString(card.getNumber()));
                    if (lastNumber == card.getNumber() - 1) {
                        Log.i("min", Integer.toString(lastNumber));

                        pos = 3;
                    }
                    break;
                case "spades":
                    if (finishedCard.get(0).size() != 0) {
                        lastNumber = finishedCard.get(0).peek().getNumber();
                    }
                    if (lastNumber == card.getNumber() - 1) {
                        pos = 0;
                    }
                    break;
            }

            if (pos != -1) {

                RelativeLayout parentRelativeLayout = (RelativeLayout) card.getParent();

                if (parentRelativeLayout.getId() == R.id.drawRelativeLayout) {
                    drawnCardStack.pop();
                } else {
                    if(card.getInPlayPosittion() != playCardArrayList.get(card.getPlayPosition()).size() - 1) return;

                    playCardArrayList.get(card.getPlayPosition()).remove(playCardArrayList.get(card.getPlayPosition()).size()-1);
                    card.setPlay(false);
                }

                parentRelativeLayout.removeView(card);
                card.setFinished(true);
                card.setFinishedPosition(pos);
                finishedRelativeLayout.addView(card);
                card = cardForFinished(pos, card);
            }
        }
    };

    private  Boolean canPickCard(Card card){
        boolean canPick = true;
        int lastNumber = card.getNumber();
        for(int i = card.getInPlayPosittion() + 1 ; i < playCardArrayList.get(card.getPlayPosition()).size() ; i++){
            Log.i("min" , "gaeasdhfkjadshkfh" + " " + Integer.toString(playCardArrayList.get(card.getPlayPosition()).get(i).getNumber()) + " " + Integer.toString(lastNumber) + " " + Integer.toString(i));
            if(playCardArrayList.get(card.getPlayPosition()).get(i).getNumber() != lastNumber - 1 ){
                canPick = false;
                break;
            }
            lastNumber = playCardArrayList.get(card.getPlayPosition()).get(i).getNumber();
        }

        return canPick;
    }

    private View.OnTouchListener onCardTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {


                Log.i("min" , "gere");
                Card temp = ((Card) v);
                /*if(!canPickCard(temp)) {
                    return false;
                }*/

                Card card = ((Card) v);

                Log.i("min" , "gere2");

                RelativeLayout parentLayout = (RelativeLayout) card.getParent();
                Log.i("min" , Integer.toString(card.getNumber()));

               // parentLayout.removeView(playRelativeArray.get(card.getPlayPosition()));

                int add = 0;
                for(int i = card.getInPlayPosittion() ; i < playCardArrayList.get(card.getPlayPosition()).size(); i++){
                    Card t = playCardArrayList.get(card.getPlayPosition()).get(i);
                    parentLayout.removeView(t);
                    Log.i("min"  ,"ahhhhhhhhhhhhhhhhhhhhhhh" + " " + Integer.toString(t.getNumber()) + " " + Integer.toString(t.getInPlayPosittion()));
                    t.setPosition(0f,0f + add);
                    add += 50;
                    playRelativeArray.get(card.getPlayPosition()).removeView(t);
                    playRelativeArray.get(card.getPlayPosition()).addView(t );
                }


                //parentLayout.addView(playRelativeArray.get(card.getPlayPosition()));

                RelativeLayout tv = playRelativeArray.get(card.getPlayPosition());
                ClipData data = ClipData.newPlainText("", "");
                RelativeLayout.DragShadowBuilder shadowBuilder = new RelativeLayout.DragShadowBuilder(tv);
                tv.startDrag(data, shadowBuilder, tv , 0);
            }

            Card card = ((Card) v);
            playRelativeArray.get(card.getPlayPosition()).removeAllViews();
            /*Card card = ((Card) v);
            for(int i = card.getInPlayPosittion() ; i < playCardArrayList.get(card.getPlayPosition()).size(); i++){
                Card t = playCardArrayList.get(card.getPlayPosition()).get(i);

                playRelativeArray.get(card.getPlayPosition()).removeAllViews(t);
            }*/

            return false;
        }
    };


    private View.OnDragListener onCardDrag = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();

                    if (view instanceof LinearLayout) {



                    } else if (view instanceof ImageButton) {



                    }
                    break;
            }
            return true;
        }
    };

    /*----------------------------------------------------------------------------------------------------*/

}
