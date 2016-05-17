package com.hanaone.tpwr.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.hanaone.http.DownloadHelper;
import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.db.ChoiceDataSet;
import com.hanaone.tpwr.db.ExamDataSet;
import com.hanaone.tpwr.db.FileDataSet;
import com.hanaone.tpwr.db.LevelDataSet;
import com.hanaone.tpwr.db.QuestionDataSet;
import com.hanaone.tpwr.db.SectionDataSet;
import com.hanaone.tpwr.db.model.Choice;
import com.hanaone.tpwr.db.model.ExamLevel;
import com.hanaone.tpwr.db.model.Examination;
import com.hanaone.tpwr.db.model.FileExtra;
import com.hanaone.tpwr.db.model.Question;
import com.hanaone.tpwr.db.model.Section;
import com.hanaone.tpwr.util.Config;
import com.hanaone.tpwr.util.DatabaseUtils;

public class DatabaseAdapter{
	private Context mContext;
	private DatabaseHelper dbHelper;
	private DownloadHelper dlHelper;
	public DatabaseAdapter(Context context){
		this.mContext = context;
		this.dbHelper = DatabaseHelper.getInstance(mContext);
		this.dlHelper = new DownloadHelper(mContext);
	}
	
	public List<ExamDataSet> getAllExam(){
		List<Examination> examModels = dbHelper.selectAllExam();
		if(Config.LOGGING){
			Log.w("size of exam", examModels.size() + "");
		}
		List<ExamDataSet> list = new ArrayList<ExamDataSet>();
		for(Examination exam: examModels){
			ExamDataSet data = (ExamDataSet) DatabaseUtils.model2Pojo(exam);
			List<LevelDataSet> levels = new ArrayList<LevelDataSet>();
			
			List<ExamLevel> levelModels = dbHelper.selectExamLevelByExamNumber(exam.getNumber());
			if(levelModels != null){
				for(ExamLevel lmodel: levelModels){
					LevelDataSet l = (LevelDataSet) DatabaseUtils.model2Pojo(lmodel);
					
					l.setId(lmodel.getId());
					l.setNumber(lmodel.getNumber());
					l.setLabel(lmodel.getLabel());

					l.setActive(lmodel.getActive());
					
					
					FileExtra pdf = dbHelper.selectFileById(lmodel.getPdf_id());
					l.setPdf((FileDataSet)DatabaseUtils.model2Pojo(pdf));	
					
					FileExtra txt = dbHelper.selectFileById(lmodel.getTxt_id());
					l.setTxt((FileDataSet)DatabaseUtils.model2Pojo(txt));
					
					levels.add(l);
				}
				
				
				data.setLevels(levels);
				
				list.add(data);
			}

			
		}
		return list;
	}
	public boolean checkExam(int examNumber){
		Examination exam = dbHelper.selectExamByNumber(examNumber);
		
		if(exam != null) return true;
		return false;
		
	}
	public void addExam(ExamDataSet examDataSet){
		//Examination exam = DatabaseUtils.examPojo2Model(examDataSet);
		Examination exam = (Examination) DatabaseUtils.pojo2Model(examDataSet);		
		dbHelper.insert(exam);
		
		List<LevelDataSet> levels = examDataSet.getLevels();
		if(levels != null){
			for(LevelDataSet data: levels){
				
				ExamLevel examLevel = (ExamLevel) DatabaseUtils.pojo2Model(data);
				examLevel.setExam_id(exam.getNumber());
				examLevel.setNumber(data.getNumber());
				examLevel.setLabel(data.getLabel());

				
				examLevel.setPdf_id((int)dbHelper.insert(DatabaseUtils.pojo2Model(data.getPdf())));
				examLevel.setTxt_id((int)dbHelper.insert(DatabaseUtils.pojo2Model(data.getTxt())));
				
				examLevel.setActive(0);
				
				
				data.setId((int)dbHelper.insert(examLevel));
			}
		}
		
		
	}
//	public String getLevelLabel(int levelId){
//		ExamLevel examLevelModel = dbHelper.selectExamLevelById(levelId);
//		Level levelModel = dbHelper.selectLevelById(examLevelModel.getLevel_id());
//		if(levelModel != null){
//			return levelModel.getLabel();					
//		}		
//		return null;
//	}
	public LevelDataSet getLevel(int levelId){
		
		
		ExamLevel examLevelModel = dbHelper.selectExamLevelById(levelId);
		LevelDataSet levelDataSet = (LevelDataSet) DatabaseUtils.model2Pojo(examLevelModel);
		
		levelDataSet.setNumber(examLevelModel.getNumber());
		levelDataSet.setLabel(examLevelModel.getLabel());
		
		levelDataSet.setActive(examLevelModel.getActive());
		

		
		FileExtra pdf = dbHelper.selectFileById(examLevelModel.getPdf_id());
		levelDataSet.setPdf((FileDataSet)DatabaseUtils.model2Pojo(pdf));	
		
		FileExtra txt = dbHelper.selectFileById(examLevelModel.getTxt_id());
		levelDataSet.setTxt((FileDataSet)DatabaseUtils.model2Pojo(txt));	
		
		List<Section> sections = dbHelper.selectSectionByExamLevelId(examLevelModel.getId());
		List<SectionDataSet> sectiondatas = new ArrayList<SectionDataSet>();
		for(Section section:sections){
			SectionDataSet sectionData = (SectionDataSet) DatabaseUtils.model2Pojo(section);
			if(Constants.FILE_TYPE_IMG.equals(sectionData.getType())){
				FileExtra imgFile = dbHelper.selectFileById(section.getImg_id());
				sectionData.setImg((FileDataSet)DatabaseUtils.model2Pojo(imgFile));
			}
			List<Question> questionModels = dbHelper.selectQuestionBySectionId(section.getId());
			List<QuestionDataSet> questionDatas = new ArrayList<QuestionDataSet>();
			for(Question questionModel: questionModels){
				QuestionDataSet questionData = (QuestionDataSet) DatabaseUtils.model2Pojo(questionModel);
				if(Constants.FILE_TYPE_IMG.equals(questionData.getType())){
					FileExtra imgFile = this.dbHelper.selectFileById(questionModel.getImg_id());
					questionData.setImg((FileDataSet)DatabaseUtils.model2Pojo(imgFile));
				}
				List<Choice> choiceModels = dbHelper.selectChoiceByQuestionId(questionModel.getId());
				List<ChoiceDataSet> choiceDatas = new ArrayList<ChoiceDataSet>();
				for(Choice choiceModel: choiceModels){
					ChoiceDataSet choice = (ChoiceDataSet) DatabaseUtils.model2Pojo(choiceModel);
					if(Constants.FILE_TYPE_IMG.equals(choice.getType())){
						FileExtra imgModel = this.dbHelper.selectFileById(choiceModel.getFile_id());
						if(imgModel != null){
							choice.setImg((FileDataSet)DatabaseUtils.model2Pojo(imgModel));	
						} else {
							choice.setImg(new FileDataSet());
						}
											
					}	
					
					choiceDatas.add(choice);
					
				}
				questionData.setChoices(choiceDatas);
				questionDatas.add(questionData);
			}
			sectionData.setQuestions(questionDatas);
			
			sectiondatas.add(sectionData);
		}
		levelDataSet.setSections(sectiondatas);
		
		return levelDataSet;
	}
	public int updateLevelActive(int levelId, boolean active){
		ExamLevel levelModel = this.dbHelper.selectExamLevelById(levelId);
		if(levelModel != null){
			if(active){
				levelModel.setActive(1);
			} else {
				levelModel.setActive(0);
			}
			return this.dbHelper.update(levelModel);
		}
		return -1;
	}
	public int updateLevelAudio(int levelId, String audioLocal, String audioRemote){
		ExamLevel levelModel = this.dbHelper.selectExamLevelById(levelId);
		if(levelModel != null){
			FileExtra file = new FileExtra();
			file.setName("audio_" + levelId);
			file.setPathLocal(audioLocal);
			file.setPathRemote(audioRemote);
			file.setType(Constants.FILE_TYPE_MP3);
			

		}
		
		return -1;
	}
	public int updateLevelTxt(int levelId, String txtLocal, String txtRemote){
		ExamLevel levelModel = this.dbHelper.selectExamLevelById(levelId);
		if(levelModel != null){
			FileExtra file = new FileExtra();
			file.setName("audio_" + levelId);
			file.setPathLocal(txtLocal);
			file.setPathRemote(txtRemote);
			file.setType(Constants.FILE_TYPE_MP3);
			
			long audioId = this.dbHelper.insert(file);
			if(audioId != -1){
				levelModel.setTxt_id((int)audioId);
				return this.dbHelper.update(levelModel);
			}
		}
		
		return -1;
	}
	public int updateLevelScore(int levelId, int score){
		ExamLevel levelModel = this.dbHelper.selectExamLevelById(levelId);
		if(levelModel != null){
			levelModel.setScore(score);
			return this.dbHelper.update(levelModel);
		}
		
		return -1;		
	}
	public void addSection(SectionDataSet data, int levelId){
		Section model = (Section) DatabaseUtils.pojo2Model(data);
		model.setExam_level_id(levelId);
		if(Constants.FILE_TYPE_IMG.equals(model.getType())){
			FileDataSet imgFile = data.getImg();
			long imgId = dbHelper.insert(DatabaseUtils.pojo2Model(imgFile));
			model.setImg_id((int)imgId);
		}		
		
		long sectionId = dbHelper.insert(model);
		
		
		List<QuestionDataSet> questions = data.getQuestions();
		for(QuestionDataSet questionData: questions){
			Question questionModel = (Question) DatabaseUtils.pojo2Model(questionData);
			questionModel.setSection_id((int)sectionId);
			if(Constants.FILE_TYPE_IMG.equals(questionData.getType())){
				FileExtra imgFile = (FileExtra) DatabaseUtils.pojo2Model(questionData.getImg());
				long imgId = dbHelper.insert(imgFile);
				questionModel.setImg_id((int) imgId);
			}			
			long questionId = dbHelper.insert(questionModel);
			
			List<ChoiceDataSet> choiceDatas = questionData.getChoices();
			for(ChoiceDataSet choiceData: choiceDatas){
				Choice choice = (Choice) DatabaseUtils.pojo2Model(choiceData);
				choice.setQuestion_id((int)questionId);
				
				
				
				if(Constants.FILE_TYPE_IMG.equals(choiceData.getType())){
					FileDataSet img = choiceData.getImg();
					FileExtra imgModel = (FileExtra) DatabaseUtils.pojo2Model(img);
					long fileId = dbHelper.insert(imgModel);
					choice.setFile_id((int)fileId);
				}

				
				
				
//				// image type
//				if(Constants.FILE_TYPE_IMG.equals(questionData.getChoiceType())){
//					// download
//					String path = Constants.getPath(mContext, Constants.PATH_FILE);
//					
//					path += "/img_" + sectionId + "_" + questionId + "_" +  choiceData.getLabel() + ".jpg";
//					try {
//						this.dlHelper.downloadFile(choice.getText(), path);
//						choice.setText(path);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
				
				dbHelper.insert(choice);
			}
		}
		
	}
	
