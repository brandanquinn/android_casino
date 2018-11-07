package com.brandanquinn.casino.model;

import java.util.ArrayList;

// Used to replace the move pair system from Player->play().
// Will contain:
// moveType
// cardSelectedFromHand, cardPlayedFromHand, cardsSelectedFromTable, buildsSelectedFromTable
public class Move {
    private String moveType;
    private Card cardSelectedFromHand;
    private Card cardPlayedFromHand;
    private ArrayList<Card> cardsSelectedFromTable;
    private ArrayList<Build> buildsSelectedFromTable;


    /**
     * Default constructor for Move class
     */
    public Move() {

    }

    /**
     * Constructor for Move class
     * @param moveType, String representing type of move selected by player
     * @param cardSelectedFromHand, Card object selected from hand by player
     * @param cardPlayedFromHand, Card object selected to be played onto table by player
     * @param cardsSelectedFromTable, ArrayList of Card objects selected on the table.
     * @param buildsSelectedFromTable, ArrayList of Build objects selected on the table.
     */
    public Move(String moveType, Card cardSelectedFromHand, Card cardPlayedFromHand, ArrayList<Card> cardsSelectedFromTable, ArrayList<Build> buildsSelectedFromTable) {
        this.moveType = moveType;
        this.cardSelectedFromHand = cardSelectedFromHand;
        this.cardPlayedFromHand = cardPlayedFromHand;
        this.cardsSelectedFromTable = cardsSelectedFromTable;
        this.buildsSelectedFromTable = buildsSelectedFromTable;
    }

    /**
     * Getter for moveType private member variable
     * @return String move type
     */
    public String getMoveType() { return this.moveType; }

    /**
     * Getter for cardSelectedFromHand private member variable
     * @return Card object selected from hand
     */
    public Card getCardSelectedFromHand() { return this.cardSelectedFromHand; }

    /**
     * Getter for cardPlayedFromHand private member variable
     * @return Card object played from hand
     */
    public Card getCardPlayedFromHand() {
        return cardPlayedFromHand;
    }

    /**
     * Getter for cardsSelectedFromTable private member variable
     * @return ArrayList of Card objects selected from the table
     */
    public ArrayList<Card> getCardsSelectedFromTable() {
        return cardsSelectedFromTable;
    }

    /**
     * Getter for buildsSelectedFromTable private member variable
     * @return ArrayList of Build objects selected from the table
     */
    public ArrayList<Build> getBuildsSelectedFromTable() {
        return buildsSelectedFromTable;
    }
}
