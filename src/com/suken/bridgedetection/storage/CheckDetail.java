package com.suken.bridgedetection.storage;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_checkdetail")
public class CheckDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	@DatabaseField
	private String bjmc;
	@DatabaseField
	private String bycsyj;
	@DatabaseField
	private String bycsyjblank;
	@DatabaseField
	private String createBy;
	@DatabaseField
	private String createtime;
	@DatabaseField
	private String creator;
	@DatabaseField
	private int flag;
	@DatabaseField(id = true)
	private String id;
	// "bjmc": "翼墙", //部件名称
	// "blcsyj": "继续观察", //保养措施意见
	// "blcsyjblank": "", //保养措施意思 提示文本 不存储后台
	// "createBy": 10000000050039,
	// "createtime": {},
	// "creator": "李兵",
	// "flag": 0,
	// "id": 10000002170021,
	@DatabaseField
	private String orgid;
	@DatabaseField
	private String picattachment;
	@DatabaseField
	private String qhId;
	@DatabaseField
	private String qkms;
	@DatabaseField
	private String qkmsblank;
	@DatabaseField
	private String qsfw;
	@DatabaseField
	private String qsfwblank;
	@DatabaseField
	private String qslx;
	@DatabaseField
	private String qslxblank;
	@DatabaseField
	private String remark;
	@DatabaseField
	private String remarkblank;
	@DatabaseField
	private String updateBy;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private String updator;
	@DatabaseField
	private String versionno;
	@DatabaseField
	private String vidattachment;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private CheckFormData formData;

	public String getBjmc() {
		return bjmc;
	}

	public void setBjmc(String bjmc) {
		this.bjmc = bjmc;
	}

	public String getBycsyj() {
		return bycsyj;
	}

	public void setBycsyj(String bycsyj) {
		this.bycsyj = bycsyj;
	}

	public String getBycsyjblank() {
		return bycsyjblank;
	}

	public void setBycsyjblank(String bycsyjblank) {
		this.bycsyjblank = bycsyjblank;
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

	public String getPicattachment() {
		return picattachment;
	}

	public void setPicattachment(String picattachment) {
		this.picattachment = picattachment;
	}

	public String getQhId() {
		return qhId;
	}

	public void setQhId(String qhId) {
		this.qhId = qhId;
	}

	public String getQkms() {
		return qkms;
	}

	public void setQkms(String qkms) {
		this.qkms = qkms;
	}

	public String getQkmsblank() {
		return qkmsblank;
	}

	public void setQkmsblank(String qkmsblank) {
		this.qkmsblank = qkmsblank;
	}

	public String getQsfw() {
		return qsfw;
	}

	public void setQsfw(String qsfw) {
		this.qsfw = qsfw;
	}

	public String getQsfwblank() {
		return qsfwblank;
	}

	public void setQsfwblank(String qsfwblank) {
		this.qsfwblank = qsfwblank;
	}

	public String getQslx() {
		return qslx;
	}

	public void setQslx(String qslx) {
		this.qslx = qslx;
	}

	public String getQslxblank() {
		return qslxblank;
	}

	public void setQslxblank(String qslxblank) {
		this.qslxblank = qslxblank;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarkblank() {
		return remarkblank;
	}

	public void setRemarkblank(String remarkblank) {
		this.remarkblank = remarkblank;
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

	public String getVidattachment() {
		return vidattachment;
	}

	public void setVidattachment(String vidattachment) {
		this.vidattachment = vidattachment;
	}

	public CheckFormData getFormData() {
		return formData;
	}

	public void setFormData(CheckFormData formData) {
		this.formData = formData;
	}

	@Override
	public String toString() {
		return "CheckDetail [bjmc=" + bjmc + ", bycsyj=" + bycsyj + ", bycsyjblank=" + bycsyjblank + ", createBy=" + createBy + ", createtime=" + createtime + ", creator=" + creator + ", flag=" + flag
				+ ", id=" + id + ", orgid=" + orgid + ", picattachment=" + picattachment + ", qhId=" + qhId + ", qkms=" + qkms + ", qkmsblank=" + qkmsblank + ", qsfw=" + qsfw + ", qsfwblank="
				+ qsfwblank + ", qslx=" + qslx + ", qslxblank=" + qslxblank + ", remark=" + remark + ", remarkblank=" + remarkblank + ", updateBy=" + updateBy + ", updatetime=" + updatetime
				+ ", updator=" + updator + ", versionno=" + versionno + ", vidattachment=" + vidattachment + ", formData=" + formData + "]";
	}

}
