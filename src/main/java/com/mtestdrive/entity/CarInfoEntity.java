package com.mtestdrive.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mtestdrive.MaseratiConstants;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Title: Entity
 * @Description: 车辆信息
 * @author zhangdaihao
 * @date 2017-03-10 17:28:07
 * @version V1.0
 * 
 */
@Entity
@Table(name = "t_car_info", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class CarInfoEntity extends BaseEntity implements java.io.Serializable {
	/** 4S经销商 */
	
	private AgencyInfoEntity agency;
	
	@Excel(name = "经销商")
	private AgencyInfoEntity agencyName;
	
	/** 编号 */
	private java.lang.String code;
	/** VIN码 车架号 */
	@Excel(name = "车架号")
	private java.lang.String vin;
	
	/** 集团 */
	@Excel(name = "集团")
	private String dearlerGroup;
	/** 试驾车名称 */
	@Excel(name = "试驾车名称")
	private java.lang.String name;
	/** 车型 */
	@Excel(name = "车型")
	private java.lang.String type;
	/** 车牌 */
	@Excel(name = "车牌")
	private java.lang.String plateNo;
	/** 形象照存放地址 */
	private java.lang.String picPath;
	/** 上市年份 */
	private java.util.Date saleYear;
	/** 试驾次数 */
	private java.lang.Integer driveTotal = 0;
	/** 创建人 */
	private java.lang.String createBy;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 修改人 */
	private java.lang.String updateBy;
	/** 修改时间 */
	private java.util.Date updateTime;
	/** sfID */
	private java.lang.String sfId;
	/**OBD设备编码*/
	private java.lang.String obdId;
	/**无行驶记录时间*/
	private java.util.Date fallowTime[];
	@Excel(name = "无试驾起始日期",exportFormat="yyyy-MM-dd")
	private java.util.Date fallowStartTime;
	@Excel(name = "无试驾结束日期",exportFormat="yyyy-MM-dd")
	private java.util.Date fallowStopTime;

	/**
	 * 方法: 取得agency
	 * 
	 * @return: com.mtestdrive.entity.AgencyInfoEntity 4S经销商
	 */
	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "AGENCY_ID")
	public AgencyInfoEntity getAgency() {
		return this.agency;
	}
	
	@Transient
	public String getAgencyName() {
		if(agency!=null){
			return this.agency.getName();
		}else{
			return null;
		}
		
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
	 * 方法: 取得code
	 * 
	 * @return: java.lang.String 编号
	 */
	@Column(name = "CODE", nullable = true, length = 30)
	public java.lang.String getCode() {
		return this.code;
	}

	/**
	 * 方法: 设置code
	 * 
	 * @param: java.lang.String 编号
	 */
	public void setCode(java.lang.String code) {
		this.code = code;
	}

	/**
	 * 方法: 取得vin
	 * 
	 * @return: java.lang.String VIN码 车架号
	 */
	@Column(name = "VIN", nullable = true, length = 50)
	public java.lang.String getVin() {
		return this.vin;
	}

	/**
	 * 方法: 设置vin
	 * 
	 * @param: java.lang.String VIN码 车架号
	 */
	public void setVin(java.lang.String vin) {
		this.vin = vin;
	}

	/**
	 * 方法: 取得name
	 * 
	 * @return: java.lang.String 试驾车名称
	 */
	@Column(name = "NAME", nullable = true, length = 50)
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 * 方法: 设置name
	 * 
	 * @param: java.lang.String 试驾车名称
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * 方法: 取得type
	 * 
	 * @return: java.lang.String 车型
	 */
	@Column(name = "TYPE", nullable = true, length = 20)
	public java.lang.String getType() {
		return this.type;
	}

	/**
	 * 方法: 设置type
	 * 
	 * @param: java.lang.String 车型
	 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

	/**
	 * 方法: 取得plateNo
	 * 
	 * @return: java.lang.String 车牌
	 */
	@Column(name = "PLATE_NO", nullable = true, length = 20)
	public java.lang.String getPlateNo() {
		return this.plateNo;
	}

	/**
	 * 方法: 设置plateNo
	 * 
	 * @param: java.lang.String 车牌
	 */
	public void setPlateNo(java.lang.String plateNo) {
		this.plateNo = plateNo;
	}

	/**
	 * 方法: 取得picPath
	 * 
	 * @return: java.lang.String 形象照存放地址
	 */
	@Column(name = "PIC_PATH", nullable = true, length = 200)
	public java.lang.String getPicPath() {
		return this.picPath;
	}

	/**
	 * 方法: 设置picPath
	 * 
	 * @param: java.lang.String 形象照存放地址
	 */
	public void setPicPath(java.lang.String picPath) {
		this.picPath = picPath;
	}

	/**
	 * 方法: 取得saleYear
	 * 
	 * @return: java.util.Date 上市年份
	 */
	@Column(name = "SALE_YEAR", nullable = true)
	public java.util.Date getSaleYear() {
		return this.saleYear;
	}

	/**
	 * 方法: 设置saleYear
	 * 
	 * @param: java.util.Date 上市年份
	 */
	public void setSaleYear(java.util.Date saleYear) {
		this.saleYear = saleYear;
	}

	/**
	 * 方法: 取得driveTotal
	 * 
	 * @return: java.lang.Integer 试驾次数
	 */
	@Column(name = "DRIVE_TOTAL", nullable = true, precision = 10, scale = 0)
	public java.lang.Integer getDriveTotal() {
		return this.driveTotal;
	}

	/**
	 * 方法: 设置driveTotal
	 * 
	 * @param: java.lang.Integer 试驾次数
	 */
	public void setDriveTotal(java.lang.Integer driveTotal) {
		this.driveTotal = driveTotal;
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
	 * 方法: 取得updateBy
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

	@Column(name = "sf_id", nullable = true, length = 32)
	public java.lang.String getSfId() {
		return sfId;
	}

	public void setSfId(java.lang.String sfId) {
		this.sfId = sfId;
	}

	@Column(name ="obd_id",nullable=true)
	public java.lang.String getObdId() {
		return obdId;
	}

	public void setObdId(java.lang.String obdId) {
		this.obdId = obdId;
	}
	@Transient
	public String getDearlerGroup() {
		return dearlerGroup;
	}

	public void setDearlerGroup(String dearlerGroup) {
		this.dearlerGroup = dearlerGroup;
	}

	@Transient
	public java.util.Date getFallowStartTime() {
		if(fallowTime==null){
			return null;
		}
		return fallowTime[0];
	}

	@Transient
	public java.util.Date getFallowStopTime() {
		if(fallowTime==null){
			return null;
		}
		if (1 == fallowTime.length) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(fallowTime[0]);
			calendar.roll(calendar.DATE, -1);
			return calendar.getTime();
		}
		return fallowTime[1];
	}

//    /**
//     * 设定车辆状态
//     * @param carStatus
//     */
//	public void setStatus(Integer carStatus){
//	    this.setStatus(carStatus);
//    }

	public void setFallowTime(java.util.Date[] fallowTime) {
		
		this.fallowTime = fallowTime;
	}

	public CarInfoEntity(String id, java.lang.String code, java.lang.String vin, java.lang.String name, java.lang.String plateNo, java.lang.String picPath,
			java.util.Date saleYear, java.lang.Integer driveTotal, java.lang.Integer status, java.lang.String obdId) {
		super();
		this.setId(id);
		this.code = code;
		this.vin = vin;
		this.name = name;
		this.plateNo = plateNo;
		this.picPath = picPath;
		this.saleYear = saleYear;
		this.driveTotal = driveTotal;
		this.setStatus(status);
		this.obdId = obdId;
	}

	public CarInfoEntity(String id,java.lang.String type, java.lang.String code, java.lang.String vin, java.lang.String name, java.lang.String plateNo, java.lang.String picPath,
			java.util.Date saleYear, java.lang.Integer driveTotal, java.lang.Integer status, java.lang.String obdId) {
		super();
		this.setId(id);
		this.code = code;
		this.vin = vin;
		this.name = name;
		this.plateNo = plateNo;
		this.picPath = picPath;
		this.saleYear = saleYear;
		this.driveTotal = driveTotal;
		this.setStatus(status);
		this.obdId = obdId;
		this.type = type;
	}
	
	public CarInfoEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
