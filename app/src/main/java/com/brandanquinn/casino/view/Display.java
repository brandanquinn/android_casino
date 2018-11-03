package com.brandanquinn.casino.view;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandanquinn.casino.casino.R;
import com.brandanquinn.casino.model.Card;
import com.brandanquinn.casino.model.Player;
import com.brandanquinn.casino.model.Table;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Display {
    private ArrayList<ImageButton> computerButtons;
    private ArrayList<ImageButton> humanButtons;
    private ArrayList<ImageButton> tableButtons;
    private Activity gameActivity;
    private Context appContext;

    public Display(Context context) {
        this.humanButtons = new ArrayList<>();
        this.computerButtons = new ArrayList<>();
        this.tableButtons = new ArrayList<>();
        this.appContext = context;
        this.gameActivity = (Activity)this.appContext;

    }

    /**
     * Getter for humanButtons private member variable
     * @return ArrayList of buttons in human's hand
     */
    public ArrayList<ImageButton> getHumanButtons() {
        return this.humanButtons;
    }

    /**
     * Getter for computerButtons private member variable
     * @return ArrayList of buttons in the computer's hand
     */
    public ArrayList<ImageButton> getComputerButtons() { return this.computerButtons; }

    /**
     * Getter for tableButtons private member variable
     * @return ArrayList of buttons on the table
     */
    public ArrayList<ImageButton> getTableButtons() { return this.tableButtons; }

    /**
     * Updates the various view components using information from the model.
     * @param gamePlayers, ArrayList of game players
     * @param gameTable, Table object used to access cards/builds on the table
     * @param roundNum, int used to keep track of current round number
     */
    public void updateView(ArrayList<Player> gamePlayers, Table gameTable, int roundNum, String whoIsPlaying) {

        displayCards(gamePlayers, gameTable);
        displayRoundNum(roundNum);
        displayScores(gamePlayers, whoIsPlaying);
    }

    /**
     * Adds dynamically created cards to their respective grids in the view.
     * @param gamePlayers, ArrayList of game players
     * @param gameTable, Table object used to access cards/builds on the table
     */
    private void displayCards(ArrayList<Player> gamePlayers, Table gameTable) {
        // For each card in human hand - create an image button with proper card image
        // Assign button to a column in the respective players grid
        ArrayList<Card> humanHand = gamePlayers.get(0).getHand();
        ArrayList<Card> computerHand = gamePlayers.get(1).getHand();
        ArrayList<Card> tableCards = gameTable.getTableCards();

        // Grid components in view
        LinearLayout humanGrid = this.gameActivity.findViewById(R.id.humanHand);
        LinearLayout computerGrid = this.gameActivity.findViewById(R.id.computerHand);
        LinearLayout tableGrid = this.gameActivity.findViewById(R.id.tableGrid);

        // Empty grid components and button containers
        humanGrid.removeAllViews();
        humanButtons.clear();

        computerGrid.removeAllViews();
        computerButtons.clear();

        tableGrid.removeAllViews();
        tableButtons.clear();


        for (int i = 0; i < humanHand.size(); i++) {
            humanGrid.addView(createButton(humanHand.get(i), "humanHand", humanHand.size()));
        }

        for (int i = 0; i < computerHand.size(); i++) {
            computerGrid.addView(createButton(computerHand.get(i), "computerHand", computerHand.size()));
        }

        for (int i = 0; i < tableCards.size(); i++) {
            tableGrid.addView(createButton(tableCards.get(i), "tableCards", tableCards.size()));
        }


    }

    /**
     * Dynamically creates ImageButtons for cards using drawable resources.
     * @param gameCard, Card object used to get image resource and model ImageButton
     * @param layoutId, Used to identify where the card will be placed on its respective grid and how it must be sized.
     * @param numCards, Also used to dynamically scale size of cards based on how many are placed in grid.
     * @return ImageButton dynamically created and based on Card from model.
     */
    private ImageButton createButton(Card gameCard, String layoutId, int numCards) {
        // Programmatically create and return an ImageButton based on card passed.
        // Whenever ImageButton is created, add it to currentButtons list

        // Initializing ImageButton and basic resources
        ImageButton cardBtn = new ImageButton(appContext);
        String cardImageResource = gameCard.getImageResourceName();
        int imageID = appContext.getResources().getIdentifier(cardImageResource, "drawable", appContext.getPackageName());

        // Setting ImageButton to proper card image
        cardBtn.setImageResource(imageID);
        cardBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        cardBtn.setTag(gameCard.getCardString());


        TextView debug = this.gameActivity.findViewById(R.id.debugBox);

        // Max width of table is 785 dp
        if (layoutId == "tableCards" && numCards > 8) {
            int width = 1268 / numCards;
            cardBtn.setLayoutParams(new LinearLayout.LayoutParams(width, 180));
        } else {
            cardBtn.setLayoutParams(new LinearLayout.LayoutParams(150, 180));
        }

        if (layoutId == "tableCards") {
            this.tableButtons.add(cardBtn);
        } else if (layoutId == "humanHand") {
            this.humanButtons.add(cardBtn);
        } else {
            this.computerButtons.add(cardBtn);
        }

        return cardBtn;
    }

    /**
     * Update view component for Round Number
     * @param roundNum, int value to track current round number.
     */
    public void displayRoundNum(int roundNum) {
        TextView roundNumDisplay = this.gameActivity.findViewById(R.id.roundNumber);

        roundNumDisplay.setText(Integer.toString(roundNum));
    }

    /**
     * Update view component for player scores and prints who is playing
     * @param gamePlayers, ArrayList of game players to access scores.
     * @param whoIsPlaying, String denoting who is currently playing.
     */
    public void displayScores(ArrayList<Player> gamePlayers, String whoIsPlaying) {
        // Set human score
        TextView humanScoreDisplay = this.gameActivity.findViewById(R.id.humanScore);
        humanScoreDisplay.setText(Integer.toString(gamePlayers.get(0).getScore()));

        // Set computer score
        TextView compScoreDisplay = this.gameActivity.findViewById(R.id.computerScore);
        compScoreDisplay.setText(Integer.toString(gamePlayers.get(1).getScore()));

        // Who is playing
        TextView debug = this.gameActivity.findViewById(R.id.debugBox);
        debug.setText(whoIsPlaying);
    }
}
