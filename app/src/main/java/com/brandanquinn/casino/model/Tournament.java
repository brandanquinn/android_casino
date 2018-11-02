package com.brandanquinn.casino.model;

import com.brandanquinn.casino.casino.StartScreen;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Tournament {
    /*
    Function Name: Tournament
    Purpose: Default constructor for the Tournament class
    Parameters: None
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
     */
    public Tournament() {
        this.roundsPlayed = 1;
        this.cardCount = 0;
        this.coinToss = ' ';
        this.gamePlayers = new ArrayList<>();
        this.gamePlayers.add(new Human());
        this.gamePlayers.add(new Computer());
    }

    /*
    Function Name: startRound
    Purpose: Initialize a Round object and start game play
    Parameters:
        boolean firstRound, Whether or not this is starting the first round.
    Return Value: None
    Local Variables:
    Algorithm:
    Assistance Received: None
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

    private Round currentRound;
    private int roundsPlayed;
    private int cardCount;
    private int coinToss;
    private ArrayList<Player> gamePlayers;

}
