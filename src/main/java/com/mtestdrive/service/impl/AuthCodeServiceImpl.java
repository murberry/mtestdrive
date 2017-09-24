package com.mtestdrive.service.impl;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.MaseratiConstants.AuthCodeStatus;
import com.mtestdrive.entity.AuthCodeEntity;
import com.mtestdrive.service.AuthCodeServiceI;

@Service("authCodeService")
@Transactional
public class AuthCodeServiceImpl extends CommonServiceImpl implements AuthCodeServiceI {

	/**
	 * @Title: getLastCode   
	 * @Description: 返回指定手机号码，最后一次发送的验证码
	 * @param:       
	 * @return: AuthCodeEntity      
	 * @throws   
	 * @author: mengtaocui
	 */
	@Override
	public AuthCodeEntity getLastCode(String tel) {
		String hql = " from AuthCodeEntity where phone=:phone order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("phone", tel);
		query.setMaxResults(1);
		List<AuthCodeEntity> codeList = query.list();
		if(codeList != null && !codeList.isEmpty())
			return codeList.get(0);
		return null;
	}
	
	/**
	 * @Title: checkAuthCode   
	 * @Description: 判断验证码是否正确且是否在有效期内
	 * @param:       
	 * @return: boolean      
	 * @throws   
	 * @author: mengtaocui
	 */
	@Override
	public boolean checkAuthCode(String tel, String code) {
		DetachedCriteria dc = DetachedCriteria.forClass(AuthCodeEntity.class);
		dc.add(Restrictions.eq("status", AuthCodeStatus.UNUSED));
		dc.add(Restrictions.eq("code", code));
		dc.add(Restrictions.eq("phone", tel));
		dc.addOrder(Order.desc("createTime"));
		List<AuthCodeEntity> codeList = findByDetached(dc);
		if(codeList == null || codeList.isEmpty())
			return false;
		else{
			AuthCodeEntity authCode = codeList.get(0);
			//判断验证码发送时间距离当前时间间隔时长，超过30分钟即为失效
			if(authCode != null && authCode.getCreateTime() != null && 
					DateUtils.getTimeDifferenceAboutNow(authCode.getCreateTime().getTime()) > 30){
				//验证码过期
				return false;
			}else{
				return true;
			}
		}
	}
}