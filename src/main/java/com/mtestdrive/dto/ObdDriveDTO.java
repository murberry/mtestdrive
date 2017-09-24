package com.mtestdrive.dto;

import java.math.BigDecimal;

import org.jeecgframework.poi.excel.annotation.Excel;

public class ObdDriveDTO {
	/** 4S经销商 */
	@Excel(name = "经销商")
	private String agencyName;
	
	/** 车牌号 */
	@Excel(name = "车牌号")
	private String plateNo;
	
	/** 集团 */
	@Excel(name = "集团")
	private String dearlerGroup;
	
	/** 试驾车型 */
	@Excel(name = "车型")
	private String carType;
	
	
	/** 销售代表名称 */
	@Excel(name = "销售代表名称")
	private String salesmanName;
	
	/** 客户姓名 */
	@Excel(name = "客户姓名")
	private String customerName;
	
	/**0-无效试驾，1-有效试驾*/
	@Excel(name = "状态")
	private java.lang.String status;
	
	/** 无效原因*/
	@Excel(name = "无效原因")
	/**无效原因*/
	private java.lang.String description;
	
	/**试驾开始时间*/
	@Excel(name = "试驾开始时间")
	private java.util.Date driveStartTime;
	
	/**时间结束时间*/
	@Excel(name = "试驾结束时间")
	private java.util.Date driveEndTime;
	
	/** 行驶时间 */
	@Excel(name = "行驶时间")
	private float driveTime;
	
	/**行驶里程数*/
	@Excel(name = "行驶公里")
	private BigDecimal mileage;
	
	/**试驾合同照片*/
	private java.lang.String contractPicPath;
	
	/**结束后照片存放地址*/
	private java.lang.String endPicPath;
	
	/**试驾公里数达成率*/
	@Excel(name = "试驾公里数达成率")
	private java.lang.String achievement;

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getMileage() {
		return mileage;
	}

	public void setMileage(BigDecimal mileage) {
		this.mileage = mileage;
	}

	public java.util.Date getDriveStartTime() {
		return driveStartTime;
	}

	public void setDriveStartTime(java.util.Date driveStartTime) {
		this.driveStartTime = driveStartTime;
	}

	public java.util.Date getDriveEndTime() {
		return driveEndTime;
	}

	public void setDriveEndTime(java.util.Date driveEndTime) {
		this.driveEndTime = driveEndTime;
	}

	public java.lang.String getContractPicPath() {
		return contractPicPath;
	}

	public void setContractPicPath(java.lang.String contractPicPath) {
		this.contractPicPath = contractPicPath;
	}

	public java.lang.String getEndPicPath() {
		return endPicPath;
	}

	public void setEndPicPath(java.lang.String endPicPath) {
		this.endPicPath = endPicPath;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public float getDriveTime() {
		return driveTime;
	}

	public void setDriveTime(float driveTime) {
		this.driveTime = driveTime;
	}

	public String getDearlerGroup() {
		return dearlerGroup;
	}

	public void setDearlerGroup(String dearlerGroup) {
		this.dearlerGroup = dearlerGroup;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.String getAchievement() {
		return achievement;
	}

	public void setAchievement(java.lang.String achievement) {
		this.achievement = achievement;
	}
	
	
}
