package com.brandanquinn.casino.model;

import com.brandanquinn.casino.casino.StartScreen;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Tournament {
    private Round currentRound;
    private int roundsPlayed;
    private int cardCount;
    private int coinToss;
    private ArrayList<Player> gamePlayers;
    private String winningPlayer;

    /**
     * Default constructor for the Tournament class
     */
    public Tournament() {
        this.roundsPlayed = 1;
        this.cardCount = 0;
        this.coinToss = ' ';
        this.winningPlayer = "";
        this.gamePlayers = new ArrayList<>();
        this.gamePlayers.add(new Human());
        this.gamePlayers.add(new Computer());
    }

    /**
     * Getter for currentRound private member variable
     * @return Round object that is currently being played in.
     */
    public Round getCurrentRound() {
        return this.currentRound;
    }

    /**
     * Getter for winningPlayer private member variable
     * @return String representing the player that won the tournament, empty if tournament is still being played.
     */
    public String getWinningPlayer() {
        return winningPlayer;
    }

    /**
     * Initializes the current round, conducts coin flip if necessary, and starts game.
     * @param firstRound, boolean value determining whether or not coin flip needs to occur.
     */
    public void startRound(boolean firstRound) {
        Round gameRound = new Round(roundsPlayed, gamePlayers);

        this.currentRound = gameRound;
        ArrayList<Card> deckList = new ArrayList<>();

        // if firstRound, coin toss
        if (firstRound) {
            while (coinToss == ' ') {
                coinToss = StartScreen.getCoinToss();
            }

            double flip = Math.round(Math.random() * 2);

            if (coinToss == flip) {
                // correct call
                this.currentRound.startGame(true, false, deckList);
            } else {
                // wrong call
                this.currentRound.startGame(false, false, deckList);
            }
        } else {
            this.currentRound.startGame(gamePlayers.get(0).getCapturedLast(), false, deckList);
        }
    }

    public void endRound() {
        this.roundsPlayed = this.currentRound.getRoundNum() + 1;

        computePlayerScores();

        if (this.gamePlayers.get(0).getScore() >= 21 && this.gamePlayers.get(1).getScore() >= 21) {
            this.winningPlayer = "tie";
        } else if (this.gamePlayers.get(0).getScore() >= 21) {
            this.winningPlayer = gamePlayers.get(0).getPlayerIdentity();
        } else if (this.gamePlayers.get(1).getScore() >= 21) {
            this.winningPlayer = gamePlayers.get(1).getPlayerIdentity();
        } else {
            this.gamePlayers.get(0).clearHand();
            this.gamePlayers.get(0).clearPile();

            this.gamePlayers.get(1).clearHand();
            this.gamePlayers.get(1).clearPile();

            this.currentRound.clearGameTable();
            startRound(false);
        }
    }

    /**
     * Computes and sets scores for game players.
     */
    private void computePlayerScores() {
        int p1CurrentScore = this.gamePlayers.get(0).getScore();
        ArrayList<Card> p1CurrentPile = this.gamePlayers.get(0).getPile();

        int p2CurrentScore = this.gamePlayers.get(1).getScore();
        ArrayList<Card> p2CurrentPile = this.gamePlayers.get(1).getPile();

        if (p1CurrentPile.size() > p2CurrentPile.size()) {
            p1CurrentScore += 3;
        } else if (p2CurrentPile.size() > p1CurrentPile.size()) {
            p2CurrentScore += 3;
        }

        int p1SpadesCount = 0, p2SpadesCount = 0;
        for (int i = 0; i < p1CurrentPile.size(); i++) {
            if (p1CurrentPile.get(i).getSuit() == 'S') {
                p1SpadesCount++;
            }
            if (p1CurrentPile.get(i).getType() == 'A') {
                p1CurrentScore++;
            }
            if (p1CurrentPile.get(i).getCardString().equals("DX")) {
                p1CurrentScore += 2;
            }
            if (p1CurrentPile.get(i).getCardString().equals("S2")) {
                p1CurrentScore++;
            }
        }

        for (int i = 0; i < p2CurrentPile.size(); i++) {
            if (p2CurrentPile.get(i).getSuit() == 'S') {
                p2SpadesCount++;
            }
            if (p2CurrentPile.get(i).getType() == 'A') {
                p2CurrentScore++;
            }
            if (p2CurrentPile.get(i).getCardString().equals("DX")) {
                p2CurrentScore += 2;
            }
            if (p2CurrentPile.get(i).getCardString().equals("S2")) {
                p2CurrentScore++;
            }
        }

        if (p1SpadesCount > p2SpadesCount) {
            p1CurrentScore++;
        } else if (p2SpadesCount > p1SpadesCount) {
            p2CurrentScore++;
        }

        this.gamePlayers.get(0).setScore(p1CurrentScore);
        this.gamePlayers.get(1).setScore(p2CurrentScore);
    }
}
