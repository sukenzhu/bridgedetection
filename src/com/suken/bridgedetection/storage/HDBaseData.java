package com.suken.bridgedetection.storage;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_hdbasedata")
public class HDBaseData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DatabaseField
	private String createBy;
	@DatabaseField
	private String createtime;
	@DatabaseField
	private String creator;
	@DatabaseField
	private int flag;
	@DatabaseField
	private double gpsX;
	@DatabaseField
	private double gpsY;
	@DatabaseField
	private String gydwId;
	@DatabaseField
	private String gydwName;
	@DatabaseField
	private String id;
	@DatabaseField
	private String lxbh;
	@DatabaseField
	private String lxdj;
	@DatabaseField
	private String lxid;
	@DatabaseField
	private String lxmc;
	@DatabaseField
	private int orgid;
	@DatabaseField
	private String hdbh;
	@DatabaseField
	private String hdlx;
	@DatabaseField
	private String hdmc;
	@DatabaseField
	private String hdwz;
	@DatabaseField
	private String zgfzr;
	@DatabaseField
	private String updateBy;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private String updator;
	@DatabaseField
	private String versionno;
	@DatabaseField
	private String zxzh;

	@DatabaseField
	private String userId;

	@DatabaseField(id = true)
	private String localId;

	@DatabaseField
	private int mtimes = 0;

	public int getMtimes() {
		return mtimes;
	}

	public void setMtimes(int mtimes) {
		this.mtimes = mtimes;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		setLocalId(id + userId);
	}

	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
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
	public String getGydwId() {
		return gydwId;
	}
	public void setGydwId(String gydwId) {
		this.gydwId = gydwId;
	}
	public String getGydwName() {
		return gydwName;
	}
	public void setGydwName(String gydwName) {
		this.gydwName = gydwName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLxbh() {
		return lxbh;
	}
	public void setLxbh(String lxbh) {
		this.lxbh = lxbh;
	}
	public String getLxdj() {
		return lxdj;
	}
	public void setLxdj(String lxdj) {
		this.lxdj = lxdj;
	}
	public String getLxid() {
		return lxid;
	}
	public void setLxid(String lxid) {
		this.lxid = lxid;
	}
	public String getLxmc() {
		return lxmc;
	}
	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}
	public int getOrgid() {
		return orgid;
	}
	public void setOrgid(int orgid) {
		this.orgid = orgid;
	}
	public String getHdbh() {
		return hdbh;
	}
	public void setHdbh(String hdbh) {
		this.hdbh = hdbh;
	}
	public String getHdlx() {
		return hdlx;
	}
	public void setHdlx(String hdlx) {
		this.hdlx = hdlx;
	}
	public String getHdmc() {
		return hdmc;
	}
	public void setHdmc(String hdmc) {
		this.hdmc = hdmc;
	}
	public String getHdwz() {
		return hdwz;
	}
	public void setHdwz(String hdwz) {
		this.hdwz = hdwz;
	}
	public String getZgfzr() {
		return zgfzr;
	}
	public void setZgfzr(String zgfzr) {
		this.zgfzr = zgfzr;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public String getVersionno() {
		return versionno;
	}
	public void setVersionno(String versionno) {
		this.versionno = versionno;
	}
	public String getZxzh() {
		return zxzh;
	}
	public void setZxzh(String zxzh) {
		this.zxzh = zxzh;
	}
	@Override
	public String toString() {
		return "HDBaseData [createBy=" + createBy + ", createtime=" + createtime + ", creator=" + creator + ", flag=" + flag + ", gpsX=" + gpsX + ", gpsY=" + gpsY + ", gydwId=" + gydwId
				+ ", gydwName=" + gydwName + ", id=" + id + ", lxbh=" + lxbh + ", lxdj=" + lxdj + ", lxid=" + lxid + ", lxmc=" + lxmc + ", orgid=" + orgid + ", hdbh=" + hdbh + ", hdlx=" + hdlx
				+ ", hdmc=" + hdmc + ", hdwz=" + hdwz + ", zgfzr=" + zgfzr + ", updateBy=" + updateBy + ", updatetime=" + updatetime + ", updator=" + updator + ", versionno=" + versionno + ", zxzh="
				+ zxzh + "]";
	}

}
