package com.suken.bridgedetection.storage;

import java.io.Serializable;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_sdcheckform")
public class SdxcFormData implements Serializable {
	private static final long serialVersionUID = 1l;
	@DatabaseField
	private String createBy;
	@DatabaseField
	private String createtime;
	@DatabaseField
	private String creator;
	@DatabaseField
	private int flag;
	@DatabaseField
	private String fzry;
	@DatabaseField
	private String gldwId;

	//
	// "bno": "2016012900002", //业务编号，后台统一生成
	// "createBy": 10000000050039,
	// "createtime": {},
	// "creator": "李兵",
	// "flag": 0,
	// "fzry": "王五", //负责人员
	// "gldwId": 10000000220011, //管理单位ID
	@DatabaseField
	private String gldwName;
	@DatabaseField
	private String gydwId;
	@DatabaseField
	private String gydwName;
	@DatabaseField
	private long id;
	@DatabaseField(generatedId = true)
	private long localId;
	@DatabaseField
	private String jcsd;
	@DatabaseField
	private String jcsj;
	@DatabaseField
	private String jlry;
	@DatabaseField
	private String lxbh;
	@DatabaseField
	private int type;

	@DatabaseField
	private String lxid;
	@DatabaseField
	private String lxmc;

	// "gldwName": "青银高速石家庄养护工区", //管理单位名称
	// "hzf": "", //是否会诊 1表示会诊
	// "id": 10000002170020, //记录ID
	// "jcsj": "2016-01-29 09:04:26", //检查时间
	// "jlry": "李兵", //记录人员
	// "lxbm": "G20",
	// "lxid": 10000001740003,
	// "lxmc": "青银高速",
	private List<SdxcFormDetail> inspectLogDetailList;
	@DatabaseField
	private String sdbh;
	@DatabaseField
	private String sdid;
	// "orgid": 0,
	// "pddj": "2", //评定等级，业务分类字典ID：10000001160070
	// "prePddj": "2", //上次评定等级，业务分类字典ID：10000001160070
	// "qhbm": "G20130124L0860", //桥涵编码
	// "qhid": 10000002090004, //桥涵名称
	@DatabaseField
	private String sdfx;
	@DatabaseField
	private String sdmc;
	// "qhlx": "b", //桥涵类型，b表示桥梁 c表示涵洞
	// "qhmc": "运粮河大桥", //桥涵名称
	@DatabaseField
	private String status;
	@DatabaseField
	private String updateBy;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private String updator;
	@DatabaseField
	private String yhdwId;
	@DatabaseField
	private String yhdwName;
	@DatabaseField
	private String zxzh;
	@DatabaseField
	private String yhjgName;
	@DatabaseField
	private String yhjgId;
	@DatabaseField
	private String weather;
	@DatabaseField
	private String sdzh;
	@DatabaseField
	private String dealwith;
	@DatabaseField
	private String xcry;
	@DatabaseField
	private String jcry;
	@DatabaseField
	private String qhlx;
	@DatabaseField
	private String tjsj;
	@DatabaseField
	private long savedTime = System.currentTimeMillis();

	@DatabaseField
	private String userId;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJcsj() {
		return jcsj;
	}

	public void setJcsj(String jcsj) {
		this.jcsj = jcsj;
	}

	public String getDealWith() {
		return dealwith;
	}

	public void setDealWith(String dealWith) {
		this.dealwith = dealWith;
	}

	public String getXcry() {
		return xcry;
	}

	public void setXcry(String xcry) {
		this.xcry = xcry;
	}

	public String getJcry() {
		return jcry;
	}

	public void setJcry(String jcry) {
		this.jcry = jcry;
	}

	public String getQhlx() {
		return qhlx;
	}

