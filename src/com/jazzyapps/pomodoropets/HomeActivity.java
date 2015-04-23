package com.jazzyapps.pomodoropets;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class HomeActivity extends Activity {
	
	//Variables for the counter calculations
	//Consider doing an int to char conversion to make it prettier.
	int clickCount = 10; //start at ten seconds
	int origClickCount = 10;
	int min = 0;
	int sec = 10;
	
	private boolean working = false;
	private boolean resetGame = false;
	private int money = 20;
	
	//Various Views of DOOM
	private PetCountDownTimer c;
	private TextView tv;
	private Button button;
	private ProgressBar pb;
	private AlertDialog.Builder taBuilder;
	private AlertDialog timerAlert;
	
	//Shared Preference Variables, need to use sp and sped for all of these
	//keys have the same names as the variables
	private int currProgress;

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Change preferences
        SharedPreferences prefs = getSharedPreferences("PomoPetsPrefs", 0);
        boolean petPicked = prefs.getBoolean("petPicked", false);
        
        // if no pet has been picked before, run the PickPetActivity
        if(!petPicked)
        {
            startActivity(new Intent(this, PickPetActivity.class));
            finish();	// finish first instance of MainActivity if first time 
            			// using app so back button does not go back to blank screen
            			// (since setContentView() has not been called, no layout
            			// is displayed)
        } 
        else 
        {
	        setContentView(R.layout.activity_home);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        
	        //Might work? Don't think there's a way to make sp global.
	        SharedPreferences sp = getSharedPreferences("PomoPetsPrefs", 0);
	        SharedPreferences.Editor sped = sp.edit();
	        currProgress = sp.getInt("currProgress", 50);
	        
	        tv = (TextView) findViewById(R.id.textView1);
			button = (Button) findViewById(R.id.button1);
			pb = (ProgressBar) findViewById(R.id.progressBar1);
			//Stuff for customizing the progress bar.
			pb.setProgress(currProgress);
			int color = 0xFF00FF00;
			pb.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
			pb.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
			
			initClock();
	        initTimerDialogue();
	        
	        sped.commit(); //I think I need this for first time I make prefs file
        }
    }

	/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

	
	public class PetCountDownTimer extends CountDownTimer 
	{
		SharedPreferences sp = getSharedPreferences("PomoPetsPrefs", 0);
        SharedPreferences.Editor sped = sp.edit();

		
		public PetCountDownTimer(long startTime, long interval)
		{
			super(startTime, interval);
		}
		
		//I don't think I need to use the parameter.
		//Lots of calculation errors based on clock cycles, using my own variables for display.
		@Override
	    public void onTick(long millsTillDone) 
	    {
			clickCount--; //might want to put this after the loop?
			
			if(sec == 0) //need to roll the minute over
			{
				sec = 59;
				min--;
			}
			else
			{
				sec--;
			}

			
			initClock();
			
			/*
			min = millsTillDone / (60 * 1000);
			sec = (millsTillDone/(double)1000) % 60;
			//sec = Math.floor(sec);
			
	        tv.setText(min + ":" + sec);
	        */
	    }

		@Override
	    public void onFinish() 
	    {
			//You'll get money in here!
	        tv.setText("Timer Done!");
	        working = false;
	        button.setText("Reset Timer?");
	        
	        currProgress = pb.getProgress() + money;
	        pb.setProgress(currProgress);
	        
	        sped.putInt("currProgress", currProgress);
	        
	        if(pb.getProgress() == 100)
	        {
	        	tv.setText("You win!");
	        	button.setText("Reset Game?");
	        	resetGame = true;
	        }
	        
	        clickCount = origClickCount;
	        
	        sped.commit();
	    }
	};
	

	
	public void workButtonClicked(View v) //like an interrupt?
	{
		SharedPreferences sp = getSharedPreferences("PomoPetsPrefs", 0);
        SharedPreferences.Editor sped = sp.edit();
		
        //consider making it so reset doesn't start the timer again?
		if(resetGame)
		{
			pb.setProgress(50);
			
			sped.putInt("currProgress", 50);
			
			resetGame = false;
		}
		
		if(!working)
		{
			//Set up timer
			c = new PetCountDownTimer((clickCount*1000), 1000);
			c.start();
			
			button.setText("Give Up");
			working = true;
		}
		else //working
		{
			// Only happens if you give up early.
			// Pet loses Happiness
			c.cancel();
			
			tv.setText("Wow, Really?");
			button.setText("You Suck, Reset Timer?");
			working = false;
			
			currProgress = pb.getProgress() - money;
			pb.setProgress(currProgress);
			
			
			sped.putInt("currProgress", currProgress);
			
			if(pb.getProgress() == 0)
			{
				tv.setText("Your Pet is Dead. Congrats.");
				button.setText("Reset Game?");
				resetGame = true;
			}
			
			clickCount = origClickCount;
		}
		
		sped.commit();
		
	}
	
	private void initTimerDialogue()
	{
		CharSequence timeOptions[] = new CharSequence[] {"1", "5", "10", "25", "Test"}; 
		
		//Might have to put this in main, check if program crashes?
		taBuilder = new AlertDialog.Builder(this) //Awesome shortcuts ahead!
		.setTitle("Work for how many minutes?")
		.setCancelable(true)
		.setPositiveButton("Done", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				//Nothing Yet
				//consider making the dialog non cancelable
			}
		})
		.setSingleChoiceItems(timeOptions, 4, new DialogInterface.OnClickListener(){
			//Middle param = 0 means the default choice with show on choice index 0
			//Make sure you change this when testing phase is done.
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch(which)
				{
				case 0: setClickCount(1);
						break;
				case 1: setClickCount(5);
						break;
				case 2: setClickCount(10);
						break;
				case 3: setClickCount(25);
						break;
				case 4: setClickCount(0); //test case!
						break;
				}
			}
		});
			timerAlert = taBuilder.create();
	}
	
	//i represents the number of minutes user wants to wait for on next work cycle.
	public void setClickCount(int i)
	{
		if(i != 0)
		{
			clickCount = i*60;
		}
		else //test case
		{
			clickCount = 10;
		}
		origClickCount = clickCount;
		initClock();
		return;
	}
	
	//Runs when the clock is touched by the user
	public void changeTimeButtonClicked(View v)
	{
		if(!working)
		{
			timerAlert.show();
		}
	}

	//Used to display the current clock
	private void initClock()
	{
		if(clickCount >= 60)
		{
			min = clickCount/60;
			sec = clickCount%60; //should work?
		}
		else
		{
			min = 0;
			sec = clickCount;
		}
		
		tv.setText(min + ":" + sec);		        
	}
}
