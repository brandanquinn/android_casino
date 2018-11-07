package com.brandanquinn.casino.model;

import com.brandanquinn.casino.casino.GameScreen;

import java.util.ArrayList;

public class Round {
    private int roundNum;
    private Deck gameDeck;
    private Table gameTable;
    private ArrayList<Player> gamePlayers;

    /**
     * Overloaded constructor for Round class.
     * @param roundNum, int value used to set current round number
     * @param gamePlayers, ArrayList of players of the game
     */
    public Round(int roundNum, ArrayList<Player> gamePlayers) {
        this.roundNum = roundNum;
        this.gameDeck = new Deck();
        this.gameTable = new Table();
        this.gamePlayers = gamePlayers;
    }


    /**
     * Getter for gamePlayers private member variable.
     * @return ArrayList of game player objects.
     */
    public ArrayList<Player> getGamePlayers() {
        return this.gamePlayers;
    }

    /**
     * Allows controller to check if Deck is empty.
     * @return boolean value determining whether or not deck is empty.
     */
    public boolean deckIsEmpty() {
        return this.gameDeck.isEmpty();
    }

    /**
     * Function where most of the game occurs, rotates through Players turns and allows them to select and make proper moves.
     * @param humanIsFirst, boolean value used to determine which player goes first.
     * @param loadedGame, boolean value used to determine whether cards are preloaded are need to be dealt.
     * @param deckList, ArrayList of cards if user chose to use a seeded deck.
     */
    public void startGame(boolean humanIsFirst, boolean loadedGame, ArrayList<Card> deckList) {
        if (!loadedGame) {
            if (!deckList.isEmpty()) {
                this.gameDeck = new Deck(deckList);
            }
            dealHands();
            dealToTable();
        }

        Player playerOne, playerTwo;

        if (humanIsFirst) {
            playerOne = this.gamePlayers.get(0);
            playerTwo = this.gamePlayers.get(1);
        } else {
            playerOne = this.gamePlayers.get(1);
            playerTwo = this.gamePlayers.get(0);
        }

        playerOne.setGameTable(this.gameTable);
        playerTwo.setGameTable(this.gameTable);

        playerOne.setIsPlaying(true);

        GameScreen.updateActivity(this.gamePlayers, this.gameTable, this.roundNum, true);
    }

    /**
     * Used to print who's turn it is to a TextView in the main activity
     * @return String denoting who's turn it currently is.
     */
    public String whoIsPlaying() {
        String playing = "Unknown";
        for (int i = 0; i < this.gamePlayers.size(); i++) {
            if (gamePlayers.get(i).getIsPlaying()) {
                playing = gamePlayers.get(i).getPlayerString();
            }
        }

        return playing + " is playing.";
    }

    /**
     * Properly deals hands to the Players
     */
    public void dealHands() {
        for (int i = 0; i < 4; i++) {
            Card newCard = this.gameDeck.drawCard();
            if (newCard.getIsRealCard()) {
                this.gamePlayers.get(0).addToHand(newCard);
            }
        }

        for (int i = 0; i < 4; i++) {
            Card newCard = this.gameDeck.drawCard();
            if (newCard.getIsRealCard()) {
                this.gamePlayers.get(1).addToHand(newCard);
            }
        }

        GameScreen.updateActivity(this.gamePlayers, this.gameTable, this.roundNum, true);
    }

    /**
     * Properly deals cards to the table.
     */
    private void dealToTable() {
        for (int i = 0; i < 4; i++) {
            Card newCard = this.gameDeck.drawCard();
            if (newCard.getIsRealCard()) {
                this.gameTable.addToTableCards(newCard);
            }
        }
    }

