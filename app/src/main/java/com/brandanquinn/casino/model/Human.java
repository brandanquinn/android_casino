package com.brandanquinn.casino.model;

import android.util.Pair;

import com.brandanquinn.casino.casino.GameScreen;

import java.util.ArrayList;

public class Human extends Player {

    /**
     * Default constructor for Human class
     */
    public Human() {
        setScore(0);
        setPlayerString("Human");
    }

    /**
     * Overloaded constructor for Human class
     * @param score, int value to set score to
     */
    public Human(int score) {
        setScore(score);
        setPlayerString("Human");
    }

    /**
     * Play function that interacts with controller to get move selection from user.
     * @return Pair that holds Card selected for play as well as move selected.
     */
    public Pair<ArrayList<Card>, String> play() {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < this.hand.size(); i++) {
            if (this.hand.get(i).getCardString().equals(cardSelectedFromHand)) {
                cards.add(this.hand.get(i));
            }
        }
        if (moveSelected == "build") {
            for (int i = 0; i < this.hand.size(); i++) {
                if (this.hand.get(i).getCardString().equals(cardPlayedIntoBuild)) {
                    cards.add(this.hand.get(i));
                }
            }
        }

        ArrayList<Card> tableCards = this.gameTable.getTableCards();
        if (moveSelected != "trail") {
            for (int i = 0; i < cardsSelectedFromTable.size(); i++) {
                for (int j = 0; j < tableCards.size(); j++) {
                    if (tableCards.get(j).getCardString().equals(cardsSelectedFromTable.get(i))) {
                        cards.add(tableCards.get(j));
                    }
                }
            }
        }

        return new Pair<>(cards, moveSelected);
    }
}
