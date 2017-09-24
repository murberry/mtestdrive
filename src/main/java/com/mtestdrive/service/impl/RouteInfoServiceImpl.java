package com.mtestdrive.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.entity.RouteInfoEntity;
import com.mtestdrive.service.RouteInfoServiceI;

@Service("routeInfoService")
@Transactional
public class RouteInfoServiceImpl extends CommonServiceImpl implements RouteInfoServiceI {
	
	@Override
	public List<RouteInfoEntity> getByAgencyId(String agencyId) {
		DetachedCriteria reportDc = DetachedCriteria.forClass(RouteInfoEntity.class);
		reportDc.add(Restrictions.eq("agencyId", agencyId));
		reportDc.add(Restrictions.eq("routeStatus", 1));
		return findByDetached(reportDc);
	}
	
}