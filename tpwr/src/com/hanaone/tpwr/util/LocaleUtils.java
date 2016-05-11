package com.hanaone.tpwr.util;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class LocaleUtils {
	public static void setLocale(Context context, int position){
		String language = "en";
		switch (position) {
		case 0:
			language = "en";
			break;
		case 1:
			language = "vi";
			break;
		case 2:
			language = "ko";
			break;
		default:
			break;
		}
		Locale myLocale = new Locale(language);
		Resources res = context.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
		
		PreferenceHandler.setLanguagePositionPreference(context, position);		
	}
}
