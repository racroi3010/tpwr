package com.hanaone.tpwr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hanaone.tpwr.adapter.DatabaseAdapter;
import com.hanaone.tpwr.adapter.ListAdapterListener;
import com.hanaone.tpwr.adapter.ListSectionAdapter;
import com.hanaone.tpwr.adapter.QuestionSlideAdapter;
import com.hanaone.tpwr.db.LevelDataSet;
import com.hanaone.tpwr.util.Config;
import com.hanaone.tpwr.util.PreferenceHandler;
import com.hanaone.tpwr.view.DigitalClockView;

public class QuestionActivity extends FragmentActivity{
	private Context mContext;
//	private Timer timer;
	// view pager
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private LevelDataSet level;
	private String mMode;
	private int currentItem;
	private DatabaseAdapter dbAdapter;
	//private int sectionIndex;
	private int questionNumber;
	
	// list
	private ListView mList;
	private ListSectionAdapter mListAdapter;
	
	
	// list result
	//private ArrayList<ResultDataSet> listResult;
	
//	private Timer timer;
	private DigitalClockView clock;
	
	private boolean activityPaused;
	
	private ListAdapterListener mListener = new ListAdapterListener() {
		@Override
		public void onSelect(int questionNumber, int sectionNumber) {
			
		}


	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_question_practice);
		
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		mContext = this;
		dbAdapter = new DatabaseAdapter(mContext);
		
