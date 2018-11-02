package com.brandanquinn.casino.model;

import android.util.Pair;

public class Computer extends Player{
    /*
    Function Name: Computer
    Purpose: Default constructor for Computer class.
    Parameters: None
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public Computer() {
        setScore(0);
        setPlayerString("Computer");
    }

    /*
    Function Name: Computer
    Purpose: Overloaded constructor for Computer class; used for deserialization
    Parameters:
        int score, value used to set player's score.
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public Computer(int score) {
        setScore(score);
        setPlayerString("Computer");
    }

    /*
    Function Name: play
    Purpose: Calls the AI function from the Player parent class
    Parameters: None
    Return Value: A pair object containing the Card selected to play by the AI, as well as the best move to make with that card.
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public Pair<Card, Character> play() {
        Pair<Card, Character> movePair = new Pair<Card, Character>(new Card(), 't');

        return movePair;
    }
}
