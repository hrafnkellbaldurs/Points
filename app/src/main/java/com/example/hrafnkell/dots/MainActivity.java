package com.example.hrafnkell.dots;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

    DatabaseHandler db;
    SharedPreferences m_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);
        m_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        String clearDb = m_sp.getString("cleardb", "no");
        if(clearDb.equals("yes")){
            db.clearDb();
            m_sp.edit().putString("cleardb", "no").commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance){
        super.onSaveInstanceState(savedInstance);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            try{
                Intent intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onPlayClick(View view){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    /**
     * If the High Score button is pressed, we go to that activity
     * @param view The current view
     */
    public void onHighScoreClick(View view){
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }

    public void onOptionsClick(View view){
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
}
