package com.jazzyapps.pomodoropets;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
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
    
    public class ImageAdapter extends PagerAdapter {

    	Context context;
    	
    	// Variables for namePetDialog()
    	EditText petName;
    	AlertDialog dialog;
    	
    	// is called when image of pet is selected in viewPager
    	OnClickListener clickListener = new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			adoptPetDialog((Integer) v.getTag());	// tag contains viewPager position
    		}
        };
    	
    	private int[] petImages = {
    		R.drawable.shiba,
    		R.drawable.ditto,
    		};
    	
    	ImageAdapter(Context context)
    	{
    		this.context=context;
    	}
    	
    	@Override
    	public int getCount()
    	{
    		return petImages.length;
    	}
    	 
    	@Override
    	public boolean isViewFromObject(View view, Object object)
    	{
    		return view == ((ImageButton) object);
    	}
    	
    	@Override
    	public Object instantiateItem(ViewGroup container, int position)
    	{		
    		ImageButton imageButton = new ImageButton(context);
    		int padding = 50;
    		
    		imageButton.setPadding(padding, padding, padding, padding);
    		imageButton.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
    		imageButton.setBackgroundColor(Color.TRANSPARENT);
    		imageButton.setImageResource(petImages[position]);
    		imageButton.setTag(position);
    		imageButton.setOnClickListener(clickListener);
    		
    		((ViewPager) container).addView(imageButton, 0);
    		return imageButton;
    	}
    	 
    	@Override
    	public void destroyItem(ViewGroup container, int position, Object object)
    	{
    		((ViewPager) container).removeView((ImageButton) object);
    	}
    	
    	// Dialog congratulating user on adopting a pet
    	public void adoptPetDialog(int position)
    	{
    		// Message to be displayed when pet is selected for adoption
    		int petDialog[] = {
    			R.string.dialog_adopted_dog,
    			R.string.dialog_adopted_ditto
    		};
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(context);
    		builder.setMessage(petDialog[position])
    			   .setPositiveButton(R.string.btn_next, new DialogInterface.OnClickListener()
    			   {
    				   public void onClick(DialogInterface dialog, int id)
    				   {
    					   //save pet type
    					   namePetDialog();
    				   }
    			   });

            builder.create().show();	// create AlertDialog and show
    	}
    	
    	// Dialog prompting user to pick a name for their pet
    	public void namePetDialog()
    	{
    		// fun names that show up as a randomly chosen hint
    		String[] sampleNames = {
    			"April Schauer",
    			"Carrie Oakey",
    			"Chris P. Bacon",
    			"Dinah Soares",
    			"Hazle Nutt",
    			"Ty Coon",
    		};
    		
    		Random rand = new Random();
    		petName = new EditText(context);
    		
    		// makes every word start with a capital
    		petName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
    		// set random sample name as hint
    		petName.setHint(sampleNames[rand.nextInt(sampleNames.length)]);
    		// disable next button if no text is entered
            petName.addTextChangedListener(new TextWatcher() 
            {
                @Override	// must be implemented or java gets angry
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override	// must be implemented or java gets angry
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) 
                {
                    if (TextUtils.isEmpty(s))												// TextView in dialog is empty
                    {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);	// disable next button
                    }
                    else																	// TextView in dialog is not empty
                    {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);		// enable next button
                    }
                }
            });
            
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            
    		builder.setMessage(R.string.name_pet)
    			   .setView(petName)
    			   .setPositiveButton(R.string.btn_next, new DialogInterface.OnClickListener()
    			   {
    				   public void onClick(DialogInterface dialog, int id)
    				   {
    					   Prefs.setBoolean(context, Prefs.firstTime, false);
    					   Prefs.setBoolean(context, Prefs.newPet, false);
    					   Prefs.setString(context, Prefs.petName, petName.getText().toString());
    					   context.startActivity(new Intent(context, HomeActivity.class));
    					   
    					   // finish PickPetActivity so the back button cannot go back to it
    					   ((Activity) context).finish();
    				   }
    			   });
            
            dialog = builder.create();
            dialog.show();
            
            // disable 'next' button initially, it will be enabled by textChangedListener
            // when text is entered
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    	}
    }
}