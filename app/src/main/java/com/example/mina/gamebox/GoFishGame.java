package com.example.mina.gamebox;

import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

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
    private Button nextPlayer;
    private HashMap<String , Integer> suitIdx;
    private ArrayList<Card> allCards;
    private ArrayList<ArrayList<Card>> players ;
    private int coverCardID , emptyDeckID;
    private ArrayList<String> cardType;
    private Float[] sizeOfFirstPlayerCard= new Float[4];
    private int currentPlayer= 0;
    private boolean goFishNow=false,win=false;
    private int selcteedCard =-1;
    private ImageView goFishView,firstPlayerArrow,secondPlayerArrow,thirdPlayerArrow,fourthPlayerArrow,gameOver,bigWin;
    private Float distBetweenDeckAndPlayersCard;
    private MediaPlayer gameOverSound,winSound,moveCard;

    private RewardedVideoAd rewardedVideoAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_go_fish_game);


        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded () {

            }

            @Override
            public void onRewardedVideoAdOpened () {

            }

            @Override
            public void onRewardedVideoStarted () {

            }

            @Override
            public void onRewardedVideoAdClosed () {
                loadRewardedVideoAd();
            }

            @Override
            public void onRewarded (RewardItem rewardItem){
                //Toast.makeText(getBaseContext(), "Ad triggered reward.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLeftApplication () {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad ( int i){

            }

            @Override
            public void onRewardedVideoCompleted () {

            }
        });
        loadRewardedVideoAd();

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
        initializeImage();
        initializeSound();
        distributeCards();

    }
    private void initializeSound()
    {
        gameOverSound = MediaPlayer.create(this,R.raw.gameover);
        winSound =MediaPlayer.create(this,R.raw.congratulations);
        moveCard = MediaPlayer.create(this,R.raw.movecard);

    }
    private void initializeImage()
    {
        goFishView = (ImageView) findViewById(R.id.GoFishView);
        firstPlayerArrow = (ImageView) findViewById(R.id.arrowFirstPlayer);
        secondPlayerArrow = (ImageView) findViewById(R.id.arrowSecondPlayer);
        thirdPlayerArrow = (ImageView) findViewById(R.id.arrowThirdPlayer);
        fourthPlayerArrow = (ImageView) findViewById(R.id.arrowFourthPlayer);
        gameOver = (ImageView) findViewById(R.id.gameOverImage);
        bigWin=(ImageView) findViewById(R.id.winImage);
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
            sizeOfFirstPlayerCard[0]+=Float.valueOf(150);

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
            sizeOfFirstPlayerCard[1]+=Float.valueOf(150);
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
            sizeOfFirstPlayerCard[2]+=Float.valueOf(150);

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
            sizeOfFirstPlayerCard[3]+=Float.valueOf(150);
            deck.pop();
        }
        resetCards(0,firstPos);
        resetCards(1,secondPos);
        resetCards(2,thirdPos);
        resetCards(3,fourthPos);
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
        distBetweenDeckAndPlayersCard=deckPosition.first-firstPos.first-100;
        nextPlayer = (Button) findViewById(R.id.nextPlayer);

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Card card = ((Card ) view);
            if(currentPlayer==0) {
                boolean ok = false;

                Float gap = Float.valueOf(distBetweenDeckAndPlayersCard / players.get(currentPlayer).size());
                if (gap > 150)
                    gap = Float.valueOf(150);

                sizeOfFirstPlayerCard[currentPlayer] = Float.valueOf(0);
                for (Card cardNow : players.get(currentPlayer)) {
                    if (card == cardNow) {
                        cardNow.setPosition(currentPos.first + (sizeOfFirstPlayerCard[currentPlayer]), currentPos.second - 25);
                        selcteedCard = cardNow.getNumber();
                        ok = true;
                    } else
                        cardNow.setPosition(currentPos.first + (sizeOfFirstPlayerCard[currentPlayer]), currentPos.second);
                    sizeOfFirstPlayerCard[currentPlayer] += Float.valueOf(gap);
                }
                if (ok == false) {
                    selcteedCard = -1;
                }
            }


            if(card.getDeck())
            {
                if(goFishNow && deck.size()>0 && currentPlayer==0)
                {
                    GoFish();
                }

            }

        }

    };
    public void GoFish()
    {
        Card cardNow = deck.lastElement();
        deck.pop();
        cardNow.toUnDeck();
        if (currentPlayer == 0) {
            cardNow.showCard();
        }
        moveCard.start();
        cardNow.setPosition(currentPos.first + sizeOfFirstPlayerCard[currentPlayer], currentPos.second);
        sizeOfFirstPlayerCard[currentPlayer] += Float.valueOf(100);
        players.get(currentPlayer).add(cardNow);
        goFishNow = false;
        calculateCardsToWin();
        nextRound();
    }
    public void calculateCardsToWin()
    {
        int sum=0;
        for(int cards=1 ; cards<=13 ; cards++)
        {
            ArrayList<Card> current= new ArrayList<Card>();
            for(Card Now:players.get(currentPlayer))
            {
                if(Now.getNumber()==cards)
                {
                    current.add(Now);
                }
            }
            if(current.size()==4)
            {
                sum++;
                for(Card Now:current)
                {
                        constraintLayout.removeView(Now);
                        players.get(currentPlayer).remove(Now);
                }
                resetCards(currentPlayer,currentPos);
            }
            current.clear();
        }
        if(GameOver())
        {
            if(win==true)
            {
                bigWin.setVisibility(View.VISIBLE);
                winSound.start();
            }
            else
            {   gameOverSound.start();
                gameOver.setVisibility(View.VISIBLE);
            }
        }
        if(currentPlayer==0)
        {

            TextView text= (TextView) findViewById(R.id.firstPlayerScore);
            String tvValue = text.getText().toString();
            int num=0;
            if (!tvValue.equals("") ) {
             num = Integer.parseInt(tvValue);
            }
            text.setText( String.valueOf(num + sum));

        }
        else if(currentPlayer==1)
        {
            TextView text= (TextView) findViewById(R.id.secondPlayerScore);
            String tvValue = text.getText().toString();
            int num=0;
            if (!tvValue.equals("") ) {
                num = Integer.parseInt(tvValue);
            }
            text.setText( String.valueOf(num + sum));
        }
        else if(currentPlayer==2)
        {
            TextView text= (TextView) findViewById(R.id.thirdPlayerScore);
            String tvValue = text.getText().toString();
            int num=0;
            if (!tvValue.equals("") ) {
                num = Integer.parseInt(tvValue);
            }
            text.setText( String.valueOf(num + sum));
        }
        else
        {
            TextView text= (TextView) findViewById(R.id.fourthPlayerScore);
            String tvValue = text.getText().toString();
            int num=0;
            if (!tvValue.equals("") ) {
                num = Integer.parseInt(tvValue);
            }
            text.setText( String.valueOf(num + sum));
        }
    }
    public void nextRound()
    {
        if(currentPlayer==0)
        {
            firstPlayerArrow.setVisibility(View.INVISIBLE);
        }
        else if(currentPlayer==1)
        {
            secondPlayerArrow.setVisibility(View.INVISIBLE);

        }
        else if(currentPlayer==2)
        {
            thirdPlayerArrow.setVisibility(View.INVISIBLE);
        }
        else
        {
            fourthPlayerArrow.setVisibility(View.INVISIBLE);
        }
        resetCards(currentPlayer,currentPos);
        goFishView.setVisibility(View.INVISIBLE);
        if(GameOver())
        {
            if(win==true)
            {
                winSound.start();
                bigWin.setVisibility(View.VISIBLE);
            }
            else
            {
                gameOverSound.start();
                gameOver.setVisibility(View.VISIBLE);
            }
            startVideo();
        }
        else {
            currentPlayer++;
            currentPlayer %= 4;
            while (players.get(currentPlayer).size()==0) {
                currentPlayer++;
                currentPlayer %= 4;
            }


            if (currentPlayer == 0) {
                firstPlayerArrow.setVisibility(View.VISIBLE);
                nextPlayer.setVisibility(View.INVISIBLE);
                currentPos = firstPos;
            } else if (currentPlayer == 1) {
                secondPlayerArrow.setVisibility(View.VISIBLE);
                nextPlayer.setVisibility(View.VISIBLE);
                currentPos = secondPos;

            } else if (currentPlayer == 2) {
                thirdPlayerArrow.setVisibility(View.VISIBLE);
                nextPlayer.setVisibility(View.VISIBLE);
                currentPos = thirdPos;
            } else {
                fourthPlayerArrow.setVisibility(View.VISIBLE);
                nextPlayer.setVisibility(View.VISIBLE);
                currentPos = fourthPos;
            }
            resetCards(currentPlayer,currentPos);
        }
        goFishNow = false;
    }
    public boolean GameOver()
    {
        if(deck.size()==0 && players.get(0).size()==0 && players.get(1).size()==0 && players.get(2).size()==0 && players.get(3).size()==0)
        {
            TextView text= (TextView) findViewById(R.id.firstPlayerScore);
            String tvValue = text.getText().toString();
            int maxFirstPlayer=Integer.valueOf(tvValue);
            boolean ok=false;
            text= (TextView) findViewById(R.id.secondPlayerScore);
            tvValue = text.getText().toString();
            if(maxFirstPlayer<=Integer.valueOf(tvValue))
                ok=true;
            text= (TextView) findViewById(R.id.thirdPlayerScore);
            tvValue = text.getText().toString();
            if(maxFirstPlayer<=Integer.valueOf(tvValue))
                ok=true;
            text= (TextView) findViewById(R.id.fourthPlayerScore);
            tvValue = text.getText().toString();
            if(maxFirstPlayer<=Integer.valueOf(tvValue))
                ok=true;
            if(ok==false)
            {
                win=true;
            }
            return  true;

        }
        return false;
    }
    public void resetCards(int player,Pair<Float,Float> pos)
    {
        Float gap=Float.valueOf(distBetweenDeckAndPlayersCard/players.get(player).size());
        if(gap>150)
            gap=Float.valueOf(150);

            sizeOfFirstPlayerCard[player]=Float.valueOf(0);
            for(Card cardNow : players.get(player))
            {
                cardNow.setPosition(pos.first+(sizeOfFirstPlayerCard[player]),pos.second);
                sizeOfFirstPlayerCard[player]+=Float.valueOf(gap);
            }
    }
    public void firstPlayerClick(View view) {
        if( currentPlayer!=0 && goFishNow==false &&players.get(0).size()>0)
        {

            if(selcteedCard>=0)
            {
                askPlayerForCards(0,firstPos);
            }
            else
            {
                Toast.makeText(this, "Please select card", Toast.LENGTH_SHORT).show();
            }


        }
        else if(goFishNow==true)
        {
            Toast.makeText(this, "Go Fish", Toast.LENGTH_SHORT).show();
            if(deck.size()==0)
        {
            goFishNow = false;
            nextRound();
        }
        }
        else
        {
            Toast.makeText(this, "Please select another player", Toast.LENGTH_SHORT).show();
        }
    }
    public void secondPlayerClick(View view) {
        if( currentPlayer==0 && goFishNow==false && players.get(1).size()>0)
        {



                if(selcteedCard>=0)
                {
                    askPlayerForCards(1,secondPos);
                }
                else
                {
                    Toast.makeText(this, "Please select card", Toast.LENGTH_SHORT).show();
                }


        }
        else if(goFishNow==true)
        {
            Toast.makeText(this, "Go Fish", Toast.LENGTH_SHORT).show();
            if(deck.size()==0)
            {
                goFishNow = false;
                nextRound();
            }
        }
        else
        {
            Toast.makeText(this, "Please select another player", Toast.LENGTH_SHORT).show();
        }
    }

    public void thirdPlayerClick(View view) {
        if( currentPlayer==0 && goFishNow==false && players.get(2).size()>0)
        {
            if(selcteedCard>=0)
            {
                askPlayerForCards(2,thirdPos);
            }
            else
            {
                Toast.makeText(this, "Please select card", Toast.LENGTH_SHORT).show();
            }


        }
        else if(goFishNow==true)
        {
            Toast.makeText(this, "Go Fish", Toast.LENGTH_SHORT).show();
            if(deck.size()==0)
            {
                goFishNow = false;
                nextRound();
            }
        }
        else
        {
            Toast.makeText(this, "Please select another player", Toast.LENGTH_SHORT).show();
        }
    }

    public void fourthPlayerClick(View view) {
        if( currentPlayer==0 && goFishNow==false && players.get(3).size()>0)
        {
            if(selcteedCard>=0)
            {
               askPlayerForCards(3,fourthPos);
            }
            else
            {
                Toast.makeText(this, "Please select card", Toast.LENGTH_SHORT).show();
            }


        }
        else if(goFishNow==true)
        {
            Toast.makeText(this, "Go Fish", Toast.LENGTH_SHORT).show();
            if(deck.size()==0)
            {
                goFishNow = false;
                nextRound();
            }
        }
        else
        {
            Toast.makeText(this, "Please select another player", Toast.LENGTH_SHORT).show();
        }
    }
    public  void askPlayerForCards(int playerSelceted,Pair<Float,Float> pos)
    {
        ArrayList<Card> currentCads= new ArrayList<Card>();
        for (Card cardNow : players.get(playerSelceted)){

            if(cardNow.getNumber()==selcteedCard)
            {
                currentCads.add(cardNow);
            }
        }
        if(currentCads.size()>0)
        {
            moveCard.start();
            for (Card cardNow : currentCads) {
                players.get(playerSelceted).remove(cardNow);

                if(currentPlayer==0) {
                    cardNow.showCard();
                }
                else
                {
                    cardNow.hideCard();
                }
                cardNow.setPosition(currentPos.first+sizeOfFirstPlayerCard[currentPlayer],currentPos.second);
                sizeOfFirstPlayerCard[currentPlayer]+=Float.valueOf(100);
                players.get(currentPlayer).add(cardNow);
            }
            currentCads.clear();
            resetCards(playerSelceted,pos);
            resetCards(currentPlayer,currentPos);
            selcteedCard=-1;
            if(players.get(playerSelceted).size()==0 && deck.size()>0)
            {
                Card cardNow;

                cardNow = deck.lastElement();
                cardNow.toUnDeck();
                if(playerSelceted==0)
                {
                    cardNow.showCard();
                }
                cardNow.setPosition(pos.first+(sizeOfFirstPlayerCard[playerSelceted]),pos.second);
                players.get(playerSelceted).add(cardNow);
                sizeOfFirstPlayerCard[playerSelceted]+=Float.valueOf(100);

                addCardToConstraint(cardNow);

                deck.pop();
            }

            calculateCardsToWin();
            if(players.get(currentPlayer).size()==0 )
            {
                nextRound();
            }

        }
        else
        {
            goFishView.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "Go Fish", Toast.LENGTH_SHORT).show();
            goFishNow=true;
            if(deck.size()==0)
            {
                nextRound();
            }
        }
    }


    public void nextPlayer(View view) {
        int selectplayer = random.nextInt(4);
        while (selectplayer==currentPlayer || players.get(selectplayer).size()==0)
        {
             selectplayer = random.nextInt(4);
        }
        int cardPos=random.nextInt(players.get(currentPlayer).size());
        selcteedCard=players.get(currentPlayer).get(cardPos).getNumber();
        String currentPlayerName=" ",selectedPlayerName=" " ,cardKind=" ";
        Pair<Float,Float> nextPos;
        if(currentPlayer==1)
        {
            currentPlayerName="Second Player ";
        }
        else if(currentPlayer==2)
        {
            currentPlayerName="Third Player ";
        }
        else if(currentPlayer==3)
        {
            currentPlayerName="Fourth Player ";
        }
        if(selectplayer==1)
        {
            selectedPlayerName="Second Player ";
            nextPos=secondPos;
        }
        else if(selectplayer==2)
        {
            selectedPlayerName="Third Player ";
            nextPos=thirdPos;
        }
        else if(selectplayer==3)
        {
            selectedPlayerName="Fourth Player ";
            nextPos=fourthPos;
        }
        else
        {
            selectedPlayerName="You ";
            nextPos=firstPos;
        }
        if(selcteedCard==11)
        {
            cardKind="Jack";
        }
        else if(selcteedCard==12)
        {
            cardKind="Queen";
        }
        else if(selcteedCard==13)
        {
            cardKind="King";
        }
        else
        {
            cardKind=String.valueOf( selcteedCard);
        }
        Toast.makeText(getApplicationContext(),"The " + currentPlayerName + " Wants " + cardKind + " From " + selectedPlayerName ,Toast.LENGTH_SHORT ).show();
        askPlayerForCards(selectplayer,nextPos);
        if(goFishNow && deck.size()>0)
        {
            GoFish();
            Toast.makeText(getApplicationContext(),"The " + currentPlayerName + "Go Fish",Toast.LENGTH_SHORT).show();
        }
        else if(goFishNow)
        {
            goFishView.setVisibility(View.INVISIBLE);
            nextRound();
        }
        else if(players.get(currentPlayer).size()==0)
        {
            if(deck.size()>0)
            {
                Card cardNow;

                cardNow = deck.lastElement();
                cardNow.toUnDeck();
                if(currentPlayer==0)
                {
                    cardNow.showCard();
                }
                cardNow.setPosition(currentPos.first+(sizeOfFirstPlayerCard[currentPlayer]),currentPos.second);
                players.get(currentPlayer).add(cardNow);
                sizeOfFirstPlayerCard[currentPlayer]+=Float.valueOf(100);

                addCardToConstraint(cardNow);

                deck.pop();
            }

            nextRound();
        }



    }
    private void loadRewardedVideoAd() {
        if (!rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.loadAd("ca-app-pub-9180907491591852/5169239813", new AdRequest.Builder().build());
            rewardedVideoAd.show();
        }
    }

    public void startVideo()
    {
        if(rewardedVideoAd.isLoaded())
        {
            rewardedVideoAd.show();
        }
    }

    @Override
    public void onResume() {
        rewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        rewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
