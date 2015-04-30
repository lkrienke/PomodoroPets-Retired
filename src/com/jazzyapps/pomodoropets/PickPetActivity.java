package com.jazzyapps.pomodoropets;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
//import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
//import android.widget.LinearLayout;
import android.widget.TextView;


public class PickPetActivity extends Activity {
	private TextView pickPetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pick_pet);
        
        SharedPreferences prefs = getSharedPreferences("PomoPetsPrefs", 0);
        pickPetText = (TextView) findViewById(R.id.pick_pet_text);
        
        //this is working
        if(prefs.getBoolean("petPicked", false))
        {
        	pickPetText.setText(R.string.pick_new_pet);
        }
        
	    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
	    ImageAdapter adapter = new ImageAdapter(this);
	    viewPager.setAdapter(adapter);
    }
}