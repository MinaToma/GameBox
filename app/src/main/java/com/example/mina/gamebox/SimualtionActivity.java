package com.example.mina.gamebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        simulationView = (SimulationView) findViewById(R.id.simulateView);

        node = new ArrayList<>();
        node.add(new Node(10 , 10 , 100));

        addDel = (Button) findViewById(R.id.addDelButton);
        addDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulationView.simulate(node);
            }
        });
    }
}
