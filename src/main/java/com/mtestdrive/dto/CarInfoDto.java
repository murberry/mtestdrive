package com.mtestdrive.dto;

import com.mtestdrive.entity.AgencyInfoEntity;

public class CarInfoDto {

	/** 试驾车id */
	private String id;
	/** 区域编码 */
	private String regionId;
	/** 4S经销商编码 */
	private String agencyId;
	/** 车型 */
	private String type;
	/** 车牌号 */
	private String plateNo;
	/** 月份（用于统计） */
	private String month;
	/** 状态*/
	private Integer status;
	/** 4S经销商 */
	private AgencyInfoEntity agency;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getMonth() {
		if (null == month || 6 == month.length()) {
			return month;
		} else if (5 == month.length()) {
			return new StringBuilder(month).insert(4, 0).toString();
		} else {
			return "";
		}
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public AgencyInfoEntity getAgency() {
		return agency;
	}

	public void setAgency(AgencyInfoEntity agency) {
		this.agency = agency;
	}
	
	
	
}
