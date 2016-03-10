package com.suken.bridgedetection.storage;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_ywzd")
public class YWDictionaryInfo {

	@DatabaseField
	private int createBy;
	@DatabaseField
	private String createtime;
	@DatabaseField
	private String decp;
	@DatabaseField(id = true)
	private String dicId;
	@DatabaseField
	private String itemKey;
	@DatabaseField
	private String itemName;
	@DatabaseField
	private String itemValue;
	@DatabaseField
	private String nodePath;
	@DatabaseField
	private String open;
	@DatabaseField
	private String parentId;
	@DatabaseField
	private int sn;
	@DatabaseField
	private int type;
	@DatabaseField
	private String typeId;
	@DatabaseField
	private int updateBy;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private String userId;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getCreateBy() {
		return createBy;
	}
	public void setCreateBy(int createBy) {
		this.createBy = createBy;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getDecp() {
		return decp;
	}
	public void setDecp(String decp) {
		this.decp = decp;
	}
	public String getDicId() {
		return dicId;
	}
	public void setDicId(String dicId) {
		this.dicId = dicId;
	}
	public String getItemKey() {
		return itemKey;
	}
	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(int updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	@Override
	public String toString() {
		return "YWDictionaryInfo [createBy=" + createBy + ", createtime=" + createtime + ", decp=" + decp + ", dicId=" + dicId + ", itemKey=" + itemKey + ", itemName=" + itemName + ", itemValue="
				+ itemValue + ", nodePath=" + nodePath + ", open=" + open + ", parentId=" + parentId + ", sn=" + sn + ", type=" + type + ", updateBy=" + updateBy + ", updatetime=" + updatetime + "]";
	}

}
