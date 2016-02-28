package com.suken.bridgedetection.storage;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_filedesc")
public class FileDesc {

	@DatabaseField(id = true)
	public String fileId;
	@DatabaseField
	public String fileName;

}
