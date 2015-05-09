package com.jazzyapps.pomodoropets;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


public class PickPetActivity extends Activity {
	private TextView pickPetText;
	
	ViewPager viewPager;
	ImageButton next;
    ImageButton prev;
    
    int numItems;			// in viewPager
    final int PREV = 0;
    final int NEXT = 1;
    
    // for when previous or next arrow clicked
    OnClickListener clickListener = new OnClickListener()
    {
    	@Override
    	public void onClick(View v)
		{
    		int position = viewPager.getCurrentItem();
    		
    		if ((Integer) v.getTag() == NEXT)					// next arrow clicked
    			viewPager.setCurrentItem(position+1, true);		// set viewPager to next page
    		else												// prev arrow clicked
    			viewPager.setCurrentItem(position-1, true);		// set viewPager to previous page
		}
    };
    	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pick_pet);
        
        pickPetText = (TextView) findViewById(R.id.pick_pet_text);
        
        // if not first time app opened, change text
        if(!Prefs.getBoolean(this, Prefs.firstTime, true))
        {
        	pickPetText.setText(R.string.pick_new_pet);
        }
        
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    	next = (ImageButton) findViewById(R.id.right_arrow_button);
        prev = (ImageButton) findViewById(R.id.left_arrow_button);
	    
        // so OnClickListener knows which button is being pressed
        next.setTag(NEXT);
        prev.setTag(PREV);
        
        next.setOnClickListener(clickListener);
        prev.setOnClickListener(clickListener);
        
        ImageAdapter adapter = new ImageAdapter(this);
        numItems = adapter.getCount();
        
        // for more than one one item in viewPager, show initial next arrow button
        if (numItems > 1)
        	next.setVisibility(View.VISIBLE);
        
	    viewPager.setAdapter(adapter);	// adapter provides images for viewPager to display
	    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	    	
	    	@Override	// must be implemented or java will complain
	    	public void onPageScrollStateChanged (int state) { }
	    	
	    	@Override	// must be implemented or java will complain
	    	public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) { }
	    	
	    	// updates arrow button visibility when page is changed
	    	@Override
	    	public void onPageSelected (int position)
	    	{
	    		if (position == 0)									// first page
	    		{													// hide prev arrow, show right arrow 
	    															// if only 2 images in viewPager
	    			prev.setVisibility(View.INVISIBLE);
	    			if (next.getVisibility() == View.INVISIBLE)
	    				next.setVisibility(View.VISIBLE);
	    		}
	    		else if (position == numItems-1)					// 2nd to last page
	    		{													// hide next arrow, show prev arrow
	    															// if only 2 images in viewPager
	    			next.setVisibility(View.INVISIBLE);
		    		if (prev.getVisibility() == View.INVISIBLE)
	    				prev.setVisibility(View.VISIBLE);
	    		}
	    		else												// all other pages
	    		{													// both arrows should be visible
	    			if (prev.getVisibility() == View.INVISIBLE)
	    				prev.setVisibility(View.VISIBLE);
	    			if (next.getVisibility() == View.INVISIBLE)
	    				next.setVisibility(View.VISIBLE);
	    		}
	    	}
	    });
    }
}