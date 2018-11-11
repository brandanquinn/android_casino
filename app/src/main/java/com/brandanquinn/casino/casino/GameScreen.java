package com.brandanquinn.casino.casino;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brandanquinn.casino.model.Player;
import com.brandanquinn.casino.model.Round;
import com.brandanquinn.casino.model.Tournament;
import com.brandanquinn.casino.view.Display;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class GameScreen extends AppCompatActivity {
    private static Context context;
    private static Display gameDisplay;
    private static Tournament tourney;
    private static String moveType;
    private static String cardSelectedFromHand;
    private static String cardPlayedIntoBuild;
    private static ArrayList<String> cardsSelectedFromTable;
    private static boolean moveBeingMade;
    private static String saveFileSelected;
    private static boolean loadAttemped;
    private static String saveFileName;

    private static Button makeMove;
    private static Button clearSelection;
    private static TextView moveDisplay;
    private static TextView cardsSelection;
    private static AlertDialog selectFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        gameDisplay = new Display(this);
        tourney = new Tournament();
        moveType = "";
        cardSelectedFromHand = "";
        cardPlayedIntoBuild = "";
        cardsSelectedFromTable = new ArrayList<>();
        moveBeingMade = false;
        saveFileSelected = "";
        loadAttemped = false;
        saveFileName = "";

        makeMove = findViewById(R.id.makeMove);
        clearSelection = findViewById(R.id.clearSelection);
        moveDisplay = findViewById(R.id.moveDisplay);
        cardsSelection = findViewById(R.id.cardsSelected);

        Bundle loadState = getIntent().getExtras();

        if (loadState.getBoolean("load")) {
            getFileList();
        } else {
            tourney.startRound(true);
            setupMoveButtons();
            setupCardButtons();
            setupGameplay(tourney.getCurrentRound());
        }
    }

    /**
     * Static function used for the model to alert the Controller to update the View.
     * @param roundIsOver, boolean representing whether or not the current round is over.
     */
    public static void updateActivity(boolean roundIsOver) {
        if (roundIsOver) {
            // send tournament information and start new round
            tourney.endRound();
            displayEndOfRound();
            setupGameplay(tourney.getCurrentRound());
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

    /**
     * Stringify cards selected from table
     * @return String with all cards / builds currently selected from the table.
     */
    public static String stringifyTableSelection() {
        String tableSelectedString = "Table cards selected: ";
        for (int i = 0; i < cardsSelectedFromTable.size(); i++) {
            tableSelectedString += cardsSelectedFromTable.get(i) + " ";
        }

        return tableSelectedString;
    }

    /**
     * Called to generate AlertDialog with save file options.
     */
    private void getFileList() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "serialization");
        if (!file.exists()) {
            file.mkdir();
        }

        ArrayList<File> saveFiles = getListFiles(file);

        LinearLayout layout = gameDisplay.getFileSelection(saveFiles);

        generateSaveButtons(gameDisplay.getFileButtons());

        AlertDialog.Builder saveFileSelection = new AlertDialog.Builder(context)
                .setTitle("Load File Selection")
                .setView(layout)
                .setPositiveButton("Start" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadAttemped = true;
                        if (saveFileSelected.isEmpty() && loadAttemped) {
                            System.exit(1);
                        } else {
                            // load saved file
                            try {
                                tourney.loadSavedGame(saveFileSelected);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        setupMoveButtons();
                        setupCardButtons();
                        setupGameplay(tourney.getCurrentRound());

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1);
                    }
                });

        selectFile = saveFileSelection.show();

    }

    private void generateSaveButtons(ArrayList<Button> saveButtons) {
        for (int i = 0; i < saveButtons.size(); i++) {
            saveButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveFileSelected = (String)v.getTag();
                }
            });
        }
    }

    /**
     * Generates ArrayList of files in a given parent directory.
     * @param parentDir, File directory object
     * @return ArrayList of files within directory.
     */
    private ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".txt")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
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
        Button save = findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText saveInput = new EditText(context);

                AlertDialog.Builder saveGameBuilder = new AlertDialog.Builder(context)
                        .setTitle("Save Game File")
                        .setMessage("Enter a name for your save file: ")
                        .setView(saveInput)
                        .setNegativeButton("Save Game", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!saveInput.getText().toString().equals("")) {
                                    saveFileName = saveInput.getText().toString();
                                    tourney.getCurrentRound().saveGame(saveFileName);
                                    System.exit(1);
                                }
                                dialog.cancel();
                            }
                        });

                AlertDialog saveGame = saveGameBuilder.show();


            }
        });
    }

    /**
     * Initializes the button selected to make a move; move selection is handled based on type of player
     * @param currentRound, Round object to access game play features.
     */
    private static void setupGameplay(final Round currentRound) {
        final ArrayList<Player> gamePlayers = currentRound.getGamePlayers();

        makeMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gamePlayers.get(0).getIsPlaying()) {
                    if (gamePlayers.get(0).getPlayerIdentity().equals("Computer") && !moveBeingMade) {
                        // If current player is a computer
                        moveBeingMade = true;
                        Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), LENGTH_LONG);
                        toast.show();
                    } else {
                        // If current player is a human
                        if (moveType.equals("trail") && !cardSelectedFromHand.isEmpty() && !moveBeingMade) {
                            // Make trail move.
                            moveBeingMade = true;
                            gamePlayers.get(0).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(0).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("build") && !cardSelectedFromHand.isEmpty() && !cardsSelectedFromTable.isEmpty() && !moveBeingMade) {
                            // Setup move pair for Human
                            moveBeingMade = true;
                            gamePlayers.get(0).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(0).setCardPlayedIntoBuild(cardPlayedIntoBuild);
                            gamePlayers.get(0).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(0).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("capture") && !cardSelectedFromHand.isEmpty() && !cardsSelectedFromTable.isEmpty() && !moveBeingMade) {
                            // Setup move pair for Human
                            moveBeingMade = true;
                            gamePlayers.get(0).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(0).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(0).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("increase") && !cardSelectedFromHand.isEmpty() &&!cardPlayedIntoBuild.isEmpty() && !cardsSelectedFromTable.isEmpty() && !moveBeingMade) {
                            // Setup move pair for Human
                            moveBeingMade = true;
                            gamePlayers.get(0).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(0).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(0).setCardPlayedIntoBuild(cardPlayedIntoBuild);
                            gamePlayers.get(0).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("help") && !moveBeingMade) {
                            // Human getting help from AI
                            moveBeingMade = true;
                            gamePlayers.get(0).setMoveSelected("help");
                            Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(0)), LENGTH_LONG);
                            toast.show();
                        }
                    }
                } else {
                    if (gamePlayers.get(1).getPlayerIdentity().equals("Computer")) {
                        // If current player is a computer
                        Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), LENGTH_LONG);
                        toast.show();
                    } else {
                        // If current player is a human
                        if (moveType.equals("trail") && !cardSelectedFromHand.isEmpty()) {
                            // Make trail move.
                            gamePlayers.get(1).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(1).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("build") && !cardSelectedFromHand.isEmpty() && !cardsSelectedFromTable.isEmpty()) {
                            // Setup move pair for Human
                            gamePlayers.get(1).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(1).setCardPlayedIntoBuild(cardPlayedIntoBuild);
                            gamePlayers.get(1).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(1).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("capture") && !cardSelectedFromHand.isEmpty() && !cardsSelectedFromTable.isEmpty()) {
                            // Setup move pair for Human
                            gamePlayers.get(1).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(1).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(1).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("increase") && !cardSelectedFromHand.isEmpty() && !cardPlayedIntoBuild.isEmpty() && !cardsSelectedFromTable.isEmpty()) {
                            // Setup move pair for Human
                            gamePlayers.get(1).setCardSelectedFromHand(cardSelectedFromHand);
                            gamePlayers.get(1).setCardsSelectedFromTable(cardsSelectedFromTable);
                            gamePlayers.get(1).setCardPlayedIntoBuild(cardPlayedIntoBuild);
                            gamePlayers.get(1).setMoveSelected(moveType);
                            Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), LENGTH_LONG);
                            toast.show();
                        } else if (moveType.equals("help")) {
                            // Human getting help from AI
                            gamePlayers.get(1).setMoveSelected("help");
                            Toast toast = Toast.makeText(context.getApplicationContext(), currentRound.playTurn(gamePlayers.get(1)), LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
                clearSelection();
            }
        });


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
    private static void clearSelection() {
        moveType = "";
        cardSelectedFromHand = "";
        cardPlayedIntoBuild = "";
        cardsSelectedFromTable.clear();
        moveBeingMade = false;
        moveDisplay.setText("");

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
                Toast toast = Toast.makeText(context, gamePlayers.get(0).getPileString(), LENGTH_LONG);
                toast.show();
            }
        });

        gameDisplay.getComputerPile().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context, gamePlayers.get(1).getPileString(), LENGTH_LONG);
                toast.show();
            }
        });
    }

    private static void displayEndOfRound() {
        // Display dialog box with all end of Round information.
        AlertDialog.Builder endOfRoundBuilder = new AlertDialog.Builder(context)
                .setTitle("End of Round Info")
                .setMessage(tourney.generateEndOfRoundReport())
                .setNegativeButton("Resume", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!tourney.getWinningPlayer().isEmpty()) {
                            // Tournament is over -> move to next Activity.
                            Intent endGame = new Intent(context.getApplicationContext(), EndScreen.class);

                            Bundle scores = new Bundle();
                            scores.putInt("humanScore", tourney.getCurrentRound().getGamePlayers().get(0).getScore());
                            scores.putInt("computerScore", tourney.getCurrentRound().getGamePlayers().get(1).getScore());
                            scores.putString("winner", tourney.getWinningPlayer());

                            endGame.putExtras(scores);
                            context.startActivity(endGame);
                        }
                        dialog.cancel();
                    }
                });

        AlertDialog endOfRound = endOfRoundBuilder.show();
    }
}
