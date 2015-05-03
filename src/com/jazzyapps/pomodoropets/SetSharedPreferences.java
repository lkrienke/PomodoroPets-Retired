package com.jazzyapps.pomodoropets;

import android.app.Activity;
import android.content.SharedPreferences;

public class SetSharedPreferences {
	
	public static void setBoolean(Activity activity, String key, boolean bool) 
	{
		SharedPreferences prefs = activity.getSharedPreferences("PomoPetsPrefs", 0);
		SharedPreferences.Editor edit = prefs.edit();
	    if (bool)
	        edit.putBoolean(key, Boolean.TRUE);
		else
	        edit.putBoolean(key, Boolean.FALSE);
		
	    edit.commit();
	}
	
	public static void setInt(Activity activity, String key, int num) 
	{
		SharedPreferences prefs = activity.getSharedPreferences("PomoPetsPrefs", 0);
		SharedPreferences.Editor edit = prefs.edit();
	    edit.putInt(key, num);
		
	    edit.commit();
	}
	
	public static void setString(Activity activity, String key, String string) 
	{
		SharedPreferences prefs = activity.getSharedPreferences("PomoPetsPrefs", 0);
		SharedPreferences.Editor edit = prefs.edit();
	    edit.putString(key, string);
		
	    edit.commit();
	}
}
