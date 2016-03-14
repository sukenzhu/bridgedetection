package com.suken.bridgedetection.storage;

import com.j256.ormlite.dao.Dao;
import com.suken.bridgedetection.BridgeDetectionApplication;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GpsGjDataDao {

    private Dao<GpsGjData, String> mGpsDao = null;

    public GpsGjDataDao() {
        super();
        try {
            mGpsDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(GpsGjData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean create(GpsGjData data) {
        try {
            data.setUserId(BridgeDetectionApplication.mCurrentUser.getUserId());
            mGpsDao.create(data);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public int countQueryGpsData() {
        try {
            return (int) mGpsDao.queryBuilder().where().eq("userId", BridgeDetectionApplication.mCurrentUser.getUserId()).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<GpsGjData> queryGpsData() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
        try {
            List<GpsGjData> list = mGpsDao.queryForFieldValues(map);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean  deleteAll() {
        try {
            List<GpsGjData> list = queryGpsData();
            if (list != null) {
                for (GpsGjData data : list) {
                    mGpsDao.delete(data);
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
