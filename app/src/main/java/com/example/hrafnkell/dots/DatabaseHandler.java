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
    private static final String KEY_DOTCOUNT = "dotcount";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){


        String CREATE_HIGHSCORES_TABLE = "CREATE TABLE " + TABLE_HIGHSCORES + "("
                + KEY_NAME + " TEXT," + KEY_SCORE + " INTEGER," + KEY_DOTCOUNT + " INTEGER)";
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
        values.put(KEY_DOTCOUNT, highScore.getDotCount());

        db.insert(TABLE_HIGHSCORES, null, values);
        db.close();
    }

    public ArrayList<HighScore> getHighScores(int table){
        ArrayList<HighScore> highScoresList = new ArrayList<>();

        String selectQuery = "";
        if(table == 6){
            selectQuery += "SELECT name, score FROM " + TABLE_HIGHSCORES + " WHERE dotcount = 6";
        }
        else if(table == 8){
            selectQuery += "SELECT name, score FROM " + TABLE_HIGHSCORES + " WHERE dotcount = 8";
        }

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

    public int getHighScoresCount(int table){

        String countQuery = "SELECT * FROM " + TABLE_HIGHSCORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public void clearDb(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE "+ TABLE_HIGHSCORES);
        onCreate(db);
    }
}
