package com.suken.bridgedetection.storage;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_gxluxian")
public class GXLuXianInfo implements Serializable {

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
	private String gydwId;
	@DatabaseField
	private String gydwName;
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String lxbh;
	@DatabaseField
	private String lxmc;
	@DatabaseField
	private String qdmc;
	@DatabaseField
	private int qdzh;
	@DatabaseField
	private String updateBy;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private String updator;
	@DatabaseField
	private String versionno;
	@DatabaseField
	private String xzdj;
	@DatabaseField
	private String xzdjmc;
	@DatabaseField
	private String zdmc;
	@DatabaseField
	private String zdzh;

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

	public String getLxmc() {
		return lxmc;
	}

	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}

	public String getQdmc() {
		return qdmc;
	}

	public void setQdmc(String qdmc) {
		this.qdmc = qdmc;
	}

	public int getQdzh() {
		return qdzh;
	}

	public void setQdzh(int qdzh) {
		this.qdzh = qdzh;
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

	public String getXzdj() {
		return xzdj;
	}

	public void setXzdj(String xzdj) {
		this.xzdj = xzdj;
	}

	public String getXzdjmc() {
		return xzdjmc;
	}

	public void setXzdjmc(String xzdjmc) {
		this.xzdjmc = xzdjmc;
	}

	public String getZdmc() {
		return zdmc;
	}

	public void setZdmc(String zdmc) {
		this.zdmc = zdmc;
	}

	public String getZdzh() {
		return zdzh;
	}

	public void setZdzh(String zdzh) {
		this.zdzh = zdzh;
	}

	@Override
	public String toString() {
		return "GXLuXianInfo [createBy=" + createBy + ", createtime=" + createtime + ", creator=" + creator + ", flag=" + flag + ", gydwId=" + gydwId + ", gydwName=" + gydwName + ", id=" + id
				+ ", lxbh=" + lxbh + ", lxmc=" + lxmc + ", qdmc=" + qdmc + ", qdzh=" + qdzh + ", updateBy=" + updateBy + ", updatetime=" + updatetime + ", updator=" + updator + ", versionno="
				+ versionno + ", xzdj=" + xzdj + ", xzdjmc=" + xzdjmc + ", zdmc=" + zdmc + ", zdzh=" + zdzh + "]";
	}
	
}
