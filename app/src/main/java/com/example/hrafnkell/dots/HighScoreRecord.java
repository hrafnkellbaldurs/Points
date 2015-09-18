package com.example.hrafnkell.dots;

import java.io.Serializable;

/**
 * Created by Hrafnkell on 9/9/2015.
 */
public class HighScoreRecord implements Serializable, Comparable<HighScoreRecord> {

    private String m_name;
    private int m_score;

    HighScoreRecord(String name, int score){
        m_name = name;
        m_score = score;
    }

    @Override
    public int compareTo(HighScoreRecord record){
        return record.getScore() - getScore();
    }

    String getName(){
        return m_name;
    }

    int getScore(){
        return m_score;
    }


}
