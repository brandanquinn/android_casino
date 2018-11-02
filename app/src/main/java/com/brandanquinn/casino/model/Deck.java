package com.brandanquinn.casino.model;

import java.util.*;

public class Deck {
    private ArrayList<Card> gameDeck;
    private Card topOfDeck;

    /**
     * Default constructor for Deck class.
     */
    public Deck() {
        char[] suits = {'S', 'C', 'H', 'D'};
        char[] types = {'2', '3', '4', '5', '6', '7', '8', '9', 'X', 'J', 'Q', 'K', 'A'};

        gameDeck = new ArrayList<>();

        for (int i = 0; i < 52; i++) {
            this.gameDeck.add(new Card(suits[i/13], types[i%13]));
        }

        shuffleDeck();
        this.topOfDeck = this.gameDeck.get(0);
    }

    /**
     * Overloaded constructor for Deck class, used for deserialzation and seeding deck
     * @param preLoadedDeck, ArrayList of cards to create preloaded deck with.
     */
    public Deck(ArrayList<Card> preLoadedDeck) {
        for (int i = 0; i < preLoadedDeck.size(); i++) {
            this.gameDeck.add(preLoadedDeck.get(i));
        }

        this.topOfDeck = this.gameDeck.get(0);
    }

    /**
     * Draws a single card from the top of the deck to be dealt accordingly.
     * @return Card object to be dealt or used by Players.
     */
    public Card drawCard() {
        if (gameDeck.isEmpty()) {
            return new Card();
        }

        Card cardDrawn = this.topOfDeck;
        gameDeck.remove(cardDrawn);
        this.topOfDeck = gameDeck.get(0);
        return cardDrawn;
    }

    /**
     * Shuffles the current deck.
     */
    public void shuffleDeck() {
        Collections.shuffle(this.gameDeck);
    }

    /**
     * Used to check if the deck is currently empty.
     * @return Boolean value determining whether or not the deck is empty.
     */
    public boolean isEmpty() {
        return this.gameDeck.isEmpty();
    }

    /**
     * Gets the current number of cards left in the deck.
     * @return int value as size of deck.
     */
    public int getNumCardsLeft() {
        return this.gameDeck.size();
    }

    /**
     * Stringifies the current deck for serialization / logging purposes.
     * @return
     */
    public String getDeckString() {
        String deckStr = "";

        for (int i = 0; i < this.gameDeck.size(); i++) {
            deckStr += gameDeck.get(i).getCardString();
        }

        return deckStr;
    }
}