    /**
     * Allows each player to select moves until a possible move is made.
     * @param gamePlayer, Player currently making a move.
     * @return String to tell user what move was made.
     */
    public String playTurn(Player gamePlayer) {
        boolean possibleMoveSelected = false;
        while (!gamePlayer.handIsEmpty() && !possibleMoveSelected) {
            Move moveObj = gamePlayer.play();
            if (moveObj.getMoveType().equals("trail")) {
                possibleMoveSelected = trail(moveObj.getCardSelectedFromHand(), gamePlayer);
                if (possibleMoveSelected) {
                    changeTurn();
                    return "" + gamePlayer.getPlayerString() + " selected to " + moveObj.getMoveType() + " with card: " + moveObj.getCardSelectedFromHand().getCardString();
                } else {
                    return "Trail move cannot be made.";
                }
            } else if (moveObj.getMoveType().equals("build")) {
                possibleMoveSelected = build(moveObj.getCardSelectedFromHand(), moveObj.getCardPlayedFromHand(), moveObj.getCardsSelectedFromTable(), gamePlayer);
                if (possibleMoveSelected) {
                    changeTurn();
                    return "" + gamePlayer.getPlayerString() + " selected to " + moveObj.getMoveType() + " with card: " + moveObj.getCardSelectedFromHand().getCardString();
                } else {
                    return "Build move cannot be made with cards selected.";
                }
            } else if (moveObj.getMoveType().equals("capture")) {
                possibleMoveSelected = capture(moveObj.getCardSelectedFromHand(), moveObj.getCardsSelectedFromTable(), moveObj.getBuildsSelectedFromTable(), gamePlayer);
                if (possibleMoveSelected) {
                    changeTurn();
                    return "" + gamePlayer.getPlayerString() + " selected to " + moveObj.getMoveType() + " with card: " + moveObj.getCardSelectedFromHand().getCardString();
                } else {
                    return "Capture move cannot be made with cards selected.";
                }
            } else if (moveObj.getMoveType().equals("increase")) {
                // increase
                possibleMoveSelected = increase(moveObj.getCardSelectedFromHand(), moveObj.getCardPlayedFromHand(), moveObj.getBuildsSelectedFromTable(), gamePlayer);
                if (possibleMoveSelected) {
                    changeTurn();
                    return "" + gamePlayer.getPlayerString() + " selected to " + moveObj.getMoveType() + " with card: " + moveObj.getCardSelectedFromHand().getCardString();
                } else {
                    return "Cannot increase build with cards selected.";
                }
            }

        }

        changeTurn();
        return "Move cannot be made.";
    }

    /**
     * Used to change from one player's turn to the other.
     */
    private void changeTurn() {
        if (gamePlayers.get(0).getIsPlaying()) {
            gamePlayers.get(0).setIsPlaying(false);
            gamePlayers.get(1).setIsPlaying(true);
        } else {
            gamePlayers.get(1).setIsPlaying(false);
            gamePlayers.get(0).setIsPlaying(true);
        }

        if (gamePlayers.get(0).handIsEmpty() && gamePlayers.get(1).handIsEmpty()) {
            dealHands();
        } else {
            GameScreen.updateActivity(this.gamePlayers, this.gameTable, this.roundNum, true);
        }
    }

    /**
     * Checks to make sure a trail move can be made, and makes it if possible.
     * @param cardPlayed, Card object to be played to the table.
     * @param gamePlayer, Player making the trail move
     * @return boolean value determining whether or not move was made.
     */
    private boolean trail(Card cardPlayed, Player gamePlayer) {
        if (cardPlayed.getLockedToBuild()) {
            return false;
        }
        gamePlayer.discard(cardPlayed);
        this.gameTable.addToTableCards(cardPlayed);
        return true;
    }

    /**
     * Assesses a build move and makes the move if it is possible.
     * @param lockedCard, Card in hand that was used to sum the build to
     * @param playedCard, Card played from hand into build.
     * @param selectedFromTable, Cards selected from the table by user.
     * @param gamePlayer, Player currently making move.
     * @return
     */
    private boolean build(Card lockedCard, Card playedCard, ArrayList<Card> selectedFromTable, Player gamePlayer) {
        ArrayList<Card> tableCards = new ArrayList<>(this.gameTable.getTableCards());
        ArrayList<Card> filteredCards = new ArrayList<>(tableCards);
        boolean extendingBuild = false;

        if (lockedCard.getLockedToBuild()) { extendingBuild = true; }

        int selectedVal = lockedCard.getValue();
        int playedVal = playedCard.getValue();

        if (playedVal >= selectedVal) { return false; }

        ArrayList<Card> buildCards = new ArrayList<>();
        buildCards.add(playedCard);

        int setSum = playedVal;
        for (int i = 0; i < selectedFromTable.size(); i++) {
            setSum += selectedFromTable.get(i).getValue();
            buildCards.add(selectedFromTable.get(i));
            if (setSum > selectedVal) {
                return false;
            }
            if (setSum == selectedVal) {
                break;
            }
        }

        if (setSum != selectedVal) { return false; }

        if (!extendingBuild) {
            // creating new single build
            ArrayList<ArrayList<Card>> totalBuildCards = new ArrayList<>();
            totalBuildCards.add(buildCards);
            Build b1 = new Build(totalBuildCards, selectedVal, lockedCard, gamePlayer.getPlayerString());
            this.gameTable.addBuild(b1);
            for (int i = 0; i < buildCards.size(); i++) {
                buildCards.get(i).setPartOfBuild(true);
            }
            playedCard.setBuildBuddies(buildCards);
            gamePlayer.discard(playedCard);
            this.gameTable.addToTableCards(playedCard);
            lockedCard.setLockedToBuild(true);

            return true;
        } else {
            // extending current build to a multi build
            Build b1 = getCorrectBuild(lockedCard);
            b1.extendBuild(buildCards);
            for (int i = 0; i < buildCards.size(); i++) {
                buildCards.get(i).setPartOfBuild(true);
            }
            playedCard.setBuildBuddies(buildCards);
            gamePlayer.discard(playedCard);
            this.gameTable.addToTableCards(playedCard);

            return true;
        }
    }

