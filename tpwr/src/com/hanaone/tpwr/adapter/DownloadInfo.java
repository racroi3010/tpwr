package com.hanaone.tpwr.adapter;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DownloadInfo {
	public static final int COMPLETED = 0;
	public static final int DOWNLOADING = 1;
	public static final int QUEUED = 2;
	public static final int NOT_START = 3;

	private volatile ProgressBar prgBar;

	private volatile RelativeLayout layout;

	private volatile TextView txtPer;
	private volatile TextView txtSize;

	private volatile int status;
	
	private volatile int progress;
	public DownloadInfo() {
		this.status = NOT_START;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public ProgressBar getPrgBar() {
		return prgBar;
	}

	public void setPrgBar(ProgressBar prgBar) {
		this.prgBar = prgBar;
	}

	public RelativeLayout getLayout() {
		return layout;
	}

	public void setLayout(RelativeLayout layout) {
		this.layout = layout;
	}

	public TextView getTxtPer() {
		return txtPer;
	}

	public void setTxtPer(TextView txtPer) {
		this.txtPer = txtPer;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public TextView getTxtSize() {
		return txtSize;
	}

	public void setTxtSize(TextView txtSize) {
		this.txtSize = txtSize;
	}

	// old
	 private volatile ProgressBar prgBar1;
	 private volatile ProgressBar prgBar2;
	 private volatile ProgressBar prgBar3;
	
	 private volatile TextView txtScore1;
	 private volatile TextView txtScore2;
	 private volatile TextView txtScore3;
	
	 private volatile LinearLayout layoutLevel3;
	 private volatile LinearLayout layoutLevel2;
	 private volatile LinearLayout layoutLevel1;
	
	 private int status1;
	 private int status2;
	 private int status3;
	
	 private int progress1;
	 private int progress2;
	 private int progress3;
	
	
	
//	 public DownloadInfo() {
//	 this.status1 = NOT_START;
//	 this.status2 = NOT_START;
//	 this.status3 = NOT_START;
//	 }
	 public int getStatus1() {
	 return status1;
	 }
	 public void setStatus1(int status1) {
	 this.status1 = status1;
	 }
	 public int getStatus2() {
	 return status2;
	 }
	 public void setStatus2(int status2) {
	 this.status2 = status2;
	 }
	 public int getStatus3() {
	 return status3;
	 }
	 public void setStatus3(int status3) {
	 this.status3 = status3;
	 }
	 public int getProgress1() {
	 return progress1;
	 }
	 public void setProgress1(int progress1) {
	 this.progress1 = progress1;
	 }
	 public int getProgress2() {
	 return progress2;
	 }
	 public void setProgress2(int progress2) {
	 this.progress2 = progress2;
	 }
	 public int getProgress3() {
	 return progress3;
	 }
	 public void setProgress3(int progress3) {
	 this.progress3 = progress3;
	 }
	 public LinearLayout getLayoutLevel3() {
	 return layoutLevel3;
	 }
	 public ProgressBar getPrgBar1() {
	 return prgBar1;
	 }
	 public void setPrgBar1(ProgressBar prgBar1) {
	 this.prgBar1 = prgBar1;
	 }
	 public ProgressBar getPrgBar2() {
	 return prgBar2;
	 }
	 public void setPrgBar2(ProgressBar prgBar2) {
	 this.prgBar2 = prgBar2;
	 }
	 public ProgressBar getPrgBar3() {
	 return prgBar3;
	 }
	 public void setPrgBar3(ProgressBar prgBar3) {
	 this.prgBar3 = prgBar3;
	 }
	 public TextView getTxtScore1() {
	 return txtScore1;
	 }
	 public void setTxtScore1(TextView txtScore1) {
	 this.txtScore1 = txtScore1;
	 }
	 public TextView getTxtScore2() {
	 return txtScore2;
	 }
	 public void setTxtScore2(TextView txtScore2) {
	 this.txtScore2 = txtScore2;
	 }
	 public TextView getTxtScore3() {
	 return txtScore3;
	 }
	 public void setTxtScore3(TextView txtScore3) {
	 this.txtScore3 = txtScore3;
	 }
	 public void setLayoutLevel3(LinearLayout layoutLevel3) {
	 this.layoutLevel3 = layoutLevel3;
	 }
	 public LinearLayout getLayoutLevel2() {
	 return layoutLevel2;
	 }
	 public void setLayoutLevel2(LinearLayout layoutLevel2) {
	 this.layoutLevel2 = layoutLevel2;
	 }
	 public LinearLayout getLayoutLevel1() {
	 return layoutLevel1;
	 }
	 public void setLayoutLevel1(LinearLayout layoutLevel1) {
	 this.layoutLevel1 = layoutLevel1;
	 }

}
