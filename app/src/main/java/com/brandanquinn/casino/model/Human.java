package com.brandanquinn.casino.model;

import java.util.ArrayList;

public class Human extends Player {

    /**
     * Default constructor for Human class
     */
    public Human() {
        setScore(0);
    }

    /**
     * Overloaded constructor for Human class
     * @param score, int value to set score to
     */
    public Human(int score) {
        setScore(score);
    }

    /**
     * Polymorphic function to get player identity.
     * @return String representing player identity.
     */
    @Override
    public String getPlayerIdentity() {
        return "Human";
    }

    /**
     * Play function that interacts with controller to get move selection from user.
     * @return Pair that holds Card selected for play as well as move selected.
     */
    @Override
    public Move play() {
        if (moveSelected.equals("help")) {
            return getHelp();
        }

        Card cardSelected = new Card();
        for (int i = 0; i < this.hand.size(); i++) {
            if (this.hand.get(i).getCardString().equals(cardSelectedFromHand)) {
                cardSelected = this.hand.get(i);
            }
        }

        Card cardPlayed = new Card();
        if (moveSelected.equals("build") || moveSelected.equals("increase")) {
            for (int i = 0; i < this.hand.size(); i++) {
                if (this.hand.get(i).getCardString().equals(cardPlayedIntoBuild)) {
                    cardPlayed = this.hand.get(i);
                }
            }
        }

        ArrayList<Card> tableCards = this.gameTable.getTableCards();
        ArrayList<Build> currentBuilds = this.gameTable.getCurrentBuilds();
        ArrayList<Card> cardsSelected = new ArrayList<>();
        ArrayList<Build> buildsSelected = new ArrayList<>();
        if (!moveSelected.equals("trail")) {
            for (int i = 0; i < cardsSelectedFromTable.size(); i++) {
                for (int j = 0; j < tableCards.size(); j++) {
                    if (tableCards.get(j).getCardString().equals(cardsSelectedFromTable.get(i))) {
                        cardsSelected.add(tableCards.get(j));
                    }
                }
                for (int j = 0; j < currentBuilds.size(); j++) {
                    if (currentBuilds.get(j).getBuildStringForView().equals(cardsSelectedFromTable.get(i))) {
                        buildsSelected.add(currentBuilds.get(j));
                    }
                }
            }
        }

        return new Move(moveSelected, cardSelected, cardPlayed, cardsSelected, buildsSelected);
    }
}
