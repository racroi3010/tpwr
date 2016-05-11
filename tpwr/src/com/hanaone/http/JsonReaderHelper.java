package com.hanaone.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;

import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.db.ChoiceDataSet;
import com.hanaone.tpwr.db.ExamDataSet;
import com.hanaone.tpwr.db.FileDataSet;
import com.hanaone.tpwr.db.LevelDataSet;
import com.hanaone.tpwr.db.QuestionDataSet;
import com.hanaone.tpwr.db.SectionDataSet;

public class JsonReaderHelper {
	public static List<ExamDataSet> readExams(File file) throws IOException{
		FileInputStream is = new FileInputStream(file);
		JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
		try {
			return readExams(reader);
		} finally{
			reader.close();
		}
		
	}
	public static List<SectionDataSet> readSections(File file) throws IOException{
		FileInputStream is = new FileInputStream(file);
		JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
		try {
			return readSections(reader);
		} finally{
			reader.close();
		}
		
	}	
	
	// read google json
	public static FileDataSet readFileDataset(File file) throws IOException{
		FileInputStream is = new FileInputStream(file);
		JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
		
		reader.beginObject();
		FileDataSet rs = new FileDataSet();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("downloadUrl")){
				rs.setPathRemote(reader.nextString());
			} else if(name.equals("fileName")){
				rs.setName(reader.nextString());
			} else if(name.equals("sizeBytes")){
				rs.setSize(reader.nextLong());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return rs;
		
	}
	
	// read question
	public static List<SectionDataSet> readSections(JsonReader reader) throws IOException{
		List<SectionDataSet> sections = new ArrayList<SectionDataSet>();
		reader.beginArray();
		while(reader.hasNext()){
			SectionDataSet section = readSection(reader);
			
			if(Constants.FILE_TYPE_IMG.equals(section.getType())){
				FileDataSet img = new FileDataSet();
				img.setPathRemote(section.getHint());
				section.setImg(img);
				section.setHint(null);
			}
				
			sections.add(section);
		}
		reader.endArray();
		return sections;		
	}
	
	private static SectionDataSet readSection(JsonReader reader) throws IOException{
		SectionDataSet section = new SectionDataSet();
		
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("section_number")){
				section.setNumber(reader.nextInt());		
			} else if(name.equals("section_txt")){
				section.setText(reader.nextString());
			} else if(name.equals("section_hint")){
				section.setHint(reader.nextString());
			} else if(name.equals("section_type")){
				section.setType(reader.nextString());				
			} else if(name.equals("questions")){
				section.setQuestions(readQuestions(reader));
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return section;
		
		
	}
	private static List<QuestionDataSet> readQuestions(JsonReader reader) throws IOException{
		List<QuestionDataSet> questions = new ArrayList<QuestionDataSet>();
		reader.beginArray();
		while(reader.hasNext()){
			QuestionDataSet question = readQuestion(reader);
			if(Constants.FILE_TYPE_IMG.equals(question.getType())){
				FileDataSet img = new FileDataSet();
				img.setPathRemote(question.getText());
				question.setImg(img);	
				question.setText(null);
			}
			
			questions.add(question);
		}
		reader.endArray();
		return questions;
	}
	private static QuestionDataSet readQuestion(JsonReader reader) throws IOException{
		QuestionDataSet data = new QuestionDataSet();
		
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("question_number")){
				data.setNumber(reader.nextInt());
			} else if(name.equals("question_mark")){
				data.setMark(reader.nextInt());
			} else if(name.equals("question_type")){
				data.setType(reader.nextString());
			} else if(name.equals("question_txt")){
				data.setText(reader.nextString());
			} else if(name.equals("question_hint")){
				data.setHint(reader.nextString());
			} else if(name.equals("question_answer")){
				data.setAnswer(reader.nextInt());
			} else if(name.equals("question_choices")){
				data.setChoices(readChoices(reader));			
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return data;
	}
	private static List<ChoiceDataSet> readChoices(JsonReader reader) throws IOException{
		List<ChoiceDataSet> choices = new ArrayList<ChoiceDataSet>();
		
		reader.beginArray();
		int i = 0;
		ChoiceDataSet choice = null;
		while(reader.hasNext()){
			choice = readChoice(reader);
			if(choice != null){
				choice.setNumber(i++);
				
				if(Constants.FILE_TYPE_IMG.equals(choice.getType())){
					FileDataSet img = new FileDataSet();
					img.setPathRemote(choice.getContent());
					choice.setImg(img);
					choice.setContent(null);
				}
				choices.add(choice);
			}
		}
		reader.endArray();
		
		return choices;
	}
	private static ChoiceDataSet readChoice(JsonReader reader) throws IOException{
		ChoiceDataSet choice = new ChoiceDataSet();
		
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("label")){
				choice.setLabel(reader.nextString());
			} else if(name.equals("type")) {
				choice.setType(reader.nextString());
			} else if(name.equals("txt")){
				choice.setContent(reader.nextString());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		
		return choice;
	}
	// read conf file
	private static List<ExamDataSet> readExams(JsonReader reader) throws IOException{
		List<ExamDataSet> exams = new ArrayList<ExamDataSet>();
		
		reader.beginArray();
		while(reader.hasNext()){
			exams.add(readExam(reader));
		}
		reader.endArray();
		
		
		return exams;
	}
	
	private static ExamDataSet readExam(JsonReader reader) throws IOException{
		ExamDataSet exam = new ExamDataSet();
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("number")){
				exam.setNumber(reader.nextInt());
			} else if(name.equals("date")){
				exam.setDate(reader.nextString());
			} else if(name.equals("levels")){
				exam.setLevels(readLevels(reader));
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return exam;
	}
	private static List<LevelDataSet> readLevels(JsonReader reader) throws IOException{
		List<LevelDataSet> levels = new ArrayList<LevelDataSet>();
		reader.beginArray();
		while(reader.hasNext()){
			levels.add(readLevel(reader));
		}
		reader.endArray();
		return levels;
	}
	private static LevelDataSet readLevel(JsonReader reader) throws IOException{
		LevelDataSet data = new LevelDataSet();
		
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("number")){
				data.setNumber(reader.nextInt());
			} else if(name.equals("label")){
				data.setLabel(reader.nextString());
			} else if(name.equals("txt")){
				FileDataSet txt = new FileDataSet();
				txt.setName("txt");
				txt.setPathRemote(reader.nextString());
				data.setTxt(txt);
			} else if(name.equals("pdf")){
				FileDataSet pdf = new FileDataSet();
				pdf.setName("pdf");
				pdf.setPathRemote(reader.nextString());
				data.setPdf(pdf);				
			} else if(name.equals("mp3")){
				FileDataSet audio = new FileDataSet();
				audio.setName("pdf");
				audio.setPathRemote(reader.nextString());
				List<FileDataSet> audios = new ArrayList<FileDataSet>();
				audios.add(audio);
			} else if(name.equals("max_score")){
				data.setMaxScore(reader.nextInt());
			} else if(name.equals("time")){
				data.setTime(reader.nextInt());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return data;
	}

}
