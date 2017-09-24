package com.mtestdrive.dto;

public class ReportRecordsDto {

	/** 销售区域id */
	private String regionId;
	/** 经销商名称 */
	private String dealerName;
	/** 试驾车型 */
	private String carType;
	/** 车牌号 */
	private String plateNo;

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

}
