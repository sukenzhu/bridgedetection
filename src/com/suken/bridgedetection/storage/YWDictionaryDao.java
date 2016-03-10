package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.suken.bridgedetection.BridgeDetectionApplication;

public class YWDictionaryDao {

	private Dao<YWDictionaryInfo, String> mGXLuXianInfoDao = null;

	public YWDictionaryDao() {
		try {
			mGXLuXianInfoDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance())
					.getDao(YWDictionaryInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void create(List<YWDictionaryInfo> list) {
		for (YWDictionaryInfo info : list) {
			create(info);
		}
	}

	public void create(YWDictionaryInfo info) {
		try {
			mGXLuXianInfoDao.createOrUpdate(info);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<YWDictionaryInfo> queryAll() {
		try {
			return mGXLuXianInfoDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public YWDictionaryInfo queryById(String value) {
		try {
			return mGXLuXianInfoDao.queryForId(value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<YWDictionaryInfo> queryByTypeId(String typeId){
		try {
			List<YWDictionaryInfo> infos = mGXLuXianInfoDao.queryBuilder().where().eq("typeId", typeId).query();
			return infos;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
