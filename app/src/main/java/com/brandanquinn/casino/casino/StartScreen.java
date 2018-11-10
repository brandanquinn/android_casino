package com.brandanquinn.casino.casino;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {

    private static int coinTossVal;

    private View.OnClickListener newGameOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            newGameClicked();
        }
    };
    private View.OnClickListener loadGameOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadGameClicked();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        coinTossVal = ' ';

        final Button newGame = findViewById(R.id.newgame);
        newGame.setOnClickListener(newGameOnClickListener);
        newGame.setClickable(false);

        final Button loadGame = findViewById(R.id.loadgame);
        loadGame.setOnClickListener(loadGameOnClickListener);
        loadGame.setClickable(false);

        final Button heads = findViewById(R.id.headsButton);
        final Button tails = findViewById(R.id.tailsButton);

        heads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCoinTossVal(0);
                TextView debug = findViewById(R.id.debugging);
                debug.setText("You called heads!");
                heads.setVisibility(View.GONE);
                tails.setVisibility(View.GONE);
                newGame.setClickable(true);
                newGame.setVisibility(View.VISIBLE);
                loadGame.setClickable(true);
            }
        });

        tails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCoinTossVal(1);
                TextView debug = findViewById(R.id.debugging);
                debug.setText("You called tails!");
                heads.setVisibility(View.GONE);
                tails.setVisibility(View.GONE);
                newGame.setClickable(true);
                newGame.setVisibility(View.VISIBLE);
                loadGame.setClickable(true);
            }
        });
    }

    /**
     * Starts a new game when proper button is clicked.
     */
    public void newGameClicked() {
        Intent i = new Intent(getApplicationContext(), GameScreen.class);

        Bundle myBundle = new Bundle();
        myBundle.putBoolean("load", false);

        i.putExtras(myBundle);
        startActivity(i);
    }

    /**
     * Attempts to load a saved game when proper button is clicked.
     */
    public void loadGameClicked() {
        Intent loadGame = new Intent(getApplicationContext(), GameScreen.class);

        Bundle myBundle = new Bundle();
        myBundle.putBoolean("load", true);

        loadGame.putExtras(myBundle);
        startActivity(loadGame);
    }

    /**
     * Sets the private member variable for coin toss to whatever user selected.
     * @param coinTossVal, int value based on what was called for coin toss.
     */
    public void setCoinTossVal(int coinTossVal) {
        this.coinTossVal = coinTossVal;
    }

    /**
     * Used for the Tournament class to statically get coinTossValue from previous Activity
     * @return int value based on what was called for coin toss
     */
    public static int getCoinToss() {
        return coinTossVal;
    }
}