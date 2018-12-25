package com.example.mina.gamebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void startSolitaire(View view) {
        Intent intent = new Intent(getBaseContext() , SolitaireStartpage.class);
        startActivity(intent);
    }

    public void startGoFish(View view) {
        Intent intent = new Intent(getBaseContext() , GoFishGame.class);
        startActivity(intent);
    }

    public void startFlappyBird(View view) {
        Intent intent = new Intent(getBaseContext() , FlappyBirdGame.class);
        startActivity(intent);
    }
    public void startSimulation(View view) {
        Intent intent = new Intent(getBaseContext() , SimualtionActivity.class);
        startActivity(intent);
    }
}
