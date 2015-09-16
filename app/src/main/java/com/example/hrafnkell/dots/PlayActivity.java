package com.example.hrafnkell.dots;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PlayActivity extends Activity {

    public TextView scoreView;
    //Listener
    BoardView m_bv;


    //publi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        m_bv = (BoardView) findViewById(R.id.boardView);
        scoreView = (TextView) findViewById(R.id.play_score);
        scoreView.setText("0");

        m_bv.setScoreHandler(new ScoreHandler() {
            @Override
            public void setScore(int score) {
                String scoreString = String.valueOf(score);
                scoreView.setText(scoreString);
            }
        });


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
