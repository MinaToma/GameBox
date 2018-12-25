package com.example.mina.gamebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class startGoFish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_go_fish);
    }

    public void start(View view) {
        Intent intent = new Intent(getBaseContext() , GoFishGame.class);
        startActivity(intent);
    }

}
