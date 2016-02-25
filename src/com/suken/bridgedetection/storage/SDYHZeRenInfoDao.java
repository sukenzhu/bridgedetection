package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.suken.bridgedetection.BridgeDetectionApplication;

public class SDYHZeRenInfoDao {

	private Dao<SDYangHuZeRenInfo, String> mGXLuXianInfoDao = null;

	public SDYHZeRenInfoDao() {
		try {
			mGXLuXianInfoDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance())
					.getDao(SDYangHuZeRenInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void create(List<SDYangHuZeRenInfo> list) {
		for (SDYangHuZeRenInfo info : list) {
			create(info);
		}
	}

	public void create(SDYangHuZeRenInfo info) {
		try {
			mGXLuXianInfoDao.createOrUpdate(info);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<SDYangHuZeRenInfo> queryAll() {
		try {
			return mGXLuXianInfoDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public SDYangHuZeRenInfo queryById(String value) {
		try {
			return mGXLuXianInfoDao.queryForId(value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
