package com.brandanquinn.casino.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandanquinn.casino.casino.AppContextProvider;
import com.brandanquinn.casino.casino.R;
import com.brandanquinn.casino.model.Card;
import com.brandanquinn.casino.model.Player;
import com.brandanquinn.casino.model.Table;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Display {
    public Display(Context context) {
        this.currentHumanButtons = new ArrayList<>();
        this.appContext = context;

    }

    public ArrayList<ImageButton> getCurrentButtons() {
        return this.currentHumanButtons;
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
//        View appView = View.inflate(appContext, R.layout.activity_game_screen, null);

        LinearLayout humanGrid = ((Activity)appContext).findViewById(R.id.humanHand);
        LinearLayout computerGrid = ((Activity)appContext).findViewById(R.id.computerHand);


        for (int i = 0; i < humanHand.size(); i++) {
            ImageButton cardBtn = createButton(humanHand.get(i));
            humanGrid.addView(cardBtn);
        }

        for (int i = 0; i < computerHand.size(); i++) {
            ImageButton cardBtn = createButton(computerHand.get(i));
            computerGrid.addView(cardBtn);
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
        cardBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        cardBtn.setLayoutParams(new LinearLayout.LayoutParams(150, 200));

        this.currentHumanButtons.add(cardBtn);

        return cardBtn;
    }

    private ArrayList<ImageButton> currentHumanButtons;
    private Context appContext;
}
