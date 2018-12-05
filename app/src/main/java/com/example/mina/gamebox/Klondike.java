package com.example.mina.gamebox;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Klondike extends AppCompatActivity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klondike);
        game = null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if(hasFocus && game == null) {
            ConstraintLayout mainConstraintLayout = (ConstraintLayout) findViewById(R.id.mainConstraint);
            game = new Game(getBaseContext() , mainConstraintLayout );
        }
    }
}

