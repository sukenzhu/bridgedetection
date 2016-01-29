package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.suken.bridgedetection.BridgeDetectionApplication;

public class GXLuXianInfoDao {

	private Dao<GXLuXianInfo, String> mGXLuXianInfoDao = null;

	public GXLuXianInfoDao() {
		try {
			mGXLuXianInfoDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance())
					.getDao(GXLuXianInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void create(List<GXLuXianInfo> list) {
		for (GXLuXianInfo info : list) {
			create(info);
		}
	}

	public void create(GXLuXianInfo info) {
		try {
			mGXLuXianInfoDao.createOrUpdate(info);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<GXLuXianInfo> queryAll() {
		try {
			return mGXLuXianInfoDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public GXLuXianInfo queryById(String value) {
		try {
			return mGXLuXianInfoDao.queryForId(value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
