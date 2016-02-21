package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.suken.bridgedetection.BridgeDetectionApplication;

public class GpsDataDao {

	private Dao<GpsData, String> mGpsDao = null;

	public GpsDataDao() {
		super();
		try {
			mGpsDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(GpsData.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean create(GpsData data) {
		try {
			mGpsDao.create(data);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public GpsData queryGpsData(long qhid, String qhlx){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", qhid);
		map.put("qhlx", qhlx);
		try {
			List<GpsData> list = mGpsDao.queryForFieldValues(map);
			if(list != null && list.size() > 0){
				return list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
