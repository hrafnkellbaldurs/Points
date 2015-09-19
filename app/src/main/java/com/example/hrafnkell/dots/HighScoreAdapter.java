package com.example.hrafnkell.dots;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class HighScoreAdapter extends ArrayAdapter<HighScore> {


    public HighScoreAdapter(Context context, ArrayList<HighScore> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HighScore hs = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout_highscore, parent, false);
        }

        // Set the row number of a highscore
        TextView numberView = (TextView) convertView.findViewById(R.id.row_number);
        String pos = String.valueOf(position + 1);
        numberView.setText(pos);

        // Set the row score of a highscore
        TextView scoreView = (TextView) convertView.findViewById(R.id.row_score);
        String score = String.valueOf(hs.getScore());
        scoreView.setText(score);

        // Set the name of the user with the highscore
        TextView nameView = (TextView) convertView.findViewById(R.id.row_name);
        nameView.setText( hs.getName());

        return convertView;
    }
}