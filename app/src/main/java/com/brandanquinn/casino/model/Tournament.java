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

    /**
     * Default constructor for the Tournament class
     */
    public Tournament() {
        this.roundsPlayed = 1;
        this.cardCount = 0;
        this.coinToss = ' ';
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
}
