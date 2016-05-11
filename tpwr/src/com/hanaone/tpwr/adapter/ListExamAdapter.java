package com.hanaone.tpwr.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanaone.http.DownloadHelper;
import com.hanaone.http.JsonReaderHelper;
import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.R;
import com.hanaone.tpwr.db.ChoiceDataSet;
import com.hanaone.tpwr.db.ExamDataSet;
import com.hanaone.tpwr.db.LevelDataSet;
import com.hanaone.tpwr.db.QuestionDataSet;
import com.hanaone.tpwr.db.SectionDataSet;
import com.hanaone.tpwr.util.ColorUtils;
import com.hanaone.tpwr.util.Config;

public class ListExamAdapter extends BaseAdapter {
	private Context mContext;
	private ListLevelListener mListener;
	private List<ExamDataSet> exams;
	private LayoutInflater mInflater;
	private DatabaseAdapter dbAdapter;
	private List<DownloadInfo> infos;
	public ListExamAdapter(Context mContext, ListLevelListener mListener) {
		super();
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = LayoutInflater.from(mContext);
		this.dbAdapter  = new DatabaseAdapter(mContext);
	}

	public void setExams(List<ExamDataSet> exams) {
		this.exams = exams;
		Collections.sort(exams, new Comparator<ExamDataSet>() {

			@Override
			public int compare(ExamDataSet arg0, ExamDataSet arg1) {
				if(arg0.getNumber() > arg1.getNumber()){
					return -1;
				} else if(arg0.getNumber() < arg1.getNumber()){
					return 1;
				}
				return 0;
			}
			
		});		
		this.notifyDataSetChanged();
	}
	

	public void setDownloadInfos(List<DownloadInfo> infos) {
		this.infos = infos;
	}

	@Override
	public int getCount() {
		if(exams != null) return exams.size(); 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(exams != null && exams.size() > position){
			return exams.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return exams.get(position).getNumber();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final ExamDataSet data = exams.get(position);
		final DownloadInfo info = infos.get(position);
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.layout_exam, parent, false);
			
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
			holder.btnView = (Button) convertView.findViewById(R.id.btn_down);

			holder.layoutLevel1 = (LinearLayout) convertView.findViewById(R.id.layout_level1);
			holder.layoutLevel2 = (LinearLayout) convertView.findViewById(R.id.layout_level2);
			holder.layoutLevel3 = (LinearLayout) convertView.findViewById(R.id.layout_level3);
			
			holder.prgBar1 = (ProgressBar) convertView.findViewById(R.id.prg_level_1);
			holder.prgBar2 = (ProgressBar) convertView.findViewById(R.id.prg_level_2);
			holder.prgBar3 = (ProgressBar) convertView.findViewById(R.id.prg_level_3);
			holder.txtScore1 = (TextView) convertView.findViewById(R.id.txt_score_1);
			holder.txtScore2 = (TextView) convertView.findViewById(R.id.txt_score_2);
			holder.txtScore3 = (TextView) convertView.findViewById(R.id.txt_score_3);
			

			
			holder.info = info;
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();	
			
			holder.info.setLayoutLevel1(null);
			holder.info.setLayoutLevel2(null);
			holder.info.setLayoutLevel3(null);
			
			holder.info.setPrgBar1(null);
			holder.info.setPrgBar2(null);
			holder.info.setPrgBar3(null);
			holder.info.setTxtScore1(null);
			holder.info.setTxtScore2(null);
			holder.info.setTxtScore3(null);	
			
			holder.info = info;
			
			info.setPrgBar1(holder.prgBar1);
			info.setPrgBar2(holder.prgBar2);
			info.setPrgBar3(holder.prgBar3);
			info.setTxtScore1(holder.txtScore1);
			info.setTxtScore2(holder.txtScore2);
			info.setTxtScore3(holder.txtScore3);
			info.setLayoutLevel1(holder.layoutLevel1);
			info.setLayoutLevel2(holder.layoutLevel2);
			info.setLayoutLevel3(holder.layoutLevel3);
			
			
		} 		

		info.setPrgBar1(holder.prgBar1);
		info.setPrgBar2(holder.prgBar2);
		info.setPrgBar3(holder.prgBar3);
		info.setTxtScore1(holder.txtScore1);
		info.setTxtScore2(holder.txtScore2);
		info.setTxtScore3(holder.txtScore3);
		info.setLayoutLevel1(holder.layoutLevel1);
		info.setLayoutLevel2(holder.layoutLevel2);
		info.setLayoutLevel3(holder.layoutLevel3);
		
