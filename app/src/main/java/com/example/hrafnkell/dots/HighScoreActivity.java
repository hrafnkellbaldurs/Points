package com.example.hrafnkell.dots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreActivity extends AppCompatActivity {

    private ListView m_listView;

    ArrayList<HighScoreRecord> m_highScoreRecords = new ArrayList<>();

    HighScoreRecordAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        Intent intent = getIntent();
        m_listView = (ListView) findViewById(R.id.highscore_records);
        m_adapter = new HighScoreRecordAdapter(this, m_highScoreRecords);
        m_listView.setAdapter(m_adapter);

        storeHighScoreRecord("Gunnar", 42);
        storeHighScoreRecord("Atli", 502);
        storeHighScoreRecord("Magnus", 1337);
        storeHighScoreRecord("Hrabbi", 1337);
        storeHighScoreRecord("Sara", 1);
        storeHighScoreRecord("Rannveig", 80085);
        storeHighScoreRecord("Sigmundur David", 404);
    }

    @Override
    public void onStart(){
        super.onStart();
        readRecords();
        m_adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop(){
        super.onStop();
        writeRecords();
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

    /**
     * Adds a new high score to the high score record list
     * @param name The name of the user
     * @param score The score of the user
     */
    public void storeHighScoreRecord(String name, int score){
        if(!name.isEmpty() && score >= 0){
            m_highScoreRecords.add(new HighScoreRecord(name, score));
            //sortHighScores();
            m_adapter.notifyDataSetChanged();
        }
    }

    void sortHighScores(){
        // TODO: fix alghorithm
        int highest = 0;
        for (int i = 0; i < m_highScoreRecords.size() ; i++) {
            int score = m_highScoreRecords.get(i).getScore();
            if (score > highest) {
                highest = score;
                HighScoreRecord newHighScore = m_highScoreRecords.remove(i);
                m_highScoreRecords.add(0, newHighScore);
            }
        }
    }

    void writeRecords(){
        try{
            FileOutputStream fos = openFileOutput("highscores2.ser", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(m_highScoreRecords);
            oos.close();
            fos.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    void readRecords(){
        try{
            FileInputStream fis = openFileInput("highscores2.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<HighScoreRecord> highScoreRecords = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
            m_highScoreRecords.clear();
            for(HighScoreRecord rec : highScoreRecords){
                m_highScoreRecords.add(rec);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
