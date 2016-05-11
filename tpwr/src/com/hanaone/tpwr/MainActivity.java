package com.hanaone.tpwr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanaone.http.DownloadHelper;
import com.hanaone.http.JsonReaderHelper;
import com.hanaone.jni.JNIHanaone;
import com.hanaone.tpwr.adapter.DatabaseAdapter;
import com.hanaone.tpwr.adapter.DownloadInfo;
import com.hanaone.tpwr.adapter.DownloadLevelAdapter;
import com.hanaone.tpwr.adapter.ListExamHeaderAdapter;
import com.hanaone.tpwr.adapter.ListLevelListener;
import com.hanaone.tpwr.adapter.ListExamHeaderAdapter.ExamHeader;
import com.hanaone.tpwr.db.ExamDataSet;
import com.hanaone.tpwr.db.LevelDataSet;
import com.hanaone.tpwr.util.ColorUtils;
import com.hanaone.tpwr.util.LocaleUtils;
import com.hanaone.tpwr.util.PreferenceHandler;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private Context mContext;
	private DatabaseAdapter dbAdapter;
	private ListView listExam;
	private List<ListExamHeaderAdapter.ExamItem> listItem;
	private List<ExamDataSet> list;
//	private List<DownloadInfo> infos;
//	private ListExamAdapter adapter;
	private ListExamHeaderAdapter adapter;
	private ImageView imgSync;
	private LinearLayout layoutSync;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		dbAdapter = new DatabaseAdapter(mContext);
		
		initLayout();
		
		initData();
	}
    @Override
	protected void onResume() {
		super.onResume();


		new LoadingNewData().execute();
	}
 
    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(intent != null){
			int levelId = intent.getIntExtra(Constants.UPDATE_SCORE_LEVEL_ID, -1);
			if(levelId > -1){
				int score = intent.getIntExtra(Constants.UPDATE_SCORE, -1);
				if(score > -1){
					for(ExamDataSet exam: list)
						for(LevelDataSet level: exam.getLevels())
							if(level.getId() == levelId){
								level.setScore(score);
								
								break;
							}
					adapter.notifyDataSetChanged();			
				}

			}	
			
			// check update locale
			boolean updateLocale = intent.getBooleanExtra(Constants.UPDATE_LOCALE, false);
			if(updateLocale){
				initLayout();
				updateData();
			}
		}
	}
	public void onClick(View v){
    	switch (v.getId()) {
		case R.id.btn_setting:
			Intent intent = new Intent(mContext, HelpActivity.class);
			startActivityForResult(intent, Constants.REQ_UPDATE_LOCALE);
			break;
		case R.id.btn_sample_test:
			intent = new Intent(mContext, SelectionActivity.class);
			intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODE_SAMPLE);

			startActivity(intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			break;
		default:
			break;
		}
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(RESULT_OK == resultCode && Constants.REQ_UPDATE_LOCALE == requestCode){
			// check update locale
			boolean updateLocale = data.getBooleanExtra(Constants.UPDATE_LOCALE, false);
			if(updateLocale){
				initLayout();
				updateData();
			}			
		}
	}
	private void initData(){
//		adapter = new ListExamAdapter(mContext, mListener);	
//		listExam.setAdapter(adapter);		
//		list = dbAdapter.getAllExam();		
//		infos = new ArrayList<DownloadInfo>();
//		for(int i = 0; i < list.size(); i ++){
//			infos.add(new DownloadInfo());
//		}
//		adapter.setDownloadInfos(infos);
//		adapter.setExams(list);		
//		new LoadingNewData().execute();
		
		// test
		listItem = new ArrayList<ListExamHeaderAdapter.ExamItem>();
		list = dbAdapter.getAllExam();
		for(ExamDataSet exam: list){
			listItem.add(new ListExamHeaderAdapter.ExamHeader(exam));
			for(LevelDataSet level: exam.getLevels()){
				listItem.add(new ListExamHeaderAdapter.ExamLevelItem(level, new DownloadInfo(), mListener));
			}
		}		
			
		adapter = new ListExamHeaderAdapter(mContext, null);	
		adapter.setItems(listItem);
		listExam.setAdapter(adapter);	
		new LoadingNewData().execute();
	}
	private void updateData(){
		listExam.setAdapter(adapter);	
		adapter.notifyDataSetChanged();
		new LoadingNewData().execute();
	}
	private void initLayout(){
		int position = PreferenceHandler.getLanguagePositionPreference(mContext);
		LocaleUtils.setLocale(mContext, position);		
		setContentView(R.layout.activity_main);				
		
		
		listExam = (ListView) findViewById(R.id.list_exam);
		
		imgSync = (ImageView) findViewById(R.id.img_sync);
		layoutSync = (LinearLayout) findViewById(R.id.layout_sync);	
		
		
	}

	private class LoadingNewData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			DownloadHelper dHelper = new DownloadHelper(mContext);
			String confPath = Constants.getInternalRootPath(mContext) + "/config.txt";
			//confPath = Environment.getExternalStorageDirectory().getPath() + "/config.txt";
			
			String url = Constants.REMOTE_CONFIG_FILE_JSON;
			
			// test
			url = new JNIHanaone().stringFromJNI();
			boolean loaded = false;
			try {						
				loaded = dHelper.downloadFile(url, confPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(loaded){
				File file = new File(confPath);
				if(file.exists()){
					try {
						List<ExamDataSet> exams = JsonReaderHelper.readExams(file);
						int colorOld = 0;
						int colorNew = 0;
						for(ExamDataSet exam: exams){
							if(!dbAdapter.checkExam(exam.getNumber())){
								
								do{
									colorOld = colorNew;
									colorNew = ColorUtils.randomColor(50, 250, 20, 0.9f, 0.5f);
								} while(colorNew == colorOld);
								//exam.setColor(colorNew);
								for(LevelDataSet level: exam.getLevels()){
									level.setColor(colorNew);
									level.setExam_id(exam.getNumber());
								}
								dbAdapter.addExam(exam);
								
								list.add(exam);	
								//infos.add(new DownloadInfo());
								int index = 0;
								for(int i = 0; i < listItem.size(); i ++){
									if(listItem.get(i) instanceof ListExamHeaderAdapter.ExamHeader){
										ListExamHeaderAdapter.ExamHeader headerItem = (ExamHeader) listItem.get(i);
										if(exam.getNumber() > headerItem.getExam().getNumber()){
											index = i;
											break;
										}
									}
									index ++;
								}
								listItem.add(index ++, new ListExamHeaderAdapter.ExamHeader(exam));
								for(LevelDataSet level: exam.getLevels()){
									listItem.add(index,new ListExamHeaderAdapter.ExamLevelItem(level, new DownloadInfo(), mListener));
								}
								publishProgress();
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			adapter.notifyDataSetChanged();
			if(imgSync.getAnimation() != null) imgSync.getAnimation().cancel();
			layoutSync.setVisibility(LinearLayout.GONE);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			layoutSync.setVisibility(LinearLayout.VISIBLE);
			Animation rotation = AnimationUtils.loadAnimation(mContext, R.anim.clock_wise);
			imgSync.startAnimation(rotation);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			adapter.notifyDataSetChanged();
			super.onProgressUpdate(values);
		}
		
	}
	
	private ListLevelListener mListener = new ListLevelListener() {
		
		@Override
		public void onSelect(int examLevelId, String examLevelName) {
			//Toast.makeText(mContext, "" + examLevelId, Toast.LENGTH_SHORT).show();
			
			Intent intent = new Intent(mContext, SelectionActivity.class);	

			intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODE_EXAM);
			intent.putExtra(Constants.LEVEL_ID, examLevelId);
			intent.putExtra(Constants.LEVEL_NAME, examLevelName);
			startActivity(intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		}

		@Override
		public void onSelect(LevelDataSet level, DownloadInfo info) {
			if(level.getActive() == Constants.STATUS_ACTIVE){
				String selectName = mContext.getResources().getString(R.string.selection_title);
				String examLevelName = String.format(selectName, level.getExam_id(),level.getLabel());
				mListener.onSelect(level.getId(), examLevelName);
			} else {				
				confirmDownload(level, info);
			}			
		}
		
	};
	public void confirmDownload(final LevelDataSet level, final DownloadInfo info){
		Resources resouces = mContext.getResources();
		
		boolean download = false;
		String msg = null;
		
		if(info.getStatus() == DownloadInfo.NOT_START){
			msg = resouces.getString(R.string.dialog_ask_download_data);
			download = true;
		} else if(info.getStatus() == DownloadInfo.DOWNLOADING) {
			msg = resouces.getString(R.string.dialog_ask_downloading);
		} else if(info.getStatus() == DownloadInfo.QUEUED){
			msg = resouces.getString(R.string.dialog_ask_queued);
		}

		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(download){
			dialog.setContentView(R.layout.layout_dialog_yes_no);
		} else {
			dialog.setContentView(R.layout.layout_dialog_ok);
		}
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.show();
		
		((TextView)dialog.findViewById(R.id.txt_dialog_content)).setText(msg);
		
		if(download){
			final DownloadLevelAdapter dlAdapter = new DownloadLevelAdapter(mContext, level, info, dbAdapter);
			
			dialog.findViewById(R.id.btn_dialog_ok).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// set queue
					info.setStatus(DownloadInfo.QUEUED);
					dlAdapter.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					dialog.dismiss();
				}
			});	
			dialog.findViewById(R.id.btn_dialog_no).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});			
		} else {
			dialog.findViewById(R.id.btn_dialog_ok).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});				
		}

		
		
	}	
	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	
}
