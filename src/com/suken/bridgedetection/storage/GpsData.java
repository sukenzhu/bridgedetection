package com.suken.bridgedetection.storage;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_gps")
public class GpsData {

	@DatabaseField(generatedId = true)
	private long localId;

	@DatabaseField
	private long id;
	@DatabaseField
	private String qhlx;
	@DatabaseField
	private double gpsX;
	@DatabaseField
	private double gpsY;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public String getQhlx() {
		return qhlx;
	}

	public void setQhlx(String qhlx) {
		this.qhlx = qhlx;
	}

	public double getGpsX() {
		return gpsX;
	}

	public void setGpsX(double gpsX) {
		this.gpsX = gpsX;
	}

	public double getGpsY() {
		return gpsY;
	}

	public void setGpsY(double gpsY) {
		this.gpsY = gpsY;
	}

}