	public LevelDataSet generateSampleTest(int level){
		LevelDataSet levelData = new LevelDataSet();
		levelData.setNumber(level);
		levelData.setLabel(level + "");
		levelData.setActive(Constants.STATUS_ACTIVE);
		levelData.setScore(100);
		levelData.setTime(60);
		
		
		List<SectionDataSet> sections = new ArrayList<SectionDataSet>();

		
		levelData.setSections(sections);

		
		Random random = new Random();
		
		// question 51 - 52
		for(int i = 0; i < 1; i ++){
			SectionDataSet section = new SectionDataSet();			
			section.setNumber(i);
			List<QuestionDataSet> questionDatasets = new ArrayList<QuestionDataSet>();
				
			
			List<Question> questionModels = this.dbHelper.selectionQuestionByExamLevelAndQuestionNumber(35, level, i);
			
			if(questionModels != null && questionModels.size() > 0){
				int rdIdx = random.nextInt(questionModels.size());
				
				Question questionModel = questionModels.get(rdIdx);
				QuestionDataSet questionDataset = (QuestionDataSet) DatabaseUtils.model2Pojo(questionModel);			
				if(Constants.FILE_TYPE_IMG.equals(questionDataset.getType())){
					FileExtra imgFile = this.dbHelper.selectFileById(questionModel.getImg_id());
					questionDataset.setImg((FileDataSet)DatabaseUtils.model2Pojo(imgFile));
				}		
				List<Choice> choiceModels = this.dbHelper.selectChoiceByQuestionId(questionModel.getId());
				List<ChoiceDataSet> choiceDataset = new ArrayList<ChoiceDataSet>();
				for(Choice choiceModel: choiceModels){
					ChoiceDataSet choice = (ChoiceDataSet) DatabaseUtils.model2Pojo(choiceModel);
					if(Constants.FILE_TYPE_IMG.equals(choice.getType())){
						FileExtra imgModel = this.dbHelper.selectFileById(choiceModel.getFile_id());
						choice.setImg((FileDataSet)DatabaseUtils.model2Pojo(imgModel));						
					}
					
					choiceDataset.add(choice);											
				}
				Section sectionModel = this.dbHelper.selectSectionByQuestionId(questionModel.getId());
				questionDataset.setChoices(choiceDataset);				
				questionDatasets.add(questionDataset);
				
				section = (SectionDataSet) DatabaseUtils.model2Pojo(sectionModel);
				if(Constants.FILE_TYPE_IMG.equals(section.getType())){
					FileExtra imgFile = dbHelper.selectFileById(sectionModel.getImg_id());
					section.setImg((FileDataSet)DatabaseUtils.model2Pojo(imgFile));
				}	
				section.setQuestions(questionDatasets);	
				
				

				sections.add(section);
			}		
			
			
		}	
		
		// question 53 - 54
		for(int i = 1; i < 4; i ++){
			List<Section> sectionModels = this.dbHelper.selectSectionByExamLevelandSectionNumber(35, level, i);
			if(sectionModels != null && sectionModels.size() >  0){
				int rdIdx = random.nextInt(sectionModels.size());
				Section sectionModel = sectionModels.get(rdIdx);			
				SectionDataSet section = (SectionDataSet) DatabaseUtils.model2Pojo(sectionModel);
				if(Constants.FILE_TYPE_IMG.equals(section.getType())){
					FileExtra imgFile = dbHelper.selectFileById(sectionModel.getImg_id());
					section.setImg((FileDataSet)DatabaseUtils.model2Pojo(imgFile));
				}				
				List<Question> questionModels = this.dbHelper.selectQuestionBySectionId(sectionModel.getId());
				List<QuestionDataSet> questionDatasets = new ArrayList<QuestionDataSet>();
				for(Question questionModel: questionModels){
					QuestionDataSet questionDataset = (QuestionDataSet) DatabaseUtils.model2Pojo(questionModel);
					if(Constants.FILE_TYPE_IMG.equals(questionDataset.getType())){
						FileExtra imgFile = this.dbHelper.selectFileById(questionModel.getImg_id());
						questionDataset.setImg((FileDataSet)DatabaseUtils.model2Pojo(imgFile));
					}						
					List<Choice> choiceModels = this.dbHelper.selectChoiceByQuestionId(questionModel.getId());
					List<ChoiceDataSet> choiceDataset = new ArrayList<ChoiceDataSet>();
					for(Choice choiceModel: choiceModels){
						ChoiceDataSet choice = (ChoiceDataSet) DatabaseUtils.model2Pojo(choiceModel);
						if(Constants.FILE_TYPE_IMG.equals(choice.getType())){
							FileExtra imgModel = this.dbHelper.selectFileById(choiceModel.getFile_id());
							choice.setImg((FileDataSet)DatabaseUtils.model2Pojo(imgModel));						
						}
						
						choiceDataset.add(choice);
						
					}
					
					questionDataset.setChoices(choiceDataset);
					
					
					questionDatasets.add(questionDataset);
				}
				
				section.setQuestions(questionDatasets);
				
				sections.add(section);
				
			}
		}
		
		
		return levelData;
	}
	public boolean checkLevel(int level){
		List<Question> questionModels = this.dbHelper.selectionQuestionByExamLevelAndQuestionNumber(35, level, 51);	
		if(questionModels.size() == 0){
			return false;
		}
		return true;
	}
	public int updateFile(FileDataSet file){
		if(file == null) return -1;
		FileExtra fileModel = (FileExtra) DatabaseUtils.pojo2Model(file);
		if(fileModel != null){
			return this.dbHelper.update(fileModel);
		}
		return -1;
	}
	public int updateChoice(ChoiceDataSet choice, int questionId){
		if(choice == null) return -1;
		
		Choice choiceModel = (Choice) DatabaseUtils.pojo2Model(choice);
		choiceModel.setQuestion_id(questionId);
		if(choiceModel != null){
			return this.dbHelper.update(choiceModel);
		}
		
		return -1;
		
	}
	
}
