package com.mtestdrive.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.beans.factory.annotation.Autowired;

import com.mtestdrive.service.AgencyInfoServiceI;

import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: OBD试驾表
 * @author zhangdaihao
 * @date 2017-06-15 17:30:58
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_obd_drive_recods", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class ObdDriveRecodsEntity implements java.io.Serializable {
	
	/**本表id*/
	private java.lang.String id;
	/**试驾流程id*/
	@Excel(name = "试驾流程")
	private java.lang.String driveId;
	/**4S经销商id*/
	@Excel(name = "经销商")
	private java.lang.String agencyId;
	/**试驾车id*/
	@Excel(name = "车辆")
	private java.lang.String carId;
	/**销售代表id*/
	@Excel(name = "销售")
	private java.lang.String salesmanId;
	/**客户id*/
	@Excel(name = "客户")
	private java.lang.String customerId;
	/**预约开始时间*/
	private java.util.Date orderStartTime;
	/**预约结束时间*/
	private java.util.Date orderEndTime;
	/**试驾开始时间*/
	@Excel(name = "试驾开始时间")
	private java.util.Date driveStartTime;
	/**时间结束时间*/
	@Excel(name = "试驾结束时间")
	private java.util.Date driveEndTime;
	/**开始前照片存放地址*/
	private java.lang.String startPicPath;
	/**结束后照片存放地址*/
	private java.lang.String endPicPath;
	/**行驶里程数*/
	@Excel(name = "距离")
	private BigDecimal mileage;
	/**试驾反馈*/
	private java.lang.String feedback;
	/**创建人*/
	private java.lang.String createBy;
	/**创建时间*/
	private java.util.Date createTime;
	/**修改人*/
	private java.lang.String updateBy;
	/**修改时间*/
	private java.util.Date updateTime;
	/**0-无效试驾，1-有效试驾，2-非预约试驾*/
	@Excel(name = "状态")
	private java.lang.Integer status;
	/**试驾合同照片*/
	private java.lang.String contractPicPath;
	
	/**无效原因*/
	private java.lang.String description;
	
	/**试驾公里数达成率*/
	private java.lang.String achievement;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  本表id
	 */
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="id",nullable=false,length=32)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  本表id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  试驾流程id
	 */
	@Column(name ="drive_id",nullable=true,length=32)
	public String getDriveId() {
		return driveId;
	}

	public void setDriveId(String driveId) {
		this.driveId = driveId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  4S经销商id
	 */
	@Column(name ="agency_id",nullable=true,length=32)
	public java.lang.String getAgencyId(){
		return this.agencyId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  4S经销商id
	 */
	public void setAgencyId(java.lang.String agencyId){
		this.agencyId = agencyId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  试驾车id
	 */
	@Column(name ="car_id",nullable=true,length=32)
	public java.lang.String getCarId(){
		return this.carId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  试驾车id
	 */
	public void setCarId(java.lang.String carId){
		this.carId = carId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  销售代表id
	 */
	@Column(name ="salesman_id",nullable=true,length=32)
	public java.lang.String getSalesmanId(){
		return this.salesmanId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  销售代表id
	 */
	public void setSalesmanId(java.lang.String salesmanId){
		this.salesmanId = salesmanId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客户id
	 */
	@Column(name ="customer_id",nullable=true,length=32)
	public java.lang.String getCustomerId(){
		return this.customerId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客户id
	 */
	public void setCustomerId(java.lang.String customerId){
		this.customerId = customerId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  预约开始时间
	 */
	@Column(name ="order_start_time",nullable=true)
	public java.util.Date getOrderStartTime(){
		return this.orderStartTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  预约开始时间
	 */
	public void setOrderStartTime(java.util.Date orderStartTime){
		this.orderStartTime = orderStartTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  预约结束时间
	 */
	@Column(name ="order_end_time",nullable=true)
	public java.util.Date getOrderEndTime(){
		return this.orderEndTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  预约结束时间
	 */
	public void setOrderEndTime(java.util.Date orderEndTime){
		this.orderEndTime = orderEndTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  试驾开始时间
	 */
	@Column(name ="drive_start_time",nullable=true)
	public java.util.Date getDriveStartTime(){
		return this.driveStartTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  试驾开始时间
	 */
	public void setDriveStartTime(java.util.Date driveStartTime){
		this.driveStartTime = driveStartTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  时间结束时间
	 */
	@Column(name ="drive_end_time",nullable=true)
	public java.util.Date getDriveEndTime(){
		return this.driveEndTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  时间结束时间
	 */
	public void setDriveEndTime(java.util.Date driveEndTime){
		this.driveEndTime = driveEndTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开始前照片存放地址
	 */
	@Column(name ="start_pic_path",nullable=true,length=200)
	public java.lang.String getStartPicPath(){
		return this.startPicPath;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开始前照片存放地址
	 */
	public void setStartPicPath(java.lang.String startPicPath){
		this.startPicPath = startPicPath;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结束后照片存放地址
	 */
	@Column(name ="end_pic_path",nullable=true,length=200)
	public java.lang.String getEndPicPath(){
		return this.endPicPath;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结束后照片存放地址
	 */
	public void setEndPicPath(java.lang.String endPicPath){
		this.endPicPath = endPicPath;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  行驶里程数
	 */
	@Column(name ="mileage",nullable=true,precision=6,scale=2)
	public BigDecimal getMileage(){
		return this.mileage;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  行驶里程数
	 */
	public void setMileage(BigDecimal mileage){
		this.mileage = mileage;
	}
	/**
	 * 方法: 取得feedback
	 * 
	 * @return: java.lang.Object 试驾反馈
	 */
	@Column(name = "feedback", nullable = true, length = 500)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人
	 */
	@Column(name ="create_by",nullable=true,length=32)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="create_time",nullable=true)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  修改人
	 */
	@Column(name ="update_by",nullable=true,length=32)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  修改人
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  修改时间
	 */
	@Column(name ="update_time",nullable=true)
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
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  0-无效试驾，1-有效试驾，2-非预约试驾
	 */
	@Column(name ="status",nullable=true,precision=10,scale=0)
	public java.lang.Integer getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  0-无效试驾，1-有效试驾，2-非预约试驾
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  试驾合同照片
	 */
	@Column(name ="contract_pic_path",nullable=true)
	public String getContractPicPath() {
		return contractPicPath;
	}

	public void setContractPicPath(String contractPicPath) {
		this.contractPicPath = contractPicPath;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  无效原因
	 */
	@Column(name ="description",nullable=true,length=200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String 
	 */
	@Column(name ="achievement",nullable=true)
	public java.lang.String getAchievement(){
		return this.achievement;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String 
	 */
	public void setAchievement(java.lang.String achievement){
		this.achievement = achievement;
	}

	
}
