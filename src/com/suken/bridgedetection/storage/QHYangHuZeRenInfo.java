package com.suken.bridgedetection.storage;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_qhyhzrInfo")
public class QHYangHuZeRenInfo {

	@DatabaseField
	private String id;
	@DatabaseField
	private String orgid;
	@DatabaseField
	private String versionno;
	@DatabaseField
	private String createBy;
	@DatabaseField
	private String creator;
	@DatabaseField
	private String createtime;
	@DatabaseField
	private String updateby;
	@DatabaseField
	private String updator;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private int flag;
	@DatabaseField
	private String lxid;
	@DatabaseField
	private String lxbm;
	@DatabaseField
	private String lxmc;
	@DatabaseField
	private String qhid;
	@DatabaseField
	private String qhbm;
	@DatabaseField
	private String qhmc;
	@DatabaseField
	private double zh;
	@DatabaseField
	private double cdm;
	@DatabaseField
	private String kjks;
	@DatabaseField
	private String jglx;
	@DatabaseField
	private String yhgcsid;
	@DatabaseField
	private String yhgcs;
	@DatabaseField
	private String jsfz;
	@DatabaseField
	private String xzfz;
	@DatabaseField
	private String lxdh;
	@DatabaseField
	private String imptime;
	@DatabaseField
	private String gydwid;
	@DatabaseField
	private String gydwname;

	@DatabaseField
	private String userId;

	@DatabaseField(id = true)
	private String localId;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getVersionno() {
		return versionno;
	}

	public void setVersionno(String versionno) {
		this.versionno = versionno;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getLxid() {
		return lxid;
	}

	public void setLxid(String lxid) {
		this.lxid = lxid;
	}

	public String getLxbm() {
		return lxbm;
	}

	public void setLxbm(String lxbm) {
		this.lxbm = lxbm;
	}

	public String getLxmc() {
		return lxmc;
	}

	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}

	public String getQhid() {
		return qhid;
	}

	public void setQhid(String qhid) {
		this.qhid = qhid;
	}

	public String getQhbm() {
		return qhbm;
	}

	public void setQhbm(String qhbm) {
		this.qhbm = qhbm;
	}

	public String getQhmc() {
		return qhmc;
	}

	public void setQhmc(String qhmc) {
		this.qhmc = qhmc;
	}

	public double getZh() {
		return zh;
	}

	public void setZh(double zh) {
		this.zh = zh;
	}

	public double getCdm() {
		return cdm;
	}

	public void setCdm(double cdm) {
		this.cdm = cdm;
	}

	public String getKjks() {
		return kjks;
	}

	public void setKjks(String kjks) {
		this.kjks = kjks;
	}

	public String getJglx() {
		return jglx;
	}

	public void setJglx(String jglx) {
		this.jglx = jglx;
	}

	public String getYhgcsid() {
		return yhgcsid;
	}

	public void setYhgcsid(String yhgcsid) {
		this.yhgcsid = yhgcsid;
	}

	public String getYhgcs() {
		return yhgcs;
	}

	public void setYhgcs(String yhgcs) {
		this.yhgcs = yhgcs;
	}

	public String getJsfz() {
		return jsfz;
	}

	public void setJsfz(String jsfz) {
		this.jsfz = jsfz;
	}

	public String getXzfz() {
		return xzfz;
	}

	public void setXzfz(String xzfz) {
		this.xzfz = xzfz;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getImptime() {
		return imptime;
	}

	public void setImptime(String imptime) {
		this.imptime = imptime;
	}

	public String getGydwid() {
		return gydwid;
	}

	public void setGydwid(String gydwid) {
		this.gydwid = gydwid;
	}

	public String getGydwname() {
		return gydwname;
	}

	public void setGydwname(String gydwname) {
		this.gydwname = gydwname;
	}

	@Override
	public String toString() {
		return "QHYangHuZeRenInfo [id=" + id + ", orgid=" + orgid + ", versionno=" + versionno + ", createBy=" + createBy + ", creator=" + creator + ", createtime=" + createtime + ", updateby="
				+ updateby + ", updator=" + updator + ", updatetime=" + updatetime + ", flag=" + flag + ", lxid=" + lxid + ", lxbm=" + lxbm + ", lxmc=" + lxmc + ", qhid=" + qhid + ", qhbm=" + qhbm
				+ ", qhmc=" + qhmc + ", zh=" + zh + ", cdm=" + cdm + ", kjks=" + kjks + ", jglx=" + jglx + ", yhgcsid=" + yhgcsid + ", yhgcs=" + yhgcs + ", jsfz=" + jsfz + ", xzfz=" + xzfz + ", lxdh="
				+ lxdh + ", imptime=" + imptime + ", gydwid=" + gydwid + ", gydwname=" + gydwname + "]";
	}

}
