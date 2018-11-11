package com.brandanquinn.casino.model;

import android.content.Context;
import android.os.Environment;

import com.brandanquinn.casino.casino.R;
import com.brandanquinn.casino.casino.StartScreen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Tournament {
    private Round currentRound;
    private int roundsPlayed;
    private int cardCount;
    private int coinToss;
    private ArrayList<Player> gamePlayers;
    private String winningPlayer;
    private String humanFinalPile, computerFinalPile;
    private int humanPileSize, computerPileSize, humanSpades, computerSpades;

    /**
     * Default constructor for the Tournament class
     */
    public Tournament() {
        this.roundsPlayed = 1;
        this.cardCount = 0;
        this.coinToss = ' ';
        this.winningPlayer = "";
        this.gamePlayers = new ArrayList<>();
        this.gamePlayers.add(new Human());
        this.gamePlayers.add(new Computer());
    }

    /**
     * Getter for currentRound private member variable
     * @return Round object that is currently being played in.
     */
    public Round getCurrentRound() {
        return this.currentRound;
    }

    /**
     * Getter for winningPlayer private member variable
     * @return String representing the player that won the tournament, empty if tournament is still being played.
     */
    public String getWinningPlayer() {
        return winningPlayer;
    }

    /**
     * Initializes the current round, conducts coin flip if necessary, and starts game.
     * @param firstRound, boolean value determining whether or not coin flip needs to occur.
     */
    public void startRound(boolean firstRound) {
        Round gameRound = new Round(roundsPlayed, gamePlayers);

        this.currentRound = gameRound;
        ArrayList<Card> deckList = new ArrayList<>();

        // if firstRound, coin toss
        if (firstRound) {
            while (coinToss == ' ') {
                coinToss = StartScreen.getCoinToss();
            }

            double flip = Math.round(Math.random() * 2);

            if (coinToss == flip) {
                // correct call
                this.currentRound.startGame(true, false, deckList);
            } else {
                // wrong call
                this.currentRound.startGame(false, false, deckList);
            }
        } else {
            this.currentRound.startGame(gamePlayers.get(0).getCapturedLast(), false, deckList);
        }
    }

    public void loadSavedGame(String fileName) throws IOException {
        File fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "serialization");
        File file = new File(fileDir, "serialization.txt");

        String line = "";
        int lineNum = 0;

        int currentRoundNum = 0;
        int computerScore = 0, humanScore = 0;
        ArrayList<Card> humanHand = new ArrayList<>();
        ArrayList<Card> humanPile = new ArrayList<>();
        ArrayList<Card> computerHand = new ArrayList<>();
        ArrayList<Card> computerPile = new ArrayList<>();
        ArrayList<Card> deckList = new ArrayList<>();
        ArrayList<Card> tableCards = new ArrayList<>();
        ArrayList<String> buildStrings = new ArrayList<>();
        ArrayList<Build> currentBuilds = new ArrayList<>();
        String whoCapturedLast = "";
        boolean humanCapturedLast = false;
        String nextPlayer = "";
        boolean humanIsNext = false;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                switch(lineNum) {
                    case 0:
                        currentRoundNum = Integer.parseInt(line.substring(line.indexOf(':') + 2));
                        break;
                    case 3:
                        computerScore = Integer.parseInt(line.substring(line.indexOf(':') + 2));
                        break;
                    case 4:
                        computerHand = parseCardsFromFile(line);
                        break;
                    case 5:
                        computerPile = parseCardsFromFile(line);
                        break;
                    case 8:
                        humanScore = Integer.parseInt(line.substring(line.indexOf(':') + 2));
                        break;
                    case 9:
                        humanHand = parseCardsFromFile(line);
                        break;
                    case 10:
                        humanPile = parseCardsFromFile(line);
                        break;
                    case 12:
                        buildStrings = parseBuilds(line.substring(line.indexOf(':') + 1));
                        tableCards = getTableCards(line.substring(line.indexOf(':') + 1));
                        break;
                    case 14:
                        currentBuilds = getBuildObjects(buildStrings, line.substring(line.indexOf(':') + 1), humanHand, computerHand);
                        break;
                    case 16:
                        whoCapturedLast = line.substring(line.indexOf(':') + 2);
                        if (whoCapturedLast.equals("Human")) {
                            humanCapturedLast = true;
                        } else {
                            humanCapturedLast = false;
                        }
                        break;
                    case 18:
                        deckList = parseCardsFromFile(line);
                        break;
                    case 20:
                        nextPlayer = line.substring(line.indexOf(':') + 2);
                        if (nextPlayer.equals("Human")) {
                            humanIsNext = true;
                        } else {
                            humanIsNext = false;
                        }
                        break;
                }
                lineNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        modifyTableCards(tableCards, currentBuilds);

        this.gamePlayers.get(0).setHand(humanHand);
        this.gamePlayers.get(0).setPile(humanPile);
        this.gamePlayers.get(0).setScore(humanScore);

        if (humanCapturedLast) {
            this.gamePlayers.get(0).setCapturedLast(true);
            this.gamePlayers.get(1).setCapturedLast(false);
        } else {
            this.gamePlayers.get(0).setCapturedLast(false);
            this.gamePlayers.get(1).setCapturedLast(true);
        }

        this.gamePlayers.get(1).setHand(computerHand);
        this.gamePlayers.get(1).setPile(computerPile);
        this.gamePlayers.get(1).setScore(computerScore);

        this.roundsPlayed = currentRoundNum;

        Round gameRound = new Round(roundsPlayed, gamePlayers, deckList, tableCards, currentBuilds);
        this.currentRound = gameRound;
        this.currentRound.startGame(humanIsNext, true, new ArrayList<Card>());
    }

    /**
     * Ends the current round and performs score calculations.
     * If tournament is over, report winner. Else start new round.
     */
    public void endRound() {
        this.roundsPlayed = this.currentRound.getRoundNum() + 1;

        computePlayerScores();

        if (this.gamePlayers.get(0).getScore() >= 21 && this.gamePlayers.get(1).getScore() >= 21) {
            this.winningPlayer = "tie";
        } else if (this.gamePlayers.get(0).getScore() >= 21) {
            this.winningPlayer = gamePlayers.get(0).getPlayerIdentity();
        } else if (this.gamePlayers.get(1).getScore() >= 21) {
            this.winningPlayer = gamePlayers.get(1).getPlayerIdentity();
        } else {
            this.gamePlayers.get(0).clearHand();
            this.gamePlayers.get(0).clearPile();

            this.gamePlayers.get(1).clearHand();
            this.gamePlayers.get(1).clearPile();

            this.currentRound.clearGameTable();
            startRound(false);
        }
    }

    /**
     * Takes in a String and parses each card in the string into an ArrayList of Card objects
     * @param line, String of cards from save file.
     * @return ArrayList of Card objects
     */
    private ArrayList<Card> parseCardsFromFile(String line) {
        String parsedStr = line.substring(line.indexOf(':') + 1);

        String[] cardStrings = parsedStr.split("\\s+");
        ArrayList<Card> parsedCards = new ArrayList<>();

        for (int i = 0; i < cardStrings.length; i++) {
            if (!cardStrings[i].isEmpty()) {
                parsedCards.add(new Card(cardStrings[i].charAt(0), cardStrings[i].charAt(1)));
            }
        }

        return parsedCards;
    }

    /**
     * Parses build strings from table
     * @param line, String read from save file
     * @return ArrayList of Build strings
     */
    private ArrayList<String> parseBuilds(String line) {
        int openBracketCounter = 0, closedBracketCounter = 0;
        boolean inBuild = false;

        ArrayList<String> totalBuildStrings = new ArrayList<>();
        String buildString = "";

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '[') {
                openBracketCounter++;
                inBuild = true;
            } else if (line.charAt(i) == ']' && closedBracketCounter != openBracketCounter) {
                closedBracketCounter++;
            }
            if (inBuild) {
                buildString += line.charAt(i);
            }
            if (closedBracketCounter == openBracketCounter && closedBracketCounter != 0) {
                inBuild = false;
                totalBuildStrings.add(buildString);
                buildString = "";
                openBracketCounter = 0;
                closedBracketCounter = 0;
            }
        }

        return totalBuildStrings;
    }

    /**
     * Generates an ArrayList of all table cards on the table.
     * @param line, String of cards on the table from save file.
     * @return ArrayList of card objects
     */
    private ArrayList<Card> getTableCards(String line) {
        line = line.replace('[', ' ');
        line = line.replace(']', ' ');

        ArrayList<Card> tableCardList = parseCardsFromFile(line);

        return tableCardList;
    }

    /**
     * Parses build owners and creates Build objects
     * @param buildStrings, ArrayList of build strings
     * @param line, String of builds and their respective owners from save file
     * @param humanHand, ArrayList of cards in Human's hand
     * @param computerHand, ArrayList of cards in Computer's hand
     * @return ArrayList of Build objects generated.
     */
    private ArrayList<Build> getBuildObjects(ArrayList<String> buildStrings, String line, ArrayList<Card> humanHand, ArrayList<Card> computerHand) {
        ArrayList<Build> buildList = new ArrayList<>();

        for (int i = 0; i < buildStrings.size(); i++) {
            line = line.replace(buildStrings.get(i), "");
        }

        String[] owners = line.split("\\s+");
        ArrayList<String> ownerList = new ArrayList<>();

        for (int i = 0; i < owners.length; i++) {
            if (!owners[i].isEmpty()) {
                ownerList.add(owners[i]);
            }
        }

        int buildStrVal;

        for (int i = 0; i < ownerList.size(); i++) {
            buildStrVal = getBuildStrVal(buildStrings.get(i));
            if (ownerList.get(i).equals("Human")) {
                for (int j = 0; j < humanHand.size(); j++) {
                    if (humanHand.get(j).getValue() == buildStrVal) {
                        buildList.add(new Build(getBuildCards(buildStrings.get(i)), buildStrVal, humanHand.get(j), "Human"));
                        humanHand.get(j).setLockedToBuild(true);
                    }
                }
            } else {
                for (int j = 0; j < computerHand.size(); j++) {
                    if (computerHand.get(j).getValue() == buildStrVal) {
                        buildList.add(new Build(getBuildCards(buildStrings.get(i)), buildStrVal, computerHand.get(j), "Computer"));
                        computerHand.get(j).setLockedToBuild(true);
                    }
                }
            }
        }

        return buildList;
    }

    /**
     * Gets the sum value of a build given a build string.
     * @param buildStr, Build string parsed from save file
     * @return int value representing sum of build.
     */
    private int getBuildStrVal(String buildStr) {
        int bracketCounter = 0;
        for (int i = 0; i < buildStr.length(); i++) {
            if (buildStr.charAt(i) == '[') {
                bracketCounter++;
            }
        }

        buildStr = buildStr.replace("[", "");
        buildStr = buildStr.replace("]", "");

        ArrayList<Card> buildCards = parseCardsFromFile(buildStr);

        int buildSum = 0;
        for (int i = 0; i < buildCards.size(); i++) {
            buildSum += buildCards.get(i).getValue();
        }

        if (bracketCounter > 1) {
            bracketCounter -= 1;
        }

        return buildSum / bracketCounter;
    }

    /**
     * Generates 2d ArrayList of Card objects involved in a build.
     * @param buildStr, Build string from save file
     * @return 2d ArrayList of Card objects involved in a build.
     */
    private ArrayList<ArrayList<Card>> getBuildCards(String buildStr) {
        ArrayList<ArrayList<Card>> totalBuildCards = new ArrayList<>();

        int bracketCount = 0;
        for (int i = 0; i < buildStr.length(); i++) {
            if (buildStr.charAt(i) == '[') {
                bracketCount++;
            }
        }

        if (bracketCount > 1) {
            buildStr = buildStr.substring(1, buildStr.length() - 1);
        }

        boolean bracketFound = false;
        String subBuild = "";

        for (int i = 0; i < buildStr.length(); i++) {
            if (buildStr.charAt(i) == '[') {
                bracketFound = true;
            }
            if (buildStr.charAt(i) == ']') {
                bracketFound = false;
                ArrayList<Card> buildCards = parseCardsFromFile(subBuild);
                totalBuildCards.add(buildCards);
                subBuild = "";
            }
            if (bracketFound && buildStr.charAt(i) != '[') {
                subBuild += buildStr.charAt(i);
            }
        }

        return totalBuildCards;

    }

    /**
     * Sets various build attributes to cards involved in builds on the table
     * @param tableCardsList, List of all cards on the table
     * @param currentBuilds, ArrayList of all build objects.
     */
    private void modifyTableCards(ArrayList<Card> tableCardsList, ArrayList<Build> currentBuilds) {
        for (int i = 0; i < currentBuilds.size(); i++) {
            ArrayList<ArrayList<Card>> totalBuildCards = currentBuilds.get(i).getTotalBuildCards();
            for (int j = 0; j < totalBuildCards.size(); j++) {
                for (int k = 0; k < totalBuildCards.get(j).size(); k++) {
                    for (int l = 0; l < tableCardsList.size(); l++) {
                        if (totalBuildCards.get(j).get(k).getCardString().equals(tableCardsList.get(l).getCardString())) {
                            tableCardsList.get(l).setBuildBuddies(totalBuildCards.get(j));
                            tableCardsList.get(l).setPartOfBuild(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Computes and sets scores for game players.
     */
    private void computePlayerScores() {
        int p1CurrentScore = this.gamePlayers.get(0).getScore();
        ArrayList<Card> p1CurrentPile = this.gamePlayers.get(0).getPile();
        this.humanFinalPile = this.gamePlayers.get(0).getPileString();
        this.humanPileSize = p1CurrentPile.size();

        int p2CurrentScore = this.gamePlayers.get(1).getScore();
        ArrayList<Card> p2CurrentPile = this.gamePlayers.get(1).getPile();
        this.computerFinalPile = this.gamePlayers.get(1).getPileString();
        this.computerPileSize = p2CurrentPile.size();

        if (p1CurrentPile.size() > p2CurrentPile.size()) {
            p1CurrentScore += 3;
        } else if (p2CurrentPile.size() > p1CurrentPile.size()) {
            p2CurrentScore += 3;
        }

        int p1SpadesCount = 0, p2SpadesCount = 0;
        for (int i = 0; i < p1CurrentPile.size(); i++) {
            if (p1CurrentPile.get(i).getSuit() == 'S') {
                p1SpadesCount++;
            }
            if (p1CurrentPile.get(i).getType() == 'A') {
                p1CurrentScore++;
            }
            if (p1CurrentPile.get(i).getCardString().equals("DX")) {
                p1CurrentScore += 2;
            }
            if (p1CurrentPile.get(i).getCardString().equals("S2")) {
                p1CurrentScore++;
            }
        }
        this.humanSpades = p1SpadesCount;

        for (int i = 0; i < p2CurrentPile.size(); i++) {
            if (p2CurrentPile.get(i).getSuit() == 'S') {
                p2SpadesCount++;
            }
            if (p2CurrentPile.get(i).getType() == 'A') {
                p2CurrentScore++;
            }
            if (p2CurrentPile.get(i).getCardString().equals("DX")) {
                p2CurrentScore += 2;
            }
            if (p2CurrentPile.get(i).getCardString().equals("S2")) {
                p2CurrentScore++;
            }
        }
        this.computerSpades = p2SpadesCount;

        if (p1SpadesCount > p2SpadesCount) {
            p1CurrentScore++;
        } else if (p2SpadesCount > p1SpadesCount) {
            p2CurrentScore++;
        }

        this.gamePlayers.get(0).setScore(p1CurrentScore);
        this.gamePlayers.get(1).setScore(p2CurrentScore);
    }

    /**
     * Generates a string consisting of all the end of round information for the players.
     * @return String with all end of round info
     */
    public String generateEndOfRoundReport() {
        String report = "";
        report += "Human final pile: " + humanFinalPile + '\n';
        report += "Computer final pile: " + computerFinalPile + '\n';
        report += "Human pile size: " + humanPileSize + '\n';
        report += "Computer pile size: " + computerPileSize + '\n';
        report += "Human spades count: " + humanSpades + '\n';
        report += "Computer spades count: " + computerSpades + '\n';
        report += "Human current score: " + gamePlayers.get(0).getScore() + '\n';
        report += "Computer current score: " + gamePlayers.get(1).getScore() + '\n';

        return report;
    }
}
