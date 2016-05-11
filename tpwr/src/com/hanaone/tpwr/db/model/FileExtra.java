package com.hanaone.tpwr.db.model;

import com.hanaone.tpwr.db.FileDataSet;
import com.hanaone.tpwr.db.Pojo;

import android.provider.BaseColumns;

public class FileExtra implements Model {
	private int id;
	private String type;
	private String name;
	private String pathLocal;
	private String pathRemote;
	private long size;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}

	public abstract class FileExtraEntry implements BaseColumns{
		public static final String TABLE_NAME = "fileextra";
		public static final String COLUMN_NAME_FILE_TYPE = "filetype";
		public static final String COLUMN_NAME_FILE_NAME = "filename";
		public static final String COLUMN_NAME_FILE_PATH_LOCAL = "path_local";
		public static final String COLUMN_NAME_FILE_PATH_REMOTE = "path_remote";
		public static final String COLUMN_NAME_FILE_SIZE = "size";
	}

	@Override
	public FileDataSet toPojo() {
		FileDataSet file = new FileDataSet();
		file.setId(id);
		file.setType(type);
		file.setName(name);
		file.setPathLocal(pathLocal);
		file.setPathRemote(pathRemote);
		file.setSize(size);
		return file;
	}		
}
