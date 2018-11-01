package com.brandanquinn.casino.model;

import java.util.*;

public class Card {
    /*
     * Function Name: Card
     * Purpose: Default constructor for Card class.
     * Params: None
     * Returns: None
     * Local Vars: None
     * Algo: None
     * Assistance: None
     */
    public Card() {
        setSuit('X');
        setType('0');
        this.isRealCard = false;
    }

    /*
     * Function Name: Card
     * Purpose: Overloaded constructor for Card class.
     * Params: char suit, char type
     * Returns: None
     * Local Variables: None
     * Algorithm: None
     * Assistance: None
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

    /*
     * Function Name: createMap
     * Purpose: Generate map to pair card type to integer value.
     * Params: None
     * Return Value: None
     * Local Variables: None
     * Algorithm: None
     * Assistance: None
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

    /*
     * Function Name: getSuit
     * Purpose: Get the card's suit.
     * Params: None
     * Return Value: char suit
     * Local Variables: None
     * Algorithm: None
     * Assistance: None
     */
    public char getSuit() {
        return this.suit;
    }

    /*
 	* Function Name: setSuit
 	* Purpose: Set the card's suit.
 	* Params:
		char suit, Suit to set member variable to.
 	* Return Value: None
 	* Local Variables: None
 	* Algorithm: None
 	* Assistance: None
 	*/
    public void setSuit(char suit) {
        this.suit = suit;
    }

    /*
     * Function Name: getType
     * Purpose: Get the card's type.
     * Params: None
     * Return Value: character value representing the value or rank of the playing card.
     * Local Variables: None
     * Algorithm: None
     * Assistance: None
     */
    public char getType() {
        return this.type;
    }

    /*
 	* Function Name: setType
 	* Purpose: Set the card's type.
 	* Params:
		char type, character value representing the value or rank of the playing card.
 	* Return Value: None
 	* Local Variables: None
 	* Algorithm: None
 	* Assistance: None
 	*/
    public void setType(char type) {
        this.type = type;
    }

    /*
     * Function Name: get_value
     * Purpose: Get the card's value.
     * Params: None
     * Return Value: Returns the card's value represented as an integer.
     * Local Variables: None
     * Algorithm: None
     * Assistance: None
     */
    public int getValue() {
        return this.value;
    }

    /*
 	* Function Name: setValue
 	* Purpose: Set the value of a card to based on its type / rank.
 	* Params: None
 	* Return Value: None
 	* Local Variables: None
 	* Algorithm:
		1. Check type_value map to get corresponding value.
 		2. Assign proper value to member variable.
 	* Assistance: None
 	*/
    public void setValue() {
        this.value = typeValuePairs.get(this.type);
    }

    /*
 	* Function Name: getCardString
 	* Purpose: Generate string containing suit and type of a card.
 	* Params: None
 	* Return Value: String
 	* Local Variables: cardString, Used to concatenate suit and type of card.
 	* Algo:
		1. Create string for concatenation
 	* 	2. To add the char member variables using '+' operator, use string() as the
 	* 	 first arg so that compiler knows you want a string not an integer.
 	* 	3. Return string.
 	* Assistance: None
 	*/
    public String getCardString() {
        String cardString = "" + getSuit() + getType();
        return cardString;
    }

    /*
    Function Name: getImageResourceName
    Purpose: Get a string for each card that matches the naming scheme for the drawable resources
    Parameters: None
    Return Value: String in the form: "suit_type.png"
    Local Variables: None
    Algorithm: None
    Assistance Received: None
     */
    public String getImageResourceName() {
        return " " + Character.toLowerCase(getSuit()) + "_" + getType() + ".png";
    }

    /*
	Function Name: getLockedToBuild
	Purpose: Getter for lockedToBuild private member variable.
	Parameters: None
	Return Value: Whether or not the current Card is locked to a build as a capture card, represented as a boolean
	Local Variables: None
	Algorithm: None
	Assistance: None
	*/
    public boolean getLockedToBuild() {
        return this.lockedToBuild;
    }

    /*
	Function Name: setLockedToBuild
	Purpose: Setter for lockedToBuild private member variable
	Parameters:
		bool lockedToBuild, Whether or not the current Card is locked to a build as a capture card, represented as a boolean
	Return Value: None
	Algorithm: None
	Assistance: None
	*/
    public void setLockedToBuild(boolean lockedToBuild) {
        this.lockedToBuild = lockedToBuild;
    }

    /*
	Function Name: getPartOfBuild
	Purpose: Getter for partOfBuild private member variable
	Parameters: None
	Return Value: Whether or not the current Card is part of a build, represented as a boolean.
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
    public boolean getPartOfBuild() {
        return this.partOfBuild;
    }

    /*
	Function Name: setPartOfBuild
	Purpose: Setter for partOfBuild private member variable.
	Parameters:
		bool partOfBuild, Whether or not the current Card is part of a build, represented as a boolean.
	Return Value: None
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
    public void setPartOfBuild(boolean partOfBuild) {
        this.partOfBuild = partOfBuild;
    }

    /*
	Function Name: getBuildBuddies
	Purpose: Getter for buildBuddies private member variable
	Parameters: None
	Return Value: If card is part of a build, contains the cards that are in that build.
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
    public ArrayList<Card> getBuildBuddies() {
        return this.buildBuddies;
    }

    /*
	Function Name: setBuildBuddies
	Purpose: Setter for buildBuddies private member variable
	Parameters:
		vector<Card*> buildBuddies, If card is part of a build, contains the cards that are in that build.
	Return Value: None
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
    public void setBuildBuddies(ArrayList<Card> buildBuddies) {
        this.buildBuddies = buildBuddies;
    }
    /*
	Function Name: getIsRealCard
	Purpose: Getter for isRealCard private member variable
	Parameters: None
	Return Value: Whether or not card created is a real card
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
    public boolean getIsRealCard() {
        return this.isRealCard;
    }

    private char suit;
    private char type;
    private int value;
    private HashMap<Character, Integer> typeValuePairs;
    private boolean lockedToBuild;
    private boolean partOfBuild;
    private boolean isRealCard;
    private ArrayList<Card> buildBuddies;
}
