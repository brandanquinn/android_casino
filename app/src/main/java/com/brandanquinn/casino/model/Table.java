package com.brandanquinn.casino.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Table {

    private ArrayList<ArrayList<Card>> totalTableCards;
    private ArrayList<Card> tableCards;
    private ArrayList<ArrayList<Card>> tableBuilds;
//    private ArrayList<Build> currentBuilds;

    /**
     * Default constructor for Table class.
     */
    public Table() {
        totalTableCards = new ArrayList<>();
        tableBuilds = new ArrayList<>();
        tableCards = new ArrayList<>();
    }

    /**
     * Overloaded Table constructor used in deserialization
     */
//    public Table(ArrayList<Card> tableCards, ArrayList<Build> currentBuilds) {
//
//    }

    /**
     * Getter for tableCards private member variable.
     * @return
     */
    public ArrayList<Card> getTableCards() {
        return this.tableCards;
    }

    /**
     * Combines loose tableCards list with tableBuilds list to get full list of cards on the table.
     * @return 2d ArrayList of all cards on the table.
     */
    public ArrayList<ArrayList<Card>> getTotalTableCards() {
        return this.totalTableCards;
    }

    /**
     * Used to add a card to the table, if card is part of a build it will evaluate where it should be placed within the tableBuilds list
     * @param newCard, Card object to be added to the table.
     */
    public void addToTableCards(Card newCard) {
        if (newCard.getPartOfBuild()) {
            ArrayList<Card> buildBuddies = newCard.getBuildBuddies();

            for (int i = 0; i < buildBuddies.size(); i++) {
                this.tableCards.remove(buildBuddies.get(i));
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

    /**
     * Clears loose table cards.
     */
    public void clearTableCards() {
        this.tableCards.clear();
    }

    /**
     * Removes a list of cards from tableCards list, used for set capture.
     * @param cardsToRemove, ArrayList of Card objects to remove
     */
    public void removeCards(ArrayList<Card> cardsToRemove) {
        for (int i = 0; i < cardsToRemove.size(); i++) {
            this.tableCards.remove(cardsToRemove.get(i));
        }
    }

    /**
     * Removes multiple sets of cards by calling removeCards on each individual set.
     * @param setsToRemove, 2d ArrayList of Card objects to remove.
     */
    public void removeSets(ArrayList<ArrayList<Card>> setsToRemove) {
        for (int i = 0; i < setsToRemove.size(); i++) {
            removeCards(setsToRemove.get(i));
        }
    }

    /**
     * Used to remove captured builds from the table.
     * describe param when finished
     */
//    public void removeBuilds(ArrayList<Build> buildsToRemove) {
//
//    }

    /**
     * Gets whether the loose tableCards list is empty or not.
     * @return Boolean value representing whether or not the tableCards list is empty.
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

    /**
     * Breaks down 2d lists of cards into a single dimensional list to be used in serialization etc.
     * @return ArrayList of all cards on the table.
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

    /**
     * Generate a stringified table object for serialization.
     * @return
     */
    public String getTableString() {
        String tableStr = "";

        return tableStr;
    }

    /**
     * Used to check if a build exists on the table by checking each individual set of cards in tableBuilds.
     * @param buildBuddies, An ArrayList of Cards in a build.
     * @return Boolean value representing whether or not the build exists.
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
//    }




}
