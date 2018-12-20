package com.example.mina.gamebox;

import android.hardware.display.DisplayManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SimualtionActivity extends AppCompatActivity {

    SimulationView simulationView;
    Button addDel , balance;
    BST bst;
    EditText nodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simualtion);
        addDel = (Button) findViewById(R.id.addDelButton);

        nodeValue = (EditText) findViewById(R.id.nodeValue);
        addDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bst.insert(Integer.parseInt(nodeValue.getText().toString()));
                bst.setTree();
                simulationView.simulateBST(bst.root);
            }
        });

        balance = (Button) findViewById(R.id.balance);
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bst.balance();
                bst.setTree();
                simulationView.simulateBST(bst.root);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if(hasFocus && simulationView == null) {
            simulationView = (SimulationView) findViewById(R.id.simulateView);
            Display display = getWindowManager(
            ).getDefaultDisplay();
            simulationView.initialize(display);

            simulationView.isBST = true;
            bst = new BST();
        }
    }
}
