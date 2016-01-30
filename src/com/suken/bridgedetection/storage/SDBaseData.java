package com.suken.bridgedetection.storage;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_sdbasedata")
public class SDBaseData {
	@DatabaseField
	private String cdfl;
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
	@DatabaseField(id = true)
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
	private String sdbh;
	@DatabaseField
	private String sdmc;
	@DatabaseField
	private String sdwz;
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

	public String getCdfl() {
		return cdfl;
	}

	public void setCdfl(String cdfl) {
		this.cdfl = cdfl;
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

	public String getSdbh() {
		return sdbh;
	}

	public void setSdbh(String sdbh) {
		this.sdbh = sdbh;
	}

	public String getSdmc() {
		return sdmc;
	}

	public void setSdmc(String sdmc) {
		this.sdmc = sdmc;
	}

	public String getSdwz() {
		return sdwz;
	}

	public void setSdwz(String sdwz) {
		this.sdwz = sdwz;
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
		return "SDBaseData [cdfl=" + cdfl + ", createBy=" + createBy + ", createtime=" + createtime + ", creator=" + creator + ", flag=" + flag + ", gpsX=" + gpsX + ", gpsY=" + gpsY + ", gydwId="
				+ gydwId + ", gydwName=" + gydwName + ", id=" + id + ", lxbh=" + lxbh + ", lxdj=" + lxdj + ", lxid=" + lxid + ", lxmc=" + lxmc + ", orgid=" + orgid + ", sdbh=" + sdbh + ", sdmc="
				+ sdmc + ", sdwz=" + sdwz + ", zgfzr=" + zgfzr + ", updateBy=" + updateBy + ", updatetime=" + updatetime + ", updator=" + updator + ", versionno=" + versionno + ", zxzh=" + zxzh + "]";
	}
}
