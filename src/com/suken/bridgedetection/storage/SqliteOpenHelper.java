package com.suken.bridgedetection.storage;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SqliteOpenHelper extends OrmLiteSqliteOpenHelper {

	private static final String TABLE_NAME = "bridgedetection.db";
	private static SqliteOpenHelper instance;
	private SqliteOpenHelper(Context context) {
		super(context, TABLE_NAME, null, 10);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, UserInfo.class);
			TableUtils.createTable(connectionSource, GXLuXianInfo.class);
			TableUtils.createTable(connectionSource, QLBaseData.class);
			TableUtils.createTable(connectionSource, HDBaseData.class);
			TableUtils.createTable(connectionSource, SDBaseData.class);
			TableUtils.createTable(connectionSource, YWDictionaryInfo.class);
			TableUtils.createTable(connectionSource, QHYangHuZeRenInfo.class);
			TableUtils.createTable(connectionSource, SDYangHuZeRenInfo.class);
			TableUtils.createTable(connectionSource, CheckFormData.class);
			TableUtils.createTable(connectionSource, CheckDetail.class);
			TableUtils.createTable(connectionSource, SdxcFormData.class);
			TableUtils.createTable(connectionSource, SdxcFormDetail.class);
			TableUtils.createTable(connectionSource, GpsData.class);
			TableUtils.createTable(connectionSource, GpsGjData.class);
			TableUtils.createTable(connectionSource, FileDesc.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, UserInfo.class, true);
			TableUtils.dropTable(connectionSource, GXLuXianInfo.class, true);
			TableUtils.dropTable(connectionSource, QLBaseData.class, true);
			TableUtils.dropTable(connectionSource, HDBaseData.class, true);
			TableUtils.dropTable(connectionSource, SDBaseData.class, true);
			TableUtils.dropTable(connectionSource, YWDictionaryInfo.class, true);
			TableUtils.dropTable(connectionSource, QHYangHuZeRenInfo.class, true);
			TableUtils.dropTable(connectionSource, SDYangHuZeRenInfo.class, true);
			TableUtils.dropTable(connectionSource, CheckFormData.class, true);
			TableUtils.dropTable(connectionSource, CheckDetail.class, true);
			TableUtils.dropTable(connectionSource, SdxcFormData.class, true);
			TableUtils.dropTable(connectionSource, SdxcFormDetail.class, true);
			TableUtils.dropTable(connectionSource, GpsData.class, true);
			TableUtils.dropTable(connectionSource, GpsGjData.class, true);
			TableUtils.dropTable(connectionSource, FileDesc.class, true);
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 单例获取该Helper
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized SqliteOpenHelper getHelper(Context context) {
		if (instance == null) {
			synchronized (SqliteOpenHelper.class) {
				if (instance == null)
					instance = new SqliteOpenHelper(context);
			}
		}

		return instance;
	}

	/**
	 * 释放资源
	 */
	@Override
	public void close() {
		super.close();
	}

}
