package com.hanaone.tpwr.db.model;

import com.hanaone.tpwr.db.ChoiceDataSet;
import com.hanaone.tpwr.db.Pojo;

import android.provider.BaseColumns;

public class Choice implements Model {
	private int id;
	private int number;
	private String type;
	private String label;
	private String content;
	private int file_id;
	private int question_id;	
	private String answer;
	private int goalMin;
	private int goalMax;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getFile_id() {
		return file_id;
	}
	public void setFile_id(int file_id) {
		this.file_id = file_id;
	}
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}





	public int getGoalMin() {
		return goalMin;
	}
	public void setGoalMin(int goalMin) {
		this.goalMin = goalMin;
	}
	public int getGoalMax() {
		return goalMax;
	}
	public void setGoalMax(int goalMax) {
		this.goalMax = goalMax;
	}





	public static abstract class ChoiceEntry implements BaseColumns{
		public static final String TABLE_NAME = "choice";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_LABEL = "label";
		public static final String COLUMN_NAME_CONTENT = "content";
		public static final String COLUMN_FILE_ID = "file_id";
		public static final String COLUMN_QUESTION_ID = "question_id";
		public static final String COLUMN_NAME_ANSWER = "answer";
		public static final String COLUMN_NAME_GOAL_MIN = "goal_min";
		public static final String COLUMN_NAME_GOAL_MAX = "goal_max";
	}


	@Override
	public ChoiceDataSet toPojo() {
		ChoiceDataSet choice = new ChoiceDataSet();
		choice.setId(id);
		choice.setNumber(number);
		choice.setType(type);
		choice.setLabel(label);
		choice.setContent(content);
		choice.setAnswer(answer);
		choice.setGoalMin(goalMin);
		choice.setGoalMax(goalMax);
		return choice;
	}	
}
