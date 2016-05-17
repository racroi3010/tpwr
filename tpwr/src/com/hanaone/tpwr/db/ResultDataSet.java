package com.hanaone.tpwr.db;

import com.hanaone.tpwr.db.model.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultDataSet implements Parcelable, Pojo {
	private int number;
	private String label;
	private int goalMin;
	private int goalMax;
	private int finish;
	
	public ResultDataSet() {
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






	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
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

	public int getFinish() {
		return finish;
	}

	public void setFinish(int finish) {
		this.finish = finish;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(number);
		dest.writeString(label);
		dest.writeInt(goalMin);
		dest.writeInt(goalMax);
		dest.writeInt(finish);
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
		label = in.readString();
		goalMin = in.readInt();
		goalMax = in.readInt();
		finish = in.readInt();
	}

	@Override
	public Model toModel() {
		return null;
	}
}
