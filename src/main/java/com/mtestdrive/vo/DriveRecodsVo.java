package com.mtestdrive.vo;

import java.util.Date;

public class DriveRecodsVo {
	private String id;
	private Object agency;
	private CarInfoVo car = new CarInfoVo();
	private Object salesman;
	private Object customer;
	private Date orderStartTime;
	private Date orderEndTime;
	private String driver;
	private String routeId;
	private Date driveStartTime;
	private Date driveEndTime;
	private String startPicPath;
	private String endPicPath;
	private String feedback;
	private Integer status;
	/** 行驶里程数 */
	private java.math.BigDecimal mileage;
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getAgency() {
		return agency;
	}

	public void setAgency(Object agency) {
		this.agency = agency;
	}

	public CarInfoVo getCar() {
		return car;
	}

	public void setCar(CarInfoVo car) {
		this.car = car;
	}

	public Object getSalesman() {
		return salesman;
	}

	public void setSalesman(Object salesman) {
		this.salesman = salesman;
	}

	public Object getCustomer() {
		return customer;
	}

	public void setCustomer(Object customer) {
		this.customer = customer;
	}

	public Date getOrderStartTime() {
		return this.orderStartTime;
	}

	public void setOrderStartTime(Date orderStartTime) {
		this.orderStartTime = orderStartTime;
	}

	public Date getOrderEndTime() {
		return this.orderEndTime;
	}

	public void setOrderEndTime(Date orderEndTime) {
		this.orderEndTime = orderEndTime;
	}

	public String getDriver() {
		return this.driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public Date getDriveStartTime() {
		return this.driveStartTime;
	}

	public void setDriveStartTime(Date driveStartTime) {
		this.driveStartTime = driveStartTime;
	}

	public Date getDriveEndTime() {
		return this.driveEndTime;
	}

	public void setDriveEndTime(Date driveEndTime) {
		this.driveEndTime = driveEndTime;
	}

	public String getStartPicPath() {
		return this.startPicPath;
	}

	public void setStartPicPath(String startPicPath) {
		this.startPicPath = startPicPath;
	}

	public String getEndPicPath() {
		return this.endPicPath;
	}

	public void setEndPicPath(String endPicPath) {
		this.endPicPath = endPicPath;
	}

	public String getFeedback() {
		return this.feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCarId() {
		return car.getId();
	}

	public void setCarId(String carId) {
		car.setId(carId);
	}

	public java.math.BigDecimal getMileage() {
		return mileage;
	}

	public void setMileage(java.math.BigDecimal mileage) {
		this.mileage = mileage;
	}
}
