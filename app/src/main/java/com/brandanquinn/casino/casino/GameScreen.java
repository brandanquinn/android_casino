package com.brandanquinn.casino.casino;

import android.content.Context;
import android.media.Image;
import android.media.ImageReader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.brandanquinn.casino.model.Card;
import com.brandanquinn.casino.model.Human;
import com.brandanquinn.casino.model.Player;
import com.brandanquinn.casino.model.Round;
import com.brandanquinn.casino.model.Table;
import com.brandanquinn.casino.model.Tournament;
import com.brandanquinn.casino.view.Display;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {
    private static Context context;
    private static Display gameDisplay;
    private static Tournament tourney;
    private static boolean moveBeingMade;
    private static String moveType;
    private static String cardSelectedFromHand;
    private static String cardSelectedFromTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        gameDisplay = new Display(this);
        this.tourney = new Tournament();
        moveBeingMade = false;
        moveType = "";
        cardSelectedFromHand = "";
        cardSelectedFromTable = "";
        tourney.startRound(true);
        Round currentRound = tourney.getCurrentRound();

        setupMoveButtons();
        setupCardButtons();
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
        setupCardButtons();
    }

    /**
     * Static setter for card selected from hand.
     * @param cardString, String tag from card button selected.
     */
    public static void setCardSelectedFromHand(String cardString) {
        cardSelectedFromHand = cardString;
    }

    /**
     * Static setter for card selected from table.
     * @param cardString, String tag from card button selected.
     */
    public static void setCardSelectedFromTable(String cardString) {
        cardSelectedFromTable = cardString;
    }

    /**
     * Initialize move selection buttons.
     */
    private void setupMoveButtons() {
        final TextView moveDisplay = findViewById(R.id.moveDisplay);
        Button trail = findViewById(R.id.trailButton);
        trail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveType = "trail";
                moveDisplay.setText("Trail selected.");
            }
        });
        Button capture = findViewById(R.id.captureButton);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveType = "capture";
                moveDisplay.setText("Capture selected.");
            }
        });
    }

    /**
     * Initializes the button selected to make a move; move selection is handled based on type of player
     * @param currentRound, Round object to access game play features.
     */
    private void setupGameplay(final Round currentRound) {
        final ArrayList<Player> gamePlayers = currentRound.getGamePlayers();
        Button makeMove = findViewById(R.id.makeMove);
        makeMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gamePlayers.get(0).getIsPlaying()) {
                    if (gamePlayers.get(0).getPlayerString() == "Computer") {
                        // If current player is a computer
                        Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        // If current player is a human
                        if (moveType == "trail" && !cardSelectedFromHand.isEmpty()) {
                            // Make trail move.
                            gamePlayers.get(0).setCardSelected(cardSelectedFromHand);
                            gamePlayers.get(0).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                } else {
                    if (gamePlayers.get(1).getPlayerString() == "Computer") {
                        // If current player is a computer
                        Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        // If current player is a human
                        if (moveType == "trail" && !cardSelectedFromHand.isEmpty()) {
                            // Make trail move.
                            gamePlayers.get(1).setCardSelected(cardSelectedFromHand);
                            gamePlayers.get(1).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
                clearSelection();
            }
        });
    }

    /**
     * Sets up the onClickListeners for the card buttons in hand and on the table
     */
    private static void setupCardButtons() {
        moveBeingMade = true;
        ArrayList<ImageButton> myHand = gameDisplay.getHumanButtons();
        ArrayList<ImageButton> tableButtons = gameDisplay.getTableButtons();


        for (int i = 0; i < myHand.size(); i++) {
            myHand.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCardSelectedFromHand((String)v.getTag());
                    Toast toast = Toast.makeText(context, cardSelectedFromHand + "selected.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }

        if (moveType != "trail") {

            for (int i = 0; i < tableButtons.size(); i++) {
                tableButtons.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setCardSelectedFromTable((String)v.getTag());
                    }
                });
            }
        }
    }

    /**
     * Clears out variables set during player's turn.
     */
    private void clearSelection() {
        moveType = "";
        cardSelectedFromHand = "";
        cardSelectedFromTable = "";
        TextView moveDisplay = findViewById(R.id.moveDisplay);
        moveDisplay.setText("");
    }
}
