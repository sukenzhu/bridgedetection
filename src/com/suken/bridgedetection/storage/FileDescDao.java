package com.suken.bridgedetection.storage;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.suken.bridgedetection.BridgeDetectionApplication;

public class FileDescDao {

	private Dao<FileDesc, String> mFileDescDao = null;

	public FileDescDao() {
		super();
		try {
			mFileDescDao = SqliteOpenHelper.getHelper(BridgeDetectionApplication.getInstance()).getDao(FileDesc.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void create(FileDesc desc) {
		try {
			mFileDescDao.createOrUpdate(desc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void create(List<FileDesc> files) {
		for (FileDesc desc : files) {
			create(desc);
		}
	}

}
