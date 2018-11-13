package com.brandanquinn.casino.model;

import android.os.Environment;

import com.brandanquinn.casino.casino.GameScreen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Round {
    private int roundNum;
    private Deck gameDeck;
    private Table gameTable;
    private ArrayList<Player> gamePlayers;
    private boolean roundIsOver;
    private String previousMove;
    private String recommendedMove;

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
        this.roundIsOver = false;
        this.previousMove = "";
        this.recommendedMove = "";
    }

    /**
     * Overloaded constructor for Round class. Used for deserialization
     * @param roundNum, int value used to set current round number
     * @param gamePlayers, ArrayList of players of the game
     * @param deckList, ArrayList of Cards in pre-seeded deck
     * @param tableCards, ArrayList of Cards on pre-seeded table
     * @param currentBuilds, ArrayList of Build objects from saved file.
     */
    public Round(int roundNum, ArrayList<Player> gamePlayers, ArrayList<Card> deckList, ArrayList<Card> tableCards, ArrayList<Build> currentBuilds) {
        this.roundNum = roundNum;
        this.gameDeck = new Deck(deckList);
        this.gameTable = new Table(tableCards, currentBuilds);
        this.gamePlayers = gamePlayers;
        this.roundIsOver = false;
        this.previousMove = "";
        this.recommendedMove = "";
    }


    /**
     * Getter for gamePlayers private member variable.
     * @return ArrayList of game player objects.
     */
    public ArrayList<Player> getGamePlayers() {
        return this.gamePlayers;
    }

    /**
     * Getter for roundNum private member variable
     * @return int current round number.
     */
    public int getRoundNum() {
        return roundNum;
    }

    /**
     * Getter for gameDeck private member variable.
     * @return Deck obj used in current round.
     */
    public Deck getGameDeck() {
        return gameDeck;
    }

    /**
     * Getter for gameTable private member variable
     * @return Table object holding cards / builds
     */
    public Table getGameTable() {
        return this.gameTable;
    }

    /**
     * Getter for previosuMove private member variable
     * @return String representing the previous move made by player.
     */
    public String getPreviousMove() {
        return previousMove;
    }

    /**
     * Getter for recommendedMove private member variable
     * @return String rerpresenting the AI recommended move for player.
     */
    public String getRecommendedMove() {
        return recommendedMove;
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
        playerTwo.setIsPlaying(false);

        GameScreen.updateActivity(roundIsOver);
    }

    /**
     * Used to print who's turn it is to a TextView in the main activity
     * @return String denoting who's turn it currently is.
     */
    public String whoIsPlaying() {
        String playing = "Unknown";
        for (int i = 0; i < this.gamePlayers.size(); i++) {
            if (gamePlayers.get(i).getIsPlaying()) {
                playing = gamePlayers.get(i).getPlayerIdentity();
            }
        }

        return playing + " is playing.";
    }

    /**
     * Get Player who is currently playing.
     * @return Player who's turn it is.
     */
    public Player getCurrentPlayer() {
        for (int i = 0; i < this.gamePlayers.size(); i++) {
            if (gamePlayers.get(i).getIsPlaying()) {
                return gamePlayers.get(i);
            }
        }

        return null;
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

        GameScreen.updateActivity(roundIsOver);
    }

    /**
     * Clears gameTable of all loose cards.
     */
    public void clearGameTable() {
        this.gameTable.clearTableCards();
    }

    public void saveGame(String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "serialization");
        String lastCapturer = "";
        if (this.gamePlayers.get(0).getCapturedLast()) {
            lastCapturer = "Human";
        } else {
            lastCapturer = "Computer";
        }

        String nextPlayer = "";
        if (this.gamePlayers.get(0).getIsPlaying()) {
            nextPlayer = "Human";
        } else {
            nextPlayer = "Computer";
        }

        try {
            File saveFile = new File(file, fileName+".txt");
            FileWriter writer = new FileWriter(saveFile);

            writer.append("Round: " + Integer.toString(this.roundNum));
            writer.append("\n\n");

            // Computer player info
            writer.append("Computer: \n");
            writer.append("\tScore: " + gamePlayers.get(1).getScore() + "\n");
            writer.append("\tHand: " + gamePlayers.get(1).getHandString() + "\n");
            writer.append("\tPile: " + gamePlayers.get(1).getPileString());
            writer.append("\n\n");

            // Human player info
            writer.append("Human: \n");
            writer.append("\tScore: " + gamePlayers.get(0).getScore() + "\n");
            writer.append("\tHand: " + gamePlayers.get(0).getHandString() + "\n");
            writer.append("\tPile: " + gamePlayers.get(0).getPileString());
            writer.append("\n\n");

            // Table info
            writer.append("Table: " + this.gameTable.getTableString());
            writer.append("\n\n");
            writer.append("Build Owner: " + getBuildStrings());
            writer.append("\n\n");
            writer.append("Last Capturer: " + lastCapturer);
            writer.append("\n\n");
            writer.append("Deck: " + this.gameDeck.getDeckString());
            writer.append("\n\n");
            writer.append("Next Player: " + nextPlayer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public String playTurn(Player gamePlayer, boolean makingMove) {
        boolean possibleMoveSelected = false;
        this.previousMove = "";
        this.recommendedMove = "";
        while (!gamePlayer.handIsEmpty() && !possibleMoveSelected) {
            Move moveObj = gamePlayer.play();
            if (moveObj.getMoveType().equals("trail")) {
                possibleMoveSelected = trail(moveObj.getCardSelectedFromHand(), gamePlayer, makingMove);
                if (possibleMoveSelected) {
                    if (makingMove) {
                        changeTurn();
                    }
                    return "" + gamePlayer.getPlayerIdentity() + " selected to " + moveObj.getMoveType() + " with card: " + moveObj.getCardSelectedFromHand().getCardString();
                } else {
                    return "Trail move cannot be made.";
                }
            } else if (moveObj.getMoveType().equals("build")) {
                possibleMoveSelected = build(moveObj.getCardSelectedFromHand(), moveObj.getCardPlayedFromHand(), moveObj.getCardsSelectedFromTable(), gamePlayer, makingMove);
                if (possibleMoveSelected) {
                    if (makingMove) {
                        changeTurn();
                    }
                    return "" + gamePlayer.getPlayerIdentity() + " selected to " + moveObj.getMoveType() + " with card: " + moveObj.getCardSelectedFromHand().getCardString();
                } else {
                    return "Build move cannot be made with cards selected.";
                }
            } else if (moveObj.getMoveType().equals("capture")) {
                possibleMoveSelected = capture(moveObj.getCardSelectedFromHand(), moveObj.getCardsSelectedFromTable(), moveObj.getBuildsSelectedFromTable(), gamePlayer, makingMove);
                if (possibleMoveSelected) {
                    setCapturedLast(gamePlayer);
                    if (makingMove) {
                        changeTurn();
                    }
                    return "" + gamePlayer.getPlayerIdentity() + " selected to " + moveObj.getMoveType() + " with card: " + moveObj.getCardSelectedFromHand().getCardString();
                } else {
                    return "Capture move cannot be made with cards selected.";
                }
            } else if (moveObj.getMoveType().equals("increase")) {
                // increase
                possibleMoveSelected = increase(moveObj.getCardSelectedFromHand(), moveObj.getCardPlayedFromHand(), moveObj.getBuildsSelectedFromTable(), gamePlayer, makingMove);
                if (possibleMoveSelected) {
                    if (makingMove) {
                        changeTurn();
                    }
                    return "" + gamePlayer.getPlayerIdentity() + " selected to " + moveObj.getMoveType() + " with card: " + moveObj.getCardSelectedFromHand().getCardString();
                } else {
                    return "Cannot increase build with cards selected.";
                }
            }
        }

        if (makingMove) {
            changeTurn();
        }
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
            if (this.gameDeck.isEmpty()) {
                this.roundIsOver = true;
            }
            dealHands();
        } else {
            GameScreen.updateActivity(roundIsOver);
        }
    }

    /**
     * Checks to make sure a trail move can be made, and makes it if possible.
     * @param cardPlayed, Card object to be played to the table.
     * @param gamePlayer, Player making the trail move
     * @return boolean value determining whether or not move was made.
     */
    private boolean trail(Card cardPlayed, Player gamePlayer, boolean makingMove) {
        if (cardPlayed.getLockedToBuild()) {
            return false;
        }

        for (int i = 0; i < this.gameTable.getTableCards().size(); i++) {
            if (cardPlayed.getType() == this.gameTable.getTableCards().get(i).getType()) {
                return false;
            }
        }

        for (int i = 0; i < gamePlayer.getHand().size(); i++) {
            if (gamePlayer.getHand().get(i).getLockedToBuild()) {
                return false;
            }
        }

        if (makingMove) {
            gamePlayer.discard(cardPlayed);
            this.gameTable.addToTableCards(cardPlayed);
            this.previousMove += gamePlayer.getPlayerIdentity() + " has trailed card: " + cardPlayed.getCardString();
        } else {
            this.recommendedMove += "Trail with card: " + cardPlayed.getCardString();
        }



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
    private boolean build(Card lockedCard, Card playedCard, ArrayList<Card> selectedFromTable, Player gamePlayer, boolean makingMove) {
        boolean extendingBuild = false;

        if (lockedCard.getLockedToBuild()) { extendingBuild = true; }

        int selectedVal = lockedCard.getValue();
        int playedVal;
        if (playedCard.getType() == 'A') {
            playedVal = 1;
        } else {
            playedVal = playedCard.getValue();
        }

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

            if (makingMove) {
                ArrayList<ArrayList<Card>> totalBuildCards = new ArrayList<>();
                totalBuildCards.add(buildCards);
                Build b1 = new Build(totalBuildCards, selectedVal, lockedCard, gamePlayer.getPlayerIdentity());
                this.gameTable.addBuild(b1);
                for (int i = 0; i < buildCards.size(); i++) {
                    buildCards.get(i).setPartOfBuild(true);
                }
                playedCard.setBuildBuddies(buildCards);
                gamePlayer.discard(playedCard);
                this.gameTable.addToTableCards(playedCard);
                lockedCard.setLockedToBuild(true);

                this.previousMove += gamePlayer.getPlayerIdentity() + " has created a new build by playing: " + playedCard.getCardString() + " with table card(s): " + cardListToString(new ArrayList<>(buildCards.subList(1, buildCards.size())));
            } else {
                this.recommendedMove += "Create a new build by selecting: " + lockedCard.getCardString() + " and playing: " + playedCard.getCardString() + " with table card(s) " + cardListToString(new ArrayList<>(buildCards.subList(1, buildCards.size())));
            }
            return true;
        } else {
            // extending current build to a multi build
            Build b1 = getCorrectBuild(lockedCard);
            String oldBuildStr = b1.getBuildString();

            if (makingMove) {
                b1.extendBuild(buildCards);
                for (int i = 0; i < buildCards.size(); i++) {
                    buildCards.get(i).setPartOfBuild(true);
                }
                playedCard.setBuildBuddies(buildCards);
                gamePlayer.discard(playedCard);
                this.gameTable.addToTableCards(playedCard);

                this.previousMove += gamePlayer.getPlayerIdentity() + " has extended: " + oldBuildStr + " by playing: " + playedCard.getCardString() + " with table card(s): " + cardListToString(new ArrayList<>(buildCards.subList(1, buildCards.size())));
            } else {
                this.recommendedMove += "Extend: " + oldBuildStr + " selecting: " + lockedCard.getCardString() + " and playing: " + playedCard.getCardString() + " with table card(s) " + cardListToString(new ArrayList<>(buildCards.subList(1, buildCards.size())));
            }
            return true;
        }
    }

    /**
     * Generates a string representation of a list of cards
     * @param list, ArrayList of Card objects.
     * @return String representation of list.
     */
    private String cardListToString(ArrayList<Card> list) {
        String listStr = "";

        for (int i = 0; i < list.size(); i++) {
            listStr += list.get(i).getCardString() + " ";
        }

        return listStr;
    }

    /**
     * Find whether capture move can be made or not.
     * @param cardPlayed, Card played to capture cards.
     * @param selectedCards, ArrayList Cards on the table selected to capture.
     * @param selectedBuilds, ArrayList of Builds selected for capture.
     * @param gamePlayer, Player making the move.
     * @return boolean value determining whether or not the capture move was made.
     */
    private boolean capture(Card cardPlayed, ArrayList<Card> selectedCards, ArrayList<Build> selectedBuilds, Player gamePlayer, boolean makingMove) {
        ArrayList<Card> tableCards = new ArrayList<>(this.gameTable.getTableCards());
        for (int i = 0; i < tableCards.size(); i++) {
            if ((tableCards.get(i).getValue() == cardPlayed.getValue()) && !selectedCards.contains(tableCards.get(i))) {
                return false;
            }
        }

       ArrayList<Build> capturableBuilds = new ArrayList<>();

       for (int i = 0; i < selectedBuilds.size(); i++) {
           if (selectedBuilds.get(i).getSumCard().getCardString().equals(cardPlayed.getCardString())
                   || (!selectedBuilds.get(i).getBuildOwner().equals(gamePlayer.getPlayerIdentity()) && selectedBuilds.get(i).getSumCard().getValue() == cardPlayed.getValue())) {
               capturableBuilds.add(selectedBuilds.get(i));
           }
       }

       for (int i = 0; i < capturableBuilds.size(); i++) {
           selectedBuilds.remove(capturableBuilds.get(i));
       }

       if (!selectedBuilds.isEmpty()) {
           return false;
       }

       int playedVal = cardPlayed.getValue();
       ArrayList<Card> capturableCards = new ArrayList<>();

        ArrayList<ArrayList<Card>> capturableSets = getCapturableSets(selectedCards, playedVal);

       // Iterate through selected cards and pull off same val capturable cards.
       for (int i = 0; i < selectedCards.size(); i++) {
           if (selectedCards.get(i).getType() == cardPlayed.getType()) {
               capturableCards.add(selectedCards.get(i));
           }
       }

       for (int i = 0; i < capturableCards.size(); i++) {
           selectedCards.remove(capturableCards.get(i));
       }

       // If you have selected cards that cannot be captured. Invalid move selected.
       if (!selectedCards.isEmpty()) {
           return false;
       }

        ArrayList<Card> pileAdditions = new ArrayList<>();
        pileAdditions.add(cardPlayed);

        pileAdditions.addAll(capturableCards);

        for (int i = 0; i < capturableSets.size(); i++) {
            pileAdditions.addAll(capturableSets.get(i));
        }

        for (int i = 0; i < capturableBuilds.size(); i++) {
            capturableBuilds.get(i).getSumCard().setLockedToBuild(false);
            ArrayList<ArrayList<Card>> tempBuildCards = capturableBuilds.get(i).getTotalBuildCards();
            for (int j = 0; j < tempBuildCards.size(); j++) {
                pileAdditions.addAll(tempBuildCards.get(j));
            }
        }

       if (makingMove) {
           gamePlayer.discard(cardPlayed);
           this.gameTable.removeCards(capturableCards);
           this.gameTable.removeSets(capturableSets);
           this.gameTable.removeBuilds(capturableBuilds);


           gamePlayer.addToPile(pileAdditions);

           this.previousMove += gamePlayer.getPlayerIdentity() + " has captured " + cardListToString(new ArrayList<>(pileAdditions.subList(1, pileAdditions.size()))) + " by playing card: " + cardPlayed.getCardString();
       } else {
           this.recommendedMove += "Capture these cards: " + cardListToString(new ArrayList<>(pileAdditions.subList(1, pileAdditions.size()))) + " by playing card: " + cardPlayed.getCardString();
       }
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
            if (set.get(i).getType() == 'A') {
                sum += 1;
            } else {
                sum += set.get(i).getValue();
            }
        }

        return sum;
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

    /**
     * Check to see if increase move can be made; if possible update model and return true, else return false.
     * @param lockedCard, Card to lock increased build to.
     * @param playedCard, Card played into build increased.
     * @param buildsSelected, ArrayList of Build objects selected, in this case should only be one.
     * @param gamePlayer, Player making move
     * @return boolean value representing whether or not move was made.
     */
    private boolean increase(Card lockedCard, Card playedCard, ArrayList<Build> buildsSelected, Player gamePlayer, boolean makingMove) {
        if (buildsSelected.isEmpty()) {
            return false;
        }

        if (buildsSelected.size() > 1) {
            return false;
        }

        if (buildsSelected.get(0).getIsMultiBuild()) {
            return false;
        }

        int playedVal;
        if (playedCard.getType() == 'A') {
            playedVal = 1;
        } else {
            playedVal = playedCard.getType();
        }

        if ((lockedCard.getValue() == playedVal + buildsSelected.get(0).getSumVal())
                && !gamePlayer.getPlayerIdentity().equals(buildsSelected.get(0).getBuildOwner())) {
            // increase is possible.
            String oldBuildStr = buildsSelected.get(0).getBuildString();
            if (makingMove) {
                gamePlayer.discard(playedCard);
                buildsSelected.get(0).increaseBuild(playedCard, gamePlayer.getPlayerIdentity());
                buildsSelected.get(0).getSumCard().setLockedToBuild(false);
                buildsSelected.get(0).setSumCard(lockedCard);
                lockedCard.setLockedToBuild(true);

                this.previousMove += gamePlayer.getPlayerIdentity() + " has increased and claimed build: " + oldBuildStr + " by playing: " + playedCard.getCardString();
            } else {
                this.recommendedMove += "Select: " + lockedCard.getCardString() + " and play: " + playedCard.getCardString() + " to increase and claim build: " + oldBuildStr;
            }

            return true;
        }

        return false;

    }

    /**
     * Sets capturedLast variable after capture move is made.
     * @param justPlayed, Player that just made a capture move.
     */
    private void setCapturedLast(Player justPlayed) {
        if (gamePlayers.get(0) == justPlayed) {
            gamePlayers.get(0).setCapturedLast(true);
            gamePlayers.get(1).setCapturedLast(false);
        } else {
            gamePlayers.get(1).setCapturedLast(true);
            gamePlayers.get(0).setCapturedLast(false);
        }
    }

    /**
     * Generates a concatenated String of builds with their respective owners. Used in serialization.
     * @return String of all builds on the table.
     */
    private String getBuildStrings() {
        String buildStrings = "";
        ArrayList<Build> currentBuilds = this.gameTable.getCurrentBuilds();

        for (int i = 0; i < currentBuilds.size(); i++) {
            buildStrings += currentBuilds.get(i).getBuildStringWithOwner() + " ";
        }

        return buildStrings;
    }
}
