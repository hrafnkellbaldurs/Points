package com.example.hrafnkell.dots;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayActivity extends MainActivity {

    final Context context = this;
    public TextView scoreView;
    public TextView movesCountView;
    BoardView m_bv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        m_bv = (BoardView) findViewById(R.id.boardView);
        scoreView = (TextView) findViewById(R.id.play_score);
        movesCountView = (TextView) findViewById(R.id.moves_count);

        m_bv.setGameHandler(new GameHandler() {
            @Override
            public void setView(int playCount, int score) {
                String scoreString = String.valueOf(score);
                String moveCountString = String.valueOf(playCount);
                scoreView.setText(scoreString);
                movesCountView.setText(moveCountString);
            }

            @Override
            public void endGame(int score) {
                addHighScore(score);
            }
        });


    }

    public void addHighScore(final int score){

        final EditText playerName = new EditText(context);

        playerName.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder endGameDialog = new AlertDialog.Builder(context);

        endGameDialog.setTitle("Game Over");

        endGameDialog
                .setMessage("Score: " + score + "\nPlease enter your name")
                .setCancelable(false)
                .setView(playerName)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String name = "";
                                name = playerName.getText().toString();
                                db.addHighscore(new HighScore(name, score, 6));
                                Intent intent = new Intent(context, HighScoreActivity.class);
                                startActivity(intent);

                            }
                        }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        db.clearDb();
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = endGameDialog.create();

        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
