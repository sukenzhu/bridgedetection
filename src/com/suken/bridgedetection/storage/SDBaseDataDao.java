package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.suken.bridgedetection.BridgeDetectionApplication;

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
			mGXLuXianInfoDao.createOrUpdate(info);
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
	
	public int countAll(){
		List<SDBaseData> list = queryAll();
		if(list != null){
			return list.size();
		}
		return 0;
	}

}
