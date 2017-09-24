package com.mtestdrive.vo;

import java.util.Date;

public class CarInfoVo {
	private String id;
	private String code;
	private String vin;
	private String name;
	private String type;
	private String plateNo;
	private String picPath;
	private Date saleYear;
	private Integer driveTotal;
	private Integer status;
	private String statusName;
	private String saleYearTOString;
	/**OBD设备编码*/
	private java.lang.String obdId;
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVin() {
		return this.vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlateNo() {
		return this.plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getPicPath() {
		return this.picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Date getSaleYear() {
		return this.saleYear;
	}

	public void setSaleYear(Date saleYear) {
		this.saleYear = saleYear;
	}

	public Integer getDriveTotal() {
		return this.driveTotal;
	}

	public void setDriveTotal(Integer driveTotal) {
		this.driveTotal = driveTotal;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSaleYearTOString() {
		return saleYearTOString;
	}

	public void setSaleYearTOString(String saleYearTOString) {
		this.saleYearTOString = saleYearTOString;
	}

	public java.lang.String getObdId() {
		return obdId;
	}

	public void setObdId(java.lang.String obdId) {
		this.obdId = obdId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
}
