package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.suken.bridgedetection.BridgeDetectionApplication;

public class SdxcFormAndDetailDao {

	private Dao<SdxcFormData, String> mFormDao = null;
	private Dao<SdxcFormDetail, String> mDetailDao = null;

	public SdxcFormAndDetailDao() {
		try {
			mFormDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(SdxcFormData.class);
			mDetailDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(SdxcFormDetail.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean create(List<SdxcFormData> list){
		for(SdxcFormData formData : list){
			create(formData);
		}
		return true;
	}
	
	public boolean create(SdxcFormData formData){
		try {
			CreateOrUpdateStatus status = mFormDao.createOrUpdate(formData);
			if(formData.getInspectlogDetailList() != null){
				createDetails(formData.getInspectlogDetailList());
			}
			return status.isCreated() || status.isUpdated();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public void createDetails(List<SdxcFormDetail> details){
		for(SdxcFormDetail formData : details){
			create(formData);
		}
	}
	
	public boolean create(SdxcFormDetail detail){

		try {
			CreateOrUpdateStatus status = mDetailDao.createOrUpdate(detail);
			return status.isCreated() || status.isUpdated();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	
	}
	
	public List<SdxcFormData> queryByType(int type){
		try {
			List<SdxcFormData> datas =  mFormDao.queryForEq("type", type);
			for(SdxcFormData data : datas){
				data.setInspectlogDetailList(queryByFormId(data.getId(), type));
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<SdxcFormDetail> queryByFormId(long id, int type){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("formId", id);
			map.put("type", type);
			List<SdxcFormDetail> datas = mDetailDao.queryForFieldValues(map);
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<SdxcFormData> queryAll(int type){
		try {
			List<SdxcFormData> datas =  mFormDao.queryForAll();
			for(SdxcFormData data : datas){
				data.setInspectlogDetailList(queryByFormId(data.getId(), type));
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<SdxcFormData> queryByQHId(String id, int type){
		try {
			List<SdxcFormData> datas =  mFormDao.queryForEq("qhid", id);
			for(SdxcFormData data : datas){
				data.setInspectlogDetailList(queryByFormId(data.getId(), type));
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public SdxcFormData queryByQHIdAndStatus(String id, String status, int type){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("qhid", id);
			map.put("status", status);
			map.put("type", type);
			List<SdxcFormData> datas = mFormDao.queryForFieldValues(map);
			if(datas != null && datas.size() > 0){
				SdxcFormData data =  datas.get(0);
				data.setInspectlogDetailList(queryByFormId(data.getId(), type));
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
