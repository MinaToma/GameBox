package com.example.mina.gamebox;

import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class Game {

    private Random random;
    private Context context;
    private ViewGroup.LayoutParams cardParams;
    private ConstraintLayout constraintLayout;

    private Stack<Card> deck , hand , storeForDelete;
    private Pair<Float , Float> deckPosition , handPosition;

    private ArrayList<Pair<Float , Float>> suitsPosition , playAreaPosition;
    private ArrayList<Stack<Card>> suitsCard ;
    private ArrayList<ArrayList<Card>> playArea;

    private HashMap<String , Integer> suitIdx;
    //private ArrayList<Card> allCards ;
    private ArrayList<ArrayList<Card>> allCards;
    private int coverCardID , emptyDeckID;
    private ArrayList<String> cardType;

    public Game(Context context , ConstraintLayout constraintLayout)
    {
        this.constraintLayout = constraintLayout;
        this.context = context;

        initializeGame();
    }

    public void initializeGame() {

        initializeRandom();
        initializeCardLayoutParams();
        initializeDeck();
        initializeHand();
        initializeSuits();
        initializePlayArea();
        initializeAllCards();
        distributeCards();
        initializeEmptySuitCell();
    }

    private void initializeEmptySuitCell() {

        int emptySpades = context.getResources().getIdentifier("spadesbg" , "drawable" ,  context.getPackageName());
        int emptyHearts = context.getResources().getIdentifier("heartsbg" , "drawable" ,  context.getPackageName());
        int emptyClubs = context.getResources().getIdentifier("clubsbg" , "drawable" ,  context.getPackageName());
        int emptyDiamonds = context.getResources().getIdentifier("diamondsbg" , "drawable" ,  context.getPackageName());

        Card emptySpadesCard = new Card(context , emptySpades , coverCardID , cardParams , null , constraintLayout);
        Card emptyHeartsCard = new Card(context , emptyHearts , coverCardID , cardParams , null, constraintLayout);
        Card emptyClubsCard = new Card(context , emptyClubs , coverCardID , cardParams , null, constraintLayout);
        Card emptyDiamondsCard = new Card(context , emptyDiamonds , coverCardID , cardParams , null , constraintLayout);

        emptySpadesCard.setPosition(suitsPosition.get(0));
        emptyHeartsCard.setPosition(suitsPosition.get(1));
        emptyClubsCard.setPosition(suitsPosition.get(2));
        emptyDiamondsCard.setPosition(suitsPosition.get(3));

        constraintLayout.addView(emptySpadesCard);
        constraintLayout.addView(emptyHeartsCard);
        constraintLayout.addView(emptyClubsCard);
        constraintLayout.addView(emptyDiamondsCard);
    }

    private void initializeRandom() {
        random = new Random(System.currentTimeMillis());
    }

    private void distributeCards() {
        fillPlayAndDeck();
        //fillPlayArea();
        //fillDeck();
    }

    private void fillPlayAndDeck() {
        Card card = new Card(context , emptyDeckID , coverCardID , cardParams , onTouchListener , constraintLayout);
        card.toDeck(deckPosition);
        card.showCard();
        deck.add(card);
        addCardToConstraint(card);

        int assignedCard = 0;
        for(int i = 1 ; i <= 13 ; i++){
            for(int j = 0 ; j < 4 ; j++) {

                if(assignedCard >= 28){
                    card = allCards.get(i-1).get(j);
                    card.toDeck(deckPosition);
                    deck.add(card);
                    continue;
                }

                int idx = random.nextInt(7);;
                while(playArea.get(idx).size() == idx + 1)
                   idx = random.nextInt(7);

                if (idx < 7) {
                    assignedCard++;

                    card = allCards.get(i-1).get(j);

                    if (playArea.get(idx).size() < idx) {
                        card.hideCard();
                        card.setFaceUp(false);
                    }

                    card.toPlayArea(idx, playArea.get(idx).size(), new Pair<Float, Float>(playAreaPosition.get(idx).first,
                            playAreaPosition.get(idx).second + card.getLayoutParams().height / 4 * playArea.get(idx).size()));
                    card.setLastPosition(card.getPosition());
                    playArea.get(idx).add(card);
                }

                addCardToConstraint(card);
            }
        }
    }

    private void addCardToConstraint(Card card) {
        constraintLayout.removeView(card);
        constraintLayout.addView(card);
    }

    private void initializeAllCards() {
        allCards = new ArrayList<ArrayList<Card>>();
        storeForDelete = new Stack<Card>();
        cardType = new ArrayList<String>();
        suitIdx = new HashMap<String, Integer>();
        cardType.add("spades");
        cardType.add("hearts");
        cardType.add("clubs");
        cardType.add("diamonds");
        coverCardID = context.getResources().getIdentifier("cardcover" , "drawable" ,  context.getPackageName());
        emptyDeckID = context.getResources().getIdentifier("empty" , "drawable" ,  context.getPackageName());

        for(int j = 1 ; j <= 13 ; j++){
            allCards.add(new ArrayList<Card>());
            for(int i = 0 ; i < 4 ; i++){
                String name = cardType.get(i) + Integer.toString(j);
                int id = context.getResources().getIdentifier(name , "drawable" ,  context.getPackageName());

                Card card = new Card(context , id , coverCardID , cardParams , onTouchListener , constraintLayout );
                card.setPosition(deckPosition);
                allCards.get(j-1).add(card);
                storeForDelete.add(card);
                suitIdx.put(cardType.get(i) , i);
            }
        }

    }

    private void initializeCardLayoutParams() {
        cardParams = constraintLayout.findViewById(R.id.deckCard).getLayoutParams();
    }

    private void initializePlayArea() {
        playAreaPosition = new ArrayList<Pair<Float,Float>>(7);
        playArea = new ArrayList<ArrayList<Card>>(7);

        for(int i = 0 ; i < 7 ; i++){
            playArea.add(new ArrayList<Card>());
            String name = "playArea" + Integer.toString(i);

            int id = context.getResources().getIdentifier(name , "id" , context.getPackageName());
            playAreaPosition.add(new Pair<Float, Float>(constraintLayout.findViewById(id).getX() ,
                    constraintLayout.findViewById(id).getY()));
        }
    }

    private void initializeSuits() {
        suitsPosition = new ArrayList<Pair<Float, Float>>();
        suitsCard = new ArrayList<Stack<Card>>(4);

        suitsPosition.add(new Pair<Float , Float>(constraintLayout.findViewById(R.id.suitSpades).getX() ,
                constraintLayout.findViewById(R.id.handCard).getY()));
        suitsPosition.add(new Pair<Float , Float>(constraintLayout.findViewById(R.id.suitHearts).getX() ,
                constraintLayout.findViewById(R.id.handCard).getY()));
        suitsPosition.add(new Pair<Float , Float>(constraintLayout.findViewById(R.id.suitClubs).getX() ,
                constraintLayout.findViewById(R.id.handCard).getY()));
        suitsPosition.add(new Pair<Float , Float>(constraintLayout.findViewById(R.id.suitDiamonds).getX() ,
                constraintLayout.findViewById(R.id.handCard).getY()));

        for(int i = 0 ; i < 4 ; i++)
            suitsCard.add(new Stack<Card>());
    }

    private void initializeHand() {
        hand = new Stack<Card>();
        handPosition = new Pair<Float , Float>(constraintLayout.findViewById(R.id.handCard).getX() ,
                constraintLayout.findViewById(R.id.handCard).getY());
    }

    private void initializeDeck() {
        deck = new Stack<Card>();
        deckPosition = new Pair<Float , Float>(constraintLayout.findViewById(R.id.deckCard).getX() ,
                constraintLayout.findViewById(R.id.deckCard).getY());
    }

    private Boolean checkForPlayMotion(int idx , int innerIdx){
        Boolean isOk = true;
        for(int i = innerIdx + 1 ; i < playArea.get(idx).size() ; i++){
            if(playArea.get(idx).get(i).getNumber() + 1 != playArea.get(idx).get(i-1).getNumber())
                isOk = false;
        }
        return  isOk && playArea.get(idx).get(innerIdx).getFaceUp();
    }

    private Boolean canLandToPlayArea(Card dragged , Card target){
        return  target.getRed() != dragged.getRed() && target.getNumber() == dragged.getNumber() + 1;
    }

    private Boolean canLandToSuits(Card dragged , Card target){
        return  target.getRed() == dragged.getRed() && target.getNumber() + 1 == dragged.getNumber();
    }

    public boolean isDragging(Card card , float currX , float currY){
        float delteX = Math.abs(card.getX() - currX);
        float deltaY = Math.abs(card.getY() - currY);
        float acceptedDelta = 25;

        return deltaY > acceptedDelta && delteX > acceptedDelta;
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent event) {
            Card card = ((Card) view);
            float elevation = 1;

            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE: {
                    if(isDragging(card , event.getRawX() , event.getRawY())){
                        if (card.getPlay()) {
                            if (checkForPlayMotion(card.getPlayIdx(), card.getInPlayIdx())) {
                                int mainCardIdx = card.getInPlayIdx();
                                for (int i = card.getInPlayIdx(); i < playArea.get(card.getPlayIdx()).size(); i++) {
                                    card = playArea.get(card.getPlayIdx()).get(i);
                                    card.setPosition(event.getRawX() - card.getLayoutParams().width / 2,
                                            event.getRawY() - card.getLayoutParams().height / 2 + card.getLayoutParams().height / 4 * (i - mainCardIdx));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        card.setElevation(elevation++);
                                    }
                                }
                            }
                        } else if (card.getFinished() || card.getHand()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                card.setElevation(elevation);
                            }
                            card.setPosition(event.getRawX() - card.getLayoutParams().width / 2,
                                    event.getRawY() - card.getLayoutParams().height / 2);
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    if (card.getPosition().equals(card.getLastPosition())) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            card.setElevation(0f);
                        }

                        if (card.getHand()) {
                            int lastCardNumber = 0;
                            if (!suitsCard.get(suitIdx.get(card.getName())).empty()) {
                                lastCardNumber = suitsCard.get(suitIdx.get(card.getName())).peek().getNumber();
                            }
                            if (card.getNumber() == lastCardNumber + 1) {
                                cardToSuits(suitIdx.get(card.getName()), card);
                            }
                        } else if (card.getDeck()) {

                            if (deck.size() == 1) {
                                reFillDeck();
                            } else {
                                cardToHand(card);
                            }
                        }
                        else if(card.getPlay()){
                            if(card.getInPlayIdx() == playArea.get(card.getPlayIdx()).size() - 1){
                                int lastCardNumber = 0;
                                if (!suitsCard.get(suitIdx.get(card.getName())).empty()) {
                                    lastCardNumber = suitsCard.get(suitIdx.get(card.getName())).peek().getNumber();
                                }
                                if (card.getNumber() == lastCardNumber + 1) {
                                    cardToSuits(suitIdx.get(card.getName()), card);
                                }
                            }
                        }
                    }
                    else
                    {
                        int playIdx = -1 , suitsIdx = -1;
                        for(int i = 0 ; i < 7 ; i++){
                            if(playArea.get(i).isEmpty()){
                                if(checkIfInside(event.getRawX() , event.getRawY() , playAreaPosition.get(i).first ,
                                        playAreaPosition.get(i).first + cardParams.width , playAreaPosition.get(i).second
                                        , playAreaPosition.get(i).second + cardParams.height )){
                                    playIdx = i;
                                    break;
                                }
                            }
                            else{
                                if(canLandToPlayArea(card , playArea.get(i).get(playArea.get(i).size()-1)) && checkIfInside(event.getRawX() ,
                                        event.getRawY() , playAreaPosition.get(i).first ,
                                        playAreaPosition.get(i).first + cardParams.width, playAreaPosition.get(i).second
                                        , playAreaPosition.get(i).second + cardParams.height + (playArea.get(i).size() - 1) * cardParams.height / 4)){
                                    playIdx = i;
                                    break;
                                }
                            }
                        }

                        if(!card.getPlay() || card.getInPlayIdx() == playArea.get(card.getPlayIdx()).size()-1){
                            if(suitsCard.get(suitIdx.get(card.getName())).isEmpty() ){
                                if (checkIfInside(event.getRawX() ,
                                        event.getRawY() , suitsPosition.get(suitIdx.get(card.getName())).first ,
                                        suitsPosition.get(suitIdx.get(card.getName())).first + cardParams.width ,
                                        suitsPosition.get(suitIdx.get(card.getName())).second ,
                                        suitsPosition.get(suitIdx.get(card.getName())).second + cardParams.height) && card.getNumber() == 1) {
                                    suitsIdx = suitIdx.get(card.getName());
                                }
                            }
                            else{
                                if(canLandToSuits(card , suitsCard.get(suitIdx.get(card.getName())).peek() ) && checkIfInside(event.getRawX() ,
                                        event.getRawY() , suitsPosition.get(suitIdx.get(card.getName())).first ,
                                        suitsPosition.get(suitIdx.get(card.getName())).first + cardParams.width ,
                                        suitsPosition.get(suitIdx.get(card.getName())).second ,
                                        suitsPosition.get(suitIdx.get(card.getName())).second + cardParams.height) ){
                                    suitsIdx = suitIdx.get(card.getName());
                                }
                            }
                        }

                        if (playIdx == -1 && suitsIdx == -1){
                            if(card.getPlay()){
                                for(int i = card.getInPlayIdx() ; i < playArea.get(card.getPlayIdx()).size() ; i++){
                                    card = playArea.get(card.getPlayIdx()).get(i);
                                    card.setPosition(card.getLastPosition());
                                }
                            }
                            else {
                                card.setPosition(card.getLastPosition());
                            }
                        }
                        else if(playIdx != -1){
                            cardToPlay(playIdx , card);
                        }
                        else {
                            cardToSuits(suitsIdx , card);
                        }
                    }
                    break;
                }
            }
            return true;
        }
    };

    private boolean checkIfInside(float touchX , float touchY , float startX , float endX , float startY , float endY) {
        return (touchX >= startX && touchX <= endX && touchY >= startY && touchY <= endY);
    }

    private void reFillDeck(){
        while(!hand.empty()){
            Card card = hand.peek();
            hand.pop();
            card.toDeck(deckPosition);
            deck.push(card);
        }
    }

    private void cardToHand(Card card){
        deck.pop();
        card.toHand(handPosition);
        hand.push(card);
    }

    private void cardToPlay(int targetIdx , Card card) {

        int currIdx = card.getPlayIdx();
        int cardInPlayIdx = card.getInPlayIdx();
        Boolean isPlay = card.getPlay();
        Boolean isFinish = card.getFinished();
        Boolean isHand = card.getHand();

        if(playArea.get(targetIdx).size() == 0 && card.getNumber() != 13){
            if(isPlay) {
                for (int i = cardInPlayIdx; i < playArea.get(currIdx).size(); i++) {
                    card = playArea.get(currIdx).get(i);
                    card.setPosition(card.getLastPosition());
                }
            }
            else if(isHand || isFinish) {
                card.setPosition(card.getLastPosition());
            }

            return;
        }

        if(isPlay){
            for(int i = cardInPlayIdx ; i < playArea.get(currIdx).size() ; i++){
                card = playArea.get(currIdx).get(i);
                card.toPlayArea(targetIdx , playArea.get(targetIdx).size() , new Pair<Float, Float>(playAreaPosition.get(targetIdx).first ,
                        playAreaPosition.get(targetIdx).second + playArea.get(targetIdx).size() * cardParams.height / 4));
                playArea.get(targetIdx).add(card);
            }
            for(int i = playArea.get(currIdx).size() - 1 ; i >= cardInPlayIdx ; i--){
                playArea.get(currIdx).remove(i);
            }
            if(!playArea.get(currIdx).isEmpty()){
                if(!playArea.get(currIdx).get(playArea.get(currIdx).size()-1).getFaceUp()){
                    playArea.get(currIdx).get(playArea.get(currIdx).size()-1).showCard();
                }
            }
        }
        else if(isHand){
            card.toPlayArea(targetIdx , playArea.get(targetIdx).size() , new Pair<Float, Float>(playAreaPosition.get(targetIdx).first ,
                    playAreaPosition.get(targetIdx).second + playArea.get(targetIdx).size() * cardParams.height / 4));
            playArea.get(targetIdx).add(card);
            hand.pop();
        }
        else if(isFinish){
            card.toPlayArea(targetIdx , playArea.get(targetIdx).size() , new Pair<Float, Float>(playAreaPosition.get(targetIdx).first ,
                    playAreaPosition.get(targetIdx).second + playArea.get(targetIdx).size() * cardParams.height / 4));
            playArea.get(targetIdx).add(card);
            suitsCard.get(suitIdx.get(card.getName())).pop();
        }
    }

    private void cardToSuits(int idx  , Card card){
        if(card.getPlay()){
            playArea.get(card.getPlayIdx()).remove(card.getInPlayIdx());
            if(!playArea.get(card.getPlayIdx()).isEmpty()){
                playArea.get(card.getPlayIdx()).get(playArea.get(card.getPlayIdx()).size() - 1).showCard();
            }
        }
        else if(card.getHand()){
            hand.pop();
        }
        card.toSuits(idx , suitsPosition.get(idx));
        suitsCard.get(idx).add(card);
    }

    public void disposeGame()
    {
        while(!storeForDelete.isEmpty()){
            constraintLayout.removeView(storeForDelete.peek());
            storeForDelete.pop();
        }
    }

}