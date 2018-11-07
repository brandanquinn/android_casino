package com.brandanquinn.casino.model;

import android.util.Pair;

import java.util.ArrayList;

public class Computer extends Player{
    /**
     * Default constructor for Computer class.
     */
    public Computer() {
        setScore(0);
        setPlayerString("Computer");
    }

    /**
     * Overloaded constructor for Computer class used in deserialization.
     * @param score, int value to preset score to.
     */
    public Computer(int score) {
        setScore(score);
        setPlayerString("Computer");
    }

    /**
     * Computer play function that will call the AI from the Parent class Player.
     * @return Pair selected by the AI for move to be made.
     */
    public Move play() {
        Card cardSelected = new Card();

        if (!handIsEmpty()) {
            cardSelected = this.hand.get(0);
        }

        return new Move("trail", cardSelected, new Card(), new ArrayList<Card>(), new ArrayList<Build>());
    }
}
