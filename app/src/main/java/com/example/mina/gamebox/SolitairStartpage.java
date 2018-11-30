package com.example.mina.gamebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SolitairStartpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitair_startpage);


        Button StartButton = (Button) findViewById(R.id.StartButton);
       // StartButton.setOnClickListener(
                //new Button.OnClickListener() {
                 //   @Override
                  //  public void onClick(View v) {
                       // Intent intent = new Intent(SolitairStartpage.this, SolitairDifficultyActivity.class);
                        //startActivity(intent);
                    }
//                }

        //);
    //}


}
