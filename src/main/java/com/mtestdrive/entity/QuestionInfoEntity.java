package com.mtestdrive.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: adsf
 * @author zhangdaihao
 * @date 2017-04-20 14:27:08
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_questionnaire_question", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class QuestionInfoEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**问题*/
	private java.lang.String question;
	/**0无效，1有效*/
	private java.lang.String result;
	/**questionnaireid*/
	private java.lang.String questionnaireid;
	
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
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  问题
	 */
	@Column(name ="QUESTION",nullable=true,length=65535)
	public java.lang.String getQuestion(){
		return this.question;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  问题
	 */
	public void setQuestion(String question){
		this.question = question;
	}
	/**
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  0无效，1有效
	 */
	@Column(name ="RESULT",nullable=true,length=65535)
	public java.lang.String getResult(){
		return this.result;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  0无效，1有效
	 */
	public void setResult(String result){
		this.result = result;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  questionnaireid
	 */
	@Column(name ="QUESTIONNAIREID",nullable=true,length=32)
	public java.lang.String getQuestionnaireid(){
		return this.questionnaireid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  questionnaireid
	 */
	public void setQuestionnaireid(java.lang.String questionnaireid){
		this.questionnaireid = questionnaireid;
	}
}
