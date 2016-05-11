package com.hanaone.tpwr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.QuestionSlideFragment;
import com.hanaone.tpwr.db.LevelDataSet;
import com.hanaone.tpwr.db.SectionDataSet;

public class QuestionSlideAdapter extends FragmentStatePagerAdapter {
	private LevelDataSet mLevel;
	private String mMode;
	public QuestionSlideAdapter(FragmentManager fm, LevelDataSet level
						, String mode) {
		super(fm);
		this.mLevel = level;
		this.mMode = mode;
	}

	@Override
	public Fragment getItem(int arg0) {
		if(Constants.QUESTION_MODE_PRACTICE.equals(this.mMode)){
			List<SectionDataSet> list = new ArrayList<SectionDataSet>();
			list.add(mLevel.getSections().get(arg0));
			return QuestionSlideFragment.create((ArrayList<SectionDataSet>)list);			
		} else {
			return QuestionSlideFragment.create((ArrayList<SectionDataSet>)mLevel.getSections());
		}

	}

	@Override
	public int getCount() {
		if(this.mLevel != null){
			if(Constants.QUESTION_MODE_PRACTICE.equals(this.mMode)){
				return this.mLevel.getSections().size();
			} else {
				return 1;
			}
			
		}
		return 0;
	}

}
