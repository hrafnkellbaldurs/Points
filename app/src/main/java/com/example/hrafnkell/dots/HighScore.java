package com.example.hrafnkell.dots;

import java.util.Comparator;

/**
 * Created by Hrafnkell on 19/9/2015.
 */
public class HighScore implements Comparable<HighScore> {

    String _name;
    int _score;
    int _dotCount;

    public HighScore(){

    }

    public HighScore(String name, int score, int dotCount){
        this._name = name;
        this._score = score;
        this._dotCount = dotCount;
    }

    @Override
    public int compareTo(HighScore record){
        return record.getScore() - getScore();
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public int getScore(){
        return this._score;
    }

    public void setScore(int score){
        this._score = score;
    }

    public int getDotCount(){
        return this._dotCount;
    }
}
