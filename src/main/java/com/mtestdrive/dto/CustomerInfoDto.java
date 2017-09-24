package com.mtestdrive.dto;

import java.util.Date;

public class CustomerInfoDto {

	/** 本表id */
	private String id;
	/** 客户姓名 */
	private String name;
	/** 性别 0:未知; 1:男; 2:女 */
	private Integer gender;
	/** 出生日期 */
	private Date birthday;
	/** 客户手机号码 */
	private String mobile;
	/** 销售顾问*/
	private String createBy;
	/** 开始时间 */
	private Date driveStartTime;
	/** 结束时间 */
	private Date driveEndTime;
	/** 备注 */
	private String remark;
	/** 备注 */
	private Integer quarry;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getDriveStartTime() {
		return driveStartTime;
	}

	public void setDriveStartTime(Date driveStartTime) {
		this.driveStartTime = driveStartTime;
	}

	public Date getDriveEndTime() {
		return driveEndTime;
	}

	public void setDriveEndTime(Date driveEndTime) {
		this.driveEndTime = driveEndTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getQuarry() {
		return quarry;
	}

	public void setQuarry(Integer quarry) {
		this.quarry = quarry;
	}
	
}
