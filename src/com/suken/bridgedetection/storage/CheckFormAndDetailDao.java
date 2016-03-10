package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.util.UiUtil;

public class CheckFormAndDetailDao {

	private Dao<CheckFormData, Long> mFormDao = null;
	private Dao<CheckDetail, Long> mDetailDao = null;

	public CheckFormAndDetailDao() {
		try {
			mFormDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(CheckFormData.class);
			mDetailDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(CheckDetail.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean create(List<CheckFormData> list) {
		for (CheckFormData formData : list) {
			create(formData);
		}
		return true;
	}

	public boolean create(CheckFormData formData) {
		try {
			formData.setUserId(BridgeDetectionApplication.mCurrentUser.getUserId());
			CreateOrUpdateStatus status = mFormDao.createOrUpdate(formData);
			if (formData.getOftenCheckDetailList() != null) {
				createDetails(formData.getOftenCheckDetailList());
			}
			return status.isCreated() || status.isUpdated();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void createDetails(List<CheckDetail> details) {
		for (CheckDetail formData : details) {
			create(formData);
		}
	}

	public boolean create(CheckDetail detail) {

		try {
			CreateOrUpdateStatus status = mDetailDao.createOrUpdate(detail);
			return status.isCreated() || status.isUpdated();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public List<CheckDetail> queryByFormId(long id) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("formId", id);
			return mDetailDao.queryForFieldValues(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CheckFormData> queryByQHId(String id, int type) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if(type == R.drawable.suidaojiancha){
				map.put("sdid", id);
			} else {
				map.put("qhid", id);
			}
			map.put("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
			map.put("type", type);
			map.put("lastUpdate", false);
			List<CheckFormData> datas = mFormDao.queryForFieldValues(map);
			for (CheckFormData data : datas) {
				data.setOftenCheckDetailList(queryByFormId(data.getLocalId()));
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public CheckFormData queryByQHIdAndType(String id, int type) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if(type == R.drawable.suidaojiancha){
				map.put("sdid", id);
			} else {
				map.put("qhid", id);
			}
			map.put("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
			map.put("type", type);
			map.put("lastUpdate", false);
			List<CheckFormData> datas = mFormDao.queryForFieldValues(map);
			if (datas != null && datas.size() > 0) {
				CheckFormData data = datas.get(0);
				data.setOftenCheckDetailList(queryByFormId(data.getLocalId()));
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	public CheckFormData queryByQHIdAndStatus(String id, String status, int type) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if(type == R.drawable.suidaojiancha){
				map.put("sdid", id);
			} else {
				map.put("qhid", id);
			}
			map.put("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
			map.put("status", status);
			map.put("type", type);
			map.put("lastUpdate", false);
			List<CheckFormData> datas = mFormDao.queryForFieldValues(map);
			if (datas != null && datas.size() > 0) {
				CheckFormData data = datas.get(0);
				data.setOftenCheckDetailList(queryByFormId(data.getLocalId()));
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean deleteByQhId(String id, int type) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(type == R.drawable.suidaojiancha){
			map.put("sdid", id);
		} else {
			map.put("qhid", id);
		}
		map.put("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
		map.put("type", type);
		map.put("lastUpdate", false);
		try {
			List<CheckFormData> list = mFormDao.queryForFieldValues(map);
			deleteCheckFormList(list);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteCheckFormList(List<CheckFormData> list) {
		if (list != null && list.size() > 0) {
			for (CheckFormData data : list) {
				deleteChecFormDataById(data);
			}
		}
		return true;
	}
	
	public int countTypeAndStatus(int type, String status){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("status", status);
		map.put("lastUpdate", false);
		map.put("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
		try {
			List<CheckFormData> list = mFormDao.queryForFieldValues(map);
			if(list != null){
				return list.size();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean deleteLastUpdateByType(int type) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", type);
			map.put("lastUpdate", true);
			map.put("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
			List<CheckFormData> list = mFormDao.queryForFieldValues(map);
			if (list != null && list.size() > 0) {
				for (CheckFormData data : list) {
					deleteChecFormDataById(data);
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteChecFormDataById(CheckFormData data) {
		try {
			mFormDao.delete(data);
			List<CheckDetail> list = mDetailDao.queryForEq("formId", data.getLocalId());
			if (list != null && list.size() > 0) {
				for (CheckDetail detail : list) {
					mDetailDao.delete(detail);
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public CheckFormData queryByLocalId(long localId){
		CheckFormData formData;
		try {
			formData = mFormDao.queryForId(localId);
			formData.setOftenCheckDetailList(queryByFormId(formData.getLocalId()));
			return formData;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CheckFormData queryLastUpdateByTypeAndId(String id, int type){

		Map<String, Object> map = new HashMap<String, Object>();
		if(type == R.drawable.qiaoliangjiancha){
			map.put("qhid", id);
		} else {
			map.put("sdid", id);
		}
		map.put("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
		map.put("type", type);
		map.put("lastUpdate", true);
		try {
			List<CheckFormData> list = mFormDao.queryForFieldValues(map);
			if(list != null && list.size() > 0){
				CheckFormData data = list.get(0);
				data.setOftenCheckDetailList(queryByFormId(data.getLocalId()));
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	
	}
	
	
	public boolean deleteAllLocalData(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lastUpdate", false);
		map.put("status", "2");
		map.put("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
		try {
			List<CheckFormData> list = mFormDao.queryForFieldValues(map);
			boolean isDelete = false;
			if(list != null){
				for(CheckFormData data : list){
					if(!UiUtil.checkValid(data.getSaveTime())){
						deleteChecFormDataById(data);
						isDelete = true;
					}
				}
				return isDelete;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	

}
