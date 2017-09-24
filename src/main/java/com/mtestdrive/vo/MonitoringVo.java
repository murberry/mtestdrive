package com.mtestdrive.vo;


public class MonitoringVo {
	
	/**编号*/
	private java.lang.String code;
	/**VIN码 车架号*/
	private java.lang.String vin;
	/**试驾车名称*/
	private java.lang.String name;
	/**车型*/
	private java.lang.String type;
	/**车牌*/
	private java.lang.String plateNo;
	/**形象照存放地址*/
	private java.lang.String picPath;
	/**上市年份*/
	private java.util.Date saleYear;
	/**试驾次数*/
	private java.lang.Integer driveTotal;
	/**状态 0:不可用; 1:可用*/
	private java.lang.Integer status;
	
	/**纬度*/
	private java.lang.Float latitude;
	/**经度*/
	private java.lang.Float  longitude;
	/**方向。正北为0，顺时针方向角度。值域：0~359。*/
	private java.lang.Integer head;
	/**设备速度。单位：公里/小时*/
	private java.lang.Float spd;
	/**OBD速度*/
	private java.lang.Float obdspd;
	/**海拔。单位：米。*/
	private java.lang.Float alt;
	/**累计行驶里程。单位：米。*/
	private java.lang.Float mileage;
	public java.lang.String getCode() {
		return code;
	}
	public void setCode(java.lang.String code) {
		this.code = code;
	}
	public java.lang.String getVin() {
		return vin;
	}
	public void setVin(java.lang.String vin) {
		this.vin = vin;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.String getType() {
		return type;
	}
	public void setType(java.lang.String type) {
		this.type = type;
	}
	public java.lang.String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(java.lang.String plateNo) {
		this.plateNo = plateNo;
	}
	public java.lang.String getPicPath() {
		return picPath;
	}
	public void setPicPath(java.lang.String picPath) {
		this.picPath = picPath;
	}
	public java.util.Date getSaleYear() {
		return saleYear;
	}
	public void setSaleYear(java.util.Date saleYear) {
		this.saleYear = saleYear;
	}
	public java.lang.Integer getDriveTotal() {
		return driveTotal;
	}
	public void setDriveTotal(java.lang.Integer driveTotal) {
		this.driveTotal = driveTotal;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.Integer getHead() {
		return head;
	}
	public void setHead(java.lang.Integer head) {
		this.head = head;
	}
	public java.lang.Float getSpd() {
		return spd;
	}
	public void setSpd(java.lang.Float spd) {
		this.spd = spd;
	}
	public java.lang.Float getObdspd() {
		return obdspd;
	}
	public void setObdspd(java.lang.Float obdspd) {
		this.obdspd = obdspd;
	}
	public java.lang.Float getAlt() {
		return alt;
	}
	public void setAlt(java.lang.Float alt) {
		this.alt = alt;
	}
	public java.lang.Float getMileage() {
		return mileage;
	}
	public void setMileage(java.lang.Float mileage) {
		this.mileage = mileage;
	}
	public java.lang.Float getLatitude() {
		return latitude;
	}
	public void setLatitude(java.lang.Float latitude) {
		this.latitude = latitude;
	}
	public java.lang.Float getLongitude() {
		return longitude;
	}
	public void setLongitude(java.lang.Float longitude) {
		this.longitude = longitude;
	}
}
