package com.brandanquinn.casino.model;

import java.util.*;

public class Card {
    private char suit;
    private char type;
    private int value;
    private HashMap<Character, Integer> typeValuePairs;
    private boolean lockedToBuild;
    private boolean partOfBuild;
    private boolean isRealCard;
    private ArrayList<Card> buildBuddies;

    /**
     * Default constructor for card class.
     */
    public Card() {
        setSuit('X');
        setType('0');
        this.isRealCard = false;
    }

    /**
     * Overloaded constructor for card class.
     * @param suit, char representation of suit for each card
     * @param type, char representation of type for each card
     */
    public Card(char suit, char type) {
        setSuit(suit);
        setType(type);
        createMap();
        setValue();
        setLockedToBuild(false);
        setPartOfBuild(false);
        this.isRealCard = true;
    }

    /**
     * Creates a HashMap of type value pairs.
     */
    public void createMap() {
        typeValuePairs = new HashMap<>();
        typeValuePairs.put('2', 2);
        typeValuePairs.put('3', 3);
        typeValuePairs.put('4', 4);
        typeValuePairs.put('5', 5);
        typeValuePairs.put('6', 6);
        typeValuePairs.put('7', 7);
        typeValuePairs.put('8', 8);
        typeValuePairs.put('9', 9);
        typeValuePairs.put('X', 10);
        typeValuePairs.put('J', 11);
        typeValuePairs.put('Q', 12);
        typeValuePairs.put('K', 13);
        typeValuePairs.put('A', 14);
    }

    /**
     * Getter for the suit private member variable
     * @return char suit
     */
    public char getSuit() {
        return this.suit;
    }

    /**
     * Setter for suit private member variable
     * @param suit, char suit
     */
    public void setSuit(char suit) {
        this.suit = suit;
    }

    /**
     * Getter for the type private member variable
     * @return char type
     */
    public char getType() {
        return this.type;
    }

    /**
     * Setter for the type private member variable
     * @param type, char type
     */
    public void setType(char type) {
        this.type = type;
    }

    /**
     * Getter for the value private member variable
     * @return int value of each card
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Sets the value private member variable by accessing the HashMap
     */
    public void setValue() {
        this.value = typeValuePairs.get(this.type);
    }

    /**
     * Generates a stringified Card object
     * @return String cardString
     */
    public String getCardString() {
        String cardString = "" + getSuit() + getType();
        return cardString;
    }

    /**
     * Generates the file resource name for each Card.
     * @return String File resource name
     */
    public String getImageResourceName() {
        return "" + Character.toLowerCase(getSuit()) + "_" + Character.toLowerCase(getType());
    }

    /**
     * Getter for lockedToBuild private member variable.
     * @return boolean representing whether or not the Card is currently the capture card for a build.
     */
    public boolean getLockedToBuild() {
        return this.lockedToBuild;
    }

    /**
     * Setter for lockedToBuild private member variable
     * @param lockedToBuild, boolean representing whether or not the Card is currently the capture card for a build.
     */
    public void setLockedToBuild(boolean lockedToBuild) {
        this.lockedToBuild = lockedToBuild;
    }

    /**
     * Getter for partOfBuild private member variable
     * @return boolean representing whether or not the Card is currently part of a build on the table.
     */
    public boolean getPartOfBuild() {
        return this.partOfBuild;
    }

    /**
     * Setter for partOfBuild private member variable
     * @param partOfBuild, boolean representing whether or not the Card is currently part of build on the table.
     */
    public void setPartOfBuild(boolean partOfBuild) {
        this.partOfBuild = partOfBuild;
    }

    /**
     * Getter for buildBuddies private member variable
     * @return ArrayList of Card objects involved in a build.
     */
    public ArrayList<Card> getBuildBuddies() {
        return this.buildBuddies;
    }

    /**
     * Setter for buildBuddies private member variable
     * @param buildBuddies, ArrayList of Card objects involved in a build.
     */
    public void setBuildBuddies(ArrayList<Card> buildBuddies) {
        this.buildBuddies = buildBuddies;
    }

    /**
     * Getter for isRealCard private member variable
     * @return boolean variable representing whether or not a card is real.
     */
    public boolean getIsRealCard() {
        return this.isRealCard;
    }
}
