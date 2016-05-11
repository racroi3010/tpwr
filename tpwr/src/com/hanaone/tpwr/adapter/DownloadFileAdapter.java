package com.hanaone.tpwr.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.hanaone.http.DownloadHelper;
import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.db.FileDataSet;
import com.hanaone.tpwr.util.Config;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadFileAdapter extends AsyncTask<Void, Integer, Boolean> {
	private FileDataSet fileData;
	private DownloadInfo info;
	private Context mContext;
	private DatabaseAdapter dbAdapter;
	private Dialog dialog;
	private DownloadListener downloadListener;
	
	public DownloadFileAdapter(FileDataSet fileData, DownloadInfo info,
			Context mContext, DatabaseAdapter dbAdapter, Dialog dialog, DownloadListener downloadListener) {
		this.fileData = fileData;
		this.info = info;
		this.mContext = mContext;
		this.dbAdapter = dbAdapter;
		this.dialog = dialog;
		this.downloadListener = downloadListener;
	}
	@Override
	protected void onPostExecute(Boolean result) {
		info.setStatus(DownloadInfo.NOT_START);	
		if(result){
			ProgressBar prgBar = this.info.getPrgBar();
			TextView txtPer = this.info.getTxtPer();
			info.setProgress(100);
			if(prgBar != null){
				prgBar.setProgress(100);
			}
			if(txtPer != null){
				txtPer.setText("100%");				
			}			
		}

		
		if(this.dialog != null && this.dialog.isShowing()){
			try{
				this.dialog.dismiss();
			}catch (final IllegalArgumentException e) {
	        // Handle or log or ignore
		    } catch (final Exception e) {
		        // Handle or log or ignore
		    } finally {
		    	this.dialog = null;
		    }  			
		}
		if(this.downloadListener != null){
			try{
				this.downloadListener.onFinishNotify(result);
			}catch (final IllegalArgumentException e) {
	        // Handle or log or ignore
		    } catch (final Exception e) {
		        // Handle or log or ignore
		    } finally {
		    	this.downloadListener = null;
		    } 			
		}

		
		
		super.onPostExecute(result);
	}
	@Override
	protected void onPreExecute() {
		// calculate size

		
		ProgressBar prgBar = this.info.getPrgBar();
		TextView txtPer = this.info.getTxtPer();
		
		if(prgBar != null) prgBar.setProgress(0);
		if(txtPer != null) txtPer.setText("0%");
		
		super.onPreExecute();
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		info.setStatus(DownloadInfo.DOWNLOADING);
		
		
		// download audio
		boolean flag = false;
		String externalRootPath = Constants.getExternalRootPath(mContext);
		String url = fileData.getPathRemote();
		
		DownloadHelper dlHelper = new DownloadHelper(mContext);
		long size = 0;
		
			String[] links = url.split(";");
			//
			for(String link: links){
				try {
					size += dlHelper.getSize(link);
					break;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
			

		
		mHandler.obtainMessage(1, size).sendToTarget();
		long sum = 0;
		// download text
		
		
		String localPath = externalRootPath + "/" + Constants.PATH_FILE + "_" + fileData.getId();
		File file = new File(localPath);
		try {
//			InputStream is = dlHelper.parseUrl(url);
//			if(is != null){
//				file = new File(localPath);
//				FileOutputStream os = new FileOutputStream(file);
//				
//				byte[] buf = new byte[1024];
//				int read = 0;
//
//				while((read = is.read(buf)) > 0){
//					os.write(buf, 0, read);	
//					sum += read;		
//					if(size > 0) publishProgress((int)((sum * 100l)/size));							
//				}
//				os.close();
//				is.close();		
//				
//			}	
			links = url.split(";");
			//
			for(String link: links){
				InputStream is = dlHelper.parseUrl(link);
				if(is != null){
					file = new File(localPath);
					FileOutputStream os = new FileOutputStream(file);
					
					byte[] buf = new byte[1024];
					int read = 0;
											
					while((read = is.read(buf)) > 0){
						os.write(buf, 0, read);	
						
						sum += read;		
						if(size > 0) publishProgress((int)((sum * 100l)/size));
					}
					os.close();
					is.close();	
					break;
				}							
			}			
			
		} catch (IOException e) {
//			showMsg(e.getMessage());
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}			
		file = new File(localPath);
		if(file.exists()){
			fileData.setPathLocal(localPath);
			if(this.dbAdapter.updateFile(fileData) > -1){
				
				flag = true;
			}
		}
		return flag;
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		int p = values[0];		
		ProgressBar prgBar = this.info.getPrgBar();
		TextView txtPer = this.info.getTxtPer();
		if(Config.LOGGING){
			Log.d("download", p + "");
		}
		
		if(p < 99){
			info.setProgress(p);
			if(prgBar != null){
				prgBar.setProgress(p);
			}
			if(txtPer != null){
				txtPer.setText(p + "%");				
			}
		}

	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				long value = (long) msg.obj;
				TextView txtSize = info.getTxtSize();
				txtSize.setText(String.format("%.2f", (value/Math.pow(2, 20))) + " Mb");
				break;

			default:
				break;
			}
		}
		
	};
	
}
