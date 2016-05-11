package com.hanaone.tpwr.db.model;

import com.hanaone.tpwr.db.ExamDataSet;
import com.hanaone.tpwr.db.Pojo;

import android.provider.BaseColumns;

public class Examination implements Model {
//	private int id;
	private int number;
	private String date;
//	private int color;
	
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
//	public int getColor() {
//		return color;
//	}
//	public void setColor(int color) {
//		this.color = color;
//	}

	public static abstract class ExamEntry implements BaseColumns{
		public static final String TABLE_NAME = "examination";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_DATE = "date";
		
	}

	@Override
	public ExamDataSet toPojo() {
		ExamDataSet exam = new ExamDataSet();
		exam.setNumber(number);
		exam.setDate(date);
		
		return exam;
	}
}
