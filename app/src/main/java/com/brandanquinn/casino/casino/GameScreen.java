package com.brandanquinn.casino.casino;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brandanquinn.casino.model.Card;
import com.brandanquinn.casino.model.Player;
import com.brandanquinn.casino.model.Round;
import com.brandanquinn.casino.model.Table;
import com.brandanquinn.casino.model.Tournament;
import com.brandanquinn.casino.view.Display;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {
    private static Display gameDisplay;
    private static Tournament tourney;
    private static boolean moveBeingMade;
    private static char moveType;
    private static String cardSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        gameDisplay = new Display(this);
        this.tourney = new Tournament();
        moveBeingMade = false;
        moveType = ' ';
        cardSelected = " ";
        tourney.startRound(true);
        Round currentRound = tourney.getCurrentRound();

        setupMoveButtons();

        setupGameplay(currentRound);


    }

    /**
     * Static function used for the model to alert the Controller to update the View.
     * @param gamePlayers, ArrayList of current game players
     * @param gameTable, Table object to track cards/builds on the table
     * @param roundNum, int representing current round number
     */
    public static void updateActivity(ArrayList<Player> gamePlayers, Table gameTable, int roundNum) {
        gameDisplay.updateView(gamePlayers, gameTable, roundNum, tourney.getCurrentRound().whoIsPlaying());
    }

    /**
     * Used to get move selection from user clicking cards on the table.
     * @return
     */
    public Pair<String, Character> getMoveSelection() {
        moveBeingMade = true;
        ArrayList<ImageButton> myHand = gameDisplay.getHumanButtons();
        ArrayList<ImageButton> tableButtons = gameDisplay.getTableButtons();


        for (int i = 0; i < myHand.size(); i++) {
            myHand.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCardSelected((String)v.getTag());
                }
            });
        }


        return new Pair<>(cardSelected, moveType);
    }

    public static void setCardSelected(String cardString) {
        cardSelected = cardString;
    }

    /**
     * Initialize move selection buttons.
     */
    public void setupMoveButtons() {
        final TextView moveDisplay = findViewById(R.id.moveDisplay);
        Button trail = findViewById(R.id.trailButton);
        trail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveType = 't';
                moveDisplay.setText("Trail selected.");
            }
        });
        Button capture = findViewById(R.id.captureButton);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveType = 'c';
                moveDisplay.setText("Capture selected.");
            }
        });
    }

    public void setupGameplay(final Round currentRound) {
        final Player gamePlayer = currentRound.getPlayer();
        Button makeMove = findViewById(R.id.makeMove);
        makeMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gamePlayer.getPlayerString() == "Computer") {
                    currentRound.playTurn(gamePlayer);
                }
            }
        });
    }
}
