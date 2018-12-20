package com.example.mina.gamebox;

import android.hardware.display.DisplayManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class SimualtionActivity extends AppCompatActivity {

    SimulationView simulationView;
    Button addDel;
    ArrayList<Node> node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simualtion);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if(hasFocus && simulationView == null) {
            simulationView = (SimulationView) findViewById(R.id.simulateView);
            Display display = getWindowManager().getDefaultDisplay();
            simulationView.initialize(display);
        }
    }
}
