package com.jazzyapps.pomodoropets;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
	
	/* Android docs:
	 * When naming your shared preference files, you should use a name that's
	 * uniquely identifiable to your app, such as "com.example.myapp.PREFERENCE_FILE_KEY"
	 */
	// change to:
	// private static String sharedPrefsKey = "com.jazzyapps.pomodoropets.POMO_PET_PREFS";
	// after updating HomeActivity to use this class
	private static String sharedPrefsKey = "PomoPetsPrefs";
	
	// SharedPreferences keys
	final static String firstTime = "firstTime";	// first time app opened
	final static String petName = "petName";
	
	/* 
	 * @param Context context is generally 'this'
	 */
	public static void setBoolean(Context context, String key, boolean bool) 
	{
		SharedPreferences prefs = context.getSharedPreferences(sharedPrefsKey, 0);
		SharedPreferences.Editor edit = prefs.edit();
	    edit.putBoolean(key, bool);
		
	    edit.commit();
	}
	
	/* 
	 * @param Context context is generally 'this'
	 */
	public static void setInt(Context context, String key, int num) 
	{
		SharedPreferences prefs = context.getSharedPreferences(sharedPrefsKey, 0);
		SharedPreferences.Editor edit = prefs.edit();
	    edit.putInt(key, num);
		
	    edit.commit();
	}
	
	/* 
	 * @param Context context is generally 'this'
	 */
	public static void setString(Context context, String key, String string) 
	{
		SharedPreferences prefs = context.getSharedPreferences(sharedPrefsKey, 0);
		SharedPreferences.Editor edit = prefs.edit();
	    edit.putString(key, string);
		
	    edit.commit();
	}
	
	/* 
	 * @param Context context is generally 'this'
	 * @param String key is key to look for in SharedPreferences
	 * @param boolean bool will be returned if key is not found
	 * @return boolean value stored in SharedPreferences using key
	 * 		or bool if key is not found
	 */
	public static boolean getBoolean(Context context, String key, boolean bool)
	{
		SharedPreferences prefs = context.getSharedPreferences(sharedPrefsKey, 0);
		return prefs.getBoolean(key, bool);
	}
	
	/* 
	 * @param Context context is generally 'this'
	 * @param String key is key to look for in SharedPreferences
	 * @param int num will be returned if key is not found
	 * @return int value stored in SharedPreferences using key
	 * 		or num if key is not found
	 */
	public static int getInt(Context context, String key, int num)
	{
		SharedPreferences prefs = context.getSharedPreferences(sharedPrefsKey, 0);
		return prefs.getInt(key, num);
	}
	
	/* 
	 * @param Context context is generally 'this'
	 * @param String key is key to look for in SharedPreferences
	 * @param String string will be returned if key is not found
	 * @return String value stored in SharedPreferences using key
	 * 		or string if key is not found
	 */
	public static String getString(Context context, String key, String string)
	{
		SharedPreferences prefs = context.getSharedPreferences(sharedPrefsKey, 0);
		return prefs.getString(key, string);
	}
}
