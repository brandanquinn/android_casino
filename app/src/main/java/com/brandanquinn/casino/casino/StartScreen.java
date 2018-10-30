package com.brandanquinn.casino.casino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartScreen extends AppCompatActivity {

    private Button newGame;

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

        newGame = findViewById(R.id.newgame);
        newGame.setOnClickListener(newGameOnClickListener);
    }

    public void newGameClicked() {
        Intent i = new Intent(getApplicationContext(), GameScreen.class);
        startActivity(i);
    }
}