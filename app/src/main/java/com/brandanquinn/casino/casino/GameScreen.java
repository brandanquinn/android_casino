package com.brandanquinn.casino.casino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;

import com.brandanquinn.casino.model.Player;
import com.brandanquinn.casino.model.Table;
import com.brandanquinn.casino.model.Tournament;
import com.brandanquinn.casino.view.Display;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {
    private static Display gameDisplay;
    private Tournament tourney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        this.gameDisplay = new Display(this);
        this.tourney = new Tournament();
        tourney.startRound(true);
    }

    /**
     * Static function used for the model to alert the Controller to update the View.
     * @param gamePlayers, ArrayList of current game players
     * @param gameTable, Table object to track cards/builds on the table
     * @param roundNum, int representing current round number
     */
    public static void updateActivity(ArrayList<Player> gamePlayers, Table gameTable, int roundNum) {
        gameDisplay.updateView(gamePlayers, gameTable, roundNum);
    }

    /**
     * Used to get move selection from user clicking cards on the table.
     * @return
     */
    public static Pair<String, Character> getMoveSelection() {
        Pair<String, Character> moveSelectionPair = new Pair<String, Character>("CA", 't');


        return moveSelectionPair;
    }
}
