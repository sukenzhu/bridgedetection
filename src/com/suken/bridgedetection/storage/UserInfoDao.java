package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.suken.bridgedetection.BridgeDetectionApplication;

public class UserInfoDao {

	private Dao<UserInfo, String> mUserInfoDao = null;

	public UserInfoDao() {
		try {
			mUserInfoDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(UserInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void create(UserInfo info) {
		try {
			mUserInfoDao.createOrUpdate(info);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public UserInfo queryWithAccountAndPwd(String account, String pwd) {
		Map<String, Object> arg0 = new HashMap<String, Object>();
		arg0.put("account", account);
		arg0.put("password", pwd);
		try {
			List<UserInfo> list = mUserInfoDao.queryForFieldValues(arg0);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<UserInfo> queryAll() {
		try {
			List<UserInfo> list = mUserInfoDao.queryForAll();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserInfo queryById(String value) {
		try {
			return mUserInfoDao.queryForId(value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
