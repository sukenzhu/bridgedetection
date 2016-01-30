package com.suken.bridgedetection.storage;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_user")
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 100l;
	// " token": "2t7nzqgodgbr19kqb0wui21ge", //系统后台后续功能验证使用
	// " userId": "10000000050039", //当前用户ID
	// " account": "libing", //当前用户登录名
	// " userName": "李兵", //当前用户名称
	// " deptId": "10000000040022", //当前用户所属部门id
	// " deptName": "桥梁股", //当前用户所属部门名称
	// " defgqId": "10000000050020", //当前用户主岗位所在工区
	// " defgqName": "邢台养护工区", //当前用户主岗位所在工区名称
	// " defcompId": "10000000030019" //当前用户所在主岗位管理处公司ID
	// " defcompName": "河北省高速公路石安管理处" //当前用户所在主岗位管理处公司名称

	@DatabaseField
	private String token;
	@DatabaseField
	private String account;
	@DatabaseField
	private String password;
	@DatabaseField(id = true)
	private String userId;
	@DatabaseField
	private String userName;
	@DatabaseField
	private String deptId;
	@DatabaseField
	private String deptName;
	@DatabaseField
	private String defgqId;
	@DatabaseField
	private String defgqName;
	@DatabaseField
	private String defcompId;
	@DatabaseField
	private String defcompName;

	public String getToken() {
		return token;
	}

	public String getAccount() {
		return account;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDefgqId() {
		return defgqId;
	}

	public void setDefgqId(String defgqId) {
		this.defgqId = defgqId;
	}

	public String getDefgqName() {
		return defgqName;
	}

	public void setDefgqName(String defgqName) {
		this.defgqName = defgqName;
	}

	public String getDefcompId() {
		return defcompId;
	}

	public void setDefcompId(String defcompId) {
		this.defcompId = defcompId;
	}

	public String getDefcompName() {
		return defcompName;
	}

	public void setDefcompName(String defcompName) {
		this.defcompName = defcompName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