	public void setQhlx(String qhlx) {
		this.qhlx = qhlx;
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

	public String getFzry() {
		return fzry;
	}

	public void setFzry(String fzry) {
		this.fzry = fzry;
	}

	public String getGldwId() {
		return gldwId;
	}

	public void setGldwId(String gldwId) {
		this.gldwId = gldwId;
	}

	public String getGldwName() {
		return gldwName;
	}

	public void setGldwName(String gldwName) {
		this.gldwName = gldwName;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getJlry() {
		return jlry;
	}

	public void setJlry(String jlry) {
		this.jlry = jlry;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getYhdwId() {
		return yhdwId;
	}

	public void setYhdwId(String yhdwId) {
		this.yhdwId = yhdwId;
	}

	public String getYhdwName() {
		return yhdwName;
	}

	public void setYhdwName(String yhdwName) {
		this.yhdwName = yhdwName;
	}

	public String getZxzh() {
		return zxzh;
	}

	public void setZxzh(String zxzh) {
		this.zxzh = zxzh;
	}

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getYhjgName() {
		return yhjgName;
	}

	public void setYhjgName(String yhjgName) {
		this.yhjgName = yhjgName;
	}

	public String getYhjgId() {
		return yhjgId;
	}

	public void setYhjgId(String yhjgId) {
		this.yhjgId = yhjgId;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getSdmc() {
		return sdmc;
	}

	public void setSdmc(String sdmc) {
		this.sdmc = sdmc;
	}

	public String getSdid() {
		return sdid;
	}

	public void setSdid(String sdid) {
		this.sdid = sdid;
	}

	public String getSdzh() {
		return sdzh;
	}

	public void setSdzh(String sdzh) {
		this.sdzh = sdzh;
	}

	public String getSdfx() {
		return sdfx;
	}

	public void setSdfx(String sdfx) {
		this.sdfx = sdfx;
	}

	public List<SdxcFormDetail> getInspectLogDetailList() {
		return inspectLogDetailList;
	}

	public void setInspectLogDetailList(List<SdxcFormDetail> inspectLogDetailList) {
		this.inspectLogDetailList = inspectLogDetailList;
	}

	public String getJcsd() {
		return jcsd;
	}

	public void setJcsd(String jcsd) {
		this.jcsd = jcsd;
	}

	public String getLxbh() {
		return lxbh;
	}

	public void setLxbh(String lxbh) {
		this.lxbh = lxbh;
	}

	public String getSdbh() {
		return sdbh;
	}

	public void setSdbh(String sdbh) {
		this.sdbh = sdbh;
	}

	public String getCreateBy() {
		return createBy;
	}

	public String getDealwith() {
		return dealwith;
	}

	public void setDealwith(String dealwith) {
		this.dealwith = dealwith;
	}

	public String getTjsj() {
		return tjsj;
	}

	public void setTjsj(String tjsj) {
		this.tjsj = tjsj;
	}

	public long getSavedTime() {
		return savedTime;
	}

	public void setSavedTime(long savedTime) {
		this.savedTime = savedTime;
	}

	@Override
	public String toString() {
		return "SdxcFormData [createBy=" + createBy + ", createtime=" + createtime + ", creator=" + creator + ", flag=" + flag + ", fzry=" + fzry + ", gldwId="
				+ gldwId + ", gldwName=" + gldwName + ", id=" + id + ", localId=" + localId + ", jcsd=" + jcsd + ", jlry=" + jlry + ", lxbh=" + lxbh + ", type="
				+ type + ", lxid=" + lxid + ", lxmc=" + lxmc + ", inspectlogDetailList=" + inspectLogDetailList + ", sdbh=" + sdbh + ", sdid=" + sdid
				+ ", sdfx=" + sdfx + ", sdmc=" + sdmc + ", status=" + status + ", updateBy=" + updateBy + ", updatetime=" + updatetime + ", updator=" + updator
				+ ", yhdwId=" + yhdwId + ", yhdwName=" + yhdwName + ", zxzh=" + zxzh + ", yhjgName=" + yhjgName + ", yhjgId=" + yhjgId + ", weather=" + weather
				+ ", sdzh=" + sdzh + "]";
	}

}
