package com.suken.bridgedetection.storage;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_gpsgj")
public class GpsGjData {

	@DatabaseField(generatedId = true)
	private long localId;
	@DatabaseField
	private String sjbs;
	@DatabaseField
	private String gpsX;
	@DatabaseField
	private String gpsY;
	@DatabaseField
	private String userId;
	@DatabaseField
	private String gpsZ;
	@DatabaseField
	private String wz;

	public GpsGjData() {
	}

	public GpsGjData(String la, String lo, String  al, String time, String wz) {
		setGpsX(la);
		setGpsY(lo);
		setGpsZ(al);
		setSjbs(time);
		setWz(wz);

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public String getSjbs() {
		return sjbs;
	}

	public void setSjbs(String sjbs) {
		this.sjbs = sjbs;
	}

	public String getWz() {
		return wz;
	}

	public void setWz(String wz) {
		this.wz = wz;
	}

	public String getGpsX() {
		return gpsX;
	}

	public void setGpsX(String gpsX) {
		this.gpsX = gpsX;
	}

	public String getGpsY() {
		return gpsY;
	}

	public void setGpsY(String gpsY) {
		this.gpsY = gpsY;
	}

	public String getGpsZ() {
		return gpsZ;
	}

	public void setGpsZ(String gpsZ) {
		this.gpsZ = gpsZ;
	}
}
