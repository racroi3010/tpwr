package com.hanaone.tpwr.util;

import com.hanaone.tpwr.Constants;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHandler extends Constants {
	private static final String PREFERENCE_NAME = "TPRD";
	
//	private static final String AUDIO_AUTO_PLAY = "AUDIO_AUTO_PLAY";
	private static final String HINT_DISPLAY = "HINT_DISPLAY";
	
	private static final String LANGUAGE_POSITION = "LANGUAGE_POSITION";
	
	
	
	private static void removeAllPreference(Context context){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();		
//		editor.remove(AUDIO_AUTO_PLAY);
		
		editor.apply();
	}	
	
	private static void setBooleanPreference(Context context, String key, boolean value){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(key, value);
		
		editor.apply();
	}
	private static boolean getBooleanPreference(Context context, String key){
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getBoolean(key, false);
	}
	private static void setStringPreference(Context context, String key, String value){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(key, value);
		
		editor.apply();
	}
	private static String getStringPreference(Context context, String key){
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(key, null);
	}	
	private static void setIntPreference(Context context, String key, int value){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putInt(key, value);
		
		editor.apply();
	}
	private static int getIntPreference(Context context, String key){
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(key, 0);
	}		
//	public static void setAudioPlayPreference(Context context, boolean value){
//		setBooleanPreference(context, AUDIO_AUTO_PLAY, value);
//	}
//	public static boolean getAudioPlayPreference(Context context){
//		return getBooleanPreference(context, AUDIO_AUTO_PLAY);
//	}
//
	public static void setHintDisplayPreference(Context context, boolean value){
		setBooleanPreference(context, HINT_DISPLAY, value);
	}
	public static boolean getHintDisplayPreference(Context context){
		return getBooleanPreference(context, HINT_DISPLAY);
	}
	
	public static void setQuestionModePreference(Context context, String value){
		setStringPreference(context, QUESTION_MODE, value);
	}
	public static String getQuestionModePreference(Context context){
		return getStringPreference(context, QUESTION_MODE);
	}
	
	public static void setLanguagePositionPreference(Context context, int value){
		setIntPreference(context, LANGUAGE_POSITION, value);
	}
	public static int getLanguagePositionPreference(Context context){
		return getIntPreference(context, LANGUAGE_POSITION);
	}
	
	public static void setUpdateLocalePreference(Context context, boolean value){
		setBooleanPreference(context, UPDATE_LOCALE, value);
	}
	public static boolean getUpdateLocalePreference(Context context){
		return getBooleanPreference(context, UPDATE_LOCALE);
	}
}