		// init data		
		onInit();
					
		
		onInitLayout();
		
		
	}
	
	private void onInit(){
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mMode = getIntent().getStringExtra(Constants.QUESTION_MODE);
		PreferenceHandler.setQuestionModePreference(mContext, mMode);		
		
		if(Constants.QUESTION_MODE_EXAM.equals(mMode) || Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
			int levelId = getIntent().getIntExtra(Constants.LEVEL_ID, -1);
			level = dbAdapter.getLevel(levelId);	

		} else if(Constants.QUESTION_MODE_SAMPLE_BEGINNER.equals(mMode)){
			level = dbAdapter.generateSampleTest(1);
			currentItem = 0;
			
		} else if(Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE.equals(mMode)){
			level = dbAdapter.generateSampleTest(2);
			currentItem = 0;
		} else if(Constants.QUESTION_MODE_REVIEW.equals(mMode)){
			
			level = getIntent().getParcelableExtra(Constants.LEVEL);
			// keep reference object			
			questionNumber = getIntent().getIntExtra(Constants.QUESTION_NUMBER, 0);
			currentItem = getIntent().getIntExtra(Constants.SECTION_NUMBER, 0);
			
		}
		
	}
	private void onInitLayout(){
		// pager
		mPager = (ViewPager) findViewById(R.id.viewpager_question_vp);
		mList = (ListView) findViewById(R.id.list_sections);
		// ads
		
		TextView title = (TextView) findViewById(R.id.txt_question_practice_title);
		if(Config.adsSupport){	
			AdView mAdView = (AdView) findViewById(R.id.adView);
		    AdRequest adRequest = new AdRequest.Builder().build();
		    mAdView.loadAd(adRequest);	
		    
		    findViewById(R.id.layout_adView).setVisibility(LinearLayout.VISIBLE);
		    title.setVisibility(TextView.GONE);
		} else {
			findViewById(R.id.layout_adView).setVisibility(LinearLayout.GONE);
			title.setVisibility(TextView.VISIBLE);
		}
	
	    
	    
		if(Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
			findViewById(R.id.layout_previous).setVisibility(LinearLayout.VISIBLE);
			findViewById(R.id.layout_next).setVisibility(LinearLayout.VISIBLE);
			findViewById(R.id.layout_home_setting).setVisibility(RelativeLayout.VISIBLE);
			findViewById(R.id.btn_submit).setVisibility(Button.GONE);	
			findViewById(R.id.layout_list_sections).setVisibility(RelativeLayout.GONE);	
			findViewById(R.id.layout_adView).setVisibility(LinearLayout.GONE);
			title.setVisibility(TextView.GONE);
			mPager.setVisibility(ViewPager.VISIBLE);
			//mList.setVisibility(ListView.GONE);
			
			mPagerAdapter = new QuestionSlideAdapter(getSupportFragmentManager(), level, mMode);
			mPager.setAdapter(mPagerAdapter);	
			mPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int position) {
					currentItem = position;
					if(currentItem > level.getSections().size() - 1) {
						Intent intent = new Intent(mContext, ResultActivity.class);
						intent.putExtra(Constants.LEVEL, level);
						startActivity(intent);
					} 

					
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});

		} else if(Constants.QUESTION_MODE_REVIEW.equals(mMode)){
			findViewById(R.id.layout_previous).setVisibility(LinearLayout.GONE);
			findViewById(R.id.layout_next).setVisibility(LinearLayout.GONE);
			findViewById(R.id.btn_submit).setVisibility(Button.GONE);	
			findViewById(R.id.layout_home_setting).setVisibility(RelativeLayout.GONE);
			findViewById(R.id.btn_result).setVisibility(Button.VISIBLE);	
			findViewById(R.id.layout_list_sections).setVisibility(RelativeLayout.VISIBLE);
			

			title.setText(getResources().getString(R.string.question_review));
			mPager.setVisibility(ViewPager.GONE);
			//mList.setVisibility(ListView.VISIBLE);

			mListAdapter = new ListSectionAdapter(mContext, mListener);
			mList.setAdapter(mListAdapter);
			mListAdapter.setmDataSet(level.getSections());
			//mListAdapter.setResults(listResult);
			
			int index = 0;			
			for(int i = 0; i < currentItem; i ++){
				index += level.getSections().get(i).getQuestions().size() + 1;				
			}		
			index += questionNumber + 1;
				
			mList.setSelection(index);
			mList.requestFocus();

		}else {
			findViewById(R.id.layout_previous).setVisibility(LinearLayout.GONE);
			findViewById(R.id.layout_next).setVisibility(LinearLayout.GONE);
			findViewById(R.id.layout_home_setting).setVisibility(RelativeLayout.GONE);
			findViewById(R.id.btn_submit).setVisibility(Button.VISIBLE);	
			findViewById(R.id.layout_list_sections).setVisibility(RelativeLayout.VISIBLE);	

			title.setText(getResources().getString(R.string.question_test));						
			mPager.setVisibility(ViewPager.GONE);
			//mList.setVisibility(ListView.VISIBLE);
			
			mListAdapter = new ListSectionAdapter(mContext, mListener);
			mList.setAdapter(mListAdapter);
			mListAdapter.setmDataSet(level.getSections());
			
			clock = (DigitalClockView) findViewById(R.id.clock);
			Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital-7.ttf");
			clock.setTypeface(tf);
			clock.start(level.getTime() * 60 * 1000, this);
			//RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			//clock.setLayoutParams(layoutParams);
			
		}
	}
	public void onClick(View v){
		int sectionSize = level.getSections().size();	
		switch (v.getId()) {
		case R.id.btn_previous:	
			currentItem = mPager.getCurrentItem();
					
			if(currentItem > 0){
				currentItem --;
				mPager.setCurrentItem(currentItem);				
			}

			break;
		case R.id.btn_next:
			currentItem = mPager.getCurrentItem();
		
			if(currentItem < sectionSize - 1){
				currentItem ++;
				mPager.setCurrentItem(currentItem);
				
			} else {
				Intent intent = new Intent(mContext, ResultActivity.class);
				intent.putExtra(Constants.LEVEL, level);
				startActivity(intent);
			}
			break;
		case R.id.btn_result:
		case R.id.btn_submit:
			submit();
			break;
		case R.id.btn_home:		
			finish();
			startActivity(new Intent(mContext, MainActivity.class));
			break;
		case R.id.btn_setting:
			Intent intent = new Intent(mContext, HelpActivity.class);
			startActivityForResult(intent, Constants.REQ_UPDATE_LOCALE);			
			break;
		default:
			break;
		}
	}
	public void submit(){
		if(clock != null){
			clock.cancel();
		}
		Intent intent = new Intent(mContext, ResultActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Constants.LEVEL, level);
		startActivity(intent);			
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return false;
	}


	@Override
	protected void onPause() {
		activityPaused = true;	
		if(clock != null){
			clock.pause();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		activityPaused = false;
		
		if(clock != null){
			clock.resume();
		}	
		
		super.onResume();
	}


	

	@Override
	protected void onDestroy() {
		if(clock != null){
			clock.cancel();
		}	
		super.onDestroy();
	}



	
	@Override
	public void onBackPressed() {
		if(clock != null){
			clock.cancel();
		}		
		if(Constants.QUESTION_MODE_REVIEW.equals(mMode)){
			Intent intent = new Intent(mContext, ResultActivity.class);
			intent.putExtra(Constants.LEVEL, level);
			finish();
			startActivity(intent);	
			
		} else {
			super.onBackPressed();
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(RESULT_OK == resultCode && Constants.REQ_UPDATE_LOCALE == requestCode){
			// check update locale
			boolean updateLocale = data.getBooleanExtra(Constants.UPDATE_LOCALE, false);
			if(updateLocale){
				setContentView(R.layout.activity_question_practice);
				onInit();
				onInitLayout();
				mPager.setCurrentItem(currentItem);
			}			
		}
	}	
	
}
