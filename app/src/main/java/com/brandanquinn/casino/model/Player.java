package com.brandanquinn.casino.model;

import android.util.Pair;

import java.util.ArrayList;

public class Player {
    /*
    Function Name: Player
    Purpose: Default constructor for Player class
    Parameters: None
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public Player() {
        setScore(0);
        setIsPlaying(false);
        setCapturedLast(false);
        this.hand = new ArrayList<>();
        this.pile = new ArrayList<>();
    }

    /*
    Function Name: getScore
    Purpose: Getter for score private member variable
    Parameters: None
    Return Value: Integer value representing player score
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public int getScore() {
        return this.score;
    }

    /*
    Function Name: setScore
    Purpose: Setter for score private member variable
    Parameters:
        int score, Variable to set score to
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void setScore(int score) {
        this.score = score;
    }

    /*
    Function Name: addToHand
    Purpose: Adds a new card to player's hand
    Parameters:
        Card newCard, card obj pointer to add to hand private member variable.
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void addToHand(Card newCard) {
        this.hand.add(newCard);
    }

    /*
    Function Name: addToPile
    Purpose: Adds captured cards to player pile
    Parameters:
        ArrayList<Card> capturedCards, Cards captured by player to be added to their pile
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void addToPile(ArrayList<Card> capturedCards) {

    }

    /*
    Function Name: getPile
    Purpose: Getter for pile private member variable
    Parameters: None
    Return Value: vector of cards in the player's current pile
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public ArrayList<Card> getPile() {
        return this.pile;
    }

    /*
    Function Name: discard
    Purpose: Discard selected card from player's hand
    Parameters:
        Card removedCard, Card played in move to be removed from hand in model.
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void discard(Card removedCard) {

    }

    /*
    Function Name: getHand
    Purpose: Getter for hand private member variable
    Parameters: None
    Return Value: vector of cards in player's hand
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public ArrayList<Card> getHand() {
        return this.hand;
    }

    /*
    Function Name: clearHand
    Purpose: Clears all cards from player's hand.
    Parameters: None
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void clearHand() {

    }

    /*
    Function Name: clearPile
    Purpose: Clears all cards from player's pile
    Parameters: None
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void clearPile() {
        this.pile.clear();
    }

    /*
    Function Name: handIsEmpty
    Purpose: Used to check if player's hand is currently empty for round ending state / drawing purposes
    Parameters: None
    Return Value: Whether or not the players hand is empty represented as a boolean value.
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public boolean handIsEmpty() {
        return this.hand.isEmpty();
    }

    /*
    Function Name: setIsPlaying
    Purpose: Setter for isPlaying private member variable
    Parameters:
        bool isPlaying, Boolean value representing whether player is currently playing or not.
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    /*
    Function Name: getIsPlaying
    Purpose: Getter for isPlaying private member variable
    Parameters: None
    Return Value: Boolean value representing whether player is currently playing or not.
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    /*
    Function Name: setHand
    Purpose: Setter for hand private member variable, used in serialization
    Parameters:
        ArrayList<Card> hand, vector of cards to set hand to.
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    /*
    Function Name: setPile
    Purpose: Setter for pile private member variable, used in serialization
    Parameters:
        ArrayList<Card> pile, vector of cards to set pile to.
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void setPile(ArrayList<Card> pile) {
        this.pile = pile;
    }

    /*
    Function Name: setPlayerString
    Purpose: Setter for player_string private member variable
    Parameters:
        string playerString, String to keep track of whether player is Human or Computer.
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void setPlayerString(String playerString) {
        this.playerString = playerString;
    }

    /*
    Function Name: getPlayerString
    Purpose: Getter for playerString private member variable
    Parameters: None
    Return Value: Returns a string to keep track of whether player is Human or Computer.
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public String getPlayerString() {
        return this.playerString;
    }

    /*
    Function Name: getGameTable
    Purpose: Getter for game_table private member variable.
    Parameters: None
    Return Value: Returns a pointer to the current game table.
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public Table getGameTable() {
        return this.gameTable;
    }

    /*
    Function Name: setGameTable
    Purpose: Setter for game_table private member variable
    Parameters:
        Table* gameTable, Pointer to the current game table object.
    Return Value: None
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public void setGameTable(Table gameTable) {
        this.gameTable = gameTable;
    }

    /*
    Function Name: getCapturedLast
    Purpose: Getter for capturedLast private member variable
    Parameters: None
    Return Value: Boolean value to determine which player captured last.
    Local Variables: None
    Algorithm: None
    Assistance Received: None
    */
    public boolean getCapturedLast() {
        return this.capturedLast;
    }

    public void setCapturedLast(boolean capturedLast) {
        this.capturedLast = capturedLast;
    }

    private int score;
    private ArrayList<Card> hand;
    private ArrayList<Card> pile;
    private Table gameTable;
    private boolean isPlaying;
    private String playerString;
    private boolean capturedLast;

}
