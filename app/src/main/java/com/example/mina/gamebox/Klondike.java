package com.example.mina.gamebox;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Klondike extends AppCompatActivity {

    Game game;
    ConstraintLayout mainConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klondike);
        game = null;

        mainConstraintLayout = (ConstraintLayout) findViewById(R.id.mainConstraint);

        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , SolitaireDifficultyActivity.class);
                startActivity(intent);
            }
        });


        ImageButton newGame = (ImageButton) findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.disposeGame();
                game.initializeGame();
            }

        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if(hasFocus && game == null) {
            game = new Game(getBaseContext(), mainConstraintLayout);
        }
    }
}