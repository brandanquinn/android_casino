package com.brandanquinn.casino.casino;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.brandanquinn.casino.model.Player;
import com.brandanquinn.casino.model.Round;
import com.brandanquinn.casino.model.Table;
import com.brandanquinn.casino.model.Tournament;
import com.brandanquinn.casino.view.Display;

import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {
    private static Context context;
    private static Display gameDisplay;
    private static Tournament tourney;
    private static String moveType;
    private static String cardSelectedFromHand;
    private static String cardPlayedIntoBuild;
    private static ArrayList<String> cardsSelectedFromTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        gameDisplay = new Display(this);
        this.tourney = new Tournament();
        moveType = "";
        cardSelectedFromHand = "";
        cardPlayedIntoBuild = "";
        cardsSelectedFromTable = new ArrayList<>();
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
    public static void updateActivity(boolean roundIsOver) {
        if (roundIsOver) {
            // send tournament information and start new round
            tourney.endRound();
            if (!tourney.getWinningPlayer().isEmpty()) {
                // Tournament is over -> move to next Activity.
            }
        }

        gameDisplay.updateView(tourney.getCurrentRound().getGamePlayers(), tourney.getCurrentRound().getGameTable(), tourney.getCurrentRound().getRoundNum(), tourney.getCurrentRound().whoIsPlaying());
        setupCardButtons();
        setupPiles(tourney.getCurrentRound().getGamePlayers());

    }

    /**
     * Static setter for card selected from hand.
     * @param cardString, String tag from card button selected.
     */
    public static void setCardSelectedFromHand(String cardString) {
        cardSelectedFromHand = cardString;
    }

    /**
     * Static setter for card played into build.
     * @param cardString, String tag from card button selected.
     */
    public static void setCardPlayedIntoBuild(String cardString) {
        cardPlayedIntoBuild = cardString;
    }

    /**
     * Static setter for card selected from table.
     * @param cardString, String tag from card button selected.
     */
    public static void addToCardSelectedFromTable(String cardString) {
        if (!cardsSelectedFromTable.contains(cardString)) {
            cardsSelectedFromTable.add(cardString);
        }
    }

    public static String stringifyTableSelection() {
        String tableSelectedString = "Table cards selected: ";
        for (int i = 0; i < cardsSelectedFromTable.size(); i++) {
            tableSelectedString += cardsSelectedFromTable.get(i) + " ";
        }

        return tableSelectedString;
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
                cardSelectedFromHand = "";
                cardsSelectedFromTable.clear();
                cardPlayedIntoBuild = "";
                moveType = "trail";
                moveDisplay.setText("Trail selected.");
            }
        });
        Button capture = findViewById(R.id.captureButton);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardSelectedFromHand = "";
                cardsSelectedFromTable.clear();
                cardPlayedIntoBuild = "";
                moveType = "capture";
                moveDisplay.setText("Capture selected.");
            }
        });
        Button build = findViewById(R.id.buildButton);
        build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardSelectedFromHand = "";
                cardsSelectedFromTable.clear();
                cardPlayedIntoBuild = "";
                moveType = "build";
                moveDisplay.setText("Build selected");
            }
        });
        Button increase = findViewById(R.id.increaseButton);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardSelectedFromHand = "";
                cardsSelectedFromTable.clear();
                cardPlayedIntoBuild = "";
                moveType = "increase";
                moveDisplay.setText("Increase build selected.");
            }
        });
        Button help = findViewById(R.id.helpButton);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardSelectedFromHand = "";
                cardsSelectedFromTable.clear();
                cardPlayedIntoBuild = "";
                moveType = "help";
                moveDisplay.setText("Selected to get help from AI.");
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
                    if (gamePlayers.get(0).getPlayerIdentity().equals("Computer")) {
                        // If current player is a computer
                        Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        // If current player is a human
                        if (moveType.equals("trail") && !cardSelectedFromHand.isEmpty()) {
                            // Make trail move.
                            gamePlayers.get(0).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(0).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), Toast.LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("build") && !cardSelectedFromHand.isEmpty() && !cardsSelectedFromTable.isEmpty()) {
                            // Setup move pair for Human
                            gamePlayers.get(0).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(0).setCardPlayedIntoBuild(cardPlayedIntoBuild);
                            gamePlayers.get(0).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(0).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), Toast.LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("capture") && !cardSelectedFromHand.isEmpty() && !cardsSelectedFromTable.isEmpty()) {
                            // Setup move pair for Human
                            gamePlayers.get(0).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(0).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(0).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), Toast.LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("increase") && !cardSelectedFromHand.isEmpty() &&!cardPlayedIntoBuild.isEmpty() && !cardsSelectedFromTable.isEmpty()) {
                            // Setup move pair for Human
                            gamePlayers.get(0).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(0).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(0).setCardPlayedIntoBuild(cardPlayedIntoBuild);
                            gamePlayers.get(0).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), Toast.LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("help")) {
                            // Human getting help from AI
                            gamePlayers.get(0).setMoveSelected("help");
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                } else {
                    if (gamePlayers.get(1).getPlayerIdentity().equals("Computer")) {
                        // If current player is a computer
                        Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        // If current player is a human
                        if (moveType.equals("trail") && !cardSelectedFromHand.isEmpty()) {
                            // Make trail move.
                            gamePlayers.get(1).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(1).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), Toast.LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("build") && !cardSelectedFromHand.isEmpty() && !cardsSelectedFromTable.isEmpty()) {
                            // Setup move pair for Human
                            gamePlayers.get(1).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(1).setCardPlayedIntoBuild(cardPlayedIntoBuild);
                            gamePlayers.get(1).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(1).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), Toast.LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("capture") && !cardSelectedFromHand.isEmpty() && !cardsSelectedFromTable.isEmpty()) {
                            // Setup move pair for Human
                            gamePlayers.get(1).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(1).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(1).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), Toast.LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("increase") && !cardSelectedFromHand.isEmpty() && !cardPlayedIntoBuild.isEmpty() && !cardsSelectedFromTable.isEmpty()) {
                            // Setup move pair for Human
                            gamePlayers.get(1).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(1).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(1).setCardPlayedIntoBuild(cardPlayedIntoBuild);
                            gamePlayers.get(1).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), Toast.LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("help")) {
                            // Human getting help from AI
                            gamePlayers.get(1).setMoveSelected("help");
                            Toast toast = Toast.makeText(getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
                clearSelection();
            }
        });

        final Button clearSelection = findViewById(R.id.clearSelection);
        clearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelection();
            }
        });
    }

    /**
     * Sets up the onClickListeners for the card buttons in hand and on the table
     */
    private static void setupCardButtons() {
        ArrayList<ImageButton> myHand = gameDisplay.getHumanButtons();
        ArrayList<ImageButton> tableButtons = gameDisplay.getTableButtons();
        ArrayList<Button> buildButtons = gameDisplay.getBuildButtons();

        final TextView cardsSelected = ((Activity) context).findViewById(R.id.cardsSelected);

        for (int i = 0; i < myHand.size(); i++) {
            myHand.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((moveType.equals("build") || moveType.equals("increase") ) && !cardSelectedFromHand.isEmpty()) {
                        setCardPlayedIntoBuild((String)v.getTag());
                        Toast toast = Toast.makeText(context, cardPlayedIntoBuild + " selected to play into build.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        setCardSelectedFromHand((String) v.getTag());
                        Toast toast = Toast.makeText(context, cardSelectedFromHand + " selected.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }

        if (!moveType.equals("trail")) {

            for (int i = 0; i < tableButtons.size(); i++) {
                tableButtons.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToCardSelectedFromTable((String)v.getTag());
                        Toast toast = Toast.makeText(context, v.getTag() + " selected.", Toast.LENGTH_SHORT);
                        toast.show();

                        cardsSelected.setText(stringifyTableSelection());
                    }
                });
            }

            for (int i = 0; i < buildButtons.size(); i++) {
                buildButtons.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToCardSelectedFromTable((String)v.getTag());

                        Toast toast = Toast.makeText(context, v.getTag() + " selected.", Toast.LENGTH_SHORT);
                        toast.show();

                        cardsSelected.setText(stringifyTableSelection());

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
        cardPlayedIntoBuild = "";
        cardsSelectedFromTable.clear();
        TextView moveDisplay = findViewById(R.id.moveDisplay);
        moveDisplay.setText("");

        TextView cardsSelection = findViewById(R.id.cardsSelected);
        cardsSelection.setText("");
    }

    /**
     * Sets up onClickListeners for piles
     * @param gamePlayers, ArrayList of Player objects of the game.
     */
    private static void setupPiles(final ArrayList<Player> gamePlayers) {
        gameDisplay.getHumanPile().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context, gamePlayers.get(0).getPileString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });

        gameDisplay.getComputerPile().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context, gamePlayers.get(1).getPileString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
