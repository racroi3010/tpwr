package com.hanaone.tpwr.db;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tpwr.db.model.Model;
import com.hanaone.tpwr.db.model.Question;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionDataSet implements Parcelable, Pojo{
	protected int id;
	protected int number;
	protected int mark;
	protected String text;
	protected List<ChoiceDataSet> choices;
	protected int answer;
	protected int choice;
	protected String type;

	protected String hint;
	protected FileDataSet img;
	public QuestionDataSet() {
		choice = -1;
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
	public List<ChoiceDataSet> getChoices() {
		return choices;
	}
	public void setChoices(List<ChoiceDataSet> choices) {
		this.choices = choices;
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
	
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
	}
	
	public FileDataSet getImg() {
		return img;
	}
	public void setImg(FileDataSet img) {
		this.img = img;
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
		dest.writeInt(mark);
		dest.writeString(text);
		dest.writeTypedList(choices);
		dest.writeInt(answer);
		dest.writeInt(choice);
		dest.writeString(type);
		dest.writeString(hint);
		dest.writeParcelable(img, flags);
		
	}
	public static final Parcelable.Creator<QuestionDataSet> CREATOR
	= new Parcelable.Creator<QuestionDataSet>() {

		@Override
		public QuestionDataSet createFromParcel(Parcel source) {
			
			return new QuestionDataSet(source);
		}

		@Override
		public QuestionDataSet[] newArray(int size) {
			return new QuestionDataSet[size];
		}


	};
	protected QuestionDataSet(Parcel in){	
		id = in.readInt();
		number = in.readInt();
		mark = in.readInt();
		
		text = in.readString();
		choices = new ArrayList<ChoiceDataSet>();
		in.readTypedList(choices, ChoiceDataSet.CREATOR);
		
		answer = in.readInt();
		choice = in.readInt();
		type = in.readString();
		hint = in.readString();
		img = in.readParcelable(FileDataSet.class.getClassLoader());
	}
	@Override
	public Question toModel() {
		Question question = new Question();
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
