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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        this.coinTossVal = ' ';

        final Button newGame = findViewById(R.id.newgame);
        newGame.setOnClickListener(newGameOnClickListener);
        newGame.setClickable(false);

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
            }
        });
    }

    public void newGameClicked() {
        Intent i = new Intent(getApplicationContext(), GameScreen.class);
        startActivity(i);
    }

    public void setCoinTossVal(int coinTossVal) {
        this.coinTossVal = coinTossVal;
    }

    public static int getCoinToss() {
        return coinTossVal;
    }
}