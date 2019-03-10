package com.example.mina.gamebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MobileAds.initialize(this, "ca-app-pub-9180907491591852~4585951732");




    }

    public void startSolitaire(View view) {
        Intent intent = new Intent(getBaseContext() , SolitaireStartpage.class);
        startActivity(intent);
    }

    public void startGoFish(View view) {
        Intent intent = new Intent(getBaseContext() , startGoFish.class);
        startActivity(intent);
    }

    public void startFlappyBird(View view) {
        Intent intent = new Intent(getBaseContext() , FlappyBirdGame.class);
        startActivity(intent);
    }

    public void startSimulation(View view) {
        Intent intent = new Intent(getBaseContext() , SimulationActivity.class);
        startActivity(intent);
    }
}
