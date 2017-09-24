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

/**
 * @Title: Entity
 * @Description: 报备信息
 * @author zhangdaihao
 * @date 2017-03-10 17:29:49
 * @version V1.0
 * 
 */
@Entity
@Table(name = "t_report_records", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class ReportRecordsEntity implements java.io.Serializable {
	/** 本表id */
	private java.lang.String id;
	/** 4S经销商id */
	private AgencyInfoEntity agency;
	/** 试驾车id */
	private java.lang.String carId;
	/** 报备原因类型 */
	private java.lang.String type;
	/** 报备开始时间 */
	private java.util.Date startTime;
	/** 报备结束时间 */
	private java.util.Date endTime;
	/** 具体原因 */
	private java.lang.String remark;
	/** 创建人 */
	private java.lang.String createBy;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 修改人 */
	private java.lang.String updateBy;
	/** 修改时间 */
	private java.util.Date updateTime;
	/**报备状态 0：未开始报备，1：已开始报备，2：报备已结束*/
	private java.lang.Integer status;
	/**
	 * 方法: 取得id
	 * 
	 * @return: java.lang.Integer 本表id
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name = "ID", nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	/**
	 * 方法: 设置id
	 * 
	 * @param: java.lang.Integer 本表id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 方法: 取得agency
	 * 
	 * @return: com.mtestdrive.entity.AgencyInfoEntity 4S经销商
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "AGENCY_ID")
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
	 * 方法: 取得type
	 * 
	 * @return: java.lang.String 报备原因类型
	 */
	@Column(name = "TYPE", nullable = true, length = 10)
	public java.lang.String getType() {
		return this.type;
	}

	/**
	 * 方法: 设置type
	 * 
	 * @param: java.lang.String 报备原因类型
	 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

	/**
	 * 方法: 取得startTime
	 * 
	 * @return: java.util.Date 报备开始时间
	 */
	@Column(name = "START_TIME", nullable = true)
	public java.util.Date getStartTime() {
		return this.startTime;
	}

	/**
	 * 方法: 设置startTime
	 * 
	 * @param: java.util.Date 报备开始时间
	 */
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 方法: 取得endTime
	 * 
	 * @return: java.util.Date 报备结束时间
	 */
	@Column(name = "END_TIME", nullable = true)
	public java.util.Date getEndTime() {
		return this.endTime;
	}

	/**
	 * 方法: 设置endTime
	 * 
	 * @param: java.util.Date 报备结束时间
	 */
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 方法: 取得remark
	 * 
	 * @return: java.lang.String 具体原因
	 */
	@Column(name = "REMARK", nullable = true, length = 2000)
	public java.lang.String getRemark() {
		return this.remark;
	}

	/**
	 * 方法: 设置remark
	 * 
	 * @param: java.lang.String 具体原因
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	/**
	 * 方法: 取得createBy
	 * 
	 * @return: java.lang.Integer 创建人
	 */
	@Column(name = "CREATE_BY", nullable = true, precision = 19, scale = 0)
	public String getCreateBy() {
		return this.createBy;
	}

	/**
	 * 方法: 设置createBy
	 * 
	 * @param: java.lang.Integer 创建人
	 */
	public void setCreateBy(String createBy) {
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
	 * @return: java.lang.Integer 修改人
	 */
	@Column(name = "UPDATE_BY", nullable = true, precision = 19, scale = 0)
	public String getUpdateBy() {
		return this.updateBy;
	}

	/**
	 * 方法: 设置updateBy
	 * 
	 * @param: java.lang.Integer 修改人
	 */
	public void setUpdateBy(String updateBy) {
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
	 * 方法: 取得date
	 * 
	 * @return: java.util.Date 日期
	 */
	@Transient
	public java.util.Date getDate() {
		return this.startTime;
	}

	@Column(name = "STATUS", nullable = true)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
