package com.suken.bridgedetection.storage;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_checkdetail")
public class SdxcFormDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	@DatabaseField
	private String jcnr;
	@DatabaseField
	private String qkms;
	@DatabaseField
	private String dealwith;
	@DatabaseField
	private String jcsj;
	@DatabaseField
	private String createtime;
	@DatabaseField
	private String creator;
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String picattachment;
	private String updateBy;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private String updator;
	@DatabaseField
	private String vidattachment;
	@DatabaseField
	private long formId;


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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPicattachment() {
		return picattachment;
	}

	public void setPicattachment(String picattachment) {
		this.picattachment = picattachment;
	}

	public String getQkms() {
		return qkms;
	}

	public void setQkms(String qkms) {
		this.qkms = qkms;
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

	public String getVidattachment() {
		return vidattachment;
	}

	public void setVidattachment(String vidattachment) {
		this.vidattachment = vidattachment;
	}

	public long getFormId() {
		return formId;
	}

	public void setFormId(long formId) {
		this.formId = formId;
	}

	public String getJcnr() {
		return jcnr;
	}

	public void setJcnr(String jcnr) {
		this.jcnr = jcnr;
	}

	public String getDealwith() {
		return dealwith;
	}

	public void setDealwith(String dealwith) {
		this.dealwith = dealwith;
	}

	public String getJcsj() {
		return jcsj;
	}

	public void setJcsj(String jcsj) {
		this.jcsj = jcsj;
	}

}