    private boolean capture(Card cardPlayed, ArrayList<Card> selectedCards, ArrayList<Build> selectedBuilds, Player gamePlayer) {
       ArrayList<Build> capturableBuilds = new ArrayList<>();

       for (int i = 0; i < selectedBuilds.size(); i++) {
           if (selectedBuilds.get(i).getSumCard().getCardString().equals(cardPlayed.getCardString())
                   || (selectedBuilds.get(i).getBuildOwner() != gamePlayer.getPlayerString() && selectedBuilds.get(i).getSumCard().getValue() == cardPlayed.getValue())) {
               capturableBuilds.add(selectedBuilds.get(i));
           }
       }

       for (int i = 0; i < capturableBuilds.size(); i++) {
           if (selectedBuilds.contains(capturableBuilds.get(i))) {
               selectedBuilds.remove(capturableBuilds.get(i));
           }
       }

       if (!selectedBuilds.isEmpty()) {
           return false;
       }

       int playedVal = cardPlayed.getValue();
       ArrayList<Card> capturableCards = new ArrayList<>();

       // Iterate through selected cards and pull off same val capturable cards.
       for (int i = 0; i < selectedCards.size(); i++) {
           if (selectedCards.get(i).getValue() == playedVal) {
               capturableCards.add(selectedCards.get(i));
           }
       }

       for (int i = 0; i < capturableCards.size(); i++) {
           if (selectedCards.contains(capturableCards.get(i))) {
               selectedCards.remove(capturableCards.get(i));
           }
       }

       ArrayList<ArrayList<Card>> capturableSets = getCapturableSets(selectedCards, playedVal);

       // If you have selected cards that cannot be captured. Invalid move selected.
       if (!selectedCards.isEmpty()) {
           return false;
       }

       gamePlayer.discard(cardPlayed);
       this.gameTable.removeCards(capturableCards);
       this.gameTable.removeSets(capturableSets);
       this.gameTable.removeBuilds(capturableBuilds);

       ArrayList<Card> pileAdditions = new ArrayList<>();
       pileAdditions.add(cardPlayed);

       for (int i = 0; i < capturableCards.size(); i++) {
           pileAdditions.add(capturableCards.get(i));
       }

       for (int i = 0; i < capturableSets.size(); i++) {
           for (int j = 0; j < capturableSets.get(i).size(); j++) {
               pileAdditions.add(capturableSets.get(i).get(j));
           }
       }

       for (int i = 0; i < capturableBuilds.size(); i++) {
           capturableBuilds.get(i).getSumCard().setLockedToBuild(false);
           ArrayList<ArrayList<Card>> tempBuildCards = capturableBuilds.get(i).getTotalBuildCards();
           for (int j = 0; j < tempBuildCards.size(); j++) {
               for (int k = 0; k < tempBuildCards.get(j).size(); k++) {
                   pileAdditions.add(tempBuildCards.get(j).get(k));
               }
           }
       }
       gamePlayer.addToPile(pileAdditions);
       return true;

    }

    /**
     * Find capturable sets of cards given cards selected by user.
     * @param selectedCards, ArrayList of Cards selected by user to capture.
     * @param playedVal, int value that Cards must sum to.
     * @return 2d ArrayList of capturable sets of Cards
     */
    private ArrayList<ArrayList<Card>> getCapturableSets(ArrayList<Card> selectedCards, int playedVal) {
        ArrayList<String> totalSets = new ArrayList<>();
        totalSets.add("");

        // Algo to get all possible sets.
        for (int i = 0; i < selectedCards.size(); i++) {
            ArrayList<String> tempSet;
            tempSet = new ArrayList<>(totalSets);

            for (int j = 0; j < tempSet.size(); j++) {
                String newString = tempSet.get(j).concat(" " + selectedCards.get(i).getCardString());
                tempSet.set(j, newString);
            }
            for (int j = 0; j < tempSet.size(); j++) {
                totalSets.add(tempSet.get(j));
            }

            tempSet.clear();
        }

        // Need to convert totalSets from String -> cards
        ArrayList<ArrayList<Card>> totalSetCards = getSetsAsCards(totalSets, selectedCards);

        ArrayList<ArrayList<Card>> capturableSets = new ArrayList<>();
        // Iterate through list of all sets add capturable ones to list.
        for (int i = 0; i < totalSetCards.size(); i++) {
            if (getSetValue(totalSetCards.get(i)) == playedVal && totalSetCards.get(i).size() > 1 && noRepeatCardsInSet(capturableSets, totalSetCards.get(i))) {
                capturableSets.add(totalSetCards.get(i));
                for (int j = 0; j < totalSetCards.get(i).size(); j++) {
                    // Remove capturable sets from selected cards list
                    selectedCards.remove(totalSetCards.get(i).get(j));
                }
            }
        }

        return capturableSets;
    }

