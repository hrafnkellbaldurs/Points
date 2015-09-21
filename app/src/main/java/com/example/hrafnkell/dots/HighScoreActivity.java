package com.example.hrafnkell.dots;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreActivity extends MainActivity {

    private ListView m_listView;

    ArrayList<HighScore> m_highScoreRecords;

    HighScoreAdapter m_adapter;

    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        TextView title = (TextView) findViewById(R.id.highscores_title);
        String bs = m_sp.getString("boardsize", "6");
        title.setText(bs + "x" + bs + " High Scores");

        m_listView = (ListView) findViewById(R.id.highscore_records);
    }

    public void onResume(){
        super.onResume();
        int boardSize = Integer.parseInt(m_sp.getString("boardsize", "6"));
        m_highScoreRecords = db.getHighScores(boardSize);
        m_adapter = new HighScoreAdapter(this, m_highScoreRecords);
        m_listView.setAdapter(m_adapter);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_score, menu);
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
