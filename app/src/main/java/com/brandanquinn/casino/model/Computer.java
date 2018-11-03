package com.brandanquinn.casino.model;

import android.util.Pair;

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
    public Pair<Card, String> play() {

        Pair<Card, String> movePair;

        if (!handIsEmpty()) {
            movePair = new Pair<>(this.hand.get(0), "trail");
        } else {
            movePair = new Pair<>(new Card(), "trail");
        }

        return movePair;
    }
}
