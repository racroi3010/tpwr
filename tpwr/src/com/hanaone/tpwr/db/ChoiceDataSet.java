package com.hanaone.tpwr.db;

import com.hanaone.tpwr.db.model.Choice;
import com.hanaone.tpwr.db.model.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChoiceDataSet implements Parcelable, Pojo {
	private int id;
	private int number;
	private String type;	
	private String label;
	private String content;
	private FileDataSet img;
	private String answer;	
	private int goalMin;
	private int goalMax;
	public ChoiceDataSet() {

	}	
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
	public FileDataSet getImg() {
		return img;
	}
	public void setImg(FileDataSet img) {
		this.img = img;
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
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(number);
		dest.writeString(type);
		dest.writeString(label);
		dest.writeString(content);
		dest.writeParcelable(img, flags);
		dest.writeString(answer);
		dest.writeInt(goalMin);
		dest.writeInt(goalMax);
	}
	
	public static final Parcelable.Creator<ChoiceDataSet> CREATOR
				= new Parcelable.Creator<ChoiceDataSet>() {

					@Override
					public ChoiceDataSet createFromParcel(Parcel source) {
						
						return new ChoiceDataSet(source);
					}

					@Override
					public ChoiceDataSet[] newArray(int size) {
						return new ChoiceDataSet[size];
					}
		
		
				};
	private ChoiceDataSet(Parcel in){
		id = in.readInt();
		number = in.readInt();
		type = in.readString();
		label = in.readString();
		content = in.readString();
		img = in.readParcelable(FileDataSet.class.getClassLoader());
		answer = in.readString();
		goalMin = in.readInt();
		goalMax = in.readInt();
	}
	@Override
	public Choice toModel() {
		Choice choice = new Choice();
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
