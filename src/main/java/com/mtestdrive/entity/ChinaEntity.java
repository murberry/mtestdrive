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
 * @Description: 省市
 * @author zhangdaihao
 * @date 2017-03-13 14:52:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "china", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class ChinaEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**name*/
	private java.lang.String name;
	/**pid*/
	private java.lang.Integer pid;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  name
	 */
	@Column(name ="NAME",nullable=true,length=40)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  name
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  pid
	 */
	@Column(name ="PID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getPid(){
		return this.pid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  pid
	 */
	public void setPid(java.lang.Integer pid){
		this.pid = pid;
	}
}
