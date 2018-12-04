package com.example.mina.gamebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FlappyBirdGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flappy_bird_game);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, startFlappyBirdGame.class);
        startActivity(intent);
        finish();
    }
}
