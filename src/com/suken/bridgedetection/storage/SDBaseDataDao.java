package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.Constants;

import android.util.Log;

public class SDBaseDataDao {

	private Dao<SDBaseData, String> mGXLuXianInfoDao = null;

	public SDBaseDataDao() {
		try {
			mGXLuXianInfoDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance())
					.getDao(SDBaseData.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void create(List<SDBaseData> list) {
		for (SDBaseData info : list) {
			create(info);
		}
	}

	public void create(SDBaseData info) {
		try {
			CreateOrUpdateStatus status = mGXLuXianInfoDao.createOrUpdate(info);
			Log.i(Constants.SQLTAG, " status : " + (status.isCreated() || status.isUpdated()) + " info : " + info.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<SDBaseData> queryAll() {
		try {
			return mGXLuXianInfoDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public SDBaseData queryById(String value) {
		try {
			return mGXLuXianInfoDao.queryForId(value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
