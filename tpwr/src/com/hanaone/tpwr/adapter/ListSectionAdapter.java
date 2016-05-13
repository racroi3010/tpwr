package com.hanaone.tpwr.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.R;
import com.hanaone.tpwr.db.ChoiceDataSet;
import com.hanaone.tpwr.db.FileDataSet;
import com.hanaone.tpwr.db.QuestionDataSet;
import com.hanaone.tpwr.db.SectionDataSet;
import com.hanaone.tpwr.util.ImageUtils;
import com.hanaone.tpwr.util.PreferenceHandler;

public class ListSectionAdapter extends BaseAdapter implements DownloadListener{
	private Context mContext;
	private LayoutInflater mInflater;
	private ListAdapterListener mListener;
	private List<SectionDataSet> mDataSet;
	private List<Item> mItems;

	public ListSectionAdapter(Context mContext, ListAdapterListener mListener) {
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		String mode = PreferenceHandler.getQuestionModePreference(mContext);
		

	}

	public void setmDataSet(List<SectionDataSet> mDataSet) {
		this.mDataSet = mDataSet;
		
		mItems = new ArrayList<ListSectionAdapter.Item>();
		for(int i = 0; i < this.mDataSet.size(); i ++){
			SectionDataSet section = this.mDataSet.get(i);
			mItems.add(new SectionItem(section, i));
			for(int j = 0; j < section.getQuestions().size(); j ++){
				mItems.add(new QuestionItem(section, section.getQuestions().get(j), i, j));
			}
		}
		
		this.notifyDataSetChanged();
	}
//	public void setResults(ArrayList<ResultDataSet> results){
//		this.mResults = results;
//	}
	@Override
	public int getCount() {
		if(mItems != null){
			return mItems.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mItems != null && mItems.size() > position){
			return mItems.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = mItems.get(position).getView(mContext, mInflater, convertView, parent, position);
		
		return convertView;
	}
	private void onChoose(QuestionDataSet question, int btn
			, List<Button> btns){
		question.setChoice(btn);
		for(int i = 0; i < btns.size(); i ++){
			if((i + 1) == btn){
				btns.get(i).setBackgroundResource(R.drawable.circle_number_black);	
				btns.get(i).setTextColor(mContext.getResources().getColor(R.color.WHITE));
			} else {
				btns.get(i).setBackgroundResource(R.drawable.circle_number_trans);	
				btns.get(i).setTextColor(mContext.getResources().getColor(R.color.BLACK));				
			}
		}
	}
	
	public interface Item{
		public int getViewType();
		public View getView(Context context, LayoutInflater inflater, View convertView, ViewGroup parent, int position);		
	}
	public class SectionItem implements Item{
		private SectionDataSet section;
		private int sectionNumber;
		public SectionItem(SectionDataSet section, int sectionNumber) {
			this.section = section;
			this.sectionNumber = sectionNumber;
		}

		@Override
		public int getViewType() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(Context context, LayoutInflater inflater,
				View convertView, ViewGroup parent, int position) {
			final SectionViewHolder holder;
			if(convertView == null || !(convertView.getTag() instanceof SectionViewHolder)){
				convertView = inflater.inflate(R.layout.layout_question_section, parent, false);
				
				holder = new SectionViewHolder();
				holder.txtQuestion = (TextView) convertView.findViewById(R.id.txt_section_question);
				holder.txtHint = (TextView) convertView.findViewById(R.id.txt_section_hint);
				holder.imgQuestion = (ImageView) convertView.findViewById(R.id.img_section_question);
				
				convertView.setTag(holder);
			} else {
				holder = (SectionViewHolder) convertView.getTag();

			}
			

			
			// fill data
			if(section.getHint() == null || section.getHint().isEmpty()){
				holder.txtHint.setVisibility(TextView.GONE);
			} else {
				holder.txtHint.setVisibility(TextView.VISIBLE);
				holder.txtHint.setText(section.getHint());
			}	
			if(Constants.FILE_TYPE_IMG.equals(section.getType())){
				holder.imgQuestion.setVisibility(ImageView.VISIBLE);	
				
				if(section.getImg() !=  null && section.getImg().getPathLocal() != null && new File(section.getImg().getPathLocal()).exists()){
					holder.imgQuestion.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(section.getImg().getPathLocal(), 300, 300));
				} else {
					// set default image;
					holder.imgQuestion.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image_unknown));
					
					holder.imgQuestion.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showDownloadDialog(section.getImg());
						}
					});
				}				
			} else {
				holder.imgQuestion.setVisibility(ImageView.GONE);	
			}			
			
			List<QuestionDataSet> questions = section.getQuestions();

			String txt = "";
			if(questions != null && questions.size() > 1){
				txt += " # [" + questions.get(0).getNumber() + "~" + questions.get(questions.size() - 1).getNumber() + "] ";
			} else {
				txt += " # [" + questions.get(0).getNumber() + "]";
			}
			txt += section.getText();	
			holder.txtQuestion.setText(txt);			
			
			
			return convertView;
		}
		
		private class SectionViewHolder{
			TextView txtQuestion;
			TextView txtHint;
			ImageView imgQuestion;
		}
	}
	public class QuestionItem implements Item{
		private SectionDataSet section;
		private QuestionDataSet question;
		private int sectionNumber;
		private int questionNumber;
		public QuestionItem(SectionDataSet section, QuestionDataSet question, int sectionNumber, int questionNumber) {
			this.question = question;
			this.section = section;
			this.sectionNumber = sectionNumber;
			this.questionNumber = questionNumber;
		}

		@Override
		public int getViewType() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(Context context, LayoutInflater inflater,
				View convertView, ViewGroup parent, int position) {
			final QuestionViewHolder holder;
			if(convertView == null || !(convertView.getTag() instanceof QuestionViewHolder)){
				convertView = inflater.inflate(R.layout.layout_question_question, parent, false);
				
				holder = new QuestionViewHolder();
				
				
				holder.txtNumber = (TextView) convertView.findViewById(R.id.txt_question_number);
				holder.txtHint = (TextView) convertView.findViewById(R.id.txt_question_hint);
				
				holder.btnAnswers = new ArrayList<Button>(2);
				holder.edtAnswers = new ArrayList<EditText>(2);
				holder.txtHints = new ArrayList<TextView>(2);
				
				
//				holder.btnAnswers.add((Button) convertView.findViewById(R.id.btn_question_answer_1));
//				holder.btnAnswers.add((Button) convertView.findViewById(R.id.btn_question_answer_2));
//				
//				holder.edtAnswers.add((EditText) convertView.findViewById(R.id.edt_question_answer_1));
//				holder.edtAnswers.add((EditText) convertView.findViewById(R.id.edt_question_answer_2));
//				
//				holder.txtHints.add((TextView) convertView.findViewById(R.id.txt_question_answer_hint_1));
//				holder.txtHints.add((TextView) convertView.findViewById(R.id.txt_question_answer_hint_2));				
				
				//holder.layoutHint = (LinearLayout) convertView.findViewById(R.id.layout_question_hint);
				holder.txtQuestion = (TextView) convertView.findViewById(R.id.txt_question_txt);
				holder.imgQuestion = (ImageView) convertView.findViewById(R.id.img_question);				
				
				convertView.setTag(holder);
								
			} else {
				holder = (QuestionViewHolder) convertView.getTag();
							
			}		
			
			for(int i = 0; i < holder.btnAnswers.size(); i ++){
				Button btn = holder.btnAnswers.get(i);
				btn.setBackgroundResource(R.drawable.circle_number_trans);
				btn.setText((i + 1) + "");
				btn.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			}	
			
			if(question.getHint() == null || question.getHint().isEmpty()){
				holder.txtHint.setVisibility(LinearLayout.GONE);
			} else {
				holder.txtHint.setVisibility(LinearLayout.VISIBLE);
				holder.txtHint.setText(question.getHint());
										
			}
			
			holder.txtQuestion.setVisibility(TextView.VISIBLE);
			String questionTxt = question.getText(); 
			if(questionTxt != null && !questionTxt.isEmpty()){
				holder.txtQuestion.setText(questionTxt + " (" + question.getMark() + "점)");
			} else {
				holder.txtQuestion.setText("(" + question.getMark() + "점)");
			}			
			if(Constants.FILE_TYPE_IMG.equals(question.getType())){
				holder.imgQuestion.setVisibility(ImageView.VISIBLE);	
				
				if(question.getImg() !=  null && question.getImg().getPathLocal() != null && new File(question.getImg().getPathLocal()).exists()){
					holder.imgQuestion.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(question.getImg().getPathLocal(), 300, 300));
				} else {
					// set default image;
					holder.imgQuestion.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image_unknown));
					
					holder.imgQuestion.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showDownloadDialog(question.getImg());
						}
					});
				}				
			} else {
				holder.imgQuestion.setVisibility(ImageView.GONE);	
			}

			
			
			holder.txtNumber.setText(question.getNumber() + ". ");
			
			
			List<ChoiceDataSet> choices = question.getChoices();
			if(choices != null){
				for(int i = 0; i < 2; i ++){
					final ChoiceDataSet choice = choices.get(i);
					holder.btnAnswers.get(i).setText(choice.getLabel());
					holder.txtHints.get(i).setText(choice.getContent());			
				}			
				
			}
		
				
	
		
			return convertView;
		}
		private class QuestionViewHolder{
			TextView txtNumber;
			TextView txtQuestion;
			ImageView imgQuestion;
			//LinearLayout layoutHint;
			TextView txtHint;		
//			List<Button> btnChoices;
//			List<TextView> txtChoices;
//			List<ImageView> imgChoices;
			List<Button> btnAnswers;
			List<EditText> edtAnswers;
			List<TextView> txtHints;		
		}
	}
	public void showDownloadDialog(final FileDataSet file){
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_dialog_download_cancel);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.show();
		dialog.setCancelable(false);
		
		dialog.findViewById(R.id.btn_dialog_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});	
		
		DownloadInfo info = new DownloadInfo();
		info.setPrgBar((ProgressBar)dialog.findViewById(R.id.prg_dialog_download));
		info.setTxtPer((TextView)dialog.findViewById(R.id.txt_dialog_file_progress));
		info.setTxtSize((TextView)dialog.findViewById(R.id.txt_dialog_file_size));
		
		new DownloadFileAdapter(file, info, mContext, new DatabaseAdapter(mContext), dialog, this).execute();
		
	}

	@Override
	public void onFinishNotify(boolean flag) {
		if(flag){
			this.notifyDataSetChanged();
		}
	}	
}
