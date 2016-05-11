package com.hanaone.tpwr.adapter;

import com.hanaone.tpwr.db.LevelDataSet;

public interface ListLevelListener {
	public void onSelect(int examLevelId, String examLevelName);
	public void onSelect(LevelDataSet level, DownloadInfo info);
}
