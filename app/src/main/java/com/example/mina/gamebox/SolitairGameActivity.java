package com.example.mina.gamebox;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    Stack<Card> drawCardStack , drawnCardStack ;
    ArrayList<Stack<Card>> playCardStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitair_game);

        drawCardStack = new Stack<Card>();
        drawnCardStack = new Stack<Card>();
        playCardStack = new ArrayList<Stack<Card>>();
        RelativeLayout playRelativeView = (RelativeLayout) findViewById(R.id.playCardRelativeView);

        for(int i = 0 ; i < 7 ; i++) {
            playCardStack.add(new Stack<Card>());
            playCardStack.get(i).push(new Card(getBaseContext(), "heart", 55, 100));
            playCardStack.get(i).peek().setX(i * 100 + 50 * i);

            playCardStack.get(i).peek().setOnClickListener(cardOnClickListner);

            playCardStack.get(i).peek().setImageResource(R.drawable.ic_launcher_background);
            playRelativeView.addView(playCardStack.get(i).peek());

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
            Toast.makeText(getBaseContext() , (String)"Mina",
                    Toast.LENGTH_LONG).show();

            Log.i("Mina" , "I'm Clicked");
        }
    };



}
