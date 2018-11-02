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
import com.brandanquinn.casino.model.Tournament;
import com.brandanquinn.casino.view.Display;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {
    private static Display gameDisplay;
    private ArrayList<Player> gamePlayers;
    private Tournament tourney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        this.gameDisplay = new Display(this);
        this.tourney = new Tournament();
        tourney.startRound(true);
    }

    /*
    Function Name: updateActivity
    Purpose: Called from the model to update the necessary view components through the controller
    Parameters:
        ArrayList<Player> gamePlayers, Players of the game
        Table gameTable, Table object containing all cards/builds on the table
        int roundNum, Current round number
    Return Value: None
    Local Variables:
    Algorithm:
    Assistance Received: None
     */
    public static void updateActivity(ArrayList<Player> gamePlayers, Table gameTable, int roundNum) {
        gameDisplay.updateView(gamePlayers, gameTable, roundNum);
    }
}
