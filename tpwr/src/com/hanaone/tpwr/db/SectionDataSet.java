package com.hanaone.tpwr.db;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tpwr.db.model.Model;
import com.hanaone.tpwr.db.model.Section;

import android.os.Parcel;
import android.os.Parcelable;

public class SectionDataSet implements Parcelable, Pojo {
	protected int id;
	protected int number;
	protected String text;
	protected String hint;
	protected List<QuestionDataSet> questions;
	protected String type;
	protected FileDataSet img;
	public SectionDataSet() {

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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<QuestionDataSet> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionDataSet> questions) {
		this.questions = questions;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
		dest.writeString(text);
		dest.writeString(hint);
		dest.writeTypedList(questions);
		dest.writeString(type);
		dest.writeParcelable(img, flags);
		
	}
	public static final Parcelable.Creator<SectionDataSet> CREATOR
	= new Parcelable.Creator<SectionDataSet>() {

		@Override
		public SectionDataSet createFromParcel(Parcel source) {
			
			return new SectionDataSet(source);
		}

		@Override
		public SectionDataSet[] newArray(int size) {
			return new SectionDataSet[size];
		}


	};
	protected SectionDataSet(Parcel in){
		id = in.readInt();
		number = in.readInt();
		text = in.readString();
		hint = in.readString();
		
		questions = new ArrayList<QuestionDataSet>();
		in.readTypedList(questions, QuestionDataSet.CREATOR);
		
		type = in.readString();
		img = in.readParcelable(FileDataSet.class.getClassLoader());
	}
	@Override
	public Section toModel() {
		Section section = new Section();
		section.setId(id);
		section.setNumber(number);
		section.setText(text);
		section.setHint(hint);
		section.setType(type);
		return section;
	}		
}