    /**
     * Used to make sure duplicate cards are not accidentally captured in sets.
     * @param capturableSets, 2d ArrayList of sets that are already capturable
     * @param possibleSet, ArrayList of Cards that are being considered a capturable set
     * @return boolean value determining whether or not cards in the set have already been set as capturable.
     */
    private boolean noRepeatCardsInSet(ArrayList<ArrayList<Card>> capturableSets, ArrayList<Card> possibleSet) {
        for (int i = 0; i < capturableSets.size(); i++) {
            for (int j = 0; j < possibleSet.size(); j++){
                if (capturableSets.get(i).contains(possibleSet.get(j))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Maps the 2d ArrayList of card strings to their respective card objects and returns that newly generated List.
     * @param stringSets, 2d ArrayList of card strings generated by power set algorithm.
     * @param cards, ArrayList of Cards selected by user for move.
     * @return 2d ArrayList of Card sets generated by power set algorithm.
     */
    private ArrayList<ArrayList<Card>> getSetsAsCards(ArrayList<String> stringSets, ArrayList<Card> cards) {
        ArrayList<ArrayList<Card>> cardSets = new ArrayList<>();

        ArrayList<Card> subset = new ArrayList<>();
        for (int i = 0; i < stringSets.size(); i++) {
            if (stringSets.get(i).length() > 3) {
                String[] cardStrings = stringSets.get(i).split("\\s+");
                for (int j = 0; j < cardStrings.length; j++) {
                    for (int k = 0; k < cards.size(); k++) {
                        if (cardStrings[j].equals(cards.get(k).getCardString())) {
                            subset.add(cards.get(k));
                        }
                    }
                }
                cardSets.add(new ArrayList<>(subset));
                subset.clear();
            }
        }

        return cardSets;
    }

    /**
     * Gets the total sum value of the set
     * @param set, ArrayList of Cards in set
     * @return int total sum value of set.
     */
    private int getSetValue(ArrayList<Card> set) {
        int sum = 0;
        for (int i = 0; i < set.size(); i++) {
            sum += set.get(i).getValue();
        }

        return sum;
    }

    /**
     * Filters out Cards from a list that can't be used in a build.
     * @param availableCards, ArrayList of Cards available to build with
     * @param playedVal, int value of current cards played into build
     * @param buildSum, int sum value of the build
     */
    private void filterBuildOptions(ArrayList<Card> availableCards, int playedVal, int buildSum) {
        for (int i = 0; i < availableCards.size(); i++) {
            if (availableCards.get(i).getValue() + playedVal > buildSum) {
                availableCards.remove(availableCards.get(i));
            }
        }
    }

    /**
     * Finds a current Build given a locked card from hand.
     * @param myCard, Card from hand that is currently locked to a build.
     * @return
     */
    private Build getCorrectBuild(Card myCard) {
        ArrayList<Build> totalBuilds = this.gameTable.getCurrentBuilds();

        for (int i = 0; i < totalBuilds.size(); i++) {
            if (totalBuilds.get(i).getSumCard().getCardString().equals(myCard.getCardString())) {
                return totalBuilds.get(i);
            }
        }

        return null;
    }

    private boolean increase(Card lockedCard, Card playedCard, ArrayList<Build> buildsSelected, Player gamePlayer) {
        if (buildsSelected.isEmpty()) {
            return false;
        }

        if (buildsSelected.size() > 1) {
            return false;
        }

        if (buildsSelected.get(0).getIsMultiBuild()) {
            return false;
        }

        if ((lockedCard.getValue() == playedCard.getValue() + buildsSelected.get(0).getSumVal())
                && !gamePlayer.getPlayerString().equals(buildsSelected.get(0).getBuildOwner())) {
            // increase is possible.
            buildsSelected.get(0).increaseBuild(playedCard, gamePlayer.getPlayerString());
            buildsSelected.get(0).getSumCard().setLockedToBuild(false);
            buildsSelected.get(0).setSumCard(lockedCard);
        }

        return false;

    }
}
