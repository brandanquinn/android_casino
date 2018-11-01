package com.brandanquinn.casino.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.brandanquinn.casino.casino.AppContextProvider;
import com.brandanquinn.casino.casino.R;
import com.brandanquinn.casino.model.Card;
import com.brandanquinn.casino.model.Player;
import com.brandanquinn.casino.model.Table;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Display {
    public Display() {
        this.currentButtons = new ArrayList<>();
        this.appContext = AppContextProvider.getAppContext();
    }

    public ArrayList<ImageButton> getCurrentButtons() {
        return this.currentButtons;
    }

    /*
        Function Name: updateView
        Purpose: Dynamically update the view based on the model.
        Parameters:
        Return Value:
        Local Variables:
        Algorithm:
        Assistance Received:
         */
    public void updateView(ArrayList<Player> gamePlayers, Table gameTable) {
        displayCards(gamePlayers, gameTable);
    }

    /*
    Function Name: displayCards
    Purpose: Create ImageButtons for each card and display them on the screen.
    Parameters:
        ArrayList<Player> gamePlayers, List of game players
        Table gameTable, Game table object
    Return Value: None
    Local Variables:
    Algorithm:
    Assistance Received: None
     */
    private void displayCards(ArrayList<Player> gamePlayers, Table gameTable) {
        // For each card in human hand - create an image button with proper card image
        // Assign button to a column in the respective players grid
        ArrayList<Card> humanHand = gamePlayers.get(0).getHand();
        ArrayList<Card> computerHand = gamePlayers.get(1).getHand();

        // Gets the view of the game screen for manipulation
        View appView = View.inflate(appContext, R.layout.activity_game_screen, null);

        GridLayout humanGrid = appView.findViewById(R.id.humanHand);


//        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1));

        ImageButton cardBtn = createButton(humanHand.get(0));

        // Look into changing to a GridView

        for (int i = 0; i < humanHand.size(); i++) {
            humanGrid.addView(createButton(humanHand.get(i)));
        }

    }

    /*
    Function Name: createButton
    Purpose: Create proper ImageButtons for displayCards() to add to grid
    Parameters:
        Card gameCard, reference card for ImageButton
    Return Value: None
    Local Variables:
    Algorithm:
    Assistance Received: None
     */
    private ImageButton createButton(Card gameCard) {
        // Programmatically create and return an ImageButton based on card passed.
        // Whenever ImageButton is created, add it to currentButtons list

        // Initializing ImageButton and basic resources
        ImageButton cardBtn = new ImageButton(appContext);
        String cardImageResource = gameCard.getImageResourceName();
        int imageID = appContext.getResources().getIdentifier(cardImageResource, "drawable", appContext.getPackageName());

        // Setting ImageButton to proper card image
        cardBtn.setImageResource(imageID);

        this.currentButtons.add(cardBtn);

        return cardBtn;
    }

    private ArrayList<ImageButton> currentButtons;
    private Context appContext;
}
