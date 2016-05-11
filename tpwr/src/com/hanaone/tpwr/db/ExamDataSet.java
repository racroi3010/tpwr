package com.hanaone.tpwr.db;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tpwr.db.model.Examination;
import com.hanaone.tpwr.db.model.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ExamDataSet implements Parcelable, Pojo {
	private int number;
	private String date;
	private List<LevelDataSet> levels;
//	private int color;
	public ExamDataSet() {

	}		
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
	
	public List<LevelDataSet> getLevels() {
		return levels;
	}
	public void setLevels(List<LevelDataSet> levels) {
		this.levels = levels;
	}
	
//	public int getColor() {
//		return color;
//	}
//	public void setColor(int color) {
//		this.color = color;
//	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(number);
		dest.writeString(date);
		dest.writeTypedList(levels);
//		dest.writeInt(color);
		
	}
	public static final Parcelable.Creator<ExamDataSet> CREATOR
	= new Parcelable.Creator<ExamDataSet>() {

		@Override
		public ExamDataSet createFromParcel(Parcel source) {
			
			return new ExamDataSet(source);
		}

		@Override
		public ExamDataSet[] newArray(int size) {
			return new ExamDataSet[size];
		}


	};
	private ExamDataSet(Parcel in){
		number = in.readInt();
		date = in.readString();
		
		levels = new ArrayList<LevelDataSet>();
		in.readList(levels, (ClassLoader) LevelDataSet.CREATOR);
//		color = in.readInt();
	
	}
	@Override
	public Examination toModel() {
		Examination exam = new Examination();
		exam.setNumber(number);
		exam.setDate(date);
		return exam;
	}	
}
