package com.brandanquinn.casino.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Table {
    /*
	Function Name: Table
	Purpose: Default constructor for Table class
	Parameters: None
	Return Value: None
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
    public Table() {
        totalTableCards = new ArrayList<>();
        tableBuilds = new ArrayList<>();
        tableCards = new ArrayList<>();
    }

    /*
	Function Name: Table
	Purpose: Overloaded constructor for Table class; used in deserialization
	Parameters:
		ArrayList<Card> tableCards, vector of cards to add to the table
		ArrayList<Build> currentBuilds, vector of builds on the table
	Return Value: None
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
//    public Table(ArrayList<Card> tableCards, ArrayList<Build> currentBuilds) {
//
//    }

    /*
	Function Name: getTableCards
	Purpose: Getter for table_cards private member variables
	Parameters: None
	Return Value: Vector of loose table cards
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
    public ArrayList<Card> getTableCards() {
        return this.tableCards;
    }

    /*
	Function Name: getTotalTableCards
	Purpose: Gets a 2d vector of loose table cards and cards from builds
	Parameters: None
	Return Value: 2d vector of ALL table cards
	Local Variables:
		ArrayList<ArrayList<Card>> tempVec, vector to hold all table cards
	Algorithm:
		1. Initialize tempVec to hold tableBuild member variable
		2. Add loose table cards to tempVec
		3. Return tempVec
	Assistance Received: None
	*/
    public ArrayList<ArrayList<Card>> getTotalTableCards() {
        return this.totalTableCards;
    }

    /*
	Function Name: addToTableCards
	Purpose: Checks whether card is part of a build or not and adds it to proper container
	Parameters:
		Card* new_card, card to be added to table
	Return Value: None
	Local Variables:
		ArrayList<Card> buildBuddies, Vector of cards in a build
	Algorithm:
		1. If newCard is part of a build:
			a. Get its buildBuddies
			b. For each card in buildBuddies
				i. Remove it from loose table cards vector
			c. If buildBuddies aren't part of a build on the table
				a. Add them to known table builds
		2. Else
			a. Add newCard to loose table cards.
	Assistance Received: None
	*/
    public void addToTableCards(Card newCard) {
        if (newCard.getPartOfBuild()) {
            ArrayList<Card> buildBuddies = newCard.getBuildBuddies();

            for (int i = 0; i < buildBuddies.size(); i++) {
                removeCardFromVector(this.tableCards, buildBuddies.get(i));
            }

            if (!doesBuildExist(buildBuddies)) {
                for (int i = 0; i < buildBuddies.size(); i++) {
                    buildBuddies.get(i).setPartOfBuild(true);
                }
                this.tableBuilds.add(0, buildBuddies);
            }
        } else {
            this.tableCards.add(newCard);
        }
    }

    /*
	Function Name: clearTableCards
	Purpose: Remove loose cards from table
	Parameters: None
	Return Value: None
	Local Variables: None
	Algorithm: Clear table cards vector
	Assistance Received: None
	*/
    public void clearTableCards() {
        this.tableCards.clear();
    }

    /*
	Function Name: removeCards
	Purpose: Remove cards from loose table cards vector
	Parameters:
		ArrayList<Card> cardsToRemove, Cards to remove from table
	Return Value: None
	Local Variables: None
	Algorithm:
		1. For each card in cardsToRemove:
			a. Remove card from loose table cards vector
	Assistance Received: None
	*/
    public void removeCards(ArrayList<Card> cardsToRemove) {
        for (int i = 0; i < cardsToRemove.size(); i++) {
            removeCardFromVector(this.tableCards, cardsToRemove.get(i));
        }
    }

    /*
	Function Name: removeSets
	Purpose: Remove sets of cards from loose table cards vector
	Parameters:
		ArrayList<ArrayList<Card>> setsToRemove, sets to remove from table
	Return Value: None
	Local Variables: None
	Algorithm:
		1. For each set to remove:
			a. Call removeCards(set)
	Assistance Received: None
	*/
    public void removeSets(ArrayList<ArrayList<Card>> setsToRemove) {
        for (int i = 0; i < setsToRemove.size(); i++) {
            removeCards(setsToRemove.get(i));
        }
    }

    /*
	Function Name: removeBuilds
	Purpose: Remove captured builds from table
	Parameters:
		vector<Build*> buildsToRemove, builds from remove from table
	Return Value: None
	Local Variables:
		vector<vector<Card*> tempBuildCards, temp 2d vec to hold build cards
	Algorithm:
		1. For each build to remove:
			a. Get total build cards
			b. For each vector of cards in total build cards:
				i. Remove card from tableBuilds
		2. For each build to remove:
			a. Delete build object from currentBuilds vector
	Assistance Received: None
	*/
//    public void removeBuilds(ArrayList<Build> buildsToRemove) {
//
//    }

    /*
	Function Name: isEmpty
	Purpose: Check if there are no loose table cards
	Parameters: None
	Return Value: Boolean value that represents whether or not there are loose table cards.
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
    public boolean isEmpty() {
        return this.tableCards.isEmpty();
    }

    /*
	Function Name: addBuild
	Purpose: Add new build to currentBuilds vector
	Parameters:
		Build newBuild, pointer to new build object
	Return Value: None
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
//    public void addBuild(Build newBuild) {
//
//    }

    /*
	Function Name: getCurrentBuilds
	Purpose: Getter for currentBuilds private member variable
	Parameters: None
	Return Value: Vector of current builds on table
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
//    public ArrayList<Build> getCurrentBuilds() {
//
//    }

    /*
	Function Name: getFlattenedCardList
	Purpose: Generates a single vector of all cards on the table
	Parameters: None
	Return Value: Vector of ALL table cards
	Local Variables:
		ArrayList<Card> flatList, Vector of all table cards to be returned
	Algorithm:
		1. Initialize flatList
		2. For each set of cards in tableBuilds:
			a. For each card in the set:
				i. Add card to flatList
		3. For each loose card on the table
			a. Add card to flatList
		4. Return flatList
	Assistance Received: None
	*/
    public ArrayList<Card> getFlattenedCardList() {
        ArrayList<Card> flatList = new ArrayList<>();

        for (int i = 0; i < tableBuilds.size(); i++) {
            for (int j = 0; j < tableBuilds.get(i).size(); j++) {
                flatList.add(tableBuilds.get(i).get(j));
            }
        }

        for (int i = 0; i < tableCards.size(); i++) {
            flatList.add(tableCards.get(i));
        }

        return flatList;
    }

    /*
    Function Name: getTableString
    Purpose: Generate a stringified representation of the current game table
    Parameters: None
    Return Value: Table object as string
    Local Variables:
        String tableStr, String variable used to concatenate build and card strings
    Algorithm:
        1. Init tableStr
        2. For each build in currentBuilds:
            a. Add build string to tableStr
        3. For each card in loose table cards:
            a. Add card string to tableStr
        4. Return tableStr
    Assistance Received: None
     */
    public String getTableString() {
        String tableStr = "";

        return tableStr;
    }

    /*
	Function Name: removeCardFromVector
	Purpose: Remove a single card from a vector
	Parameters:
		ArrayList<Card> cardList, vector to remove card from
		Card cardToRemove, card to remove from card_list
	Return Value: None
	Local Variables: None
	Algorithm: None
	Assistance Received: None
	*/
    private void removeCardFromVector(ArrayList<Card> cardList, Card cardToRemove) {
        cardList.remove(cardToRemove);
    }

    /*
	Function Name: doesBuildExist
	Purpose: Check if build cards already exist on the table
	Parameters:
		ArrayList<Card> buildBuddies, Cards in build
	Return Value: Boolean value representing whether or not the build exists ont able
	Local Variables: None
	Algorithm:
		1. For each set of cards in tableBuilds:
			a. For each card in the set:
				i. For each card in buildBuddies:
					- if set card == buildBuddies card: return true
		2. Return false
	Assistance Received: None
	*/
    private boolean doesBuildExist(ArrayList<Card> buildBuddies) {
        for (int i = 0; i < tableBuilds.size(); i++) {
            for (int j = 0; j < tableBuilds.get(i).size(); j++) {
                for (int k = 0; k < buildBuddies.size(); k++)
                    if (tableBuilds.get(i).get(j).getCardString() == buildBuddies.get(i).getCardString()) return true;
            }
        }

        return false;
    }

    /*
    Function Name: removeFromTableBuilds
    Purpose: Remove cards from table builds
    Parameters:
        ArrayList<Card> cardsToRemove, Cards to remove from table builds
    Return Value: None
    Local Variables:
        ArrayList<ArrayList<Card>> buildsToErase, 2d vector to keep track of sets of cards to be erased
        boolean buildErased, keeps track of whether or not the build has been erased
    Algorithm:
        1. Initialize local variables
        2. For each set of cards in tableBuilds:
            a. For each card in the set:
                i. For each card to remove:
                    - If set card == card to remove and !buildErased:
                        - Add set to buildsToErase
                        - Set buildErased to true
        3. For each build to erase:
            a. Remove build from tableBuilds
    Assistance Received: None
    */
//    private void removeFromTableBuilds(ArrayList<Card> cardsToRemove) {
//
//    }

    private ArrayList<ArrayList<Card>> totalTableCards;
    private ArrayList<Card> tableCards;
    private ArrayList<ArrayList<Card>> tableBuilds;
//    private ArrayList<Build> currentBuilds;


}
