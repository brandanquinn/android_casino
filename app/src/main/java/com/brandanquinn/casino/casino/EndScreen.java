package com.brandanquinn.casino.casino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EndScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        Bundle endState = getIntent().getExtras();

        TextView winnerMessage = findViewById(R.id.tourneyWinner);
        TextView humanScore = findViewById(R.id.humanScore);
        TextView computerScore = findViewById(R.id.computerScore);

        humanScore.setText(Integer.toString(endState.getInt("humanScore")));
        computerScore.setText(Integer.toString(endState.getInt("computerScore")));

        if (endState.getString("winner").equals("tie")) {
            winnerMessage.setText("Human and Computer players have tied!");
        } else if (endState.getString("winner").equals("Human")) {
            winnerMessage.setText("Human player has won the tournament!");
        } else {
            winnerMessage.setText("Computer player has won the tournament!");
        }

    }
}
