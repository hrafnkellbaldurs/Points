package com.example.hrafnkell.dots;

import android.app.Activity;
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

public class HighScoreActivity extends MainActivity {

    private ListView m_listView;

    ArrayList<HighScore> m_highScoreRecords;

    HighScoreAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        Intent intent = getIntent();

        m_listView = (ListView) findViewById(R.id.highscore_records);


        ArrayList<HighScore> list = db.getHighScores();
        System.out.println("HIGHSCORES LIST: " + list.size());

    }

    public void onResume(){
        super.onResume();
        m_highScoreRecords = db.getHighScores();
        m_adapter = new HighScoreAdapter(this, m_highScoreRecords);
        m_listView.setAdapter(m_adapter);
    }

    @Override
    public void onStart(){
        super.onStart();
        //readRecords();
    }

    @Override
    public void onStop(){
        super.onStop();
        //writeRecords();
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

    /*public void storeHighScoreRecord(String name, int score){
        if(!name.isEmpty() && score >= 0){
            m_highScoreRecords.add(new HighScoreRecord(name, score));
            sortHighScores();
            m_adapter.notifyDataSetChanged();
        }
    }*/

    void sortHighScores(){
        //Collections.sort(m_highScoreRecords);
    }

    /*void writeRecords(){
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
    }*/

    /*void readRecords(){
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
    }*/
}
