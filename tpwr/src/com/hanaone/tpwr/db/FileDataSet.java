package com.hanaone.tpwr.db;

import com.hanaone.tpwr.db.model.FileExtra;
import com.hanaone.tpwr.db.model.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class FileDataSet implements Parcelable, Pojo {
	private int id;
	private String type;
	private String name;
	private String pathLocal;
	private String pathRemote;
	private long size;
	public FileDataSet() {

	}		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPathLocal() {
		return pathLocal;
	}
	public void setPathLocal(String pathLocal) {
		this.pathLocal = pathLocal;
	}
	public String getPathRemote() {
		return pathRemote;
	}
	public void setPathRemote(String pathRemote) {
		this.pathRemote = pathRemote;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(type);
		dest.writeString(name);
		dest.writeString(pathLocal);
		dest.writeString(pathRemote);
		dest.writeLong(size);
		
	}
	public static final Parcelable.Creator<FileDataSet> CREATOR
	= new Parcelable.Creator<FileDataSet>() {

		@Override
		public FileDataSet createFromParcel(Parcel source) {
			
			return new FileDataSet(source);
		}

		@Override
		public FileDataSet[] newArray(int size) {
			return new FileDataSet[size];
		}


	};
	private FileDataSet(Parcel in){
		id = in.readInt();
		type = in.readString();
		name = in.readString();
		pathLocal = in.readString();
		pathRemote = in.readString();
		size = in.readLong();
	}
	@Override
	public FileExtra toModel() {
		FileExtra file = new FileExtra();
		file.setId(id);
		file.setType(type);
		file.setName(name);
		file.setPathLocal(pathLocal);
		file.setPathRemote(pathRemote);
		file.setSize(size);
		return file;
	}	
	
}
