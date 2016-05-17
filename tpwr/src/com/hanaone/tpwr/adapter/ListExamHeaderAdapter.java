package com.hanaone.tpwr.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.R;
import com.hanaone.tpwr.db.ExamDataSet;
import com.hanaone.tpwr.db.LevelDataSet;

public class ListExamHeaderAdapter extends BaseAdapter {
	// adapter
	private Context mContext;
	private ListLevelListener mListener;
	private List<ExamItem> items;
	private LayoutInflater mInflater;
//	private List<DownloadInfo> infos;	
	
	// header
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	public enum RowType{
		LIST_ITEM, HEADER_ITEM
	}
	public ListExamHeaderAdapter(Context mContext, ListLevelListener mListener) {
		//super(mContext);
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	
	public void setItems(List<ExamItem> objects){
		this.items = objects;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(this.items != null) return this.items.size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if(this.items != null) return this.items.get(arg0);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	@Override
	public int getItemViewType(int position) {
		return items.get(position).getViewType();
	}
	@Override
	public int getViewTypeCount() {
		return RowType.values().length;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	

		
		convertView = items.get(position).getView(mContext, mInflater, convertView, parent);
		return convertView;
	}



	
	public interface ExamItem {
		public int getViewType();
		public View getView(Context context, LayoutInflater inflater, View convertView, ViewGroup parent);
	}
	
	public static class ExamHeader implements ExamItem{
		private final ExamDataSet exam;
		
		public ExamHeader(ExamDataSet exam){
			this.exam = exam;
		}
		
		public ExamDataSet getExam() {
			return exam;
		}

		@Override
		public int getViewType() {
			return RowType.HEADER_ITEM.ordinal();
		}

		@Override
		public View getView(Context context, LayoutInflater inflater, View convertView, ViewGroup parent) {
			final ViewHolderHeader holder;
			if(convertView == null || !(convertView.getTag() instanceof ViewHolderHeader)){
				convertView = inflater.inflate(R.layout.layout_exam_header, parent, false);
				holder = new ViewHolderHeader();
				holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);	
				
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolderHeader) convertView.getTag();
			}
			String examName = context.getResources().getString(R.string.exam_title);
			
			
			holder.txtTitle.setText(String.format(examName, exam.getNumber()));			
			return convertView;
		}
		private class ViewHolderHeader{
			TextView txtTitle;
		}		
	}
	
	public static class ExamLevelItem implements ExamItem{
		private LevelDataSet level;
		private DownloadInfo info;
		private ListLevelListener mListener;
		public ExamLevelItem(LevelDataSet level, DownloadInfo info, ListLevelListener mListener) {
			this.level = level;
			this.info = info;
			this.mListener = mListener;
		}

		@Override
		public int getViewType() {
			return RowType.LIST_ITEM.ordinal();
		}

		@Override
		public View getView(Context context, LayoutInflater inflater, View convertView, ViewGroup parent) {
			final ViewHolderItem holder;	
			if(convertView == null || !(convertView.getTag() instanceof ViewHolderItem)){
				convertView = inflater.inflate(R.layout.layout_exam_item, parent, false);
				holder = new ViewHolderItem();
				holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout_item);
				holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_new_lesson);
				holder.txtLabel = (TextView) convertView.findViewById(R.id.txt_label);
				holder.prgBar = (ProgressBar) convertView.findViewById(R.id.prg_level);
				holder.txtScore = (TextView) convertView.findViewById(R.id.txt_score);
				
				holder.info = this.info;
				
				convertView.setTag(holder);					
			} else {
				holder = (ViewHolderItem) convertView.getTag();
				
				holder.info.setLayout(null);
				holder.info.setPrgBar(null);
				holder.info.setTxtPer(null);
				
				holder.info = this.info;				
				
			}
			
			this.info.setLayout(holder.layout);
			this.info.setPrgBar(holder.prgBar);
			this.info.setTxtPer(holder.txtScore);			

			int number = level.getNumber();
			int maxScore = level.getMaxScore();
			int score = level.getScore();
			int progress = maxScore == 0 ? 0 : (score * 100)/maxScore;
			//String scoreLabel = score + "/"+ maxScore;
			
			holder.txtLabel.setText(level.getLabel() + "");
			holder.imgIcon.setBackgroundColor(level.getColor());
			if(level.getActive() != Constants.STATUS_ACTIVE){
				holder.layout.setAlpha(0.5f);
			} else {
				holder.layout.setAlpha(1f);
			}				
			if(info.getStatus() != DownloadInfo.DOWNLOADING && info.getStatus() != DownloadInfo.QUEUED){
				info.getPrgBar().setProgress(progress);
				//info.getTxtPer().setText(scoreLabel);									
			} else{
				info.getPrgBar().setProgress(info.getProgress());
				info.getTxtPer().setText(info.getProgress() + "%");
				//info.getLayoutLevel1().setAlpha(1f);
			}		
			holder.layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mListener.onSelect(level, info);
				}
			});
			
		
			return convertView;
		}
		private class ViewHolderItem{
			RelativeLayout layout;	
			TextView txtLabel;		
			ImageView imgIcon;
					
			ProgressBar prgBar;
			TextView txtScore;
			DownloadInfo info;
		}		

	}






}
