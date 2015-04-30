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
	int clickCount = 5; //start at ten seconds
	int origClickCount = 5;
	int min = 0;
	int sec = 5;
	
	//Game state variables
	private boolean working = false;
	private int money = 30; 
	private String scoreDisplay = "Score: ";
	
	//Various Views of DOOM
	private PetCountDownTimer c;
	private TextView tv;
	private Button button;
	private ProgressBar pb;
	private AlertDialog.Builder taBuilder;
	private AlertDialog timerAlert;
	private TextView scoretv;
	
	//Shared Preference Variables, need to use sp and sped for all of these
	//keys have the same names as the variables
	private int currProgress;
	private boolean resetGame = false;
	private int score = 0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
	        
	        SharedPreferences sp = getSharedPreferences("PomoPetsPrefs", 0);
	        SharedPreferences.Editor sped = sp.edit();
	        currProgress = sp.getInt("currProgress", 50);
	        score = sp.getInt("score", 0);
	        
	        tv = (TextView) findViewById(R.id.textView1);
			button = (Button) findViewById(R.id.button1);
			pb = (ProgressBar) findViewById(R.id.progressBar1);
			scoretv = (TextView) findViewById(R.id.textView3);
			displayScore();
			
	        checkForPrevEnding();
			
			//Stuff for customizing the progress bar. All other style in XML.
			pb.setProgress(currProgress);
			int color = 0xFF00FF00;
			pb.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
			pb.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
			
			initClock();
	        initTimerDialogue();
        }
    }
	
	
	public class PetCountDownTimer extends CountDownTimer 
	{
		SharedPreferences sp = getSharedPreferences("PomoPetsPrefs", 0);
        SharedPreferences.Editor sped = sp.edit();
		
		public PetCountDownTimer(long startTime, long interval)
		{
			super(startTime, interval);
		}
		
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
	    }

		@Override
	    public void onFinish() 
	    {
			//You'll get money/happiness in here!
        	tv.setText(R.string.win);
			working = false;
	        button.setText("Reset Timer?");
	        
	        currProgress = pb.getProgress() + money;
	        pb.setProgress(currProgress);
	        
	        sped.putInt("currProgress", currProgress);
	        
	        if(pb.getProgress() == 100)
	        {	
	        	sped.putBoolean("resetGame", true);
	        	runEndState();
	        }
	        
	        clickCount = origClickCount;
	        sped.commit();
	    }
	};
	
	public void workButtonClicked(View v) //like an interrupt?
	{
		SharedPreferences sp = getSharedPreferences("PomoPetsPrefs", 0);
        SharedPreferences.Editor sped = sp.edit();
        
        displayScore();
        resetGame = sp.getBoolean("resetGame", false);
		
		if(resetGame)
		{
			pb.setProgress(50);
			
			sped.putInt("currProgress", 50);
			sped.putBoolean("resetGame", false);
			
			button.setText(R.string.workButton);
			initClock();
			
			//pick a new pet! (because yours died)
            startActivity(new Intent(this, PickPetActivity.class));
            finish();
            
		} //Added else to fix part of the resetGame error
		else if(!working)
		{
			//Set up timer
			c = new PetCountDownTimer((clickCount*1000), 1000);
			c.start();
			
			button.setText("Give Up");
			working = true;
		}
		else //User gave up early
		{
			c.cancel();
			
			tv.setText(R.string.loss);
			button.setText("You Suck, Reset Timer?");
			working = false;
			
			currProgress = pb.getProgress() - money;
			pb.setProgress(currProgress);
			sped.putInt("currProgress", currProgress);
			
			if(pb.getProgress() == 0)
			{
				sped.putBoolean("resetGame", true);
				runEndState();
			}
			
			clickCount = origClickCount;
		}
		
		sped.commit();
		
	}
	
	private void initTimerDialogue()
	{
		CharSequence timeOptions[] = new CharSequence[] {"1", "5", "10", "25", "Test"}; 
		
		taBuilder = new AlertDialog.Builder(this) //Awesome shortcuts ahead!
		.setTitle("Work for how many minutes?")
		.setCancelable(true)
		.setPositiveButton("Done", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				//Nothing Yet
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
	
	//Runs when the clock is touched by the user
	//Only works if the timer isn't currently running
	public void changeTimeButtonClicked(View v)
	{
		if(!working)
		{
			timerAlert.show();
		}
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
			clickCount = 5;
		}
		origClickCount = clickCount;
		
		moneyCalc();
		initClock();
		return;
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
	
	//This method makes sure you can't cheat your way out of dying (also doesn't cheat you out of winning)
	private void checkForPrevEnding()
	{
		SharedPreferences sp = getSharedPreferences("PomoPetsPrefs", 0);
		resetGame = sp.getBoolean("resetGame", false);
		if(resetGame)
		{
			runEndState();
		}
	}
	
	//Uses origClickCount to decide how much happiness you will win or lose.
	//If test is on, money will remain the default value assigned at the top of the program.
	//Will change this calculation later after we decide on good timer options for the user.
	public void moneyCalc()
	{
		if(origClickCount > 10)
		{
			money = origClickCount/60; //1min = 1 money right now
		}
	}
	
	public void displayScore()
	{
		SharedPreferences sp = getSharedPreferences("PomoPetsPrefs", 0);
		score = sp.getInt("score", 0);
		scoreDisplay = "Score: " + score;
		scoretv.setText(scoreDisplay);
	}
	
	public void runEndState()
	{
		SharedPreferences sp = getSharedPreferences("PomoPetsPrefs", 0);
		SharedPreferences.Editor sped = sp.edit();
		score = sp.getInt("score", 0);

		if(pb.getProgress() == 100)
		{
			tv.setText("You win!");
			score++;
			sped.putInt("score", score);
			displayScore();
		}
		else if(pb.getProgress() == 0)
		{
			tv.setText("Your pet is Dead. Congrats");
			score--;
			sped.putInt("score", score);
			displayScore();
		}
		
		button.setText("Reset Game?");
		sped.commit();
	}
}
