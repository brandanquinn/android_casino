package com.brandanquinn.casino.casino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.brandanquinn.casino.model.Card;
import com.brandanquinn.casino.model.Deck;

import org.w3c.dom.Text;

public class GameScreen extends AppCompatActivity {
    private Deck gameDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        this.gameDeck = new Deck();

        TextView roundNum = findViewById(R.id.roundNumber);

        roundNum.setText(this.gameDeck.drawCard().getCardString());

    }
}
