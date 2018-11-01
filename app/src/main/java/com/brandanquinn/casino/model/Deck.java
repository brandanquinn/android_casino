package com.brandanquinn.casino.model;

import java.util.*;

public class Deck {

    /*
     * Function Name: Deck()
     * Purpose: Default constructor for Deck class; used to randomly generate a deck of cards.
     * Will be called at the beginning of every Round.
     * Params: None
     * Local variables: char suits[4] - Array of suits, char types[13] - Array of card types.
     * Algo: Instead of hard coding the entire 52 card deck, I decided to use a simple for loop
     * that works on the premise that the deck is split evenly into 4 suits and 13 types. To
     * make sure the suits were applied correctly, I found that you could use the division
     * operator to apply suit[0] ('S' or spades) to the first 14 cards because x/13=0 if
     * x < 13. That same logic can be applied to the other suits. I use similar logic for the
     * types array but instead use the modulus operator to incrementally apply types to each
     * card in the deck.
     * Return: None
     * Assistance: Neel helped me figure out an issue with pointer handling semantics during Card creation.
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

    /*
     * Function Name: Deck(vector<Card*> pre_loaded_deck)
     * Purpose: Overloaded constructor for Deck class; will be used to generate a serialized deck.
     * Params: string pre_loaded_deck
     * Return: None
     */
    public Deck(ArrayList<Card> preLoadedDeck) {
        for (int i = 0; i < preLoadedDeck.size(); i++) {
            this.gameDeck.add(preLoadedDeck.get(i));
        }

        this.topOfDeck = this.gameDeck.get(0);
    }

    /*
	* Function Name: drawCard()
	* Purpose: Will draw a card off the top of the deck and return that card to be handled by player.
	* Params: None
	* Local Variables: Card card_drawn, a pointer to keep track of the card drawn off the top of the deck.
	* Algo:
		1. If deck is empty: Let user know and return default card.
	* 	2. Create a pointer and pass the address of the top card of the deck.
	* 	3. Print the card drawn to console.
	* 	4. Remove the top card from the deck.
	* 	5. Set the top_of_deck pointer to current top card.
	* Return: Card * card_drawn - pointer to the Card drawn.
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

    /*
     * Function Name: shuffleDeck()
     * Purpose: Shuffles the deck of cards.
     * Params: None
     * Local Variables:
     * Algo:
     * 	1. Seed random generator based on time.
     * 	2. Shuffle deck.
     * Return: None
     */
    public void shuffleDeck() {
        Collections.shuffle(this.gameDeck);
    }

    /*
	Function Name: isEmpty
	Purpose: Determines if deck is empty.
	Parameters: None
	Local Variables: None
	Return Value: A boolean value that represents whether or not the game deck is empty.
	Algorithm: None
	*/
    public boolean isEmpty() {
        return this.gameDeck.isEmpty();
    }

    /*
	Function Name: getNumCardsLeft
	Purpose: Computes the number of cards left in the deck.
	Parameters: None
	Local Variables: None
	Return Value: The size of the current game deck.
	Algorithm: None
	*/
    public int getNumCardsLeft() {
        return this.gameDeck.size();
    }

    /*
	Function Name: getDeckString
	Purpose: Generates a string representation of the game deck.
	Parameters: None
	Local Variables:
		queue<Card*> gameDeckCopy, used to copy the game_deck for manipulation
		string deckStr, string variable used to concatenate the deck string
	Return Value: The string representation of the game deck.
	Algorithm:
		1. Copy the game_deck queue to another variable.
		2. Initialize an empty string variable, deck_str for concatenation.
		3. For each card in the deck:
			a. Add the card string to deckStr.
			b. Pop the card off the gameDeck copy.
		4. Return the deckStr
	*/
    public String getDeckString() {
        String deckStr = "";

        for (int i = 0; i < this.gameDeck.size(); i++) {
            deckStr += gameDeck.get(i).getCardString();
        }

        return deckStr;
    }

    private ArrayList<Card> gameDeck;
    private Card topOfDeck;
}

