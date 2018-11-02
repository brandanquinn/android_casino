package com.brandanquinn.casino.model;

import com.brandanquinn.casino.casino.GameScreen;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Round {
    private int roundNum;
    private Deck gameDeck;
    private Table gameTable;
    private ArrayList<Player> gamePlayers;

    /**
     * Overloaded constructor for Round class.
     * @param roundNum, int value used to set current round number
     * @param gamePlayers, ArrayList of players of the game
     */
    public Round(int roundNum, ArrayList<Player> gamePlayers) {
        this.roundNum = roundNum;
        this.gameDeck = new Deck();
        this.gameTable = new Table();
        this.gamePlayers = gamePlayers;
    }

    /**
     * Function where most of the game occurs, rotates through Players turns and allows them to select and make proper moves.
     * @param humanIsFirst, boolean value used to determine which player goes first.
     * @param loadedGame, boolean value used to determine whether cards are preloaded are need to be dealt.
     * @param deckList, ArrayList of cards if user chose to use a seeded deck.
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

    /**
     * Properly deals hands to the Players
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

    /**
     * Properly deals cards to the table.
     */
    private void dealToTable() {
        for (int i = 0; i < 4; i++) {
            Card newCard = this.gameDeck.drawCard();
            if (newCard.getIsRealCard()) {
                this.gameTable.addToTableCards(newCard);
            }
        }
    }
}
