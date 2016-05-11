package com.hanaone.tpwr.db;

import com.hanaone.tpwr.db.model.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultDataSet implements Parcelable, Pojo {
	private int number;
	private int choice;
	private int answer;
	private int score;
	
	public ResultDataSet() {
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}

	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(number);
		dest.writeInt(choice);
		dest.writeInt(answer);
		dest.writeInt(score);
	}
	public static final Creator<ResultDataSet> CREATOR = new Creator<ResultDataSet>() {

		@Override
		public ResultDataSet createFromParcel(Parcel source) {
			return new ResultDataSet(source);
		}

		@Override
		public ResultDataSet[] newArray(int size) {
			return new ResultDataSet[size];
		}
		
	};
	private ResultDataSet(Parcel in){
		number = in.readInt();
		choice = in.readInt();
		answer = in.readInt();
		score = in.readInt();
	}

	@Override
	public Model toModel() {
		return null;
	}
}
