package com.hanaone.tpwr.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.QuestionActivity;
import com.hanaone.tpwr.ResultActivity;
import com.hanaone.tpwr.db.FileDataSet;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DigitalClockView extends TextView {
	private int _xDelta;
	private int _yDelta;	
	private Timer timer;
	private long time;
	private QuestionActivity questionActivity;
	private boolean pause;
	public DigitalClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void start(long time, QuestionActivity questionActivity){
		this.time = time;
		this.questionActivity = questionActivity;
		this.pause = false;
		timer = new Timer();
		timer.schedule(new ClockTimer(), 0, 1000);
		
	}
	public void cancel(){
		if(timer != null){
			timer.cancel();
		}
	}
	public void pause(){
		this.pause = true;
	}
	public void resume(){
		this.pause = false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int X = (int) event.getRawX();
		final int Y = (int) event.getRawY();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) this.getLayoutParams();
			_xDelta = X - lParams.leftMargin;
			_yDelta = Y - lParams.topMargin;
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			break;
		case MotionEvent.ACTION_POINTER_UP:
			break;
		case MotionEvent.ACTION_MOVE:
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this
					.getLayoutParams();
			layoutParams.leftMargin = X - _xDelta;
			layoutParams.topMargin = Y - _yDelta;
			layoutParams.rightMargin = -250;
			layoutParams.bottomMargin = -250;
			this.setLayoutParams(layoutParams);
			break;
		}
		getRootView().invalidate();
		return true;
	}
	private static final int HANDLE_UPDATE_CLOCK = 0;
	private Handler mHander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_UPDATE_CLOCK:
				time = time - 1000;
				setText(convertTime(time) +"");
				if(time <= 0){
					timer.cancel();
					questionActivity.submit();
				}
				break;
			default:
				break;
			}
		}	
	};
	private class ClockTimer extends TimerTask{

		@Override
		public void run() {
			if(!pause){
				mHander.obtainMessage(HANDLE_UPDATE_CLOCK).sendToTarget();
			}
			
		}
		
	}
	private String convertTime(long millis){
		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
			    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
			    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
		
		return hms;
	}
}
