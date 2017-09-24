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
import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: 问卷调查
 * @author zhangdaihao
 * @date 2017-04-10 13:30:29
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_s_group", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class GroupEntity implements java.io.Serializable {
	/**本表id*/
	private java.lang.String id;
	/**试驾表ID*/
	private java.lang.String driveId;
	/**问题1：1不满意，2满意*/
	private java.lang.Integer group1;
	/**问题2: 1是，2不确定，3否*/
	private java.lang.Integer group2;
	/**问题3: 1非常满意，2一般满意，3不满意*/
	private java.lang.Integer group3;
	/**问题4：1非常专业，2一般专业，3不专业*/
	private java.lang.Integer group4;
	
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
	 *@return: java.lang.String  试驾表ID
	 */
	@Column(name ="DRIVE_ID",nullable=true,length=32)
	public java.lang.String getDriveId(){
		return this.driveId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  试驾表ID
	 */
	public void setDriveId(java.lang.String driveId){
		this.driveId = driveId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  问题1：1不满意，2满意
	 */
	@Column(name ="GROUP_1",nullable=true,precision=10,scale=0)
	public java.lang.Integer getGroup1(){
		return this.group1;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  问题1：1不满意，2满意
	 */
	public void setGroup1(java.lang.Integer group1){
		this.group1 = group1;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  问题2: 1是，2不确定，3否
	 */
	@Column(name ="GROUP_2",nullable=true,precision=10,scale=0)
	public java.lang.Integer getGroup2(){
		return this.group2;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  问题2: 1是，2不确定，3否
	 */
	public void setGroup2(java.lang.Integer group2){
		this.group2 = group2;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  问题3: 1非常满意，2一般满意，3不满意
	 */
	@Column(name ="GROUP_3",nullable=true,precision=10,scale=0)
	public java.lang.Integer getGroup3(){
		return this.group3;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  问题3: 1非常满意，2一般满意，3不满意
	 */
	public void setGroup3(java.lang.Integer group3){
		this.group3 = group3;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  问题4：1非常专业，2一般专业，3不专业
	 */
	@Column(name ="GROUP_4",nullable=true,precision=10,scale=0)
	public java.lang.Integer getGroup4(){
		return this.group4;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  问题4：1非常专业，2一般专业，3不专业
	 */
	public void setGroup4(java.lang.Integer group4){
		this.group4 = group4;
	}
}
