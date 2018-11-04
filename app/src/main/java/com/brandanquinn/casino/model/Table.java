package com.brandanquinn.casino.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Table {

    private ArrayList<Card> tableCards;
    private ArrayList<ArrayList<Card>> tableBuilds;
    private ArrayList<Build> currentBuilds;

    /**
     * Default constructor for Table class.
     */
    public Table() {
        tableBuilds = new ArrayList<>();
        tableCards = new ArrayList<>();
        currentBuilds = new ArrayList<>();
    }

    /**
     * Overloaded constructor for Table class; used in deserialization.
     * @param tableCards, ArrayList of Cards to be preloaded to table
     * @param currentBuilds, ArrayList of Builds currently on the table.
     */
    public Table(ArrayList<Card> tableCards, ArrayList<Build> currentBuilds) {
        for (int i = 0; i < tableCards.size(); i++) {
            addToTableCards(tableCards.get(i));
        }
        this.currentBuilds = currentBuilds;
    }

    /**
     * Getter for tableCards private member variable.
     * @return ArrayList of loose table cards
     */
    public ArrayList<Card> getTableCards() {
        return this.tableCards;
    }

    /**
     * Getter for tableBuilds private member variable
     * @return 2d ArrayList of all cards in table builds.
     */
    public ArrayList<ArrayList<Card>> getTableBuilds() {
        return this.tableBuilds;
    }

    /**
     * Getter for currentBuilds private member variable
     * @return ArrayList of Build objects on the table.
     */
    public ArrayList<Build> getCurrentBuilds() { return this.currentBuilds; }

    /**
     * Combines loose tableCards list with tableBuilds list to get full list of cards on the table.
     * @return 2d ArrayList of all cards on the table.
     */
    public ArrayList<ArrayList<Card>> getTotalTableCards() {
        ArrayList<ArrayList<Card>> tempVec = new ArrayList<>(this.tableBuilds);
        tempVec.add(this.tableCards);

        return tempVec;
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
     * @param buildsToRemove, ArrayList of Build objects to remove from the table.
     */
    public void removeBuilds(ArrayList<Build> buildsToRemove) {
        for (int i = 0; i < buildsToRemove.size(); i++) {
            ArrayList<ArrayList<Card>> tempBuildCards = new ArrayList<>(buildsToRemove.get(i).getTotalBuildCards());
            for (int j = 0; j < tempBuildCards.size(); j++) {
                removeFromTableBuilds(tempBuildCards.get(j));
            }
        }

        for (int i = 0; i < buildsToRemove.size(); i++) {
            this.currentBuilds.remove(buildsToRemove.get(i));
        }
    }

    /**
     * Gets whether the loose tableCards list is empty or not.
     * @return Boolean value representing whether or not the tableCards list is empty.
     */
    public boolean isEmpty() {
        return this.tableCards.isEmpty();
    }

    /**
     * Add new build to the currentBuilds vector
     * @param newBuild, Build object to add to the table.
     */
    public void addBuild(Build newBuild) {
        this.currentBuilds.add(newBuild);
    }

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

    /**
     * Find and remove builds given a list of cards.
     * @param cardsToRemove, ArrayList of Card objects to find Builds to remove.
     */
    private void removeFromTableBuilds(ArrayList<Card> cardsToRemove) {
        ArrayList<ArrayList<Card>> buildsToErase = new ArrayList<>();
        boolean buildErased = false;

        for (int i = 0; i < this.tableBuilds.size(); i++) {
            buildErased = false;
            for (int j = 0; j < this.tableBuilds.get(i).size(); j++) {
                for (int k = 0; k < cardsToRemove.size(); k++) {
                    if (tableBuilds.get(i).get(j).getCardString() == cardsToRemove.get(k).getCardString() && !buildErased) {
                        buildsToErase.add(this.tableBuilds.get(i));
                        buildErased = true;
                    }
                }
            }
        }

        for (int i = 0; i < buildsToErase.size(); i++) {
            this.tableBuilds.remove(buildsToErase.get(i));
        }
    }




}
