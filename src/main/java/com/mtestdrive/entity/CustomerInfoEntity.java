package com.mtestdrive.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * @Title: Entity
 * @Description: 客户信息
 * @author zhangdaihao
 * @date 2017-03-10 17:26:29
 * @version V1.0
 * 
 */
@Entity
@Table(name = "t_customer_info", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class CustomerInfoEntity extends BaseEntity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 4S经销商id */
	private java.lang.String agencyId;
	/** 客户来源 1:saleforce推送而来; 2:4S店添加 */
	private java.lang.Integer source;
	/** 用户姓名 */
	private java.lang.String name;
	/** 性别 0:未知; 1:男; 2:女 */
	private java.lang.Integer gender;
	/** 出生日期 */
	private java.util.Date birthday;
	/** 身份证号 */
	private java.lang.String idCard;
	/** 手机号码 */
	private java.lang.String mobile;
	/** 驾照号码 */
	private java.lang.String drivingLicense;
	/** 驾照照片存放地址 */
	private java.lang.String drivingLicensePicPath;
	/** 创建人 */
	private java.lang.String createBy;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 修改人 */
	private java.lang.String updateBy;
	/** 修改时间 */
	private java.util.Date updateTime;
	/** 创建人姓名即销售顾问姓名 */
	private java.lang.String createUserName;
	/** 区域名 */
	private java.lang.String regionName;
	/** 经销商名 */
	private java.lang.String agencyName;
	/**sfID */
	private java.lang.String sfId;
	/**备注 */
	private java.lang.String remark;
	/**备注 */
	private java.lang.Integer quarry;
	/**客户类型 */
	private java.lang.String type;
	

	/**
	 * 方法: 取得java.lang.Integer
	 * 
	 * @return: java.lang.Integer 4S经销商id
	 */
	@Column(name = "AGENCY_ID", nullable = true, precision = 19, scale = 0)
	public String getAgencyId() {
		return this.agencyId;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 * 
	 * @param: java.lang.Integer 4S经销商id
	 */
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 * 
	 * @return: java.lang.Integer 客户来源 1:saleforce推送而来; 2:4S店添加
	 */
	@Column(name = "SOURCE", nullable = true, precision = 10, scale = 0)
	public java.lang.Integer getSource() {
		return this.source;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 * 
	 * @param: java.lang.Integer 客户来源 1:saleforce推送而来; 2:4S店添加
	 */
	public void setSource(java.lang.Integer source) {
		this.source = source;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 用户姓名
	 */
	@Column(name = "NAME", nullable = true, length = 50)
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 用户姓名
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 * 
	 * @return: java.lang.Integer 性别 0:未知; 1:男; 2:女
	 */
	@Column(name = "GENDER", nullable = true, precision = 10, scale = 0)
	public java.lang.Integer getGender() {
		return this.gender;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 * 
	 * @param: java.lang.Integer 性别 0:未知; 1:男; 2:女
	 */
	public void setGender(java.lang.Integer gender) {
		this.gender = gender;
	}

	/**
	 * 方法: 取得java.util.Date
	 * 
	 * @return: java.util.Date 出生日期
	 */
	@Column(name = "BIRTHDAY", nullable = true)
	public java.util.Date getBirthday() {
		return this.birthday;
	}

	/**
	 * 方法: 设置java.util.Date
	 * 
	 * @param: java.util.Date 出生日期
	 */
	public void setBirthday(java.util.Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 身份证号
	 */
	@Column(name = "ID_CARD", nullable = true, length = 18)
	public java.lang.String getIdCard() {
		return this.idCard;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 身份证号
	 */
	public void setIdCard(java.lang.String idCard) {
		this.idCard = idCard;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 手机号码
	 */
	@Column(name = "MOBILE", nullable = true, length = 11)
	public java.lang.String getMobile() {
		return this.mobile;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 手机号码
	 */
	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 驾照号码
	 */
	@Column(name = "DRIVING_LICENSE", nullable = true, length = 30)
	public java.lang.String getDrivingLicense() {
		return this.drivingLicense;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 驾照号码
	 */
	public void setDrivingLicense(java.lang.String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 驾照照片存放地址
	 */
	@Column(name = "DRIVING_LICENSE_PIC_PATH", nullable = true, length = 200)
	public java.lang.String getDrivingLicensePicPath() {
		return this.drivingLicensePicPath;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 驾照照片存放地址
	 */
	public void setDrivingLicensePicPath(java.lang.String drivingLicensePicPath) {
		this.drivingLicensePicPath = drivingLicensePicPath;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 * 
	 * @return: java.lang.Integer 创建人
	 */
	@Column(name = "CREATE_BY", nullable = true, precision = 19, scale = 0)
	public String getCreateBy() {
		return this.createBy;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 * 
	 * @param: java.lang.Integer 创建人
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 方法: 取得java.util.Date
	 * 
	 * @return: java.util.Date 创建时间
	 */
	@Column(name = "CREATE_TIME", nullable = true)
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * 方法: 设置java.util.Date
	 * 
	 * @param: java.util.Date 创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 * 
	 * @return: java.lang.Integer 修改人
	 */
	@Column(name = "UPDATE_BY", nullable = true, precision = 19, scale = 0)
	public String getUpdateBy() {
		return this.updateBy;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 * 
	 * @param: java.lang.Integer 修改人
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 方法: 取得java.util.Date
	 * 
	 * @return: java.util.Date 修改时间
	 */
	@Column(name = "UPDATE_TIME", nullable = true)
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * 方法: 设置java.util.Date
	 * 
	 * @param: java.util.Date 修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name ="sf_id",nullable=true,length=32)
	public java.lang.String getSfId() {
		return sfId;
	}

	public void setSfId(java.lang.String sfId) {
		this.sfId = sfId;
	}

	@Transient
	public java.lang.String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(java.lang.String createUserName) {
		this.createUserName = createUserName;
	}

	@Transient
	public java.lang.String getRegionName() {
		return regionName;
	}

	public void setRegionName(java.lang.String region) {
		this.regionName = region;
	}

	@Transient
	public java.lang.String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(java.lang.String agency) {
		this.agencyName = agency;
	}
	
	@Column(name ="remark",nullable=true,length=200)
	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	@Column(name = "quarry", nullable = true, precision = 10, scale = 0)
	public java.lang.Integer getQuarry() {
		return quarry;
	}

	public void setQuarry(java.lang.Integer quarry) {
		this.quarry = quarry;
	}

	public java.lang.String getType() {
		return type;
	}

	public void setType(java.lang.String type) {
		this.type = type;
	}
	
	
}
