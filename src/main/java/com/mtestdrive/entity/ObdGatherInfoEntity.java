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
 * @Description: OBD数据
 * @author zhangdaihao
 * @date 2017-04-01 14:55:41
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_obd_gather_info", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class ObdGatherInfoEntity implements java.io.Serializable {
	/**本表id*/
	private java.lang.String id;
	/**obd编码*/
	private java.lang.String termid;
	/**采集时间*/
	private java.lang.String gnsstime;
	/**纬度*/
	private java.lang.Float lat;
	/**是否启动*/
	private Boolean accOn ;
	/**经度*/
	private java.lang.Float lon;
	/**方向。正北为0，顺时针方向角度。值域：0~359。*/
	private java.lang.Integer head;
	/**设备速度。单位：公里/小时*/
	private java.lang.Float spd;
	/**OBD速度*/
	private java.lang.Float obdSpd;
	/**海拔。单位：米。*/
	private java.lang.Float alt;
	/**累计行驶里程。单位：米。*/
	private java.lang.Float mileage;
	/**记录创建时间*/
	private java.util.Date createTime;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  本表id
	 */
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=32)
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
	 *@return: java.lang.String  obd编码
	 */
	@Column(name ="TERMID",nullable=true,length=30)
	public java.lang.String getTermid(){
		return this.termid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  obd编码
	 */
	public void setTermid(java.lang.String termid){
		this.termid = termid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  采集时间
	 */
	@Column(name ="GNSSTIME",nullable=true,length=20)
	public java.lang.String getGnsstime(){
		return this.gnsstime;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  采集时间
	 */
	public void setGnsstime(java.lang.String gnsstime){
		this.gnsstime = gnsstime;
	}
	/**
	 *方法: 取得java.lang.Float
	 *@return: java.lang.Float  纬度
	 */
	@Column(name ="LAT",nullable=true,precision=12)
	public java.lang.Float getLat(){
		return this.lat;
	}

	/**
	 *方法: 设置java.lang.Float
	 *@param: java.lang.Float  纬度
	 */
	public void setLat(java.lang.Float lat){
		this.lat = lat;
	}
	/**
	 *方法: 取得java.lang.Float
	 *@return: java.lang.Float  经度
	 */
	@Column(name ="LON",nullable=true,precision=12)
	public java.lang.Float getLon(){
		return this.lon;
	}

	/**
	 *方法: 设置java.lang.Float
	 *@param: java.lang.Float  经度
	 */
	public void setLon(java.lang.Float lon){
		this.lon = lon;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  方向。正北为0，顺时针方向角度。值域：0~359。
	 */
	@Column(name ="HEAD",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHead(){
		return this.head;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  方向。正北为0，顺时针方向角度。值域：0~359。
	 */
	public void setHead(java.lang.Integer head){
		this.head = head;
	}
	/**
	 *方法: 取得java.lang.Float
	 *@return: java.lang.Float  设备速度。单位：公里/小时
	 */
	@Column(name ="SPD",nullable=true,precision=12)
	public java.lang.Float getSpd(){
		return this.spd;
	}

	/**
	 *方法: 设置java.lang.Float
	 *@param: java.lang.Float  设备速度。单位：公里/小时
	 */
	public void setSpd(java.lang.Float spd){
		this.spd = spd;
	}
	/**
	 *方法: 取得java.lang.Float
	 *@return: java.lang.Float  OBD速度
	 */
	@Column(name ="OBDSPD",nullable=true,precision=12)
	public java.lang.Float getObdSpd(){
		return this.obdSpd;
	}

	/**
	 *方法: 设置java.lang.Float
	 *@param: java.lang.Float  OBD速度
	 */
	public void setObdSpd(java.lang.Float obdSpd){
		this.obdSpd = obdSpd;
	}
	/**
	 *方法: 取得java.lang.Float
	 *@return: java.lang.Float  海拔。单位：米。
	 */
	@Column(name ="ALT",nullable=true,precision=12)
	public java.lang.Float getAlt(){
		return this.alt;
	}

	/**
	 *方法: 设置java.lang.Float
	 *@param: java.lang.Float  海拔。单位：米。
	 */
	public void setAlt(java.lang.Float alt){
		this.alt = alt;
	}
	/**
	 *方法: 取得java.lang.Float
	 *@return: java.lang.Float  累计行驶里程。单位：米。
	 */
	@Column(name ="MILEAGE",nullable=true,precision=12)
	public java.lang.Float getMileage(){
		return this.mileage;
	}

	/**
	 *方法: 设置java.lang.Float
	 *@param: java.lang.Float  累计行驶里程。单位：米。
	 */
	public void setMileage(java.lang.Float mileage){
		this.mileage = mileage;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记录创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=true)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  记录创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}

	@Column(name ="ACCON",nullable=true)
	public Boolean getAccOn() {
		return accOn;
	}

	public void setAccOn(Boolean accOn) {
		this.accOn = accOn;
	}
}
