package com.mtestdrive.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.mtestdrive.common.DateJsonDeserializer;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.entity.CustomerInfoEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;

public class DriveRecodsDto {

	/** 销售区域id */
	private String id;
	/** 销售区域id */
	private String regionId;
	/** 4S经销商编码 */
	private String agencyCode;
	/** 试驾车id */
	private String carId;
	/** 试驾车型 */
	private String carType;
	/** 销售代表名称 */
	private String salesmanName;
	/** 客户id */
	private String customerId;
	/** 客户姓名 */
	private String customerName;
	/** 客户手机号码 */
	private String customerMobile;
	/** 试驾路线 */
	private String routeId;
	/** 开始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 日期*/
	private String orderDate;
	/** 客户姓名*/
	private CustomerInfoEntity customer;
	/** 销售顾问姓名*/
	private SalesmanInfoEntity salesman;
	/** 经销商店名*/
	private AgencyInfoEntity agency;
	/**试驾合同照片*/
	private String testDriveContractPicPath;
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

	public String getAgencyCode() {
		return agencyCode;
	}

	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public Date getStartTime() {
		return startTime;
	}

	@JsonDeserialize(using=DateJsonDeserializer.class)
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	@JsonDeserialize(using=DateJsonDeserializer.class)
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public CustomerInfoEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerInfoEntity customer) {
		this.customer = customer;
	}

	public SalesmanInfoEntity getSalesman() {
		return salesman;
	}

	public void setSalesman(SalesmanInfoEntity salesman) {
		this.salesman = salesman;
	}

	public AgencyInfoEntity getAgency() {
		return agency;
	}

	public void setAgency(AgencyInfoEntity agency) {
		this.agency = agency;
	}

	public String getTestDriveContractPicPath() {
		return testDriveContractPicPath;
	}

	public void setTestDriveContractPicPath(String testDriveContractPicPath) {
		this.testDriveContractPicPath = testDriveContractPicPath;
	}
}
