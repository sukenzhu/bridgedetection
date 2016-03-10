package com.suken.bridgedetection.storage;

import java.io.Serializable;
import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_checkform")
public class CheckFormData implements Serializable {
	private static final long serialVersionUID = 1l;
	@DatabaseField
	private String userId;
	@DatabaseField
	private String bno;
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
	private String jcry;
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
	private String hzf;
	@DatabaseField
	private long id;
	@DatabaseField(generatedId = true)
	private long localId;
	@DatabaseField
	private String jcsj;
	@DatabaseField
	private String jlry;
	@DatabaseField
	private String lxbm;
	@DatabaseField
	private int type;
	@DatabaseField
	private String cus1;
	@DatabaseField
	private String cus2;
	@DatabaseField
	private String cus3;

	public String getLxbm() {
		return lxbm;
	}

	public void setLxbm(String lxbm) {
		this.lxbm = lxbm;
	}

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
	private List<CheckDetail> oftenCheckDetailList;
	@DatabaseField
	private String orgid;
	@DatabaseField
	private String pddj; // 10000001160070
	@DatabaseField
	private String prePddj;// 10000001160070
	@DatabaseField
	private String qhbm;
	@DatabaseField
	private String qhid;
	// "orgid": 0,
	// "pddj": "2", //评定等级，业务分类字典ID：10000001160070
	// "prePddj": "2", //上次评定等级，业务分类字典ID：10000001160070
	// "qhbm": "G20130124L0860", //桥涵编码
	// "qhid": 10000002090004, //桥涵名称
	@DatabaseField
	private String qhlx;
	@DatabaseField
	private String qhmc;
	// "qhlx": "b", //桥涵类型，b表示桥梁 c表示涵洞
	// "qhmc": "运粮河大桥", //桥涵名称
	@DatabaseField
	private String status;
	@DatabaseField
	private String tjsj;
	@DatabaseField
	private String updateBy;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private String updator;
	@DatabaseField
	private String versionno;
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
	private String sdbm;
	@DatabaseField
	private String sdmc;
	@DatabaseField
	private String sdid;
	@DatabaseField
	private String sdzh;
	@DatabaseField
	private String sdfx;
	@DatabaseField(defaultValue = "false", dataType = DataType.BOOLEAN)
	private boolean lastUpdate;
	@DatabaseField
	private long saveTime = System.currentTimeMillis();

	// "status": "1", //状态 1 草稿 2 已提交，参考业务字典分类ID：10000000690004
	// "tjsj": null, //提交时间
	// "updateBy": 0,
	// "updatetime": null,
	// "updator": "",
	// "versionno": 1,
	// "yhdwId": 10000000220011, //养护单位ID
	// "yhdwName": "青银高速石家庄养护工区", //养护单位名称
	// "zxzh": 595.844 //中心桩号
	// }

	public String getBno() {
		return bno;
	}

	public String getJcry() {
		return jcry;
	}

	public void setJcry(String jcry) {
		this.jcry = jcry;
	}

	public boolean isLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(boolean lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void setBno(String bno) {
		this.bno = bno;
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

	public String getHzf() {
		return hzf;
	}

	public void setHzf(String hzf) {
		this.hzf = hzf;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getJcsj() {
		return jcsj;
	}

	public void setJcsj(String jcsj) {
		this.jcsj = jcsj;
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

	public List<CheckDetail> getOftenCheckDetailList() {
		return oftenCheckDetailList;
	}

	public void setOftenCheckDetailList(List<CheckDetail> oftenCheckDetailList) {
		this.oftenCheckDetailList = oftenCheckDetailList;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getPddj() {
		return pddj;
	}

	public void setPddj(String pddj) {
		this.pddj = pddj;
	}

	public String getPrePddj() {
		return prePddj;
	}

	public void setPrePddj(String prePddj) {
		this.prePddj = prePddj;
	}

	public String getQhbm() {
		return qhbm;
	}

	public void setQhbm(String qhbm) {
		this.qhbm = qhbm;
	}

	public String getQhid() {
		return qhid;
	}

	public void setQhid(String qhid) {
		this.qhid = qhid;
	}

	public String getQhlx() {
		return qhlx;
	}

	public void setQhlx(String qhlx) {
		this.qhlx = qhlx;
	}

	public String getQhmc() {
		return qhmc;
	}

	public void setQhmc(String qhmc) {
		this.qhmc = qhmc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTjsj() {
		return tjsj;
	}

	public void setTjsj(String tjsj) {
		this.tjsj = tjsj;
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

	public String getSdbm() {
		return sdbm;
	}

	public void setSdbm(String sdbm) {
		this.sdbm = sdbm;
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
	
	public String getCus1() {
		return cus1;
	}

	public void setCus1(String cus1) {
		this.cus1 = cus1;
	}

	public String getCus2() {
		return cus2;
	}

	public void setCus2(String cus2) {
		this.cus2 = cus2;
	}

	public String getCus3() {
		return cus3;
	}

	public void setCus3(String cus3) {
		this.cus3 = cus3;
	}

	public long getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(long saveTime) {
		this.saveTime = saveTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "JianChaFormData [bno=" + bno + ", createBy=" + createBy + ", createtime=" + createtime + ", creator=" + creator + ", flag=" + flag + ", fzry="
				+ fzry + ", gldwId=" + gldwId + ", gldwName=" + gldwName + ", hzf=" + hzf + ", id=" + id + ", jcsj=" + jcsj + ", jlry=" + jlry + ", lxid="
				+ lxid + ", lxmc=" + lxmc + ", oftenCheckDetailList=" + oftenCheckDetailList + ", orgid=" + orgid + ", pddj=" + pddj + ", prePddj=" + prePddj
				+ ", qhbm=" + qhbm + ", qhid=" + qhid + ", qhlx=" + qhlx + ", qhmc=" + qhmc + ", status=" + status + ", tjsj=" + tjsj + ", updateBy=" + updateBy
				+ ", updatetime=" + updatetime + ", updator=" + updator + ", versionno=" + versionno + ", yhdwId=" + yhdwId + ", yhdwName=" + yhdwName
				+ ", zxzh=" + zxzh + "]";
	}

}
