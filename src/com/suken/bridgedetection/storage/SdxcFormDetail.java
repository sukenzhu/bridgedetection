package com.suken.bridgedetection.storage;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_sdcheckdetail")
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
	@DatabaseField
	private String id;
	@DatabaseField
	private String picattachment;
	@DatabaseField
	private String picattachmentNames;
	@DatabaseField
	private String updateBy;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private String updator;
	@DatabaseField
	private String vidattachment;
	@DatabaseField
	private String vidattachmentNames;
	@DatabaseField
	private long formId; 
	@DatabaseField
	private String qhzh; 
	@DatabaseField
	private String fx; 
	@DatabaseField
	private String fxbh; 
	@DatabaseField
	private String shwz; 
	@DatabaseField
	private String dx; 
	@DatabaseField
	private String ys; 
	
	@DatabaseField(generatedId = true)
	private long localId;
//	"qhzh": "23.23", //桥梁桩号
//    "fx": "上行", //方向
//    "fxbh": "暂无", //发现病害
//    "shwz": "无", //损害位置
//    "dx": "无", //尺寸大小
//    "ys": "无", //病害原因

	
	public String getCreatetime() {
		return createtime;
	}

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public String getQhzh() {
		return qhzh;
	}

	public void setQhzh(String qhzh) {
		this.qhzh = qhzh;
	}
	public String getFx() {
		return fx;
	}

	public void setFx(String fx) {
		this.fx = fx;
	}

	public String getFxbh() {
		return fxbh;
	}

	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}



	public String getShwz() {
		return shwz;
	}



	public void setShwz(String shwz) {
		this.shwz = shwz;
	}



	public String getDx() {
		return dx;
	}



	public void setDx(String dx) {
		this.dx = dx;
	}



	public String getYs() {
		return ys;
	}



	public void setYs(String ys) {
		this.ys = ys;
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

	public String getPicattachmentNames() {
		return picattachmentNames;
	}

	public void setPicattachmentNames(String picattachmentNames) {
		this.picattachmentNames = picattachmentNames;
	}

	public String getVidattachmentNames() {
		return vidattachmentNames;
	}

	public void setVidattachmentNames(String vidattachmentNames) {
		this.vidattachmentNames = vidattachmentNames;
	}
	
	
}
