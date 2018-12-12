package com.example.mina.gamebox;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class GoFishGame extends AppCompatActivity {

    private Random random;
    private Context context;
    private ViewGroup.LayoutParams cardParams , playerParams;
    private ConstraintLayout constraintLayout ;

    private Stack<Card> deck , hand;
    private Pair<Float , Float> deckPosition , firstPos , secondPos , thirdPos  , fourthPos, currentPos;

    private ArrayList<Pair<Float , Float>> suitsPosition , playAreaPosition;
    private ArrayList<Stack<Card>> suitsCard ;
    private ArrayList<ArrayList<Card>> playArea;

    private HashMap<String , Integer> suitIdx;
    private ArrayList<Card> allCards;
    ArrayList<ArrayList<Card>> players ;
    private int coverCardID , emptyDeckID;
    private ArrayList<String> cardType;
    Float sizeOfFirstPlayerCard;
    int currentPlayer= 0;
    boolean goFishNow=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_go_fish_game);

    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if(hasFocus && constraintLayout == null) {
            constraintLayout  = (ConstraintLayout) findViewById(R.id.goFishConstrinat);
            players = new ArrayList<ArrayList<Card>>();

            initializeGame();

        }
    }

    private void initializeGame() {
        allCards = new ArrayList<Card>(52);

        initializeRandom();
        initializeCardLayoutParams();
        initializeDeck();
        initializeAllCards();



        distributeCards();




    }

    private void initializeRandom() {
        random = new Random(System.currentTimeMillis());
    }

    private void distributeCards() {
        fillDeck();
    }

    private void fillDeck() {
        ImageButton deckImage = (ImageButton) findViewById(R.id.allDeckCards);
        deckPosition = new Pair<Float , Float>(deckImage.getX() , deckImage.getY());
      Card card;


        while(!allCards.isEmpty()){
            int randPos = random.nextInt(allCards.size());
            card = allCards.get(randPos);
            allCards.remove(randPos);
            card.toDeck(deckPosition);
            deck.add(card);
        }

        int numOfCards=4;


        for(int i = 0 ; i <4 ; i++)
            players.add(new ArrayList<Card>());

        // give 4 cards for  first player
        sizeOfFirstPlayerCard = Float.valueOf(0);

        while (numOfCards-->0) {
            Card cardNow;

            cardNow = deck.lastElement();
            cardNow.toUnDeck();
            cardNow.showCard();
            cardNow.setPosition(firstPos.first+(sizeOfFirstPlayerCard),firstPos.second);
            players.get(0).add(cardNow);
            sizeOfFirstPlayerCard+=Float.valueOf(100);

            addCardToConstraint(cardNow);

            deck.pop();
        }




        // give 4 cards for  second player
        numOfCards=4;
        while (numOfCards-->0) {

            Card cardNow;

            cardNow = deck.lastElement();
            cardNow.toUnDeck();
            players.get(1).add(cardNow);
            cardNow.setPosition(secondPos);
            addCardToConstraint(cardNow);
            deck.pop();
        }

        // give 4 cards for  third player
        numOfCards=4;
        while (numOfCards-->0) {

            Card cardNow;

            cardNow = deck.lastElement();
            cardNow.toUnDeck();
            players.get(2).add(cardNow);
            cardNow.setPosition(thirdPos);

            addCardToConstraint(cardNow);
            deck.pop();
        }

        // give 4 cards for  fourth player
        numOfCards=4;
        while (numOfCards-->0) {

            Card cardNow;

            cardNow = deck.lastElement();
            cardNow.toUnDeck();
            players.get(3).add(cardNow);
            cardNow.setPosition(fourthPos);
            addCardToConstraint(cardNow);
            deck.pop();
        }
    }
    private void addCardToConstraint(Card card) {
        constraintLayout.removeView(card);
        constraintLayout.addView(card);
    }

    private void initializeAllCards()
    {
        allCards = new ArrayList<Card>();
        cardType = new ArrayList<String>();
        suitIdx = new HashMap<String, Integer>();
        cardType.add("spades");
        cardType.add("hearts");
        cardType.add("clubs");
        cardType.add("diamonds");
        coverCardID = getResources().getIdentifier("cardcover" , "drawable" ,  getPackageName());
        emptyDeckID = getResources().getIdentifier("empty" , "drawable" ,  getPackageName());

        for(int i = 0 ; i < 4 ; i++){
            for(int j = 1 ; j <= 13 ; j++){
                String name = cardType.get(i) + Integer.toString(j);
                int id = getResources().getIdentifier(name , "drawable" ,  getPackageName());

                Card card = new Card(getBaseContext() , id , coverCardID , cardParams , null , constraintLayout );
                card.setPosition(deckPosition);
                card.setOnClickListener(onClickListener);
                allCards.add(card);
                addCardToConstraint(card);

            }
            suitIdx.put(cardType.get(i) , i);
        }
    }

    private void initializeCardLayoutParams() {
        cardParams = constraintLayout.findViewById(R.id.allDeckCards).getLayoutParams();
        playerParams = constraintLayout.findViewById(R.id.firstPlayerCards).getLayoutParams();
    }
    private void initializeDeck() {
        deck = new Stack<Card>();
        deckPosition = new Pair<Float , Float>(constraintLayout.findViewById(R.id.allDeckCards).getX() ,
                constraintLayout.findViewById(R.id.allDeckCards).getY());
        firstPos = new Pair<Float , Float>(constraintLayout.findViewById(R.id.firstPlayerCards).getX() ,
                constraintLayout.findViewById(R.id.firstPlayerCards).getY());
        secondPos = new Pair<Float , Float>(constraintLayout.findViewById(R.id.secondPlayerCards).getX() ,
                constraintLayout.findViewById(R.id.secondPlayerCards).getY());
        thirdPos = new Pair<Float , Float>(constraintLayout.findViewById(R.id.thirdPlayerCards).getX() ,
                constraintLayout.findViewById(R.id.thirdPlayerCards).getY());
        fourthPos = new Pair<Float , Float>(constraintLayout.findViewById(R.id.thirdPlayerCards).getX() ,
                constraintLayout.findViewById(R.id.fourthPlayerCards).getY());

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Card card = ((Card ) view);
            ImageButton firstPlayer = (ImageButton) findViewById(R.id.firstPlayerButton);
            ImageButton secondPlayer = (ImageButton) findViewById(R.id.secondPlayerButton);
            ImageButton thirdPlayer = (ImageButton) findViewById(R.id.thirdPlayerButton);
            ImageButton fourthPlayer = (ImageButton) findViewById(R.id.fourthPlayerButton);
            int selcteedCard =-1;
            selcteedCard=card.getNumber();
            goFishNow=true;
            if(card.getDeck())
            {
                if(goFishNow)
                {
                    Card cardNow=deck.lastElement();
                    deck.pop();
                    cardNow.toUnDeck();
                    cardNow.showCard();
                    cardNow.setPosition(firstPos.first+sizeOfFirstPlayerCard,firstPos.second);
                    sizeOfFirstPlayerCard+=Float.valueOf(100);
                    players.get(0).add(cardNow);
                    goFishNow = false ;
                   /* currentPlayer++;
                    currentPlayer%=4;
                    if(currentPlayer==0)
                    {
                        currentPos=firstPos;
                    }
                    else if(currentPlayer==1)
                    {
                        currentPos=secondPos;

                    }
                    else if(currentPlayer==2)
                    {
                        currentPos = thirdPos;
                    }
                    else
                    {
                        currentPos= fourthPos;
                    }*/
                }
            }

            if(secondPlayer.callOnClick() && currentPlayer!=1)
            {

                if(selcteedCard>=0)
                {
                    ArrayList<Card> currentCads= new ArrayList<Card>();
                    for (Card cardNow : players.get(1)){

                        if(cardNow.getNumber()==selcteedCard)
                        {
                            currentCads.add(cardNow);

                        }

                    }
                    if(currentCads.size()>0)
                    {

                        for (Card cardNow : currentCads)
                        {
                            players.get(1).remove(cardNow);

                            cardNow.showCard();
                            cardNow.setPosition(firstPos.first+sizeOfFirstPlayerCard,firstPos.second);
                            sizeOfFirstPlayerCard+=Float.valueOf(60);
                            players.get(currentPlayer).add(cardNow);
                        }
                        currentCads.clear();

                    }
                    else
                    {
                        goFishNow=true;
                    }
                }
            }


        }
    };

}
