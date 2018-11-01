package com.brandanquinn.casino.casino;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.brandanquinn.casino.model.Card;
import com.brandanquinn.casino.model.Deck;
import com.brandanquinn.casino.model.Player;
import com.brandanquinn.casino.model.Table;
import com.brandanquinn.casino.view.Display;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {
    private Deck gameDeck;
    private Display gameDisplay;
    private Table gameTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        this.gameDeck = new Deck();
        this.gameDisplay = new Display();
        this.gameTable = new Table();

        TextView roundNum = findViewById(R.id.roundNumber);
        TextView playerScore = findViewById(R.id.humanScore);


        ArrayList<Player> gamePlayers = new ArrayList<>();
        gamePlayers.add(new Player());
        gamePlayers.add(new Player());

        for (int i = 0; i < 4; i++) {
            gamePlayers.get(0).addToHand(this.gameDeck.drawCard());
        }

        gameDisplay.updateView(gamePlayers, this.gameTable);

    }

    public Context getViewContext() {
        return this;
    }
}
