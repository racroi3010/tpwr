package com.hanaone.tpwr.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanaone.http.DownloadHelper;
import com.hanaone.http.JsonReaderHelper;
import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.db.ChoiceDataSet;
import com.hanaone.tpwr.db.FileDataSet;
import com.hanaone.tpwr.db.LevelDataSet;
import com.hanaone.tpwr.db.QuestionDataSet;
import com.hanaone.tpwr.db.SectionDataSet;
import com.hanaone.tpwr.db.model.FileExtra;
import com.hanaone.tpwr.util.Config;
import com.hanaone.tpwr.util.DatabaseUtils;

public class DownloadLevelAdapter extends AsyncTask<Void, Integer, Boolean> {
	private LevelDataSet level;
	private DownloadInfo info;
	private Context mContext;
	private DatabaseAdapter dbAdapter;

	public DownloadLevelAdapter(Context mContext, LevelDataSet level,
			DownloadInfo info, DatabaseAdapter dbAdapter) {
		this.mContext = mContext;
		this.level = level;
		this.info = info;
		this.dbAdapter = dbAdapter;
	}

	@Override
	protected void onPreExecute() {
		ProgressBar prgBar = this.info.getPrgBar();
		TextView txtPer = this.info.getTxtPer();
		RelativeLayout layout = this.info.getLayout();

		if (prgBar != null)
			prgBar.setProgress(0);
		if (txtPer != null)
			txtPer.setText("0%");
		if (layout != null) {
			layout.setClickable(false);
			layout.setAlpha(1f);
		}
		// this.layout.setAlpha(1f);

		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		info.setStatus(DownloadInfo.NOT_START);
		
		if (!result) {
			mHandler.obtainMessage(HANDLE_ACTIVE_LEVEL, false).sendToTarget();

		} else {
			TextView txtPer = this.info.getTxtPer();
			if (txtPer != null)
				txtPer.setText("100%");			
			mHandler.obtainMessage(HANDLE_ACTIVE_LEVEL, true).sendToTarget();

		}
		super.onPostExecute(result);
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		info.setStatus(DownloadInfo.DOWNLOADING);
		DownloadHelper dlHelper = new DownloadHelper(mContext);

		// download audio
		boolean audioFlag = false;
		boolean txtFlag = false;
		String internalRootPath = Constants.getInternalRootPath(mContext);
		String externalRootPath = Constants.getExternalRootPath(mContext);
		String urlTxt = level.getTxt().getPathRemote();

		// calculate size
		long size = 0;
		try {
			size += dlHelper.getSize(urlTxt);
			// long audioSize= dlHelper.getSize(urlAudio);
			// audioSize = audioSize == 0 ? 50000000 : audioSize;
			// size += audioSize;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		long sum = 0;
		// download text

		String txtPath = internalRootPath + "/" + Constants.FILE_TYPE_TXT + "_"
				+ level.getId() + ".txt";
		File file = new File(txtPath);
		try {
			InputStream is = dlHelper.parseUrl(urlTxt);
			if (is != null) {
				file = new File(txtPath);
				FileOutputStream os = new FileOutputStream(file);

				byte[] buf = new byte[1024];
				int read = 0;

				while ((read = is.read(buf)) > 0) {
					os.write(buf, 0, read);
					sum += read;
					// if(size > 0) publishProgress((int)((sum * 100l)/size));
				}
				os.close();
				is.close();

			}
		} catch (IOException e) {
			// showMsg(e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// String audioPath = externalRootPath + "/" + Constants.FILE_TYPE_MP3 +
		// "_" + level.getId() + ".mp3";
		//
		// if(urlAudio.contains("http")){
		// try {
		// // multi link
		// String[] links = urlAudio.split(";");
		// //
		// for(String link: links){
		// InputStream is = dlHelper.parseUrl(link);
		// if(is != null){
		// file = new File(audioPath);
		// FileOutputStream os = new FileOutputStream(file);
		//
		// byte[] buf = new byte[1024];
		// int read = 0;
		//
		// while((read = is.read(buf)) > 0){
		// os.write(buf, 0, read);
		//
		// sum += read;
		// if(size > 0) publishProgress((int)((sum * 100l)/size));
		// }
		// os.close();
		// is.close();
		// break;
		// }
		// }
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// //showMsg(e.getMessage());
		// } catch (SAXException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ParserConfigurationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		file = new File(txtPath);
		List<SectionDataSet> sections = null;
		if (file.exists()) {

			try {
				sections = JsonReaderHelper.readSections(file);

			} catch (IOException e) {

				e.printStackTrace();
				// showMsg(e.getMessage());
			}
		}
		int k = 2;
		if (size > 0)
			publishProgress(k);
		// // download image file
		// calculate size
		boolean flagTemp = false;
		if (sections != null) {
			for (SectionDataSet section : sections) {
				if (Constants.FILE_TYPE_IMG.equals(section.getType())) {
					String urlImgSection = section.getImg().getPathRemote();
					try {
						size += dlHelper.getSize(urlImgSection);
						k++;
						if (size > 0)
							publishProgress(k);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					flagTemp = true;
				}				
				for (QuestionDataSet question : section.getQuestions()) {
					if (Constants.FILE_TYPE_IMG.equals(question.getType())) {
						String urlImg = question.getImg().getPathRemote();
						try {
							size += dlHelper.getSize(urlImg);
							k++;
							if (size > 0)
								publishProgress(k);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						flagTemp = true;
					}
					for (ChoiceDataSet choice : question.getChoices()) {
						if (Constants.FILE_TYPE_IMG.equals(choice.getType())) {
							String urlChoice = choice.getImg().getPathRemote();
							try {
								size += dlHelper.getSize(urlChoice);
								k++;
								if (size > 0)
									publishProgress(k);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							flagTemp = true;
						}

					}
				}
			}

		}
		if (sections != null && flagTemp) {
			for (SectionDataSet section : sections){
				if (Constants.FILE_TYPE_IMG.equals(section.getType())) {
					String urlImgSection = section.getImg().getPathRemote();
					String imgPathSection = externalRootPath + "/img_"
							+ level.getId() + "_" + section.getNumber() + ".jpg";
					try {
						InputStream is = dlHelper.parseUrl(urlImgSection);
						if (is != null) {
							file = new File(imgPathSection);
							FileOutputStream os = new FileOutputStream(file);

							byte[] buf = new byte[1024];
							int read = 0;

							while ((read = is.read(buf)) > 0) {
								os.write(buf, 0, read);
								sum += read;
								if (size > 0)
									publishProgress((int) ((sum * 100l) / size));
							}
							os.close();
							is.close();

						}
						section.getImg().setPathLocal(imgPathSection);
					} catch (IOException e) {
						// showMsg(e.getMessage());
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
				for (QuestionDataSet question : section.getQuestions()) {
					if (Constants.FILE_TYPE_IMG.equals(question.getType())) {
						String urlImg = question.getImg().getPathRemote();
						String imgPath = externalRootPath + "/img_"
								+ level.getId() + "_" + section.getNumber()
								+ "_" + question.getNumber() + ".jpg";
						try {
							InputStream is = dlHelper.parseUrl(urlImg);
							if (is != null) {
								file = new File(imgPath);
								FileOutputStream os = new FileOutputStream(file);

								byte[] buf = new byte[1024];
								int read = 0;

								while ((read = is.read(buf)) > 0) {
									os.write(buf, 0, read);
									sum += read;
									if (size > 0)
										publishProgress((int) ((sum * 100l) / size));
								}
								os.close();
								is.close();

							}
							question.getImg().setPathLocal(imgPath);
						} catch (IOException e) {
							// showMsg(e.getMessage());
							e.printStackTrace();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// download
					for (ChoiceDataSet choice : question.getChoices()) {
						if (Constants.FILE_TYPE_IMG.equals(choice.getType())) {
							String urlChoice = choice.getImg().getPathRemote();
							String choicePath = externalRootPath + "/img_"
									+ level.getId() + "_" + section.getNumber()
									+ "_" + question.getNumber() + "_"
									+ choice.getLabel() + ".jpg";
							try {
								InputStream is = dlHelper.parseUrl(urlChoice);
								if (is != null) {
									file = new File(choicePath);
									FileOutputStream os = new FileOutputStream(
											file);

									byte[] buf = new byte[1024];
									int read = 0;

									while ((read = is.read(buf)) > 0) {
										os.write(buf, 0, read);
										sum += read;
										if (size > 0)
											publishProgress((int) ((sum * 100l) / size));
									}
									os.close();
									is.close();

								}
								choice.getImg().setPathLocal(choicePath);
							} catch (IOException e) {
								// showMsg(e.getMessage());
								e.printStackTrace();
							} catch (SAXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParserConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				}				
			}

		}
		// update

		if (sections != null) {
			for (SectionDataSet data : sections) {

				dbAdapter.addSection(data, level.getId());
			}
			if (dbAdapter.updateLevelTxt(level.getId(), txtPath, urlTxt) > 0) {
				level.getTxt().setPathLocal(txtPath);
				txtFlag = true;
			}
		}

		// update level
		audioFlag = true;

		// file = new File(audioPath);
		// if(file.exists()){
		//
		// if(dbAdapter.updateLevelAudio(level.getId(), audioPath) > 0){
		// audioFlag = true;
		// level.getAudio().get(0).setPath(audioPath);
		// }
		// }

		info.setStatus(DownloadInfo.COMPLETED);
		return txtFlag && audioFlag;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		int p = values[0];
		ProgressBar prgBar = this.info.getPrgBar();
		TextView txtPer = this.info.getTxtPer();
		if (Config.LOGGING) {
			Log.d("download", p + "");
		}

		if (p < 99) {
			info.setProgress(p);
			if (prgBar != null) {
				prgBar.setProgress(p);
			}
			if (txtPer != null) {
				txtPer.setText(p + "%");
			}
		}

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_INSERT_SECTION:
				String path = (String) msg.obj;

				File file = new File(path);
				if (file.exists() && file.isFile()) {
					List<SectionDataSet> sections;
					try {
						sections = JsonReaderHelper.readSections(file);
						for (SectionDataSet data : sections) {

							dbAdapter.addSection(data, level.getId());
						}
						obtainMessage(HANDLE_ACTIVE_LEVEL, true).sendToTarget();
					} catch (IOException e) {

						e.printStackTrace();
						showMsg(e.getMessage());
						obtainMessage(HANDLE_ACTIVE_LEVEL, false)
								.sendToTarget();
					}

				}
				break;
			case HANDLE_ACTIVE_LEVEL:
				boolean result = (Boolean) msg.obj;
				ProgressBar prgBar = info.getPrgBar();
				TextView txtPer = info.getTxtPer();
				RelativeLayout layout = info.getLayout();
				info.setProgress(100);
				if (result) {

					if (prgBar != null)
						prgBar.setProgress(100);
					if (txtPer != null)
						txtPer.setText("");
					if (layout != null) {
						layout.setClickable(true);
						layout.setAlpha(1f);
					}
					level.setActive(Constants.STATUS_ACTIVE);
					// layout.setAlpha(1f);
					int updatedActive = dbAdapter.updateLevelActive(
							level.getId(), true);

					showMsg("finish download exam level " + level.getLabel());
				} else {
					if (prgBar != null)
						prgBar.setProgress(0);
					if (txtPer != null)
						txtPer.setText("");
					if (layout != null) {
						layout.setClickable(true);
						layout.setAlpha(0.5f);
					}
					showMsg("Failed to download");
				}
				break;
			default:
				break;
			}
		}
	};
	private static final int HANDLE_INSERT_SECTION = 2;
	private static final int HANDLE_ACTIVE_LEVEL = 3;

	private void showMsg(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
}
