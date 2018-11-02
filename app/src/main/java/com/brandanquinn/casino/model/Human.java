package com.brandanquinn.casino.model;

import android.util.Pair;

import java.util.ArrayList;

public class Human extends Player {

    /*
    Function Name: Human
    Purpose: Default constructor for Human class
    Parameters: None
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public Human() {
        setScore(0);
        setPlayerString("Human");
    }

    /*
    Function Name: Human
    Purpose: Overloaded constructor for Human class
    Parameters:
        int score, score value to set private member variable.
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public Human(int score) {
        setScore(score);
        setPlayerString("Human");
    }

    /*
    Function Name: play
    Purpose: Get Human's input on what move to make and what card to play.
    Parameters: None
    Return Value: A pair object that contains a pointer to the card selected by player, and a char representing a move selection.
    Local Variables:
        char moveOption, move selected by player
        Pair<Card, char> movePair, pair returned by function
        vector<Card*> playerHand, vector of cards in Human's hand.
        int cardNum, keeps track of human input for card selection
    Algorithm:
        1. Initialize local variables
        2. While user hasn't input viable char for move:
            a. Print message to prompt user input.
            b. Read input to moveOption
            c. If moveOption does not match accepted inputs, print error message.
        3. Once moveOption is set, get card selection input from user
        4. Set movePair.first to card selected.
        5. Set movePair.second to moveOption char.
        6. Return movePair
    Assistance Received: None
    */
    public Pair<Card, Character> play() {
        Pair<Card, Character> movePair = new Pair<>(new Card(), 't');
        char moveOption = ' ';
        ArrayList<Card> playerHand = getHand();



        return movePair;
    }

    /*
    Function Name: getCardIndex
    Purpose: Prompt user for index of card selected for play function
    Parameters:
        char moveType, Used to print proper message for move type
    Return Value: Integer value input by user, validated within function
    Local Variables:
        int card_num, Used to keep track of user input
    Algorithm:
        1. Initialize local variables
        2. While user hasn't input valid card index
            a. Print message based on moveType
            b. If user inputs invalid index, print error message.
        3. Return cardNum - 1 to account for array indexing.
    Assistance Received: None
    */
    private int getCardIndex(char moveType) {
        return 0;
    }
}
