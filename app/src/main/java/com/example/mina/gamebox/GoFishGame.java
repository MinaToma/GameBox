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
    Float[] sizeOfFirstPlayerCard= new Float[4];
    int currentPlayer= 0;
    boolean goFishNow=false;
    int selcteedCard =-1;


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
        sizeOfFirstPlayerCard[0]=new Float(0);
        sizeOfFirstPlayerCard[1]=new Float(0);
        sizeOfFirstPlayerCard[2]=new Float(0);
        sizeOfFirstPlayerCard[3]=new Float(0);

        while (numOfCards-->0) {
            Card cardNow;

            cardNow = deck.lastElement();
            cardNow.toUnDeck();
            cardNow.showCard();
            cardNow.setPosition(firstPos.first+(sizeOfFirstPlayerCard[0]),firstPos.second);
            players.get(0).add(cardNow);
            sizeOfFirstPlayerCard[0]+=Float.valueOf(100);

            addCardToConstraint(cardNow);

            deck.pop();
        }




        // give 4 cards for  second player
        numOfCards=4;
        while (numOfCards-->0) {

            Card cardNow;

            cardNow = deck.lastElement();
            cardNow.toUnDeck();
            cardNow.setPosition(secondPos.first+(sizeOfFirstPlayerCard[1]),secondPos.second);
            players.get(1).add(cardNow);
            sizeOfFirstPlayerCard[1]+=Float.valueOf(100);
            deck.pop();
        }

        // give 4 cards for  third player
        numOfCards=4;
        while (numOfCards-->0) {

            Card cardNow;

            cardNow = deck.lastElement();
            cardNow.toUnDeck();
            cardNow.setPosition(thirdPos.first+(sizeOfFirstPlayerCard[2]),thirdPos.second);
            players.get(2).add(cardNow);
            sizeOfFirstPlayerCard[2]+=Float.valueOf(100);

            addCardToConstraint(cardNow);
            deck.pop();
        }

        // give 4 cards for  fourth player
        numOfCards=4;
        while (numOfCards-->0) {

            Card cardNow;

            cardNow = deck.lastElement();
            cardNow.toUnDeck();
            cardNow.setPosition(fourthPos.first+(sizeOfFirstPlayerCard[3]),fourthPos.second);
            players.get(3).add(cardNow);
            sizeOfFirstPlayerCard[3]+=Float.valueOf(100);
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
        currentPos=firstPos;

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Card card = ((Card ) view);

            selcteedCard=card.getNumber();

            if(card.getDeck())
            {
                if(goFishNow)
                {
                    Card cardNow=deck.lastElement();
                    deck.pop();
                    cardNow.toUnDeck();
                    if(currentPlayer==0) {
                        cardNow.showCard();
                    }
                    cardNow.setPosition(currentPos.first+sizeOfFirstPlayerCard[currentPlayer],currentPos.second);
                    sizeOfFirstPlayerCard[currentPlayer]+=Float.valueOf(100);
                    players.get(currentPlayer).add(cardNow);
                    goFishNow = false ;
                    currentPlayer++;
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
                    }
                }
            }

        }

    };

    public void firstPlayerClick(View view) {
        if( currentPlayer!=0 && goFishNow==false)
        {


            if(selcteedCard>=0) {
                ArrayList<Card> currentCads = new ArrayList<Card>();
                for (Card cardNow : players.get(0)) {

                    if (cardNow.getNumber() == selcteedCard) {
                        currentCads.add(cardNow);
                    }

                }
                if (currentCads.size() > 0) {

                    for (Card cardNow : currentCads) {
                        players.get(0).remove(cardNow);

                        cardNow.hideCard();
                        cardNow.setPosition(currentPos.first+sizeOfFirstPlayerCard[currentPlayer],currentPos.second);
                        sizeOfFirstPlayerCard[currentPlayer]+=Float.valueOf(100);
                        players.get(currentPlayer).add(cardNow);
                    }
                    currentCads.clear();
                    sizeOfFirstPlayerCard[0]=Float.valueOf(0);
                    for(Card cardNow : players.get(0))
                    {
                        cardNow.setPosition(firstPos.first+(sizeOfFirstPlayerCard[0]),firstPos.second);
                        sizeOfFirstPlayerCard[0]+=Float.valueOf(100);
                    }

                }
                else
                {
                    Toast.makeText(this, "Go Fish", Toast.LENGTH_LONG).show();
                    goFishNow = true;
                }
            }

        }
    }
    public void secondPlayerClick(View view) {
        if( currentPlayer!=1 && goFishNow==false)
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

                    for (Card cardNow : currentCads) {
                        players.get(1).remove(cardNow);

                        cardNow.hideCard();
                        cardNow.setPosition(currentPos.first+sizeOfFirstPlayerCard[currentPlayer],currentPos.second);
                        sizeOfFirstPlayerCard[currentPlayer]+=Float.valueOf(100);
                        players.get(currentPlayer).add(cardNow);
                    }
                    currentCads.clear();
                    sizeOfFirstPlayerCard[1]=Float.valueOf(0);
                    for(Card cardNow : players.get(1))
                    {
                        cardNow.setPosition(firstPos.first+(sizeOfFirstPlayerCard[1]),firstPos.second);
                        sizeOfFirstPlayerCard[1]+=Float.valueOf(100);
                    }

                }
                else
                {Toast.makeText(this, "Go Fish", Toast.LENGTH_LONG).show();
                    goFishNow=true;
                }
            }
        }
    }

    public void thirdPlayerClick(View view) {
        if( currentPlayer!=2 && goFishNow==false)
        {
            if(selcteedCard>=0)
            {
                ArrayList<Card> currentCads= new ArrayList<Card>();
                for (Card cardNow : players.get(2)){

                    if(cardNow.getNumber()==selcteedCard)
                    {
                        currentCads.add(cardNow);
                    }

                }
                if(currentCads.size()>0)
                {
                    for (Card cardNow : currentCads) {
                        players.get(2).remove(cardNow);

                        cardNow.hideCard();
                        cardNow.setPosition(currentPos.first+sizeOfFirstPlayerCard[currentPlayer],currentPos.second);
                        sizeOfFirstPlayerCard[currentPlayer]+=Float.valueOf(100);
                        players.get(currentPlayer).add(cardNow);
                    }
                    currentCads.clear();
                    sizeOfFirstPlayerCard[2]=Float.valueOf(0);
                    for(Card cardNow : players.get(2))
                    {
                        cardNow.setPosition(firstPos.first+(sizeOfFirstPlayerCard[2]),firstPos.second);
                        sizeOfFirstPlayerCard[2]+=Float.valueOf(100);
                    }

                }
                else
                {
                    Toast.makeText(this, "Go Fish", Toast.LENGTH_LONG).show();
                    goFishNow=true;
                }
            }
        }
    }

    public void fourthPlayerClick(View view) {
        if( currentPlayer!=3 && goFishNow==false)
        {
            if(selcteedCard>=0)
            {
                ArrayList<Card> currentCads= new ArrayList<Card>();
                for (Card cardNow : players.get(3)){

                    if(cardNow.getNumber()==selcteedCard)
                    {
                        currentCads.add(cardNow);

                    }

                }
                if(currentCads.size()>0)
                {

                    for (Card cardNow : currentCads) {
                        players.get(3).remove(cardNow);

                        cardNow.hideCard();
                        cardNow.setPosition(currentPos.first+sizeOfFirstPlayerCard[currentPlayer],currentPos.second);
                        sizeOfFirstPlayerCard[currentPlayer]+=Float.valueOf(100);
                        players.get(currentPlayer).add(cardNow);
                    }
                    currentCads.clear();
                    sizeOfFirstPlayerCard[3]=Float.valueOf(0);
                    for(Card cardNow : players.get(3))
                    {
                        cardNow.setPosition(firstPos.first+(sizeOfFirstPlayerCard[3]),firstPos.second);
                        sizeOfFirstPlayerCard[3]+=Float.valueOf(100);
                    }

                }
                else
                {
                    Toast.makeText(this, "Go Fish", Toast.LENGTH_LONG).show();
                    goFishNow=true;
                }
            }
        }
    }


}
