package com.brandanquinn.casino.model;

import com.brandanquinn.casino.casino.GameScreen;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Round {
    /*
    Function Name: Round
    Purpose: Constructor for Round class
    Parameters:
        int roundNum, current round number
        ArrayList<Player> gamePlayers, vector containing the two pre-intialized game players
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance: None
    */
    public Round(int roundNum, ArrayList<Player> gamePlayers) {
        this.roundNum = roundNum;
        this.gameDeck = new Deck();
        this.gameTable = new Table();
        this.gamePlayers = gamePlayers;
    }

    /*
    Function Name: start_game
    Purpose: Starts game play
    Parameters:
        bool human_is_first, boolean value representing whether or not human is playing first
        bool loaded_game, boolean value representing whether or not game is loading from file
        vector<Card*> deck_list, if player selects to load in seeded deck - round will create and use a new deck with this vector of cards
    Return Value: None
    Local Variables:
        bool possible_move_selected, keep track of whether or not to possible move has been selected by player
        Player* player_one, player_two, pointers to easily keep track of game players
    Algorithm:
        1. If player chose to start new game
            a. Deal cards to hands and table
        2. Initialize local variables
        3. If human is going first:
            a. Set player_one to human, player_two to computer
        4. Else:
            a. Set player_two to human, player_one to computer
        5. Update view
        6. While deck isn't empty and hands aren't empty:
            a. If players hand are empty and deck is empty:
                i. Deal cards to players
                ii. Update view
            b. While possible move has not been selected and player_one has cards in hand:
                i. Get move_pair from player_one's play function
                    - If player_one is a Computer:
                         - Make move based on AI move selection
                    - Else
                        - Make move based on Player's input
            c. Set player is playing accordingly.
            d. Update view
            e. While possible move has not been selected and player_two has cards in hand:
                i. Get move_pair from player_two's play function
                    - If player_two is a Computer:
                         - Make move based on AI move selection
                    - Else
                        - Make move based on Player's input
            f. Set player is playing accordingly.
            g. Update view
        7. If player_one captured last
            a. Add loose cards on table to player_one's pile
        8. Else if player_two captured last
            a. Add loose cards on table to player_two's pile
    Assistance: None
    */
    public void startGame(boolean humanIsFirst, boolean loadedGame, ArrayList<Card> deckList) {
        if (!loadedGame) {
            if (!deckList.isEmpty()) {
                this.gameDeck = new Deck(deckList);
            }
            dealHands();
            dealToTable();
        }

        boolean possibleMoveSelected = false;
        Player playerOne, playerTwo;

        if (humanIsFirst) {
            playerOne = this.gamePlayers.get(0);
            playerTwo = this.gamePlayers.get(1);
        } else {
            playerOne = this.gamePlayers.get(1);
            playerTwo = this.gamePlayers.get(0);
        }

        playerOne.setGameTable(this.gameTable);
        playerTwo.setGameTable(this.gameTable);

        playerOne.setIsPlaying(true);

        GameScreen.updateActivity(this.gamePlayers, this.gameTable, this.roundNum);
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
    Assistance Received: None
     */
    private void dealToTable() {
        for (int i = 0; i < 4; i++) {
            Card newCard = this.gameDeck.drawCard();
            if (newCard.getIsRealCard()) {
                this.gameTable.addToTableCards(newCard);
            }
        }
    }


    private int roundNum;
    private Deck gameDeck;
    private Table gameTable;
    private ArrayList<Player> gamePlayers;
}
