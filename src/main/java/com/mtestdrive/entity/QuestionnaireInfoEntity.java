package com.mtestdrive.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: adsf
 * @author zhangdaihao
 * @date 2017-04-20 14:26:47
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_questionnaire_info", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class QuestionnaireInfoEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**driveId*/
	private java.lang.String driveId;
	/**sendTime*/
	private java.util.Date sendTime;
	/**commitTime*/
	private java.lang.String commitTime;

	private java.util.Date syncTime;

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
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
	 *@param: java.lang.String  id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  driveId
	 */
	@Column(name ="DRIVE_ID",nullable=true,length=32)
	public java.lang.String getDriveId(){
		return this.driveId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  driveId
	 */
	public void setDriveId(java.lang.String driveId){
		this.driveId = driveId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  sendTime
	 */
	@Column(name ="SEND_TIME",nullable=true)
	public java.util.Date getSendTime(){
		return this.sendTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  sendTime
	 */
	public void setSendTime(java.util.Date sendTime){
		this.sendTime = sendTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  commitTime
	 */
	@Column(name ="COMMIT_TIME",nullable=true)
	public String getCommitTime(){
		return this.commitTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  commitTime
	 */
	public void setCommitTime(String commitTime){
		this.commitTime = commitTime;
	}

	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  syncTime
	 */
	@Column(name ="SYNC_TIME",nullable=true)
	public java.util.Date getSyncTime(){
		return this.syncTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  syncTime
	 */
	public void setSyncTime(java.util.Date syncTime){
		this.syncTime = syncTime;
	}
}
