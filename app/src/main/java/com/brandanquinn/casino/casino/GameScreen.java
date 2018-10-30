package com.brandanquinn.casino.casino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.brandanquinn.casino.model.Card;

import org.w3c.dom.Text;

public class GameScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        Card newCard = new Card('H', 'A');
        TextView roundNum = findViewById(R.id.roundNumber);
        roundNum.setText(newCard.getCardString());
    }
}
