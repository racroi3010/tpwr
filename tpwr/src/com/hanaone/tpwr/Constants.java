package com.hanaone.tpwr;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class Constants {
	public static final String PATH = Environment.getExternalStorageDirectory().getPath();
	
	//public static final String REMOTE_CONFIG_FILE = "https://www.dropbox.com/s/z6t3xg0afbyyaa1/exam_list.txt?raw=1";
	public static final String REMOTE_CONFIG_FILE_JSON = "https://drive.google.com/uc?export=download&id=0Byaz20I0WF7YOGFIZnFwV2NQa21";

	public static final String FILE_TYPE_PDF = "PDF";
	public static final String FILE_TYPE_TXT = "TXT";
	public static final String FILE_TYPE_MP3 = "MP3";
	public static final String FILE_TYPE_IMG = "IMG";
	
	public static final String LEVEL_ID = "level_id";
	public static final String LEVEL_NAME = "level_name";
	public static final String LEVEL = "level";
	
	public static final String SELECTION_MODE = "selection_mode";
	public static final String SELECTION_MODE_EXAM = "selection_mode_exam";
	public static final String SELECTION_MODE_SAMPLE = "selection_mode_sample";
	
	
	public static final String QUESTION_MODE = "question_mode";
	
	public static final String QUESTION_MODE_PRACTICE = "question_mode_practice";
	public static final String QUESTION_MODE_EXAM = "question_mode_exam";
	public static final String QUESTION_MODE_REVIEW = "question_mode_review";
	public static final String QUESTION_MODE_NO_START = "question_mode_no_start";
	public static final String QUESTION_MODE_SAMPLE_BEGINNER = "QUESTION_MODE_SAMPLE_BEGINNER";
	public static final String QUESTION_MODE_SAMPLE_INTERMEDIATE = "QUESTION_MODE_SAMPLE_INTERMEDIATE";
	public static final String QUESTION_MODE_SAMPLE_ADVANCED = "QUESTION_MODE_SAMPLE_ADVANCED";
	
	public static final String LIST_CHOICES = "list_choices";
	public static final String LIST_ANSWERS = "list_answers";
	public static final String LIST_SECTIONS = "list_sections";
	
	public static final String RESULT = "RESULT";
	public static final String LIST_RESULT = "LIST_RESULT";
	
	public static final String SECTION_INDEX = "section_index";
	public static final String QUESTION_NUMBER = "QUESTION_NUMBER";
	public static final String SECTION_NUMBER = "SECTION_NUMBER";
	
	public static final String PATH_ROOT = "tprd";
	
	public static final String PATH_TEMP = "temp";
	public static final String PATH_FILE = "file";
	
	public static final int STATUS_INACTIVE = 0;
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_DOWNLOADING = 2;
	
	public static final String UPDATE_SCORE_LEVEL_ID = "UPDATE_SCORE_LEVEL_ID";
	public static final String UPDATE_SCORE = "UPDATE_SCORE";
	
	public static final int REQ_UPDATE_LOCALE = 1;
	
	
	public static final String LANGUAGE_POSITION = "LANGUAGE_POSITION";
	
	protected static final String UPDATE_LOCALE = "UPDATE_LOCATION";
	
	
	public static String getInternalRootPath(Context context){
		File folder = context.getDir(Constants.PATH_ROOT, Context.MODE_PRIVATE);
		return folder.getAbsolutePath();
	}
	public static String getInternalPath(Context context, String folder){
		File root = context.getDir(Constants.PATH_ROOT, Context.MODE_PRIVATE);
		File dir = new File(root.getAbsolutePath() + File.separator + folder);
		dir.mkdirs();
		
		return dir.getAbsolutePath();
	}

	public static String getExternalRootPath(Context context){
		File folder = context.getExternalFilesDir(null);
		return folder.getAbsolutePath();
	}
	public static String getExternalPath(Context context, String folder){
		File dir = new File(getExternalRootPath(context) + File.separator + folder);
		dir.mkdirs();
		
		return dir.getAbsolutePath();
	}
}
