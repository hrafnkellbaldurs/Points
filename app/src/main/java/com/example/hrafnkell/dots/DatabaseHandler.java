package com.example.hrafnkell.dots;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Hrafnkell on 19/9/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "highscoreManager";

    private static final String TABLE_HIGHSCORES = "highscores";

    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_HIGHSCORES_TABLE = "CREATE TABLE " + TABLE_HIGHSCORES + "("
                + KEY_NAME + " TEXT," + KEY_SCORE + " INTEGER)";
        db.execSQL(CREATE_HIGHSCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);

        onCreate(db);
    }

    public void addHighscore(HighScore highScore){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highScore.getName());
        values.put(KEY_SCORE, highScore.getScore());

        db.insert(TABLE_HIGHSCORES, null, values);
        db.close();
    }

    public ArrayList<HighScore> getHighScores(){
        ArrayList<HighScore> highScoresList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_HIGHSCORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                HighScore highScore = new HighScore();
                highScore.setName(cursor.getString(0));
                highScore.setScore(Integer.parseInt(cursor.getString(1)));

                highScoresList.add(highScore);

            }while(cursor.moveToNext());
        }

        Collections.sort(highScoresList);

        return highScoresList;
    }

    public int getHighScoresCount(){
        String countQuery = "SELECT * FROM " + TABLE_HIGHSCORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void clearDb(){
        SQLiteDatabase db = this.getWritableDatabase();
    }
}
