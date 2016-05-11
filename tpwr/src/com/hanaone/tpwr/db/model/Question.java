package com.hanaone.tpwr.db.model;

import com.hanaone.tpwr.db.Pojo;
import com.hanaone.tpwr.db.QuestionDataSet;

import android.provider.BaseColumns;

public class Question implements Model {
	protected int id;
	protected int number;
	protected int mark;
	protected String text;
	protected int answer;
	protected String type;
	protected String hint;	
	protected int img_id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int section_id;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}
	
	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}


	public int getImg_id() {
		return img_id;
	}

	public void setImg_id(int img_id) {
		this.img_id = img_id;
	}


	public static abstract class QuestionEntry implements BaseColumns{
		public static final String TABLE_NAME = "question";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_MARK = "mark";
		public static final String COLUMN_NAME_TEXT = "text";
		public static final String COLUMN_NAME_ANSWER = "answer";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_HINT = "hint";
		public static final String COLUMN_NAME_SECTION_ID = "section_id";
		public static final String COLUMN_NAME_IMG_ID = "img_id";
	}

	@Override
	public QuestionDataSet toPojo() {
		QuestionDataSet question = new QuestionDataSet();
		question.setId(id);
		question.setNumber(number);
		question.setMark(mark);
		question.setText(text);
		question.setAnswer(answer);
		question.setType(type);
		question.setHint(hint);
		return question;
	}		
}
