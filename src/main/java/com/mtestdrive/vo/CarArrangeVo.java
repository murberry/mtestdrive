package com.mtestdrive.vo;

import java.util.Date;

public class CarArrangeVo {
	
	private Date startTime;
	
	private Date endTime;
	/**
	 * 1预约，2报备
	 */
	private Integer status;
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
