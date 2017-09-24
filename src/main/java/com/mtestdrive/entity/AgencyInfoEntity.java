package com.mtestdrive.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @Title: Entity
 * @Description: 经销商信息
 * @author zhangdaihao
 * @date 2017-03-10 17:21:53
 * @version V1.0
 * 
 */
@Entity
@Table(name = "t_agency_info", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class AgencyInfoEntity extends BaseEntity implements java.io.Serializable {
	/** 销售区域id */
	private String regionId;
	/** 集团 */
	private String dearlerGroup;
	/** 4S经销商编码 */
	private String code;
	/** 4S经销商名称 */
	private String name;
	/** 省份id */
	private String provinceId;
	/** 市id */
	private String cityId;
	/** 4S地址 */
	private String address;
	/** 4S座机号码 */
	private String telephone;
	/** 4S店联系人 */
	private String contact;
	/** 4S店联系人手机号 */
	private String contactMobile;
	/** 创建人 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 修改人 */
	private String updateBy;
	/** 修改时间 */
	private Date updateTime;
	/** sfID */
	private String sfId;

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 销售区域id
	 */
	@Column(name = "REGION_ID", nullable = true, precision = 19, scale = 0)
	public String getRegionId() {
		return this.regionId;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 销售区域id
	 */
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 集团
	 */
	@Column(name = "DEARLER_GROUP", nullable = true, precision = 19, scale = 0)
	public String getDearlerGroup() {
		return dearlerGroup;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 集团
	 */
	public void setDearlerGroup(String dearlerGroup) {
		this.dearlerGroup = dearlerGroup;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 4S经销商编码
	 */
	@Column(name = "CODE", nullable = true, length = 50)
	public String getCode() {
		return this.code;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 4S经销商编码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 4S经销商名称
	 */
	@Column(name = "NAME", nullable = true, length = 100)
	public String getName() {
		return this.name;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 4S经销商名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 省份id
	 */
	@Column(name = "PROVINCE_ID", nullable = true, precision = 19, scale = 0)
	public String getProvinceId() {
		return this.provinceId;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 省份id
	 */
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 市id
	 */
	@Column(name = "CITY_ID", nullable = true, precision = 19, scale = 0)
	public String getCityId() {
		return this.cityId;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 市id
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 4S地址
	 */
	@Column(name = "ADDRESS", nullable = true, length = 200)
	public String getAddress() {
		return this.address;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 4S地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 4S座机号码
	 */
	@Column(name = "TELEPHONE", nullable = true, length = 100)
	public String getTelephone() {
		return this.telephone;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 4S座机号码
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 4S店联系人
	 */
	@Column(name = "CONTACT", nullable = true, length = 30)
	public String getContact() {
		return this.contact;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 4S店联系人
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 4S店联系人手机号
	 */
	@Column(name = "CONTACT_MOBILE", nullable = true, length = 30)
	public String getContactMobile() {
		return this.contactMobile;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 4S店联系人手机号
	 */
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 创建人
	 */
	@Column(name = "CREATE_BY", nullable = true, precision = 19, scale = 0)
	public String getCreateBy() {
		return this.createBy;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 创建人
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 创建时间
	 */
	@Column(name = "CREATE_TIME", nullable = true)
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * 方法: 设置Date
	 * 
	 * @param: Date 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 修改人
	 */
	@Column(name = "UPDATE_BY", nullable = true, precision = 19, scale = 0)
	public String getUpdateBy() {
		return this.updateBy;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 修改人
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 修改时间
	 */
	@Column(name = "UPDATE_TIME", nullable = true)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * 方法: 设置Date
	 * 
	 * @param: Date 修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "sf_id", nullable = true, length = 32)
	public String getSfId() {
		return sfId;
	}

	public void setSfId(String sfId) {
		this.sfId = sfId;
	}

}
