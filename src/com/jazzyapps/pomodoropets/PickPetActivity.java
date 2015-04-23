package com.jazzyapps.pomodoropets;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;


public class PickPetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pick_pet);
        
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.pick_pet_layout);
        linearLayout.setBackgroundColor(Color.CYAN);
        
	    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
	    ImageAdapter adapter = new ImageAdapter(this);
	    viewPager.setAdapter(adapter);
    }
}