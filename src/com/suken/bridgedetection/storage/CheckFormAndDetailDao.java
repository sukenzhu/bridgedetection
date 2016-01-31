package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			if(formData.getOfenCheckDetailList() != null){
				createDetails(formData.getOfenCheckDetailList());
			}
			return status.isCreated() || status.isUpdated();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public void createDetails(List<CheckDetail> details){
		for(CheckDetail formData : details){
			create(formData);
		}
	}
	
	public boolean create(CheckDetail detail){

		try {
			CreateOrUpdateStatus status = mDetailDao.createOrUpdate(detail);
			return status.isCreated() || status.isUpdated();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	
	}
	
	public List<CheckFormData> queryByType(int type){
		try {
			List<CheckFormData> datas =  mFormDao.queryForEq("type", type);
			for(CheckFormData data : datas){
				data.setOfenCheckDetailList(queryByFormId(data.getId()));
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CheckDetail> queryByFormId(long id){
		try {
			return mDetailDao.queryForEq("formId", id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CheckFormData> queryAll(){
		try {
			List<CheckFormData> datas =  mFormDao.queryForAll();
			for(CheckFormData data : datas){
				data.setOfenCheckDetailList(queryByFormId(data.getId()));
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CheckFormData> queryByQHId(String id){
		try {
			List<CheckFormData> datas =  mFormDao.queryForEq("qhid", id);
			for(CheckFormData data : datas){
				data.setOfenCheckDetailList(queryByFormId(data.getId()));
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CheckFormData queryByQHIdAndStatus(String id, String status){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("qhid", id);
			map.put("status", status);
			List<CheckFormData> datas = mFormDao.queryForFieldValues(map);
			if(datas != null && datas.size() > 0){
				CheckFormData data =  datas.get(0);
				data.setOfenCheckDetailList(queryByFormId(data.getId()));
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
