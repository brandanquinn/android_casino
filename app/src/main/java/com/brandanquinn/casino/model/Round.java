package com.brandanquinn.casino.model;

import android.util.Pair;

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
     * Getter for gamePlayers private member variable.
     * @return ArrayList of game player objects.
     */
    public ArrayList<Player> getGamePlayers() {
        return this.gamePlayers;
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

        GameScreen.updateActivity(this.gamePlayers, this.gameTable, this.roundNum);

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
     * Used to print who's turn it is to a TextView in the main activity
     * @return String denoting who's turn it currently is.
     */
    public String whoIsPlaying() {
        String playing = "Unknown";
        for (int i = 0; i < this.gamePlayers.size(); i++) {
            if (gamePlayers.get(i).getIsPlaying()) {
                playing = gamePlayers.get(i).getPlayerString();
            }
        }

        return playing + " is playing.";
    }

    /**
     * Properly deals hands to the Players
     */
    private void dealHands() {
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

    /**
     * Allows each player to select moves until a possible move is made.
     * @param gamePlayer, Player currently making a move.
     * @return String to tell user what move was made.
     */
    public String playTurn(Player gamePlayer) {
        boolean possibleMoveSelected = false;
        while (!gamePlayer.handIsEmpty() && !possibleMoveSelected) {
            Pair<Card, String> movePair = gamePlayer.play();
            if (gamePlayer.getPlayerString() == "Computer") {
                if (movePair.first.getIsRealCard()) {
                    possibleMoveSelected = trail(movePair.first, gamePlayer);
                    changeTurn(gamePlayer);
                    GameScreen.updateActivity(this.gamePlayers, this.gameTable, this.roundNum);

                    return "" + gamePlayer.getPlayerString() + " selected to " + movePair.second + " with card: " + movePair.first.getCardString();
                } else {
                    return "Move cannot be made.";
                }
            } else {
                if (movePair.second == "trail") {
                    possibleMoveSelected = trail(movePair.first, gamePlayer);
                    changeTurn(gamePlayer);
                    GameScreen.updateActivity(this.gamePlayers, this.gameTable, this.roundNum);
                    return "" + gamePlayer.getPlayerString() + " selected to " + movePair.second + " with card: " + movePair.first.getCardString();
                }
            }

        }

        return "Move cannot be made.";
    }

    /**
     * Used to change from one player's turn to the other.
     * @param justPlayed, Player that just made a move.
     */
    private void changeTurn(Player justPlayed) {
        if (this.gamePlayers.get(0) == justPlayed) {
            this.gamePlayers.get(1).setIsPlaying(true);
        } else {
            this.gamePlayers.get(0).setIsPlaying(true);
        }
    }

    /**
     * Checks to make sure a trail move can be made, and makes it if possible.
     * @param cardPlayed, Card object to be played to the table.
     * @param gamePlayer, Player making the trail move
     * @return boolean value determining whether or not move was made.
     */
    private boolean trail(Card cardPlayed, Player gamePlayer) {
        gamePlayer.discard(cardPlayed);
        this.gameTable.addToTableCards(cardPlayed);
        gamePlayer.setIsPlaying(false);
        return true;
    }
}
