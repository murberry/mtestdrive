package com.mtestdrive.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.entity.ReportRecordsEntity;
import com.mtestdrive.service.ReportRecordsServiceI;

@Service("reportRecordsService")
@Transactional
public class ReportRecordsServiceImpl extends CommonServiceImpl implements ReportRecordsServiceI {

	@Override
	public List<ReportRecordsEntity> getCarByTime(String carId, Date startTime, Date endTime) {
		DetachedCriteria reportDc = DetachedCriteria.forClass(ReportRecordsEntity.class);
		reportDc.add(Restrictions.eq("carId", carId));
		reportDc.add(Restrictions.or(Restrictions.between("startTime", startTime, endTime), Restrictions.between("endTime", startTime, endTime)));
		return findByDetached(reportDc);
	}
}