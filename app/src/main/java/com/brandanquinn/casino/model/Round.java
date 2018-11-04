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
            Pair<ArrayList<Card>, String> movePair = gamePlayer.play();
            if (gamePlayer.getPlayerString() == "Computer") {
                if (movePair.first.get(0).getIsRealCard()) {
                    possibleMoveSelected = trail(movePair.first.get(0), gamePlayer);
                    changeTurn();
                    GameScreen.updateActivity(this.gamePlayers, this.gameTable, this.roundNum);

                    return "" + gamePlayer.getPlayerString() + " selected to " + movePair.second + " with card: " + movePair.first.get(0).getCardString();
                } else {
                    return "Move cannot be made.";
                }
            } else {
                if (movePair.second == "trail") {
                    possibleMoveSelected = trail(movePair.first.get(0), gamePlayer);
                    changeTurn();
                    GameScreen.updateActivity(this.gamePlayers, this.gameTable, this.roundNum);
                    return "" + gamePlayer.getPlayerString() + " selected to " + movePair.second + " with card: " + movePair.first.get(0).getCardString();
                } else if (movePair.second == "build") {
                    possibleMoveSelected = build(movePair.first.get(0), movePair.first.get(1), new ArrayList<>(movePair.first.subList(2, movePair.first.size())), gamePlayer);
                    if (possibleMoveSelected) {
                        changeTurn();
                        GameScreen.updateActivity(this.gamePlayers, this.gameTable, this.roundNum);
                        return "" + gamePlayer.getPlayerString() + " selected to " + movePair.second + " with card: " + movePair.first.get(0).getCardString();
                    } else {
                        return "Build move cannot be made with cards selected.";
                    }
                }
            }

        }

        return "Move cannot be made.";
    }

    /**
     * Used to change from one player's turn to the other.
     */
    private void changeTurn() {
        if (gamePlayers.get(0).getIsPlaying()) {
            gamePlayers.get(0).setIsPlaying(false);
            gamePlayers.get(1).setIsPlaying(true);
        } else {
            gamePlayers.get(1).setIsPlaying(false);
            gamePlayers.get(0).setIsPlaying(true);
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
        return true;
    }

    private boolean build(Card lockedCard, Card playedCard, ArrayList<Card> selectedFromTable, Player gamePlayer) {
        ArrayList<Card> tableCards = new ArrayList<>(this.gameTable.getTableCards());
        ArrayList<Card> filteredCards = new ArrayList<>(tableCards);
        boolean extendingBuild = false;

        if (lockedCard.getLockedToBuild()) { extendingBuild = true; }

        int selectedVal = lockedCard.getValue();
        int playedVal = playedCard.getValue();

        if (playedVal >= selectedVal) { return false; }

        ArrayList<Card> buildCards = new ArrayList<>();
        buildCards.add(playedCard);

        int setSum = playedVal;
        for (int i = 0; i < selectedFromTable.size(); i++) {
            setSum += selectedFromTable.get(i).getValue();
            buildCards.add(selectedFromTable.get(i));
            if (setSum > selectedVal) {
                return false;
            }
            if (setSum == selectedVal) {
                break;
            }
        }

        if (setSum != selectedVal) { return false; }

        if (!extendingBuild) {
            // creating new single build
            ArrayList<ArrayList<Card>> totalBuildCards = new ArrayList<>();
            totalBuildCards.add(buildCards);
            Build b1 = new Build(totalBuildCards, selectedVal, lockedCard, gamePlayer.getPlayerString());
            this.gameTable.addBuild(b1);
            for (int i = 0; i < buildCards.size(); i++) {
                buildCards.get(i).setPartOfBuild(true);
            }
            playedCard.setBuildBuddies(buildCards);
            gamePlayer.discard(playedCard);
            this.gameTable.addToTableCards(playedCard);
            lockedCard.setLockedToBuild(true);

            return true;
        } else {
            // extending current build to a multi build
            // work in prog
        }

        // Create build with build cards.
        return false;

    }

    private void filterBuildOptions(ArrayList<Card> availableCards, int playedVal, int buildSum) {
        for (int i = 0; i < availableCards.size(); i++) {
            if (availableCards.get(i).getValue() + playedVal > buildSum) {
                availableCards.remove(availableCards.get(i));
            }
        }
    }
}
