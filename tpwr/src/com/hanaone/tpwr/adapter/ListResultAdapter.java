package com.hanaone.tpwr.adapter;

import java.util.List;

import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.QuestionActivity;
import com.hanaone.tpwr.R;
import com.hanaone.tpwr.db.ResultDataSet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ListResultAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ListAdapterListener mListener;
	private List<ResultDataSet> dataSets;
	public ListResultAdapter(Context mContext, ListAdapterListener mListener) {
		super();
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = LayoutInflater.from(mContext);		
	}

	public void setDataSets(List<ResultDataSet> dataSets) {
		this.dataSets = dataSets;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(dataSets != null){
			return dataSets.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(dataSets != null && dataSets.size() > position){
			return dataSets.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.layout_result, parent, false);
			
			holder = new ViewHolder();
			holder.txtNumber = (TextView) convertView.findViewById(R.id.txt_result_number);
			holder.txtGoal = (TextView) convertView.findViewById(R.id.txt_result_goal);
			holder.txtYour = (TextView) convertView.findViewById(R.id.txt_result_your);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final ResultDataSet data = dataSets.get(position);
		if(data != null){
			
			holder.txtNumber.setText(data.getNumber() + "-" + data.getLabel());
			String goal = "";
			if(data.getGoalMin() > 0 && data.getGoalMax() > 0){
				goal = data.getGoalMin() + "~" + data.getGoalMax();
			}
			holder.txtGoal.setText(goal);
			
			holder.txtYour.setText(data.getFinish() + "");
		
		}
		
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView txtNumber;
		TextView txtGoal;
		TextView txtYour;
	}

}
