package com.mtestdrive.vo;

import com.mtestdrive.dto.ObdGatherDataDto;

/**
 * @ClassName:  PlaybackTrackVo   
 * @Description:轨迹回放页面VO
 * @author: mengtaocui
 * @date:   2017年4月6日 下午4:38:16   
 *
 */
public class PlaybackTrackVo {
	/** 驾驶员 */
	private java.lang.String driver;
	/** 试驾开始时间 */
	private java.util.Date driveStartTime;
	/** 试驾结束时间 */
	private java.util.Date driveEndTime;
	/** 行驶里程数 */
	private java.math.BigDecimal mileage;
	/** 车型 */
	private java.lang.String type;
	/** 车牌 */
	private java.lang.String plateNo;
	
	/**行驶轨迹*/
	private java.lang.String obdGathers;

	public java.lang.String getDriver() {
		return driver;
	}

	public void setDriver(java.lang.String driver) {
		this.driver = driver;
	}

	public java.util.Date getDriveStartTime() {
		return driveStartTime;
	}

	public void setDriveStartTime(java.util.Date driveStartTime) {
		this.driveStartTime = driveStartTime;
	}

	public java.math.BigDecimal getMileage() {
		return mileage;
	}

	public void setMileage(java.math.BigDecimal mileage) {
		this.mileage = mileage;
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

	public String getObdGathers() {
		return obdGathers;
	}

	public void setObdGathers(String obdGathers) {
		this.obdGathers = obdGathers;
	}

	public java.util.Date getDriveEndTime() {
		return driveEndTime;
	}

	public void setDriveEndTime(java.util.Date driveEndTime) {
		this.driveEndTime = driveEndTime;
	}
}
