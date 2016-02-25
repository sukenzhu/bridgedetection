package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.suken.bridgedetection.BridgeDetectionApplication;

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
			mGXLuXianInfoDao.createOrUpdate(info);
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
	
	public QHYangHuZeRenInfo queryByQhId(String id){
		try {
			List<QHYangHuZeRenInfo> infos = mGXLuXianInfoDao.queryForEq("qhid", id);
			if(infos != null && infos.size() > 0){
				return infos.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
