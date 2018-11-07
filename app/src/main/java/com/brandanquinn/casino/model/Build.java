package com.brandanquinn.casino.model;

import java.util.ArrayList;

public class Build {
    private boolean isMultiBuild;
    private int sumVal;
    private Card sumCard;
    ArrayList<ArrayList<Card>> totalBuildCards;
    String buildOwner;

    /**
     * Constructor for the Build class.
     * @param buildCards, ArrayList of Cards involved in the build created.
     * @param sumVal, int Value that the Cards sum to.
     * @param sumCard, Card in hand that the build sums to.
     * @param buildOwner, String representing which player owns the build.
     */
    public Build(ArrayList<ArrayList<Card>> buildCards, int sumVal, Card sumCard, String buildOwner) {
        if (buildCards.size() > 1) {
            this.isMultiBuild = true;
        } else {
            this.isMultiBuild = false;
        }
        this.totalBuildCards =  buildCards;
        this.sumVal = sumVal;
        this.sumCard = sumCard;
        this.buildOwner = buildOwner;
    }

    /**
     * Getter for private member variable isMultiBuild
     * @return, boolean value representing whether or not build is a multibuild
     */
    public boolean getIsMultiBuild() { return this.isMultiBuild; }

    /**
     * Setter for private member variable isMultiBuild
     * @param isMultiBuild, boolean value to set isMultiBuild to.
     */
    public void setMultiBuild(boolean isMultiBuild) { this.isMultiBuild = isMultiBuild; }

    /**
     * Getter for private member variable sumVal
     * @return int value that the build cards sum to.
     */
    public int getSumVal() { return this.sumVal; }

    /**
     * Setter for private member variable sumVal
     * @param sumVal, int value to set sumVal to.
     */
    public void setSumVal(int sumVal) { this.sumVal = sumVal; }

    /**
     * Getter for private member variable sumCard
     * @return Card object that the build is locked to.
     */
    public Card getSumCard() { return this.sumCard; }

    /**
     * Setter for private member variable sumCard
     * @param sumCard, Card object to set sumCard to.
     */
    public void setSumCard(Card sumCard) { this.sumCard = sumCard; }

    /**
     * Getter for private member variable totalBuildCards
     * @return ArrayList of total cards involved in the build.
     */
    public ArrayList<ArrayList<Card>> getTotalBuildCards() { return this.totalBuildCards; }

    /**
     * Getter for private member variable buildOwner
     * @return String representing which player currently owns the build.
     */
    public String getBuildOwner() { return this.buildOwner; }

    /**
     * Used to extend a single build object to a multibuild
     * @param cardsToExtend, An ArrayList of new cards to extend the build with.
     */
    public void extendBuild(ArrayList<Card> cardsToExtend) {
        this.totalBuildCards.add(cardsToExtend);
        this.isMultiBuild = true;
    }

    /**
     * Used to create a Stringified build object with owner for seriailization.
     * @return String version of build object + buildOwner
     */
    public String getBuildStringWithOwner() {
        String buildStr = "";

        if (this.isMultiBuild) { buildStr += "[ "; }
        for (int i = 0; i < this.totalBuildCards.size(); i++) {
            for (int j = 0; j < this.totalBuildCards.get(i).size(); j++) {
                if (j == 0) { buildStr += "[ "; }
                buildStr += this.totalBuildCards.get(i).get(j).getCardString() + " ";
            }
            buildStr += "] ";
        }
        if (this.isMultiBuild) { buildStr += "] "; }

        return buildStr + getBuildOwner();
    }

    /**
     * Used to create a Stringified build object for view / logging purposes.
     * @return String version of build object
     */
    public String getBuildString() {
        String buildStr = "";

        if (this.isMultiBuild) { buildStr += "[ "; }
        for (int i = 0; i < this.totalBuildCards.size(); i++) {
            for (int j = 0; j < this.totalBuildCards.get(i).size(); j++) {
                if (j == 0) { buildStr += "[ "; }
                buildStr += this.totalBuildCards.get(i).get(j).getCardString() + " ";
            }
            buildStr += "] ";
        }
        if (this.isMultiBuild) { buildStr += "] "; }

        return buildStr;
    }

    /**
     * Increase and claim an opponent's build.
     * @param increasingCard, Card object to increase build with.
     */
    public void increaseBuild(Card increasingCard, String newOwner) {
        this.totalBuildCards.get(0).add(increasingCard);
        int sum = 0;

        for (int i = 0; i < totalBuildCards.get(0).size(); i++) {
            sum += totalBuildCards.get(0).get(i).getValue();
        }
        setSumVal(sum);
        this.buildOwner = newOwner;
    }
}
