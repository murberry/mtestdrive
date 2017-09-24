package com.mtestdrive.dto;

import java.math.BigDecimal;

/**
 * @ClassName:  ObdGatherInfoDto   
 * @Description:OBD设备推送数据DTO
 * @author: mengtaocui
 * @date:   2017年4月1日 下午2:58:27   
 *     
 * @Copyright:
 */
public class ObdGatherDataDto {
	
	/**采集时间*/
	private java.lang.String gnssTime;
	
	/**纬度*/
	private BigDecimal lat;
	/**纬度*/
	private Boolean accOn;
	
	/**经度*/
	private BigDecimal lon;
	
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
	
	
	
	
	
	public java.lang.String getGnssTime() {
		return gnssTime;
	}

	public void setGnssTime(java.lang.String gnssTime) {
		this.gnssTime = gnssTime;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  方向。正北为0，顺时针方向角度。值域：0~359。
	 */
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

	public Boolean getAccOn() {
		return accOn;
	}

	public void setAccOn(Boolean accOn) {
		this.accOn = accOn;
	}
}
