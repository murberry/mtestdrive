package com.mtestdrive.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
@MappedSuperclass
public abstract class BaseEntity  {

	/** 本表id */
	private java.lang.String id;
	
	/** 状态，0无效1有效 */
	private Integer status;
	
	/**
	 * 方法: 取得java.lang.Integer
	 * 
	 * @return: java.lang.Integer 本表id
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name = "ID", nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 * 
	 * @param: java.lang.Integer 本表id
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
