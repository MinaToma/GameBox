package com.example.mina.gamebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class startFlappyBirdGame extends AppCompatActivity {

    flappyBirdGameView gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_flappy_bird_game);
        gameView = new flappyBirdGameView(this);
        setContentView(gameView);
    }
}
