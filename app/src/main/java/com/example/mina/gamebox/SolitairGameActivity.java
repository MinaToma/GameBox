package com.example.mina.gamebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Stack;

public class SolitairGameActivity extends AppCompatActivity {

    Stack<Card> drawCardStack , drawnCardStack ;
    ArrayList<Stack<Card>> playCardStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitair_game);

        drawCardStack = new Stack<Card>();
        drawnCardStack = new Stack<Card>();
        playCardStack = new ArrayList<Stack<Card>>();

        for(int i = 0 ; i < 7 ; i++)
        {
            playCardStack.add(new Stack<Card>()) ;
            playCardStack.get(i).push(new Card(getBaseContext() , "heart" , "1"));
        }
    }

    private View.OnClickListener drawStackOnClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener cardOnClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };



}
