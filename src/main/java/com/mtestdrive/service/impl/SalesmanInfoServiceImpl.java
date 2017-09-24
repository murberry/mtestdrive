package com.mtestdrive.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.SalesmanInfoServiceI;

@Service("salesmanInfoService")
@Transactional
public class SalesmanInfoServiceImpl extends CommonServiceImpl implements SalesmanInfoServiceI {

	/**
	 * Title: getSalesmanByAgencyId  
	 * Description:根据经销商ID，返回店内所有销售顾问信息
	 * @param agencyId
	 * @return
	 */
	@Override
	public List<SalesmanInfoEntity> getSalesmanByAgencyId(String agencyId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SalesmanInfoEntity.class);
		dc.add(Restrictions.eq("agencyId", agencyId));
		dc.add(Restrictions.eq("status", ConstantStatus.VALID));
		return findByDetached(dc);
	}
	
}