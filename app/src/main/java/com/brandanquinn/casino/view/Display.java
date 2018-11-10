package com.brandanquinn.casino.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandanquinn.casino.casino.R;
import com.brandanquinn.casino.model.Build;
import com.brandanquinn.casino.model.Card;
import com.brandanquinn.casino.model.Player;
import com.brandanquinn.casino.model.Table;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.Context.ACTIVITY_SERVICE;

public class Display {
    private ArrayList<Button> fileButtons;
    private ArrayList<ImageButton> humanButtons;
    private ArrayList<ImageButton> tableButtons;
    private ArrayList<Button> buildButtons;
    private Button humanPile;
    private Button computerPile;
    private Activity gameActivity;
    private Context appContext;

    public Display(Context context) {
        this.humanButtons = new ArrayList<>();
        this.tableButtons = new ArrayList<>();
        this.buildButtons = new ArrayList<>();
        this.fileButtons = new ArrayList<>();
        this.appContext = context;
        this.gameActivity = (Activity)this.appContext;

    }

    /**
     * Getter for fileButtons private member variable
     * @return ArrayList of fileButtons generated for save file selection.
     */
    public ArrayList<Button> getFileButtons() {
        return fileButtons;
    }

    /**
     * Getter for humanButtons private member variable
     * @return ArrayList of buttons in human's hand
     */
    public ArrayList<ImageButton> getHumanButtons() {
        return this.humanButtons;
    }

    /**
     * Getter for computerButtons private member variable
     * @return ArrayList of buttons in the computer's hand
    /

    /**
     * Getter for tableButtons private member variable
     * @return ArrayList of buttons on the table
     */
    public ArrayList<ImageButton> getTableButtons() { return this.tableButtons; }

    public ArrayList<Button> getBuildButtons() { return this.buildButtons; }

    /**
     * Getter for humanPile private member variable
     * @return Button to display human pile
     */
    public Button getHumanPile() { return this.humanPile; }

    /**
     * Getter for computerPile private member variable
     * @return Button to display computer pile
     */
    public Button getComputerPile() { return this.computerPile; }

    /**
     * Updates the various view components using information from the model.
     * @param gamePlayers, ArrayList of game players
     * @param gameTable, Table object used to access cards/builds on the table
     * @param roundNum, int used to keep track of current round number
     */
    public void updateView(ArrayList<Player> gamePlayers, Table gameTable, int roundNum, String whoIsPlaying) {

        displayCards(gamePlayers, gameTable);
        displayRoundNum(roundNum);
        displayScores(gamePlayers, whoIsPlaying);
    }

    public LinearLayout getFileSelection(ArrayList<File> fileList) {

        LinearLayout fileGrid = new LinearLayout(appContext);
        fileGrid.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 5, 5, 5);
        fileGrid.setLayoutParams(params);


        for (int i = 0; i < fileList.size(); i++) {
            Button fileButton = new Button(appContext);
            fileButton.setText(fileList.get(i).getName());
            fileButton.setTag(fileList.get(i).getName());
            fileButton.setHeight(50);
            fileButton.setWidth(200);
            fileButton.setVisibility(View.VISIBLE);
            fileButton.setLayoutParams(new LinearLayout.LayoutParams(200, 50));
            this.fileButtons.add(fileButton);
            fileGrid.addView(fileButton);
        }

        return fileGrid;
    }

    /**
     * Adds dynamically created cards to their respective grids in the view.
     * @param gamePlayers, ArrayList of game players
     * @param gameTable, Table object used to access cards/builds on the table
     */
    private void displayCards(ArrayList<Player> gamePlayers, Table gameTable) {
        // For each card in human hand - create an image button with proper card image
        // Assign button to a column in the respective players grid
        ArrayList<Card> humanHand = gamePlayers.get(0).getHand();
        ArrayList<Card> computerHand = gamePlayers.get(1).getHand();
        ArrayList<Card> tableCards = gameTable.getTableCards();
        ArrayList<Build> currentBuilds = gameTable.getCurrentBuilds();
        int cardNum = gameTable.getCurrentBuilds().size() + gameTable.getTableCards().size();

        // Grid components in view
        LinearLayout humanGrid = this.gameActivity.findViewById(R.id.humanHand);
        LinearLayout computerGrid = this.gameActivity.findViewById(R.id.computerHand);
        LinearLayout tableGrid = this.gameActivity.findViewById(R.id.tableGrid);

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)gameActivity.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;

//Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;

        TextView debug = gameActivity.findViewById(R.id.debugBox2);
        debug.setText(Double.toString(availableMegs) + '\n' + Double.toString(percentAvail) + "%");

        // Empty grid components and button containers
        humanGrid.removeAllViews();
        humanButtons.clear();

        computerGrid.removeAllViews();

        tableGrid.removeAllViews();
        tableButtons.clear();
        buildButtons.clear();

        for (int i = 0; i < currentBuilds.size(); i++) {
            ArrayList<ArrayList<Card>> buildCards = currentBuilds.get(i).getTotalBuildCards();
            tableGrid.addView(createBuildButton(buildCards, cardNum, currentBuilds.get(i).getBuildString()));
        }
        for (int i = 0; i < tableCards.size(); i++) {
            tableGrid.addView(createButton(tableCards.get(i), "tableCards", cardNum));
        }

        for (int i = 0; i < humanHand.size(); i++) {
            humanGrid.addView(createButton(humanHand.get(i), "humanHand", humanHand.size()));
        }
        humanPile = new Button(appContext);
        humanPile.setText("Human Pile");
        humanPile.setLayoutParams(new LinearLayout.LayoutParams(150, 180));
        humanGrid.addView(humanPile);

        for (int i = 0; i < computerHand.size(); i++) {
            computerGrid.addView(createComputerView(computerHand.get(i)));
        }
        computerPile = new Button(appContext);
        computerPile.setText("Computer Pile");
        computerPile.setLayoutParams(new LinearLayout.LayoutParams(150, 180));
        computerGrid.addView(computerPile);




    }

    /**
     * Dynamically create buttons for each build.
     * @param buildCards
     * @param numCards
     * @return
     */
    private Button createBuildButton(ArrayList<ArrayList<Card>> buildCards, int numCards, String buildString) {
        Button buildButton = new Button(appContext);
        buildButton.setTag(buildString);

        int lightBlue = Color.parseColor("#7bb3ff");
        buildButton.setBackgroundColor(lightBlue);
        buildButton.setVisibility(View.VISIBLE);

        if (buildCards.size() > 1) {
            buildButton.setText("Multi-Build of:\n " + buildString);
        } else {
            buildButton.setText("Build of: \n" + buildString);
        }

        if (numCards > 8) {
            int width = 1268 / numCards;
            buildButton.setWidth(width);
            buildButton.setLayoutParams(new LinearLayout.LayoutParams(width, 180));
        } else {
            buildButton.setWidth(150);
            buildButton.setLayoutParams(new LinearLayout.LayoutParams(150, 180));
        }
        buildButton.setHeight(180);

        buildButtons.add(buildButton);

        return buildButton;
    }

    /**
     * Dynamically creates ImageButtons for cards using drawable resources.
     * @param gameCard, Card object used to get image resource and model ImageButton
     * @param layoutId, Used to identify where the card will be placed on its respective grid and how it must be sized.
     * @param numCards, Also used to dynamically scale size of cards based on how many are placed in grid.
     * @return ImageButton dynamically created and based on Card from model.
     */
    private ImageButton createButton(Card gameCard, String layoutId, int numCards) {
        // Programmatically create and return an ImageButton based on card passed.
        // Whenever ImageButton is created, add it to currentButtons list

        System.out.println("CREATE BUTTON CALLED");
        // Initializing ImageButton and basic resources
        ImageButton cardBtn = new ImageButton(appContext);

        if (gameCard.getLockedToBuild()) {
            int lightBlue = Color.parseColor("#7bb3ff");
            cardBtn.setBackgroundColor(lightBlue);
        }

        String cardImageResource = gameCard.getImageResourceName();
        int imageID = appContext.getResources().getIdentifier(cardImageResource, "mipmap", appContext.getPackageName());

        // Setting ImageButton to proper card image
        setScaledImageButton(cardBtn, imageID);
        cardBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        cardBtn.setTag(gameCard.getCardString());

        // Max width of table is 1268 dp
        if (layoutId == "tableCards" && numCards > 8) {
            int width = 1268 / numCards;
            cardBtn.setLayoutParams(new LinearLayout.LayoutParams(width, 180));
        } else {
            cardBtn.setLayoutParams(new LinearLayout.LayoutParams(150, 180));
        }

        if (layoutId == "tableCards") {
            this.tableButtons.add(cardBtn);
        } else {
            this.humanButtons.add(cardBtn);
        }

        return cardBtn;
    }

    /**
     * Creates ImageView for each card in computer player's hand.
     * @param gameCard, Card obj from player's hand.
     * @return ImageView created from card obj reference.
     */
    private ImageView createComputerView(Card gameCard) {
        ImageView cardView = new ImageView(appContext);

        if (gameCard.getLockedToBuild()) {
            int lightBlue = Color.parseColor("#7bb3ff");
            cardView.setBackgroundColor(lightBlue);
        }

        String cardImageResource = gameCard.getImageResourceName();
        int imageID = appContext.getResources().getIdentifier(cardImageResource, "mipmap", appContext.getPackageName());

        // Setting ImageButton to proper card image
        setScaledImage(cardView, imageID);
        cardView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        cardView.setTag(gameCard.getCardString());

        cardView.setLayoutParams(new LinearLayout.LayoutParams(150, 180));

        return cardView;
    }

    /**
     * Update view component for Round Number
     * @param roundNum, int value to track current round number.
     */
    public void displayRoundNum(int roundNum) {
        TextView roundNumDisplay = this.gameActivity.findViewById(R.id.roundNumber);

        roundNumDisplay.setText(Integer.toString(roundNum));
    }

    /**
     * Update view component for player scores and prints who is playing
     * @param gamePlayers, ArrayList of game players to access scores.
     * @param whoIsPlaying, String denoting who is currently playing.
     */
    public void displayScores(ArrayList<Player> gamePlayers, String whoIsPlaying) {
        // Set human score
        TextView humanScoreDisplay = this.gameActivity.findViewById(R.id.humanScore);
        humanScoreDisplay.setText(Integer.toString(gamePlayers.get(0).getScore()));

        // Set computer score
        TextView compScoreDisplay = this.gameActivity.findViewById(R.id.computerScore);
        compScoreDisplay.setText(Integer.toString(gamePlayers.get(1).getScore()));

        // Who is playing
        TextView debug = this.gameActivity.findViewById(R.id.debugBox);
        debug.setText(whoIsPlaying);
    }

    /**
     * Used to optimize memory storage to try to prevent Java out of memory error.
     * @param imageView, ImageView created for computer player's hand.
     * @param resId, Image resource ID generated for Card background.
     */
    private void setScaledImage(ImageView imageView, final int resId) {
        final ImageView iv = imageView;
        ViewTreeObserver viewTreeObserver = iv.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                int imageViewHeight = iv.getMeasuredHeight();
                int imageViewWidth = iv.getMeasuredWidth();
                iv.setImageBitmap(
                        decodeSampledBitmapFromResource(gameActivity.getResources(),
                                resId, imageViewWidth, imageViewHeight));
                return true;
            }
        });
    }

    /**
     * Used to optimize memory storage to try to prevent Java out of memory error
     * @param imageButton, ImageButton created for Human hand / table
     * @param resId, Image resource ID generated for Card background.
     */
    private void setScaledImageButton(ImageButton imageButton, final int resId) {
        final ImageButton ib = imageButton;
        ViewTreeObserver viewTreeObserver = ib.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                ib.getViewTreeObserver().removeOnPreDrawListener(this);
                int imageViewHeight = ib.getMeasuredHeight();
                int imageViewWidth = ib.getMeasuredWidth();
                ib.setImageBitmap(
                        decodeSampledBitmapFromResource(gameActivity.getResources(),
                                resId, imageViewWidth, imageViewHeight));
                return true;
            }
        });
    }

    /**
     * Function used to decode the bitmap image in a more efficient manor.
     * @param res, List of image resources from App context
     * @param resId, Image resource ID used for specific View
     * @param reqWidth, Image width
     * @param reqHeight, Image height
     * @return
     */
    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                          int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds = true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
