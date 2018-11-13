package com.brandanquinn.casino.casino;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EndScreen extends AppCompatActivity {

    private View.OnClickListener exitTourneyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.exit(1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Bundle endState = getIntent().getExtras();

        final Button exitTourney = findViewById(R.id.exitTourney);
        exitTourney.setOnClickListener(exitTourneyListener);
        exitTourney.setVisibility(View.GONE);

        TextView winnerMessage = findViewById(R.id.tourneyWinner);
        TextView humanScore = findViewById(R.id.humanScore);
        TextView computerScore = findViewById(R.id.computerScore);

        humanScore.setText("H: " + Integer.toString(endState.getInt("humanScore")));
        computerScore.setText("C: " + Integer.toString(endState.getInt("computerScore")));

        if (endState.getString("winner").equals("tie")) {
            winnerMessage.setText("Human and Computer players have tied!");
        } else if (endState.getString("winner").equals("Human")) {
            winnerMessage.setText("Human player has won the tournament!");
        } else {
            winnerMessage.setText("Computer player has won the tournament!");
        }

    }
}
