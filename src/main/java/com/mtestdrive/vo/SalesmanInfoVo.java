package com.mtestdrive.vo;

import com.mtestdrive.entity.SalesmanInfoEntity;

public class SalesmanInfoVo extends SalesmanInfoEntity{
	/**本表id*/
	private java.lang.String id;
	/**销售区域id*/
	private java.lang.String regionId;
	/**4S销售商id*/
	private java.lang.String agencyId;
	private String agencyName;
	/**销售代表名称*/
	private java.lang.String name;
	/**工号*/
	private java.lang.String employeeNo;
	/**手机号码*/
	private java.lang.String mobile;
	/**头像存放地址*/
	private java.lang.String headPortraitPicPath;
	
	private java.lang.String address;
	/**预约成功率*/
	private java.lang.Double ceil;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getRegionId() {
		return regionId;
	}

	public void setRegionId(java.lang.String regionId) {
		this.regionId = regionId;
	}

	public java.lang.String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(java.lang.String agencyId) {
		this.agencyId = agencyId;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(java.lang.String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public java.lang.String getMobile() {
		return mobile;
	}

	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

	public java.lang.String getHeadPortraitPicPath() {
		return headPortraitPicPath;
	}

	public void setHeadPortraitPicPath(java.lang.String headPortraitPicPath) {
		this.headPortraitPicPath = headPortraitPicPath;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.Double getCeil() {
		return ceil;
	}

	public void setCeil(java.lang.Double ceil) {
		this.ceil = ceil;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
}
