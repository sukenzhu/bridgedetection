package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.suken.bridgedetection.BridgeDetectionApplication;

public class CheckFormAndDetailDao {

	private Dao<CheckFormData, String> mFormDao = null;
	private Dao<CheckDetail, String> mDetailDao = null;

	public CheckFormAndDetailDao() {
		try {
			mFormDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(CheckFormData.class);
			mDetailDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(CheckDetail.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean create(List<CheckFormData> list){
		for(CheckFormData formData : list){
			create(formData);
		}
		return true;
	}
	
	public boolean create(CheckFormData formData){
		try {
			CreateOrUpdateStatus status = mFormDao.createOrUpdate(formData);
			return status.isCreated() || status.isUpdated();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public List<CheckFormData> queryByType(int type){
		try {
			return mFormDao.queryForEq("type", type);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CheckFormData> queryByQHId(String id){
		try {
			return mFormDao.queryForEq("qhid", id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
