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
	
	// Various Views
	ViewPager viewPager;
	ImageButton next;
    ImageButton prev;
    
    // Number of items in viewPager, ie. number of pets
    int numItems;
    
    // is called when previous or next arrow clicked
    OnClickListener clickListener = new OnClickListener()
    {
    	@Override
    	public void onClick(View v)
		{    		
    		// Get tag from arrow button indicating whether left
    		// or right button was pressed. Make ViewPager scroll
    		// in that direction.
    		viewPager.arrowScroll((Integer) v.getTag());
		}
    };
    	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pick_pet);
        
        // Initiate Views
        pickPetText = (TextView) findViewById(R.id.pick_pet_text);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        // the next and prev arrow buttons start out being invisible
    	next = (ImageButton) findViewById(R.id.right_arrow_button);
        prev = (ImageButton) findViewById(R.id.left_arrow_button);
        
        // if first time app opened, display starter pet text
        if(Prefs.getBoolean(this, Prefs.firstTime, true))
        {
        	pickPetText.setText(R.string.pick_pet);
        }
	    
        // So OnClickListener knows which button is being pressed.
        // Use View.FOCUS_RIGHT and View.FOCUS_LEFT because the ViewPager.arrowScroll()
        // method inside the onClickListener uses these to determine direction
        next.setTag(View.FOCUS_RIGHT);
        prev.setTag(View.FOCUS_LEFT);
        
        // will handle scrolling the viewPager when the next or previous
        // arrow buttons are pressed
        next.setOnClickListener(clickListener);
        prev.setOnClickListener(clickListener);
        
        ImageAdapter adapter = new ImageAdapter(this);
        numItems = adapter.getCount();
        
        // for more than one one item in viewPager, show initial next arrow button
        if (numItems > 1)
        	next.setVisibility(View.VISIBLE);
        
        // adapter provides images for viewPager to display
	    viewPager.setAdapter(adapter);
	    // when page is changed, decide if left or right navigation arrows should be displayed
	    // or hidden
	    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	    	
	    	@Override	// must be implemented or java will complain
	    	public void onPageScrollStateChanged (int state) { }
	    	
	    	@Override	// must be implemented or java will complain
	    	public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) { }
	    	
	    	// updates arrow button visibility when page is changed
	    	@Override
	    	public void onPageSelected (int position)
	    	{
	    		if (position == 0)	// first page
	    		{
	    			// hide prev arrow and show next arrow
	    			prev.setVisibility(View.INVISIBLE);
	    			next.setVisibility(View.VISIBLE);
	    		}
	    		else if (position == numItems-1)	// last page
	    		{
	    			// hide next arrow and show prev arrow
	    			next.setVisibility(View.INVISIBLE);
	    			prev.setVisibility(View.VISIBLE);
	    		}
	    		else	// all other pages
	    		{
	    			// show both prev and next arrow
	    			prev.setVisibility(View.VISIBLE);
	    			next.setVisibility(View.VISIBLE);
	    		}
	    	}
	    });
    }
}