package com.mtestdrive.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * @Title: Entity
 * @Description: 试驾明细
 * @author zhangdaihao
 * @date 2017-03-10 17:36:12
 * @version V1.0
 * 
 */
@Entity
@Table(name = "t_drive_recods", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class DriveRecodsEntity implements java.io.Serializable {
	/** 本表id */
	private java.lang.String id;
	/** 4S经销商 */
	private AgencyInfoEntity agency;
	/** 试驾车id */
	private java.lang.String carId;
	/** 试驾车型 */
	private java.lang.String carType;
	/** 销售代表 */
	private SalesmanInfoEntity salesman;
	/** 客户 */
	private CustomerInfoEntity customer;
	/** 预约开始时间 */
	private java.util.Date orderStartTime;
	/** 预约结束时间 */
	private java.util.Date orderEndTime;
	/** 驾驶员 */
	private java.lang.String driver;
	/** 驾驶员 */
	private String routeId;
	/** 试驾开始时间 */
	private java.util.Date driveStartTime;
	/** 时间结束时间 */
	private java.util.Date driveEndTime;
	/** 开始前照片存放地址 */
	private java.lang.String startPicPath;
	/** 结束后照片存放地址 */
	private java.lang.String endPicPath;
	/** 行驶里程数 */
	private java.math.BigDecimal mileage;
	/** 试驾反馈 */
	private java.lang.String feedback;
	/** 创建人 */
	private java.lang.String createBy;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 修改人 */
	private java.lang.String updateBy;
	/** 修改时间 */
	private java.util.Date updateTime;
	/** 预约状态 0-预约，1-已确认，2-试驾中，3-完成，4-放弃，5-取消 */
	private java.lang.Integer status;
	/**sfID */
	private java.lang.String sfId;
	/**试驾合同照片*/
	private String testDriveContractPicPath;
	
	
	
	/**
	 * 方法: 取得id
	 * 
	 * @return: java.lang.String 本表id
	 */

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name = "ID", nullable = false, length = 32)
	public java.lang.String getId() {
		return this.id;
	}

	/**
	 * 方法: 设置id
	 * 
	 * @param: java.lang.String 本表id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * 方法: 取得agency
	 * 
	 * @return: com.mtestdrive.entity.AgencyInfoEntity 4S经销商
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "AGENCY_ID")
	@NotFound(action=NotFoundAction.IGNORE)
	public AgencyInfoEntity getAgency() {
		return this.agency;
	}

	/**
	 * 方法: 设置agency
	 * 
	 * @param: com.mtestdrive.entity.AgencyInfoEntity 4S经销商
	 */
	public void setAgency(AgencyInfoEntity agency) {
		this.agency = agency;
	}

	/**
	 * 方法: 取得carId
	 * 
	 * @return: java.lang.String 试驾车id
	 */
	@Column(name = "CAR_ID", nullable = true, length = 32)
	public java.lang.String getCarId() {
		return this.carId;
	}

	/**
	 * 方法: 设置carId
	 * 
	 * @param: java.lang.String 试驾车id
	 */
	public void setCarId(java.lang.String carId) {
		this.carId = carId;
	}

	/**
	 * 方法: 取得carType
	 * 
	 * @return: java.lang.String 试驾车型
	 */
	@Column(name = "CAR_TYPE", nullable = true, length = 20)
	public java.lang.String getCarType() {
		return this.carType;
	}

	/**
	 * 方法: 设置carType
	 * 
	 * @param: java.lang.String 试驾车型
	 */
	public void setCarType(java.lang.String carType) {
		this.carType = carType;
	}

	/**
	 * 方法: 取得salesman
	 * 
	 * @return: com.mtestdrive.entity.SalesmanInfoEntity 销售代表
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "SALESMAN_ID")
	public SalesmanInfoEntity getSalesman() {
		return this.salesman;
	}

	/**
	 * 方法: 设置salesman
	 * 
	 * @param: com.mtestdrive.entity.SalesmanInfoEntity 销售代表
	 */
	public void setSalesman(SalesmanInfoEntity salesman) {
		this.salesman = salesman;
	}

	/**
	 * 方法: 取得customer
	 * 
	 * @return: com.mtestdrive.entity.CustomerInfoEntity 客户
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "CUSTOMER_ID")
	public CustomerInfoEntity getCustomer() {
		return this.customer;
	}

	/**
	 * 方法: 设置customer
	 * 
	 * @param: com.mtestdrive.entity.CustomerInfoEntity 客户
	 */
	public void setCustomer(CustomerInfoEntity customer) {
		this.customer = customer;
	}

	/**
	 * 方法: 取得orderStartTime
	 * 
	 * @return: java.util.Date 预约开始时间
	 */
	@Column(name = "ORDER_START_TIME", nullable = true)
	public java.util.Date getOrderStartTime() {
		return this.orderStartTime;
	}

	/**
	 * 方法: 设置orderStartTime
	 * 
	 * @param: java.util.Date 预约开始时间
	 */
	public void setOrderStartTime(java.util.Date orderStartTime) {
		this.orderStartTime = orderStartTime;
	}

	/**
	 * 方法: 取得orderEndTime
	 * 
	 * @return: java.util.Date 预约结束时间
	 */
	@Column(name = "ORDER_END_TIME", nullable = true)
	public java.util.Date getOrderEndTime() {
		return this.orderEndTime;
	}

	/**
	 * 方法: 设置orderEndTime
	 * 
	 * @param: java.util.Date 预约结束时间
	 */
	public void setOrderEndTime(java.util.Date orderEndTime) {
		this.orderEndTime = orderEndTime;
	}

	/**
	 * 方法: 取得driver
	 * 
	 * @return: java.lang.String 驾驶员
	 */
	@Column(name = "DRIVER", nullable = true, length = 50)
	public java.lang.String getDriver() {
		return this.driver;
	}

	/**
	 * 方法: 设置driver
	 * 
	 * @param: java.lang.String 驾驶员
	 */
	public void setDriver(java.lang.String driver) {
		this.driver = driver;
	}

	

	/**
	 * 方法: 取得driveStartTime
	 * 
	 * @return: java.util.Date 试驾开始时间
	 */
	@Column(name = "DRIVE_START_TIME", nullable = true)
	public java.util.Date getDriveStartTime() {
		return this.driveStartTime;
	}

	/**
	 * 方法: 设置driveStartTime
	 * 
	 * @param: java.util.Date 试驾开始时间
	 */
	public void setDriveStartTime(java.util.Date driveStartTime) {
		this.driveStartTime = driveStartTime;
	}

	/**
	 * 方法: 取得driveEndTime
	 * 
	 * @return: java.util.Date 时间结束时间
	 */
	@Column(name = "DRIVE_END_TIME", nullable = true)
	public java.util.Date getDriveEndTime() {
		return this.driveEndTime;
	}

	/**
	 * 方法: 设置driveEndTime
	 * 
	 * @param: java.util.Date 时间结束时间
	 */
	public void setDriveEndTime(java.util.Date driveEndTime) {
		this.driveEndTime = driveEndTime;
	}

	/**
	 * 方法: 取得startPicPath
	 * 
	 * @return: java.lang.String 开始前照片存放地址
	 */
	@Column(name = "START_PIC_PATH", nullable = true, length = 200)
	public java.lang.String getStartPicPath() {
		return this.startPicPath;
	}

	/**
	 * 方法: 设置startPicPath
	 * 
	 * @param: java.lang.String 开始前照片存放地址
	 */
	public void setStartPicPath(java.lang.String startPicPath) {
		this.startPicPath = startPicPath;
	}

	/**
	 * 方法: 取得endPicPath
	 * 
	 * @return: java.lang.String 结束后照片存放地址
	 */
	@Column(name = "END_PIC_PATH", nullable = true, length = 200)
	public java.lang.String getEndPicPath() {
		return this.endPicPath;
	}

	/**
	 * 方法: 设置endPicPath
	 * 
	 * @param: java.lang.String 结束后照片存放地址
	 */
	public void setEndPicPath(java.lang.String endPicPath) {
		this.endPicPath = endPicPath;
	}

	/**
	 * 方法: 取得mileage
	 * 
	 * @return: BigDecimal 行驶里程数
	 */
	@Column(name = "MILEAGE", nullable = true, precision = 6, scale = 2)
	public java.math.BigDecimal getMileage() {
		return this.mileage;
	}

	/**
	 * 方法: 设置mileage
	 * 
	 * @param: BigDecimal 行驶里程数
	 */
	public void setMileage(java.math.BigDecimal mileage) {
		this.mileage = mileage;
	}

	/**
	 * 方法: 取得feedback
	 * 
	 * @return: java.lang.Object 试驾反馈
	 */
	@Column(name = "FEEDBACK", nullable = true, length = 500)
	public java.lang.String getFeedback() {
		return this.feedback;
	}

	/**
	 * 方法: 设置feedback
	 * 
	 * @param: java.lang.Object 试驾反馈
	 */
	public void setFeedback(java.lang.String feedback) {
		this.feedback = feedback;
	}

	/**
	 * 方法: 取得createBy
	 * 
	 * @return: java.lang.String 创建人
	 */
	@Column(name = "CREATE_BY", nullable = true, length = 32)
	public java.lang.String getCreateBy() {
		return this.createBy;
	}

	/**
	 * 方法: 设置createBy
	 * 
	 * @param: java.lang.String 创建人
	 */
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 方法: 取得createTime
	 * 
	 * @return: java.util.Date 创建时间
	 */
	@Column(name = "CREATE_TIME", nullable = true)
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * 方法: 设置createTime
	 * 
	 * @param: java.util.Date 创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 方法: 取得updateBy
	 * 
	 * @return: java.lang.String 修改人
	 */
	@Column(name = "UPDATE_BY", nullable = true, length = 32)
	public java.lang.String getUpdateBy() {
		return this.updateBy;
	}

	/**
	 * 方法: 设置updateBy
	 * 
	 * @param: java.lang.String 修改人
	 */
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 方法: 取得updateTime
	 * 
	 * @return: java.util.Date 修改时间
	 */
	@Column(name = "UPDATE_TIME", nullable = true)
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * 方法: 设置updateTime
	 * 
	 * @param: java.util.Date 修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 方法: 取得status
	 * 
	 * @return: java.lang.Integer 预约状态
	 */
	@Column(name = "STATUS")
	public java.lang.Integer getStatus() {
		return status;
	}

	/**
	 * 方法: 设置status
	 * 
	 * @param: java.lang.Integer 预约状态
	 */
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	@Column(name ="sf_id",nullable=true,length=32)
	public java.lang.String getSfId() {
		return sfId;
	}

	public void setSfId(java.lang.String sfId) {
		this.sfId = sfId;
	}

	/**
	 * 方法: 取得orderDate
	 * 
	 * @return: java.util.Date 预约日期
	 */
	@Transient
	public java.util.Date getOrderDate() {
		return this.orderStartTime;
	}

	@Column(name ="route_id",nullable=true,length=32)
	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	@Column(name ="contract_pic_path",nullable=true)
	public String getTestDriveContractPicPath() {
		return testDriveContractPicPath;
	}

	public void setTestDriveContractPicPath(String testDriveContractPicPath) {
		this.testDriveContractPicPath = testDriveContractPicPath;
	}
}
