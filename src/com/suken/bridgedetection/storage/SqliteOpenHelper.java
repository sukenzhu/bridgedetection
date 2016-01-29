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
		super(context, TABLE_NAME, null, 3);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, UserInfo.class);
			TableUtils.createTable(connectionSource, GXLuXianInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, UserInfo.class, true);
			TableUtils.dropTable(connectionSource, GXLuXianInfo.class, true);
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
