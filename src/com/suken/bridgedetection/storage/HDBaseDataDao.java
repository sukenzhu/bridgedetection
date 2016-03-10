package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.suken.bridgedetection.BridgeDetectionApplication;

public class HDBaseDataDao {

    private Dao<HDBaseData, String> mGXLuXianInfoDao = null;

    public HDBaseDataDao() {
        try {
            mGXLuXianInfoDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(HDBaseData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countAll() {
        List<HDBaseData> list = queryAll();
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public void create(List<HDBaseData> list) {
        for (HDBaseData info : list) {
            create(info);
        }
    }

    public void create(HDBaseData info) {
        try {
            info.setUserId(BridgeDetectionApplication.mCurrentUser.getUserId());
            mGXLuXianInfoDao.createOrUpdate(info);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HDBaseData> queryAll() {
        try {
            return mGXLuXianInfoDao.queryBuilder().orderBy("zxzh", true).where().eq("userId", BridgeDetectionApplication.mCurrentUser.getUserId()).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HDBaseData queryById(String value) {
        try {
            return mGXLuXianInfoDao.queryForId(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
