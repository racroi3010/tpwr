package com.hanaone.tpwr;

import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

import com.hanaone.tpwr.util.LocaleUtils;
import com.hanaone.tpwr.util.PreferenceHandler;
import com.kyleduo.switchbutton.SwitchButton;

public class HelpActivity extends Activity {
	private Context mContext;
	private Spinner spLanguage;
	private boolean userIsInteracting;
	private boolean updateLocale;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		onInit();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		userIsInteracting = true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			boolean updateLocale = intent.getBooleanExtra(
					Constants.UPDATE_LOCALE, false);
			if (updateLocale) {

				onInit();
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_home:
			// finish();
			Intent intent = new Intent(mContext, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(Constants.UPDATE_LOCALE, updateLocale);
			startActivity(intent);
			break;
		case R.id.layout_rate:
		case R.id.btn_rate:
			rateApp();
		case R.id.layout_share:
		case R.id.btn_share:
			shareApp();
			break;
		default:
			break;
		}
	}


	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra(Constants.UPDATE_LOCALE, updateLocale);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

	private void onInit() {
		int position = PreferenceHandler.getLanguagePositionPreference(mContext);
		LocaleUtils.setLocale(mContext, position);	
		setContentView(R.layout.activity_help);

		SwitchButton swHint = (SwitchButton) findViewById(R.id.sw_help_hint);
		swHint.setChecked(PreferenceHandler.getHintDisplayPreference(mContext));
		swHint.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				PreferenceHandler.setHintDisplayPreference(mContext, isChecked);
			}
		});

		spLanguage = (Spinner) findViewById(R.id.sp_language);

		spLanguage.setSelection(position);

		spLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (userIsInteracting) {
					LocaleUtils.setLocale(mContext, arg2);	
					userIsInteracting = false;
					updateLocale = true;

					Intent refresh = new Intent(mContext, HelpActivity.class);
					refresh.putExtra(Constants.UPDATE_LOCALE, true);
					startActivity(refresh);
				}
				// PreferenceHandler.setLanguagePosition(mContext, arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void shareApp() {
		String urlToShare = "http://play.google.com/store/apps/details?id=" + mContext.getPackageName();
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
		intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

		// See if official Facebook app is found
		boolean facebookAppFound = false;
		List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
		for (ResolveInfo info : matches) {
		    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
		        intent.setPackage(info.activityInfo.packageName);
		        facebookAppFound = true;
		        break;
		    }
		}

		// As fallback, launch sharer.php in a browser
		if (!facebookAppFound) {
		    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
		    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		}

		startActivity(intent);
	}

	private Intent getShareIntent(String type, String subject, String text) {
		boolean found = false;
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");

		List<ResolveInfo> resInfo = mContext.getPackageManager()
				.queryIntentActivities(share, 0);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo info : resInfo) {
				if (info.activityInfo.packageName.toLowerCase().contains(type)
						|| info.activityInfo.name.toLowerCase().contains(type)) {
					share.putExtra(Intent.EXTRA_SUBJECT, subject);
					share.putExtra(Intent.EXTRA_TEXT, text);
					share.setPackage(info.activityInfo.packageName);
					found = true;
					break;
				}
			}
			if (!found) {
				return null;
			}
			return share;
		}
		return null;
	}
	private void rateApp() {
		Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		// To count with Play market backstack, After pressing back button,
		// to taken back to our application, we need to add following flags to
		// intent.
		goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
				| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
				| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ mContext.getPackageName())));
		}
	}
}
