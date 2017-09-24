package com.mtestdrive.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.entity.CustomerInfoEntity;
import com.mtestdrive.service.CustomerInfoServiceI;

@Service("customerInfoService")
@Transactional
public class CustomerInfoServiceImpl extends CommonServiceImpl implements CustomerInfoServiceI {

	@Override
	public List<TSType> getAllQuarries() {
		TSTypegroup group = commonDao.findUniqueByProperty(TSTypegroup.class, "typegroupcode", "quarry");
		return group.getTSTypes();
	}
	
	@Override
	public CustomerInfoEntity getCustomerByMobileAndSalesManId(String mobile, String salesManId) {
		DetachedCriteria dc = DetachedCriteria.forClass(CustomerInfoEntity.class);
		dc.add(Restrictions.eq("mobile", mobile));
		dc.add(Restrictions.eq("createBy", salesManId));
		List<CustomerInfoEntity> list = findByDetached(dc);
		if(!ListUtils.isNullOrEmpty(list))
			return list.get(0);
		return null;
	}
	
}