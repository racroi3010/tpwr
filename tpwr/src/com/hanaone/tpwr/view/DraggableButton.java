package com.hanaone.tpwr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DraggableButton extends Button {
	private int _xDelta;
	private int _yDelta;		
	public DraggableButton(Context context, AttributeSet attrs) {
		super(context, attrs);
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

}
