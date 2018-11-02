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
    private ArrayList<Player> gamePlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        // Initialize private member variables.
        this.gameDeck = new Deck();
        this.gameDisplay = new Display(this);
        this.gameTable = new Table();
        this.gamePlayers = new ArrayList<>();
        this.gamePlayers.add(new Player());
        this.gamePlayers.add(new Player());

        dealHands();
        dealToTable();

        gameDisplay.updateView(gamePlayers, this.gameTable);

    }

    /*
    Function Name: dealHands
    Purpose: Properly deals cards to each player
    Parameters: None
    Return Value: None
    Local Variables:
        Card newCard, Card drawn from top of deck
    Algorithm:
        1. Deal 4 cards to Human Player
        2. Deal 4 cards to Computer Player
    Assistance Received: None
     */
    public void dealHands() {
        for (int i = 0; i < 4; i++) {
            Card newCard = this.gameDeck.drawCard();
            if (newCard.getIsRealCard()) {
                this.gamePlayers.get(0).addToHand(newCard);
            }
        }

        for (int i = 0; i < 4; i++) {
            Card newCard = this.gameDeck.drawCard();
            if (newCard.getIsRealCard()) {
                this.gamePlayers.get(1).addToHand(newCard);
            }
        }
    }

    /*
    Function Name: dealToTable
    Purpose: Deals cards to table
    Parameters: None
    Return Value: None
    Local Variables:
        Card newCard, Card drawn from top of deck.
    Algorithm:
        1. For integers [0, 4)
            a. Draw card from deck and store as newCard
            b. If newCard is a real card:
                - Add newCard to table cards
     */
    public void dealToTable() {
        for (int i = 0; i < 10; i++) {
            Card newCard = this.gameDeck.drawCard();
            if (newCard.getIsRealCard()) {
                this.gameTable.addToTableCards(newCard);
            }
        }
    }
}
