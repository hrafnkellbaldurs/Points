package com.example.hrafnkell.dots;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Notandi on 18-Sep-15.
 */
public class OptionsActivity extends PreferenceActivity {

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        //setContentView(R.layout.activity_options);
        addPreferencesFromResource(R.xml.preferences);

    }

}
