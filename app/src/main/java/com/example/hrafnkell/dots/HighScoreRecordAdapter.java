package com.example.hrafnkell.dots;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Hrafnkell on 9/9/2015.
 */
public class HighScoreRecordAdapter extends ArrayAdapter<HighScoreRecord> {

    private final Context context;
    private final List<HighScoreRecord> values;

    public HighScoreRecordAdapter(Context context, List<HighScoreRecord> objects){
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // Inflater service
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout_highscore, parent,false);

        // Set the row number of a highscore
        TextView numberView = (TextView) rowView.findViewById(R.id.row_number);
        String pos = String.valueOf(position + 1);
        numberView.setText(pos);

        // Set the row score of a highscore
        TextView scoreView = (TextView) rowView.findViewById(R.id.row_score);
        String score = String.valueOf(values.get(position).getScore());
        scoreView.setText(score);

        // Set the name of the user with the highscore
        TextView nameView = (TextView) rowView.findViewById(R.id.row_name);
        nameView.setText( values.get(position).getName() );

        return rowView;
    }
}
