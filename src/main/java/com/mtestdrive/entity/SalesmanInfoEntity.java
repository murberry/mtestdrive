package com.mtestdrive.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: 销售顾问信息
 * @author zhangdaihao
 * @date 2017-03-10 17:25:39
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_salesman_info", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class SalesmanInfoEntity extends BaseEntity implements java.io.Serializable {
	
	/**销售区域id*/
	private java.lang.String regionId;
	/**4S销售商id*/
	private java.lang.String agencyId;
	/**销售代表名称*/
	private java.lang.String name;
	/**工号*/
	private java.lang.String employeeNo;
	/**座机号码*/
	private java.lang.String telephone;
	/**手机号码*/
	private java.lang.String mobile;
	/**移动端登录密码*/
	private String password;
	/**头像存放地址*/
	private java.lang.String headPortraitPicPath;
	
	/**创建人*/
	private java.lang.String createBy;
	/**创建时间*/
	private java.util.Date createTime;
	/**修改人*/
	private java.lang.String updateBy;
	/**修改时间*/
	private java.util.Date updateTime;
	/**入职时间*/
	private java.util.Date entryTime;
	/**英文名*/
	private java.lang.String englishName; 
	/**sfID */
	private java.lang.String sfId;
	
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  销售区域id
	 */
	@Column(name ="REGION_ID",nullable=true,precision=19,scale=0)
	public String getRegionId(){
		return this.regionId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  销售区域id
	 */
	public void setRegionId(String regionId){
		this.regionId = regionId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  4S销售商id
	 */
	@Column(name ="AGENCY_ID",nullable=true,precision=19,scale=0)
	public String getAgencyId(){
		return this.agencyId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  4S销售商id
	 */
	public void setAgencyId(String agencyId){
		this.agencyId = agencyId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  销售代表名称
	 */
	@Column(name ="NAME",nullable=true,length=30)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  销售代表名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  工号
	 */
	@Column(name ="EMPLOYEE_NO",nullable=true,length=30)
	public java.lang.String getEmployeeNo(){
		return this.employeeNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工号
	 */
	public void setEmployeeNo(java.lang.String employeeNo){
		this.employeeNo = employeeNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  座机号码
	 */
	@Column(name ="TELEPHONE",nullable=true,length=20)
	public java.lang.String getTelephone(){
		return this.telephone;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  座机号码
	 */
	public void setTelephone(java.lang.String telephone){
		this.telephone = telephone;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  手机号码
	 */
	@Column(name ="MOBILE",nullable=true,length=11)
	public java.lang.String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  手机号码
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  头像存放地址
	 */
	@Column(name ="HEAD_PORTRAIT_PIC_PATH",nullable=true,length=30)
	public java.lang.String getHeadPortraitPicPath(){
		return this.headPortraitPicPath;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  头像存放地址
	 */
	public void setHeadPortraitPicPath(java.lang.String headPortraitPicPath){
		this.headPortraitPicPath = headPortraitPicPath;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建人
	 */
	@Column(name ="CREATE_BY",nullable=true,precision=19,scale=0)
	public String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建人
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=true)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  修改人
	 */
	@Column(name ="UPDATE_BY",nullable=true,precision=19,scale=0)
	public String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  修改人
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  修改时间
	 */
	@Column(name ="UPDATE_TIME",nullable=true)
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}

	@Column(name ="password",nullable=true,length=100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name ="sf_id",nullable=true,length=32)
	public java.lang.String getSfId() {
		return sfId;
	}

	public void setSfId(java.lang.String sfId) {
		this.sfId = sfId;
	}
	@Column(name ="entry_time",nullable=true)
	public java.util.Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(java.util.Date entryTime) {
		this.entryTime = entryTime;
	}
	@Column(name ="english_name",nullable=true,length=20)
	public java.lang.String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(java.lang.String englishName) {
		this.englishName = englishName;
	}
	
}
