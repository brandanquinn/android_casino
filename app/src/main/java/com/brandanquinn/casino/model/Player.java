package com.brandanquinn.casino.model;

import android.util.Pair;

import com.brandanquinn.casino.casino.GameScreen;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
    private int score;
    private boolean isPlaying;
    private boolean capturedLast;
    private Move recommendedMove;

    protected ArrayList<Card> hand;
    protected ArrayList<Card> pile;
    protected String cardSelectedFromHand;
    protected String cardPlayedIntoBuild;
    protected ArrayList<String> cardsSelectedFromTable;
    protected String moveSelected;
    protected Table gameTable;

    /**
     * Default constructor for Player class.
     */
    public Player() {
        setScore(0);
        setIsPlaying(false);
        setCapturedLast(false);
        this.hand = new ArrayList<>();
        this.pile = new ArrayList<>();
        recommendedMove = new Move();
    }

    /**
     * Getter for score private member variable.
     * @return int score of player
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Setter for score private member variable.
     * @param score, int score of player
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Getter for playerIdentity private member variable
     * @return String representation of player identity.
     */
    public String getPlayerIdentity() {
        return "Player";
    }

    /**
     * Used to add a new Card object to the player's hand.
     * @param newCard, Card object to be added.
     */
    public void addToHand(Card newCard) {
        this.hand.add(newCard);
    }

    /**
     * Adds a list of captured cards to the player's pile.
     * @param capturedCards, ArrayList of Card objects to be added.
     */
    public void addToPile(ArrayList<Card> capturedCards) {
        this.pile.addAll(capturedCards);
    }

    /**
     * Getter for pile private member variable.
     * @return ArrayList of Card objects in player's pile.
     */
    public ArrayList<Card> getPile() {
        return this.pile;
    }

    /**
     * Remove a card played from the player's hand.
     * @param removedCard
     */
    public void discard(Card removedCard) {
        this.hand.remove(removedCard);
    }

    /**
     * Getter for hand private member variable.
     * @return ArrayList of Card objects in player's hand.
     */
    public ArrayList<Card> getHand() {
        return this.hand;
    }

    /**
     * Setter for cardSelectedFromHand private member variable
     * @param cardSelected, Card object selected by user via Activity.
     */
    public void setCardSelectedFromHand(String cardSelected) {
        this.cardSelectedFromHand = cardSelected;
    }

    /**
     * Setter for cardPlayedIntoBuild private member variable
     * @param cardSelected, Card object selected by user via Activity.
     */
    public void setCardPlayedIntoBuild(String cardSelected) {
        this.cardPlayedIntoBuild = cardSelected;
    }

    /**
     * Setter for cardSelectedFromTable private member variable
     * @param cardsSelected, List of Card objects selected by user via Activity.
     */
    public void setCardsSelectedFromTable(ArrayList<String> cardsSelected) {
        this.cardsSelectedFromTable = cardsSelected;
    }

    /**
     * Setter for moveSelected private member variable
     * @param moveSelected, String detailing move selected by user.
     */
    public void setMoveSelected(String moveSelected) {
        this.moveSelected = moveSelected;
    }

    /**
     * Stringified version of player's hand for serialization / logging purposes.
     * @return String of Cards in player's hand.
     */
    public String getHandString() {
        String handString = " ";

        for (int i = 0; i < hand.size(); i++) {
            handString += hand.get(i).getCardString() + " ";
        }

        return handString;
    }

    /**
     * Gets a stringified version of player's pile for serialization / logging purposes
     * @return String of cards in player's pile.
     */
    public String getPileString() {
        String pileString = " ";

        if (pile.isEmpty()) {
            return "Pile is empty.";
        }

        for (int i = 0; i < pile.size(); i++) {
            pileString += pile.get(i).getCardString() + " ";
        }

        return pileString;
    }

    /**
     * Play function that will be overloaded by Human / Computer classes.
     * @return
     */
    public Move play() {
       return new Move();
    }

    /**
     * Base AI for Computer player, can also be called by Human player if they ask for help.
     * @return Move object created with whatever the AI recommends as the best move.
     */
    public Move getHelp() {
        recommendedMove = new Move();

        int maxIncreaseIdx = 0, maxBuildIdx = 0, maxCaptureIdx = 0;
        ArrayList<Integer> increaseValues = new ArrayList<>();
        ArrayList<Integer> buildValues = new ArrayList<>();
        ArrayList<Integer> captureValues = new ArrayList<>();

        for (int i = 0; i < this.hand.size(); i++) {
            increaseValues.add(assessIncrease(hand.get(i), false));
            buildValues.add(assessBuild(hand.get(i), false));
            captureValues.add(assessCapture(hand.get(i), false));
        }

        maxIncreaseIdx = getMaxScoreIdx(increaseValues);

        if (increaseValues.get(maxIncreaseIdx) != 0) {
            generateMoveObj(this.hand.get(maxIncreaseIdx), "increase");
            return this.recommendedMove;
        }

        maxBuildIdx = getMaxScoreIdx(buildValues);

        if (buildValues.get(maxBuildIdx) != 0) {
            generateMoveObj(this.hand.get(maxBuildIdx), "build");
            return this.recommendedMove;
        }

        maxCaptureIdx = getMaxScoreIdx(captureValues);

        if (captureValues.get(maxCaptureIdx) != 0) {
            generateMoveObj(this.hand.get(maxCaptureIdx), "capture");
            return this.recommendedMove;
        }

        int minVal = 15;
        int minIdx = 0;

        for (int i = 0; i < this.hand.size(); i++) {
            if (this.hand.get(i).getValue() < minVal) {
                minVal = this.hand.get(i).getValue();
                minIdx = i;
            }
        }
        generateMoveObj(this.hand.get(minIdx), "trail");
        return this.recommendedMove;
    }

    /**
     * Clears player's hand of all cards.
     */
    public void clearHand() {
        this.hand.clear();
    }

    /**
     * Clears player's pile of all cards.
     */
    public void clearPile() {
        this.pile.clear();
    }

    /**
     * Checks whether player's hand is empty or not.
     * @return boolean value to represent above.
     */
    public boolean handIsEmpty() {
        return this.hand.isEmpty();
    }

    /**
     * Setter for isPlaying private member variable
     * @param isPlaying, boolean value representing whether or not player is currently playing.
     */
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    /**
     * Getter for isPlaying private member variable.
     * @return boolean value representing whether or not player is currently playing.
     */
    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    /**
     * Sets a player's hand to preloaded list of cards
     * @param hand, ArrayList of Card objects used to set hand to.
     */
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    /**
     * Sets a player's pile to preloaded list of cards
     * @param pile, ArrayList of Card objects to set pile to.
     */
    public void setPile(ArrayList<Card> pile) {
        this.pile = pile;
    }

    /**
     * Getter for gameTable private member variable
     * @return Table object for all cards / builds on the table.
     */
    public Table getGameTable() {
        return this.gameTable;
    }

    /**
     * Setter for gameTable private member variable
     * @param gameTable, Table object for all cards / builds on the table.
     */
    public void setGameTable(Table gameTable) {
        this.gameTable = gameTable;
    }

    /**
     * Getter for capturedLast private member variable
     * @return boolean value representing whether or not player captured last.
     */
    public boolean getCapturedLast() {
        return this.capturedLast;
    }

    /**
     * Setter for capturedLast private member variable.
     * @param capturedLast, boolean value representing whether or not player captured last.
     */
    public void setCapturedLast(boolean capturedLast) {
        this.capturedLast = capturedLast;
    }

    /**
     * Returns index of element with max value.
     * @param scores, ArrayList of Integer score values
     */
    private int getMaxScoreIdx(ArrayList<Integer> scores) {
        int maxVal = 0, maxIdx = 0;

        for (int i = 0; i < scores.size(); i++) {
            if (maxVal < scores.get(i)) {
                maxVal = scores.get(i);
                maxIdx = i;
            }
        }

        return maxIdx;
    }

    /**
     * Once best move is found, this is called to generate the recommended move object to pass to Round class.
     * @param cardSelected, Card selected to play from hand
     * @param moveSelected, Move type selected
     */
    private void generateMoveObj(Card cardSelected, String moveSelected) {
        recommendedMove.setMoveType(moveSelected);
        if (moveSelected.equals("increase")) {
            assessIncrease(cardSelected, true);
        } else if (moveSelected.equals("build")) {
            assessBuild(cardSelected, true);
        } else if (moveSelected.equals("capture")) {
            assessCapture(cardSelected, true);
        } else {
            recommendedMove.setCardSelectedFromHand(cardSelected);
        }
    }

    /**
     * Assess the heuristic value of a increase and claim move.
     * @param cardSelected, Card object selected to increase with from hand
     * @param makingMove, boolean value that changes the way this function is used.
     * @return int heuristic value based on total sum value of the build.
     */
    private int assessIncrease(Card cardSelected, boolean makingMove) {
        ArrayList<Build> currentBuilds = this.gameTable.getCurrentBuilds();

        for (int i = 0; i < currentBuilds.size(); i++) {
            if (!currentBuilds.get(i).getBuildOwner().equals(getPlayerIdentity())) {
                for (int j = 0; j < this.hand.size(); j++) {
                    int playedVal;
                    if (this.hand.get(j).getType() == 'A') {
                        playedVal = 1;
                    } else {
                        playedVal = this.hand.get(j).getValue();
                    }
                    if (currentBuilds.get(i).getSumVal() + cardSelected.getValue() == playedVal
                            && !currentBuilds.get(i).getIsMultiBuild()) {
                        if (makingMove) {
                            recommendedMove.setCardSelectedFromHand(this.hand.get(j));
                            recommendedMove.setCardPlayedFromHand(cardSelected);
                            recommendedMove.addBuildSelectedFromTable(currentBuilds.get(i));
                        }
                        return this.hand.get(j).getValue();
                    }
                }
            }
        }

        return 0;
    }

    /**
     * Assess the heuristic value of creating a new build or extending a current one.
     * @param cardSelected, Card object to lock build with from hand.
     * @param makingMove, boolean value that changes the way this function is used.
     * @return
     */
    private int assessBuild(Card cardSelected, boolean makingMove) {
        ArrayList<Integer> possibleBuildVals = new ArrayList<>();

        if (cardSelected.getLockedToBuild()) {
            for (int i = 0; i < this.hand.size(); i++) {
                possibleBuildVals.add(createBuilds(cardSelected, this.hand.get(i), true, makingMove));
            }
        } else {
            for (int i = 0; i < this.hand.size(); i++) {
                possibleBuildVals.add(createBuilds(cardSelected, this.hand.get(i), false, makingMove));
            }
        }

        return possibleBuildVals.get(getMaxScoreIdx(possibleBuildVals));
    }

    /**
     * Called by assessBuilds to create and test the legitimacy of build creation by selecting each card in hand to play.
     * @param cardSelected, Card selected to sum build to.
     * @param cardPlayed, Card played into build.
     * @param extendingBuild, Whether or not build is being extended to a multi-build.
     * @param makingMove, If true: the function generates a Move object to send to Round class.
     * @return
     */
    private int createBuilds(Card cardSelected, Card cardPlayed, boolean extendingBuild, boolean makingMove) {
        int selectedVal = cardSelected.getValue();
        if (cardPlayed.getValue() >= selectedVal) {
            return 0;
        }

        ArrayList<Card> tableCards = this.gameTable.getTableCards();
        ArrayList<Card> filteredCards = new ArrayList<>(tableCards);

        int playedVal;
        if (cardPlayed.getType() == 'A') {
            playedVal = 1;
        } else {
            playedVal = cardPlayed.getValue();
        }

        ArrayList<Card> buildCards = new ArrayList<>();
        buildCards.add(cardPlayed);

        while(true) {
            filterBuildOptions(filteredCards, playedVal, selectedVal);
            if (filteredCards.isEmpty()) {
                return 0;
            }
            int bestCardSelection = 0;
            int minVal = 15;
            for (int i = 0; i < filteredCards.size(); i++) {
                if (playedVal + filteredCards.get(i).getValue() == selectedVal) {
                    bestCardSelection = i;
                    break;
                }
                if (filteredCards.get(i).getValue() < minVal) {
                    bestCardSelection = i;
                    minVal = filteredCards.get(i).getValue();
                }
            }
            Card buildCard = filteredCards.get(bestCardSelection);
            buildCards.add(buildCard);
            filteredCards.remove(buildCard);

            if (playedVal + buildCard.getValue() == selectedVal && !extendingBuild) {
                if (makingMove) {
                    recommendedMove.setCardSelectedFromHand(cardSelected);
                    recommendedMove.setCardPlayedFromHand(cardPlayed);
                    recommendedMove.setCardsSelectedFromTable(new ArrayList<>(buildCards.subList(1, buildCards.size())));
                }
                return buildCards.size();
            } else if (playedVal + buildCard.getValue() == selectedVal && extendingBuild) {
                Build b1 = getCorrectBuild(cardSelected);
                if (makingMove) {
                    recommendedMove.setCardSelectedFromHand(cardSelected);
                    recommendedMove.setCardPlayedFromHand(cardPlayed);
                    recommendedMove.setCardsSelectedFromTable(new ArrayList<>(buildCards.subList(1, buildCards.size())));
                }

                ArrayList<ArrayList<Card>> totalBuildCards = b1.getTotalBuildCards();
                int score = 0;
                for (int i = 0; i < totalBuildCards.size(); i++) {
                    score += totalBuildCards.get(i).size();
                }

                return score;
            }
            playedVal = getSetValue(buildCards);
        }
    }

    /**
     * Assess the heuristic value of a capture move
     * @param cardPlayed, Card selected to capture with.
     * @param makingMove, If true: generates a move object to return to Round class.
     * @return int heuristic value based on how many cards can be captured.
     */
    private int assessCapture(Card cardPlayed, boolean makingMove) {
        ArrayList<Build> capturableBuilds = new ArrayList<>();
        ArrayList<Build> currentBuilds = this.gameTable.getCurrentBuilds();

        for (int i = 0; i < currentBuilds.size(); i++) {
            if (currentBuilds.get(i).getSumCard().getCardString().equals(cardPlayed.getCardString())
                    || (!currentBuilds.get(i).getBuildOwner().equals(getPlayerIdentity()) && currentBuilds.get(i).getSumVal() == cardPlayed.getValue())) {
                capturableBuilds.add(currentBuilds.get(i));
            }
        }

        int playedVal = cardPlayed.getValue();

        ArrayList<Card> availCards = new ArrayList<>(this.gameTable.getTableCards());
        ArrayList<Card> capturableCards = new ArrayList<>();

        for (int i = 0; i < availCards.size(); i++) {
            if (availCards.get(i).getValue() == playedVal) {
                capturableCards.add(availCards.get(i));
            }
        }

        ArrayList<ArrayList<Card>> capturableSets = getCapturableSets(availCards, playedVal);

        if (capturableCards.isEmpty() && capturableBuilds.isEmpty() && capturableSets.isEmpty()) {
            return 0;
        }

        ArrayList<Card> pileAdditions = new ArrayList<>();

        if (makingMove) {
            recommendedMove.setCardSelectedFromHand(cardPlayed);
            recommendedMove.setCardsSelectedFromTable(getAllTableCards(capturableSets, capturableCards));
            recommendedMove.setBuildsSelectedFromTable(capturableBuilds);
        }

        pileAdditions.add(cardPlayed);

        pileAdditions.addAll(capturableCards);

        for (int i = 0; i < capturableSets.size(); i++) {
            pileAdditions.addAll(capturableSets.get(i));
        }

        for (int i = 0; i < capturableBuilds.size(); i++) {
            ArrayList<ArrayList<Card>> tempBuildCards = capturableBuilds.get(i).getTotalBuildCards();
            for (int j = 0; j < tempBuildCards.size(); j++) {
                pileAdditions.addAll(tempBuildCards.get(j));
            }
        }

        return pileAdditions.size();
    }

    /**
     * Filters out Cards from a list that can't be used in a build.
     * @param availableCards, ArrayList of Cards available to build with
     * @param playedVal, int value of current cards played into build
     * @param buildSum, int sum value of the build
     */
    private void filterBuildOptions(ArrayList<Card> availableCards, int playedVal, int buildSum) {
        ArrayList<Card> tempSet = new ArrayList<>(availableCards);
        for (int i = 0; i < tempSet.size(); i++) {
            if (tempSet.get(i).getValue() + playedVal > buildSum) {
                availableCards.remove(tempSet.get(i));
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
     * Creates a single ArrayList of all loose table cards selected for capture by AI.
     * @param capturableSets, 2d ArrayList of sets selected for capture
     * @param capturableCards, ArrayList of cards selected for capture
     * @return ArrayList of all cards to be captured.
     */
    private ArrayList<Card> getAllTableCards(ArrayList<ArrayList<Card>> capturableSets, ArrayList<Card> capturableCards) {
        ArrayList<Card> flatList = new ArrayList<>(capturableCards);

        for (int i = 0; i < capturableSets.size(); i++) {
            for (int j = 0; j < capturableSets.get(i).size(); j++) {
                flatList.add(capturableSets.get(i).get(j));
            }
        }

        return flatList;
    }

}
