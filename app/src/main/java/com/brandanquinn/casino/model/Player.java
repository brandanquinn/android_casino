package com.brandanquinn.casino.model;

import android.util.Pair;

import com.brandanquinn.casino.casino.GameScreen;

import java.util.ArrayList;

public class Player {
    private int score;
    private Table gameTable;
    private boolean isPlaying;
    private String playerString;
    private boolean capturedLast;

    protected ArrayList<Card> hand;
    protected ArrayList<Card> pile;
    protected String cardSelected;
    protected String moveSelected;

    /**
     * Default constructor for Player class.
     */
    public Player() {
        setScore(0);
        setIsPlaying(false);
        setCapturedLast(false);
        this.hand = new ArrayList<>();
        this.pile = new ArrayList<>();
    }

    /**
     * Getter for score private member variable.
     * @return int score of player
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Setter for score private member variable.
     * @param score, int score of player
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Used to add a new Card object to the player's hand.
     * @param newCard, Card object to be added.
     */
    public void addToHand(Card newCard) {
        this.hand.add(newCard);
    }

    /**
     * Adds a list of captured cards to the player's pile.
     * @param capturedCards, ArrayList of Card objects to be added.
     */
    public void addToPile(ArrayList<Card> capturedCards) {
        this.pile.addAll(capturedCards);
    }

    /**
     * Getter for pile private member variable.
     * @return ArrayList of Card objects in player's pile.
     */
    public ArrayList<Card> getPile() {
        return this.pile;
    }

    /**
     * Remove a card played from the player's hand.
     * @param removedCard
     */
    public void discard(Card removedCard) {
        this.hand.remove(removedCard);
    }

    /**
     * Getter for hand private member variable.
     * @return ArrayList of Card objects in player's hand.
     */
    public ArrayList<Card> getHand() {
        return this.hand;
    }

    /**
     * Setter for cardSelected private member variable
     * @param cardSelected, Card object selected by user via Activity.
     */
    public void setCardSelected(String cardSelected) {
        this.cardSelected = cardSelected;
    }

    /**
     * Setter for moveSelected private member variable
     * @param moveSelected, String detailing move selected by user.
     */
    public void setMoveSelected(String moveSelected) {
        this.moveSelected = moveSelected;
    }

    /**
     * Stringified version of player's hand for serialization / logging purposes.
     * @return String of Cards in player's hand.
     */
    public String getHandString() {
        String handString = " ";

        for (int i = 0; i < hand.size(); i++) {
            handString += hand.get(i).getCardString();
        }

        return handString;
    }

    /**
     * Play function that will be overloaded by Human / Computer classes.
     * @return
     */
    public Pair<Card, String> play() {
       return new Pair<>(new Card(), "");
    }

    /**
     * Clears player's hand of all cards.
     */
    public void clearHand() {
        this.hand.clear();
    }

    /**
     * Clears player's pile of all cards.
     */
    public void clearPile() {
        this.pile.clear();
    }

    /**
     * Checks whether player's hand is empty or not.
     * @return boolean value to represent above.
     */
    public boolean handIsEmpty() {
        return this.hand.isEmpty();
    }

    /**
     * Setter for isPlaying private member variable
     * @param isPlaying, boolean value representing whether or not player is currently playing.
     */
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    /**
     * Getter for isPlaying private member variable.
     * @return boolean value representing whether or not player is currently playing.
     */
    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    /**
     * Sets a player's hand to preloaded list of cards
     * @param hand, ArrayList of Card objects used to set hand to.
     */
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    /**
     * Sets a player's pile to preloaded list of cards
     * @param pile, ArrayList of Card objects to set pile to.
     */
    public void setPile(ArrayList<Card> pile) {
        this.pile = pile;
    }

    /**
     * Setter for playerString private member variable
     * @param playerString, String representing if player is Human / Computer.
     */
    public void setPlayerString(String playerString) {
        this.playerString = playerString;
    }

    /**
     * Getter for playerString private member variable
     * @return String representing if player is Human / Computer.
     */
    public String getPlayerString() {
        return this.playerString;
    }

    /**
     * Getter for gameTable private member variable
     * @return Table object for all cards / builds on the table.
     */
    public Table getGameTable() {
        return this.gameTable;
    }

    /**
     * Setter for gameTable private member variable
     * @param gameTable, Table object for all cards / builds on the table.
     */
    public void setGameTable(Table gameTable) {
        this.gameTable = gameTable;
    }

    /**
     * Getter for capturedLast private member variable
     * @return boolean value representing whether or not player captured last.
     */
    public boolean getCapturedLast() {
        return this.capturedLast;
    }

    /**
     * Setter for capturedLast private member variable.
     * @param capturedLast, boolean value representing whether or not player captured last.
     */
    public void setCapturedLast(boolean capturedLast) {
        this.capturedLast = capturedLast;
    }

}
