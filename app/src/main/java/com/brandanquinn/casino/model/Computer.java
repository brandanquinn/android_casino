package com.brandanquinn.casino.model;


public class Computer extends Player{
    /**
     * Default constructor for Computer class.
     */
    public Computer() {
        setScore(0);
    }

    /**
     * Overloaded constructor for Computer class used in deserialization.
     * @param score, int value to preset score to.
     */
    public Computer(int score) {
        setScore(score);
    }

    /**
     * Polymorphic function to get a player's identity.
     * @return String representing player's identity.
     */
    @Override
    public String getPlayerIdentity() {
        return "Computer";
    }

    /**
     * Computer play function that will call the AI from the Parent class Player.
     * @return Pair selected by the AI for move to be made.
     */
    @Override
    public Move play() {
        return getHelp();
    }
}
