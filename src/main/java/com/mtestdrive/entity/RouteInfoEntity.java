package com.mtestdrive.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * @Title: Entity
 * @Description: 试驾路线
 * @author zhangdaihao
 * @date 2017-03-10 17:39:30
 * @version V1.0
 * 
 */
@Entity
@Table(name = "t_route_info", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class RouteInfoEntity implements java.io.Serializable {
	/** 本表id */
	private java.lang.String id;
	/**OBD设备编码*/
	private java.lang.String termId;
	/**采集数据时所使用的车型*/
	private java.lang.String carType;
	/**采集数据时所使用的车辆  车牌*/
	private java.lang.String plateNo;
	/**4S经销商id*/
	private java.lang.String agencyId;
	/**线路名称*/
	/** 4S经销商id *//*
	private AgencyInfoEntity agency;*/
	/** 线路名称 */
	private java.lang.String name;
	/** 路线图存放地址 */
	/*private java.lang.String routePicPath;*/
	/**数据采集开始时间*/
	private java.util.Date startTime;
	/**数据采集结束时间*/
	private java.util.Date endTime;
	/** 线路描述 */
	private java.lang.String remark;
	/** 创建人 */
	private java.lang.String createBy;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 修改人 */
	private java.lang.String updateBy;
	/** 修改时间 */
	private java.util.Date updateTime;
	/**状态*/
	private java.lang.Integer routeStatus;

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
	/*@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "AGENCY_ID")
	public AgencyInfoEntity getAgency() {
		return this.agency;
	}*/

	/**
	 * 方法: 设置agency
	 * 
	 * @param: com.mtestdrive.entity.AgencyInfoEntity 4S经销商
	 */
	/*public void setAgency(AgencyInfoEntity agency) {
		this.agency = agency;
	}*/

	/**
	 * 方法: 取得name
	 * 
	 * @return: java.lang.String 线路名称
	 */
	@Column(name = "NAME", nullable = true, length = 50)
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 * 方法: 设置name
	 * 
	 * @param: java.lang.String 线路名称
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * 方法: 取得routePicPath
	 * 
	 * @return: java.lang.String 路线图存放地址
	 */
	/*@Column(name ="ROUTE_PIC_PATH",nullable=true,length=200)
	public java.lang.String getRoutePicPath(){
	@Column(name = "ROUTE_PIC_PATH", nullable = true, length = 200)
	public java.lang.String getRoutePicPath() {
		return this.routePicPath;
	}*/

	/**
	 * 方法: 设置routePicPath
	 * 
	 * @param: java.lang.String 路线图存放地址
	 */
	/*public void setRoutePicPath(java.lang.String routePicPath){
	public void setRoutePicPath(java.lang.String routePicPath) {
		this.routePicPath = routePicPath;
	}*/

	/**
	 * 方法: 取得remark
	 * 
	 * @return: java.lang.String 线路描述
	 */
	@Column(name = "REMARK", nullable = true, length = 1000)
	public java.lang.String getRemark() {
		return this.remark;
	}

	/**
	 * 方法: 设置remark
	 * 
	 * @param: java.lang.String 线路描述
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
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

	@Column(name ="STARTTIME",nullable=true)
	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	@Column(name ="ENDTIME",nullable=true)
	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	@Column(name ="TERM_ID",nullable=true)
	public java.lang.String getTermId() {
		return termId;
	}

	public void setTermId(java.lang.String termId) {
		this.termId = termId;
	}

	@Column(name ="carType",nullable=true)
	public java.lang.String getCarType() {
		return carType;
	}

	public void setCarType(java.lang.String carType) {
		this.carType = carType;
	}

	@Column(name ="plateNo",nullable=true)
	public java.lang.String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(java.lang.String plateNo) {
		this.plateNo = plateNo;
	}
	@Column(name ="routeStatus",nullable=true)
	public java.lang.Integer getRouteStatus() {
		return routeStatus;
	}

	public void setRouteStatus(java.lang.Integer routeStatus) {
		this.routeStatus = routeStatus;
	}
	
	@Column(name ="AGENCY_ID",nullable=true)
	public java.lang.String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(java.lang.String agencyId) {
		this.agencyId = agencyId;
	}
}
