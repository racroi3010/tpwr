package com.hanaone.tpwr;

import java.util.List;

import com.hanaone.tpwr.adapter.DatabaseAdapter;
import com.hanaone.tpwr.db.LevelDataSet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectionActivity extends Activity {
	private Context mContext;
	private TextView txtName;
	private int levelId;
	private DatabaseAdapter dbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this;
		
		setContentView(R.layout.activity_selection);
		txtName = (TextView) findViewById(R.id.txt_selection_exam_name);
		
		String mode = getIntent().getStringExtra(Constants.SELECTION_MODE);
		if(mode.equals(Constants.SELECTION_MODE_EXAM)){
			String levelName = getIntent().getStringExtra(Constants.LEVEL_NAME);
			levelId = getIntent().getIntExtra(Constants.LEVEL_ID, -1);			
			txtName.setText(levelName);	
			
			findViewById(R.id.layout_exam).setVisibility(LinearLayout.VISIBLE);
			findViewById(R.id.layout_sample_test).setVisibility(LinearLayout.GONE);
		} else if(mode.equals(Constants.SELECTION_MODE_SAMPLE)){
			findViewById(R.id.layout_exam).setVisibility(LinearLayout.GONE);
			findViewById(R.id.layout_sample_test).setVisibility(LinearLayout.VISIBLE);		
			
			txtName.setText(R.string.selection_sample);
		}
		
		
		
		dbAdapter = new DatabaseAdapter(mContext);
		
		
	}
	
	public void onClick(View v){
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_selection_practice:
			intent = new Intent(mContext, QuestionActivity.class);
			intent.putExtra(Constants.QUESTION_MODE, Constants.QUESTION_MODE_PRACTICE);
			intent.putExtra(Constants.LEVEL_ID, levelId);
			startActivity(intent);
			break;
		case R.id.btn_selection_exam:
			intent = new Intent(mContext, QuestionActivity.class);
			intent.putExtra(Constants.QUESTION_MODE, Constants.QUESTION_MODE_EXAM);
			intent.putExtra(Constants.LEVEL_ID, levelId);
			startActivity(intent);
			break;
		case R.id.btn_selection_sample_beginner:
			if(dbAdapter.checkLevel(1)){
				intent = new Intent(mContext, QuestionActivity.class);
				intent.putExtra(Constants.QUESTION_MODE, Constants.QUESTION_MODE_SAMPLE_BEGINNER);
				startActivity(intent);				
			} else {
				String msg = mContext.getResources().getString(R.string.dialog_data_not_exists);
				showDialog(msg);
			}

			break;
		case R.id.btn_selection_sample_intermediate:
			if(dbAdapter.checkLevel(2)){
				intent = new Intent(mContext, QuestionActivity.class);
				intent.putExtra(Constants.QUESTION_MODE, Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE);
				startActivity(intent);					
			} else {
				String msg = mContext.getResources().getString(R.string.dialog_data_not_exists);
				showDialog(msg);				
			}
		
			break;
		case R.id.btn_home:
			finish();
			startActivity(new Intent(mContext, MainActivity.class));
			break;			
		default:
			break;
		}
	}
	
	public void showDialog(String msg){
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_dialog_ok);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.show();
		
		((TextView)dialog.findViewById(R.id.txt_dialog_content)).setText(msg);	
		
		dialog.findViewById(R.id.btn_dialog_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});			
	}
		
}
