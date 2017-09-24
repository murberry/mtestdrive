package com.mtestdrive.service;

import java.util.Date;
import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.mtestdrive.entity.ReportRecordsEntity;

public interface ReportRecordsServiceI extends CommonService{
	List<ReportRecordsEntity> getCarByTime(String carId, Date startTime, Date endTime);
}
