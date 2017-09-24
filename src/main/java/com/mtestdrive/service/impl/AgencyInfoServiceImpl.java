package com.mtestdrive.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.service.AgencyInfoServiceI;

@Service("agencyInfoService")
@Transactional
public class AgencyInfoServiceImpl extends CommonServiceImpl implements AgencyInfoServiceI {

	@Override
	public AgencyInfoEntity getAgencyByCode(String code) {
		DetachedCriteria dc = DetachedCriteria.forClass(AgencyInfoEntity.class);
		dc.add(Restrictions.eq("code", code));
		List<AgencyInfoEntity> aiList = findByDetached(dc);
		if(ListUtils.isNullOrEmpty(aiList))
			return null;
		else
			return aiList.get(0);
	}

	/**
	 * Title: getAgencysByDearlerGroup  
	 * Description:该集团所有经销商
	 * @param dearlerGroup
	 * @return
	 */
	@Override
	public List<AgencyInfoEntity> getAgencysByDearlerGroup(String dearlerGroup) {
		if(StringUtil.isNotEmpty(dearlerGroup)){
			DetachedCriteria dc = DetachedCriteria.forClass(AgencyInfoEntity.class);
			dc.add(Restrictions.eq("dearlerGroup", dearlerGroup));
			dc.add(Restrictions.eq("status", ConstantStatus.VALID));
			return findByDetached(dc);
		}
		return findByProperty(AgencyInfoEntity.class, "status", ConstantStatus.VALID);
	}
}