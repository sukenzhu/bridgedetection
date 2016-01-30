package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.Constants;

import android.util.Log;

public class QHYHZeRenInfoDao {

	private Dao<QHYangHuZeRenInfo, String> mGXLuXianInfoDao = null;

	public QHYHZeRenInfoDao() {
		try {
			mGXLuXianInfoDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(QHYangHuZeRenInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void create(List<QHYangHuZeRenInfo> list) {
		for (QHYangHuZeRenInfo info : list) {
			create(info);
		}
	}

	public void create(QHYangHuZeRenInfo info) {
		try {
			CreateOrUpdateStatus status = mGXLuXianInfoDao.createOrUpdate(info);
			Log.i(Constants.SQLTAG, " status : " + (status.isCreated() || status.isUpdated()) + " info : " + info.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<QHYangHuZeRenInfo> queryAll() {
		try {
			return mGXLuXianInfoDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public QHYangHuZeRenInfo queryById(String value) {
		try {
			return mGXLuXianInfoDao.queryForId(value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