		holder.btnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(holder.layoutLevel3.getVisibility() != LinearLayout.GONE){
					
					holder.btnView.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
					holder.layoutLevel3.setVisibility(LinearLayout.GONE);
					holder.layoutLevel2.setVisibility(LinearLayout.GONE);
					holder.layoutLevel1.setVisibility(LinearLayout.GONE);					
				} else {
					holder.btnView.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
					holder.layoutLevel3.setVisibility(LinearLayout.VISIBLE);
					holder.layoutLevel2.setVisibility(LinearLayout.VISIBLE);
					holder.layoutLevel1.setVisibility(LinearLayout.VISIBLE);					
				}
				

			}
		});	
		
		//int color = data.getColor();
		
		((ImageView)holder.layoutLevel3.findViewById(R.id.img_new_lesson_3)).setBackgroundColor(data.getLevels().get(0).getColor());
		((ImageView)holder.layoutLevel2.findViewById(R.id.img_new_lesson_2)).setBackgroundColor(data.getLevels().get(0).getColor());
		((ImageView)holder.layoutLevel1.findViewById(R.id.img_new_lesson_1)).setBackgroundColor(data.getLevels().get(0).getColor());
		
		holder.layoutLevel3.setVisibility(RelativeLayout.GONE);
		holder.layoutLevel2.setVisibility(RelativeLayout.GONE);
		holder.layoutLevel1.setVisibility(RelativeLayout.GONE);
		
		if(data != null){
			final String examName = mContext.getResources().getString(R.string.exam_title);
			final String selectName = mContext.getResources().getString(R.string.selection_title);
			holder.txtTitle.setText(String.format(examName, data.getNumber()));
			
			final List<LevelDataSet> levels = data.getLevels();
			if(levels != null){
				for(final LevelDataSet level: levels){
					int number = level.getNumber();
					int maxScore = level.getMaxScore();
					int score = level.getScore();
					int progress = maxScore == 0 ? 0 : (score * 100)/maxScore;
					String scoreLabel = score + "/"+ maxScore;
					if(number == 1){

						holder.layoutLevel1.setVisibility(RelativeLayout.VISIBLE);
						((TextView)holder.layoutLevel1.findViewById(R.id.txt_label_1)).setText(level.getLabel() + "");
						holder.layoutLevel1.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(level.getActive() == Constants.STATUS_ACTIVE){
									String examLevelName = String.format(selectName, data.getNumber(),level.getLabel());
									mListener.onSelect(level.getId(), examLevelName);
								} else {
									
									onclick(level, info);
								}
								
								
							}
						});	
						if(level.getActive() != Constants.STATUS_ACTIVE){
							holder.layoutLevel1.setAlpha(0.5f);
						} else {
							holder.layoutLevel1.setAlpha(1f);
						}	
						if(info.getStatus1() != DownloadInfo.DOWNLOADING && info.getStatus1() != DownloadInfo.QUEUED){
							info.getPrgBar1().setProgress(progress);
							info.getTxtScore1().setText(scoreLabel);									
						} else{
							info.getPrgBar1().setProgress(info.getProgress2());
							info.getTxtScore1().setText(info.getProgress2() + "%");
							info.getLayoutLevel1().setAlpha(1f);
						}
										
					} else if(number == 2){
						holder.layoutLevel2.setVisibility(RelativeLayout.VISIBLE);
						((TextView)holder.layoutLevel2.findViewById(R.id.txt_label_2)).setText(level.getLabel() + "");
						holder.layoutLevel2.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(level.getActive() == Constants.STATUS_ACTIVE){
									String examLevelName = String.format(selectName, data.getNumber(),level.getLabel());
									mListener.onSelect(level.getId(), examLevelName);
								} else {
									onclick(level, info);
								}
								
								
							}
						});	
						if(level.getActive() != Constants.STATUS_ACTIVE){
							holder.layoutLevel2.setAlpha(0.5f);
						} else {
							holder.layoutLevel2.setAlpha(1f);
						}	
						if(info.getStatus2() != DownloadInfo.DOWNLOADING && info.getStatus2() != DownloadInfo.QUEUED){
							info.getPrgBar2().setProgress(progress);
							info.getTxtScore2().setText(scoreLabel);							
						} else{
							info.getPrgBar2().setProgress(info.getProgress2());
							info.getTxtScore2().setText(info.getProgress2() + "%");
							info.getLayoutLevel2().setAlpha(1f);
						}						
					} else if(number == 3){

						holder.layoutLevel3.setVisibility(RelativeLayout.VISIBLE);
						((TextView)holder.layoutLevel3.findViewById(R.id.txt_label_3)).setText(level.getLabel() + "");
						holder.layoutLevel3.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(level.getActive() == Constants.STATUS_ACTIVE){
									String examLevelName = String.format(selectName, data.getNumber(),level.getLabel());
									mListener.onSelect(level.getId(), examLevelName);
								} else {
									onclick(level, info);
								}
								
								
							}
						});	
						if(level.getActive() != Constants.STATUS_ACTIVE){
							holder.layoutLevel3.setAlpha(0.5f);
						} else {
							holder.layoutLevel3.setAlpha(1f);
						}	
						if(info.getStatus3() != DownloadInfo.DOWNLOADING && info.getStatus3() != DownloadInfo.QUEUED){
							info.getPrgBar3().setProgress(progress);
							info.getTxtScore3().setText(scoreLabel);							
						} else{
							info.getPrgBar3().setProgress(info.getProgress3());
							info.getTxtScore3().setText(info.getProgress3() + "%");							
							info.getLayoutLevel3().setAlpha(1f);
						}						
					}
				}
			
			}
		}

		
		
		return convertView;
	}
	
	public void onclick(final LevelDataSet level, final DownloadInfo info){
		Resources resouces = mContext.getResources();
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)	
										.setTitle(resouces.getString(R.string.dialog_caution));
		boolean download = false;
		String msg = null;
		switch (level.getNumber()) {
		case 1:
			if(info.getStatus1() == DownloadInfo.NOT_START){
				msg = resouces.getString(R.string.dialog_ask_download_data);
				download = true;
			} else if(info.getStatus1() == DownloadInfo.DOWNLOADING) {
				msg = resouces.getString(R.string.dialog_ask_downloading);
			} else if(info.getStatus1() == DownloadInfo.QUEUED){
				msg = resouces.getString(R.string.dialog_ask_queued);
			}
			break;
		case 2:
			if(info.getStatus2() == DownloadInfo.NOT_START){
				msg = resouces.getString(R.string.dialog_ask_download_data);
				download = true;
			} else if(info.getStatus2() == DownloadInfo.DOWNLOADING) {
				msg = resouces.getString(R.string.dialog_ask_downloading);
			} else if(info.getStatus2() == DownloadInfo.QUEUED){
				msg = resouces.getString(R.string.dialog_ask_queued);
			}		
			break;
		case 3:
			if(info.getStatus3() == DownloadInfo.NOT_START){
				msg = resouces.getString(R.string.dialog_ask_download_data);
				download = true;
			} else if(info.getStatus3() == DownloadInfo.DOWNLOADING) {
				msg = resouces.getString(R.string.dialog_ask_downloading);
			} else if(info.getStatus3() == DownloadInfo.QUEUED){
				msg = resouces.getString(R.string.dialog_ask_queued);
			}
			break;					
		default:
			break;
		}

		dialog.setMessage(msg);
		
		if(download){			
			dialog.setPositiveButton(resouces.getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {	
						switch (level.getNumber()) {
						case 1:
							info.setStatus1(DownloadInfo.QUEUED);
							break;
						case 2:
							info.setStatus2(DownloadInfo.QUEUED);
							break;
						case 3:
							info.setStatus3(DownloadInfo.QUEUED);
							break;					
						default:
							break;
						}						
						new Downloading(level, info).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						
					}
			})
			.setNegativeButton(resouces.getString(R.string.dialog_no), null)
			.show();			
		} else {
			dialog.setPositiveButton(resouces.getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {				
					dialog.dismiss();
					
				}
			})
			.show();			
		}

		

	}
	private class Downloading extends AsyncTask<Void, Integer, Boolean>{
		private LevelDataSet level;
//		private DownloadInfo info;
//		private Dialog mDialog;
//		private ProgressBar prgBar;
//		private TextView txtPer;
		private DownloadInfo info;
		public Downloading(LevelDataSet level, DownloadInfo info){
			
			this.level = level;
//			this.info = info;
//			this.mDialog = mDialog;
//
//			this.prgBar = (ProgressBar) mDialog.findViewById(R.id.prg_download);
//			this.txtPer = (TextView) mDialog.findViewById(R.id.txt_download_percent);
			this.info = info;
		}
		

		@Override
		protected void onPreExecute() {
			ProgressBar prgBar = null;
			TextView txtPer = null;
			LinearLayout layout = null;
			switch (this.level.getNumber()) {
			case 1:
				prgBar = this.info.getPrgBar1();
				txtPer = this.info.getTxtScore1();
				layout = this.info.getLayoutLevel1();
				break;
			case 2:
				prgBar = this.info.getPrgBar2();
				txtPer = this.info.getTxtScore2();		
				layout = this.info.getLayoutLevel2();
				break;
			case 3:
				prgBar = this.info.getPrgBar3();
				txtPer = this.info.getTxtScore3();			
				layout = this.info.getLayoutLevel3();
				break;				
			default:
				break;
			}		
			if(prgBar != null) prgBar.setProgress(0);
			if(txtPer != null) txtPer.setText("0%");
			if(layout != null){
				layout.setClickable(false);
				layout.setAlpha(1f);
			}
			//this.layout.setAlpha(1f);
			
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			switch (level.getNumber()) {
			case 1:
				info.setStatus1(DownloadInfo.NOT_START);
				break;
			case 2:
				info.setStatus2(DownloadInfo.NOT_START);
				break;
			case 3:
				info.setStatus3(DownloadInfo.NOT_START);
				break;					
			default:
				break;
			}				
			if(!result){
				mHandler.obtainMessage(HANDLE_ACTIVE_LEVEL, false).sendToTarget();
				
			} else {
				mHandler.obtainMessage(HANDLE_ACTIVE_LEVEL, true).sendToTarget();
				
			}
			super.onPostExecute(result);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			//level = params[0];
			
			switch (level.getNumber()) {
			case 1:
				info.setStatus1(DownloadInfo.DOWNLOADING);
				break;
			case 2:
				info.setStatus2(DownloadInfo.DOWNLOADING);
				break;
			case 3:
				info.setStatus3(DownloadInfo.DOWNLOADING);
				break;					
			default:
				break;
			}
			DownloadHelper dlHelper = new DownloadHelper(mContext);
			
			// download audio
			boolean audioFlag = false;
			boolean txtFlag = false;
			String internalRootPath = Constants.getInternalRootPath(mContext);
			String externalRootPath = Constants.getExternalRootPath(mContext);
			String urlTxt = level.getTxt().getPathRemote();

			// calculate size
			long size = 0;
			try {
				size += dlHelper.getSize(urlTxt);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			long sum = 0;
			// download text
			
			
			String txtPath = internalRootPath + "/" + Constants.FILE_TYPE_TXT + "_" + level.getId() + ".txt";
			File file = new File(txtPath);
			if(level.getTxt().getPathLocal() == null || level.getTxt().getPathLocal().isEmpty()){
				try {
					InputStream is = dlHelper.parseUrl(urlTxt);
					if(is != null){
						file = new File(txtPath);
						FileOutputStream os = new FileOutputStream(file);
						
						byte[] buf = new byte[1024];
						int read = 0;

						while((read = is.read(buf)) > 0){
							os.write(buf, 0, read);	
							sum += read;		
							if(size > 0) publishProgress((int)((sum * 100l)/size));							
						}
						os.close();
						is.close();		

					}		
				} catch (IOException e) {
//					showMsg(e.getMessage());
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}
			

					
			
			
			file = new File(txtPath);
			List<SectionDataSet> sections = null;
			if(file.exists()){
				
				
				try {
					sections = JsonReaderHelper.readSections(file);


				} catch (IOException e) {
					
					e.printStackTrace();
					//showMsg(e.getMessage());	
				}							
			}			
			// download image file
			if(sections != null){
				for(SectionDataSet section: sections)
					for(QuestionDataSet question: section.getQuestions())
						if(Constants.FILE_TYPE_IMG.equals(question.getChoices().get(0).getType())){
							for(ChoiceDataSet choice: question.getChoices()){
								String urlChoice = choice.getContent();
								try {
									size += dlHelper.getSize(urlChoice);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								String choicePath = externalRootPath + "/img_" + level.getId() + "_" + section.getId() 
										+ "_" + question.getId() + "_" +  choice.getLabel() + ".jpg";
								try {
									InputStream is = dlHelper.parseUrl(urlChoice);
									if(is != null){
										file = new File(choicePath);
										FileOutputStream os = new FileOutputStream(file);
										
										byte[] buf = new byte[1024];
										int read = 0;

										while((read = is.read(buf)) > 0){
											os.write(buf, 0, read);	
											sum += read;		
											if(size > 0) publishProgress((int)((sum * 100l)/size));						
										}
										os.close();
										is.close();		

									}		
									choice.setContent(choicePath);
								} catch (IOException e) {
//									showMsg(e.getMessage());
									e.printStackTrace();
								} catch (SAXException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ParserConfigurationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}	
								
							}							
						}

			}			
			// update
			
			if(sections != null){
				for(SectionDataSet data: sections){
					
					dbAdapter.addSection(data, level.getId());						
				}	
				if(dbAdapter.updateLevelTxt(level.getId(), txtPath, urlTxt) > 0){
					level.getTxt().setPathLocal(txtPath);
					txtFlag = true;						
				}					
			}
		
			
			

			// update level
			

			switch (level.getNumber()) {
			case 1:
				info.setStatus1(DownloadInfo.COMPLETED);
				break;
			case 2:
				info.setStatus2(DownloadInfo.COMPLETED);
				break;
			case 3:
				info.setStatus3(DownloadInfo.COMPLETED);
				break;					
			default:
				break;
			}
			return txtFlag && audioFlag;
		}
		
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			int p = values[0];		
			ProgressBar prgBar = null;
			TextView txtPer = null;
			switch (this.level.getNumber()) {
			case 1:
				prgBar = this.info.getPrgBar1();
				txtPer = this.info.getTxtScore1();
				break;
			case 2:
				prgBar = this.info.getPrgBar2();
				txtPer = this.info.getTxtScore2();				
				break;
			case 3:
				prgBar = this.info.getPrgBar3();
				txtPer = this.info.getTxtScore3();				
				break;					
			default:
				break;
			}	
			if(Config.LOGGING){
				Log.d("download", p + "");
			}
			
			if(p < 99){
				switch (level.getNumber()) {
				case 1:
					info.setProgress1(p);
					break;
				case 2:
					info.setProgress2(p);
					break;
				case 3:
					info.setProgress3(p);
					break;					
				default:
					break;
				}
				if(prgBar != null){
					prgBar.setProgress(p);
				}
				if(txtPer != null){
					txtPer.setText(p + "%");				
				}
			}

		}


		private Handler mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HANDLE_INSERT_SECTION:
					String path = (String) msg.obj;


					File file = new File(path);
					if(file.exists() && file.isFile()){
						List<SectionDataSet> sections;
						try {
							sections = JsonReaderHelper.readSections(file);
							for(SectionDataSet data: sections){
								
								dbAdapter.addSection(data, level.getId());						
							}		
							obtainMessage(HANDLE_ACTIVE_LEVEL, true).sendToTarget();
						} catch (IOException e) {
							
							e.printStackTrace();
							showMsg(e.getMessage());	
							obtainMessage(HANDLE_ACTIVE_LEVEL, false).sendToTarget();
						}
				
					}
					break;					
				case HANDLE_ACTIVE_LEVEL:
					boolean result = (Boolean) msg.obj;
					ProgressBar prgBar = null;
					TextView txtPer = null;
					LinearLayout layout = null;
					switch (level.getNumber()) {
					case 1:
						prgBar = info.getPrgBar1();
						txtPer = info.getTxtScore1();
						layout = info.getLayoutLevel1();
						break;
					case 2:
						prgBar = info.getPrgBar2();
						txtPer = info.getTxtScore2();
						layout = info.getLayoutLevel2();
						break;
					case 3:
						prgBar = info.getPrgBar3();
						txtPer = info.getTxtScore3();	
						layout = info.getLayoutLevel3();
						break;							
					default:
						break;
					}		
					switch (level.getNumber()) {
					case 1:
						info.setProgress1(100);
						break;
					case 2:
						info.setProgress2(100);
						break;
					case 3:
						info.setProgress3(100);
						break;					
					default:
						break;
					}					
					if(result){
										

						if(prgBar != null) prgBar.setProgress(100);
						if(txtPer != null) txtPer.setText("100%");
						if(layout != null){
							layout.setClickable(true);
							layout.setAlpha(1f);
						}						
						level.setActive(Constants.STATUS_ACTIVE);
						//layout.setAlpha(1f);
						int updatedActive = dbAdapter.updateLevelActive(level.getId(), true);
						
						showMsg("download finish! " + updatedActive);			
					} else {
						if(prgBar != null) prgBar.setProgress(0);
						if(txtPer != null) txtPer.setText("0/100");
						if(layout != null){
							layout.setClickable(true);
							layout.setAlpha(0.5f);
						}	
						showMsg("download failed!");
					}						
					break;
				default:
					break;
				}
			}			
		};
		
	}

	private static final int HANDLE_INSERT_SECTION = 2;
	private static final int HANDLE_ACTIVE_LEVEL = 3;
	
	private void showMsg(String msg){
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
	private class ViewHolder{
		TextView txtTitle;
		Button btnView;
		//ListView listLevel;
		LinearLayout layoutLevel3;
		LinearLayout layoutLevel2;
		LinearLayout layoutLevel1;
		
		ProgressBar prgBar1;	
		ProgressBar prgBar2;	
		ProgressBar prgBar3;
		
		TextView txtScore1;
		TextView txtScore2;
		TextView txtScore3;
		DownloadInfo info;
	}

	

}
