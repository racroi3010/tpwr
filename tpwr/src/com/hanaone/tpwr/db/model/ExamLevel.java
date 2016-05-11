package com.hanaone.tpwr.db.model;

import com.hanaone.tpwr.db.LevelDataSet;
import com.hanaone.tpwr.db.Pojo;

import android.provider.BaseColumns;

public class ExamLevel implements Model {
	private int id;
	private int exam_id;
	private int number;
	private String label;
	private int pdf_id;	
	private int txt_id;
	private int score;
	private int maxScore;	

	private int active;
	private int color;
	private int time; // minute	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	

	public int getPdf_id() {
		return pdf_id;
	}
	public void setPdf_id(int pdf_id) {
		this.pdf_id = pdf_id;
	}



	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	



	public int getTxt_id() {
		return txt_id;
	}
	public void setTxt_id(int txt_id) {
		this.txt_id = txt_id;
	}




	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}




	public int getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}




	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}




	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}




	public abstract class ExamLevelEntry implements BaseColumns{
		public static final String TABLE_NAME = "examlevel";
		public static final String COLUMN_NAME_EXAM_ID = "exam_id";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_LABEL = "label";	
		public static final String COLUMN_NAME_PDF_ID = "pdf_id";	
		public static final String COLUMN_NAME_TXT_ID = "txt_id";	
		public static final String COLUMN_NAME_SCORE = "score";
		public static final String COLUMN_NAME_MAXSCORE = "maxscore";
		public static final String COLUMN_NAME_ACTIVE = "active";
		public static final String COLUMN_NAME_COLOR = "color";
		public static final String COLUMN_NAME_TIME = "time";
	}




	@Override
	public LevelDataSet toPojo() {
		LevelDataSet level = new LevelDataSet();
		level.setId(id);
		level.setExam_id(exam_id);
		level.setNumber(number);
		level.setLabel(label);
		level.setScore(score);
		level.setMaxScore(maxScore);
		level.setActive(active);
		level.setColor(color);
		level.setTime(time);
		return level;
	}	
}
