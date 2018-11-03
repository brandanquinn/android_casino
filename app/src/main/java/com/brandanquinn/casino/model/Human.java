package com.brandanquinn.casino.model;

import android.util.Pair;

import com.brandanquinn.casino.casino.GameScreen;

import java.util.ArrayList;

public class Human extends Player {

    /**
     * Default constructor for Human class
     */
    public Human() {
        setScore(0);
        setPlayerString("Human");
    }

    /**
     * Overloaded constructor for Human class
     * @param score, int value to set score to
     */
    public Human(int score) {
        setScore(score);
        setPlayerString("Human");
    }

    /**
     * Play function that interacts with controller to get move selection from user.
     * @return Pair that holds Card selected for play as well as move selected.
     */
    public Pair<Card, Character> play() {
        return new Pair<>(new Card(), 't');
    }
}
