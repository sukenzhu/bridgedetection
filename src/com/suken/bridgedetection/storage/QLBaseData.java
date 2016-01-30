package com.suken.bridgedetection.storage;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_qlbasedata")
public class QLBaseData implements Serializable{
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
	private String qlbh;
	@DatabaseField
	private String qljsdj;
	@DatabaseField
	private String qlmc;
	@DatabaseField
	private String qlwz;
	@DatabaseField
	private String qlxz;
	@DatabaseField
	private String qlzgfzr;
	@DatabaseField
	private String updateBy;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private String updator;
	@DatabaseField
	private String versionno;
	@DatabaseField
	private String ytfl;
	@DatabaseField
	private String zxzh;

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

	public String getQlbh() {
		return qlbh;
	}

	public void setQlbh(String qlbh) {
		this.qlbh = qlbh;
	}

	public String getQljsdj() {
		return qljsdj;
	}

	public void setQljsdj(String qljsdj) {
		this.qljsdj = qljsdj;
	}

	public String getQlmc() {
		return qlmc;
	}

	public void setQlmc(String qlmc) {
		this.qlmc = qlmc;
	}

	public String getQlwz() {
		return qlwz;
	}

	public void setQlwz(String qlwz) {
		this.qlwz = qlwz;
	}

	public String getQlxz() {
		return qlxz;
	}

	public void setQlxz(String qlxz) {
		this.qlxz = qlxz;
	}

	public String getQlzgfzr() {
		return qlzgfzr;
	}

	public void setQlzgfzr(String qlzgfzr) {
		this.qlzgfzr = qlzgfzr;
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

	public String getYtfl() {
		return ytfl;
	}

	public void setYtfl(String ytfl) {
		this.ytfl = ytfl;
	}

	public String getZxzh() {
		return zxzh;
	}

	public void setZxzh(String zxzh) {
		this.zxzh = zxzh;
	}

	@Override
	public String toString() {
		return "QLBaseData [createBy=" + createBy + ", createtime=" + createtime + ", creator=" + creator + ", flag=" + flag + ", gpsX=" + gpsX + ", gpsY=" + gpsY + ", gydwId=" + gydwId
				+ ", gydwName=" + gydwName + ", id=" + id + ", lxbh=" + lxbh + ", lxdj=" + lxdj + ", lxid=" + lxid + ", lxmc=" + lxmc + ", orgid=" + orgid + ", qlbh=" + qlbh + ", qljsdj=" + qljsdj
				+ ", qlmc=" + qlmc + ", qlwz=" + qlwz + ", qlxz=" + qlxz + ", qlzgfzr=" + qlzgfzr + ", updateBy=" + updateBy + ", updatetime=" + updatetime + ", updator=" + updator + ", versionno="
				+ versionno + ", ytfl=" + ytfl + ", zxzh=" + zxzh + "]";
	}

}
