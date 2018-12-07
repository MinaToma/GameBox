package com.example.mina.gamebox;

import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class GoFishGame extends AppCompatActivity {

    private Random random;
    private Context context;
    private ViewGroup.LayoutParams cardParams;
    private ConstraintLayout constraintLayout;

    private Stack<Card> deck , hand;
    private Pair<Float , Float> deckPosition , handPosition;

    private ArrayList<Pair<Float , Float>> suitsPosition , playAreaPosition;
    private ArrayList<Stack<Card>> suitsCard ;
    private ArrayList<ArrayList<Card>> playArea;

    private HashMap<String , Integer> suitIdx;
    private ArrayList<Card> allCards;
    private int coverCardID , emptyDeckID;
    private ArrayList<String> cardType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_fish_game);
    }
    private void initializeGame() {
        allCards = new ArrayList<Card>(52);

        initializeRandom();
        initializeCardLayoutParams();

        initializeAllCards();
        distributeCards();
    }

    private void initializeRandom() {
        random = new Random(System.currentTimeMillis());
    }

    private void distributeCards() {
        fillPlayArea();
        fillDeck();
    }

    private void fillPlayArea() {
        for(int i = 1 ; i <= 7 ; i++){
            while(playArea.get(i-1).size() < i){
                int randPos = random.nextInt(allCards.size());
                if(allCards.get(randPos).getNumber() > 10) continue;

                Card card = allCards.get(randPos);
                allCards.remove(randPos);

                if(playArea.get(i-1).size() < i-1){
                    card.hideCard();
                    card.setFaceUp(false);
                }

                card.toPlayArea(i-1 , playArea.get(i-1).size() , new Pair<Float, Float>(playAreaPosition.get(i-1).first ,
                        playAreaPosition.get(i-1).second + card.getLayoutParams().height / 4 * playArea.get(i-1).size()));
                card.setLastPosition(card.getPosition());
                playArea.get(i-1).add(card);

                addCardToConstraint(card);
            }
        }
    }

    private void addCardToConstraint(Card card) {
        constraintLayout.removeView(card);
        constraintLayout.addView(card);
    }

    private void fillDeck() {
        Card card = new Card(context , emptyDeckID , coverCardID , cardParams , onTouchListener , constraintLayout);
        card.toDeck(deckPosition);
        card.showCard();
        deck.add(card);
        addCardToConstraint(card);

        while(!allCards.isEmpty()){
            int randPos = random.nextInt(allCards.size());
            card = allCards.get(randPos);
            allCards.remove(randPos);
            card.toDeck(deckPosition);
            deck.add(card);
            addCardToConstraint(card);
        }
    }

    private void initializeAllCards() {
        allCards = new ArrayList<Card>();
        cardType = new ArrayList<String>();
        suitIdx = new HashMap<String, Integer>();
        cardType.add("spades");
        cardType.add("hearts");
        cardType.add("clubs");
        cardType.add("diamonds");
        coverCardID = context.getResources().getIdentifier("cardcover" , "drawable" ,  context.getPackageName());
        emptyDeckID = context.getResources().getIdentifier("empty" , "drawable" ,  context.getPackageName());

        for(int i = 0 ; i < 4 ; i++){
            for(int j = 1 ; j <= 13 ; j++){
                String name = cardType.get(i) + Integer.toString(j);
                int id = context.getResources().getIdentifier(name , "drawable" ,  context.getPackageName());

                Card card = new Card(context , id , coverCardID , cardParams , onTouchListener , constraintLayout );
                card.setPosition(deckPosition);
                allCards.add(card);
            }
            suitIdx.put(cardType.get(i) , i);
        }
    }

    private void initializeCardLayoutParams() {
        cardParams = constraintLayout.findViewById(R.id.deckCard).getLayoutParams();
    }
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };


}
