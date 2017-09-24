package com.mtestdrive.service;

import java.util.Date;
import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.mtestdrive.entity.ObdGatherInfoEntity;

public interface ObdGatherInfoServiceI extends CommonService{
	public ObdGatherInfoEntity getLastData(String termid);
	
	public List<ObdGatherInfoEntity> getDatasByTimeQuantum(String termid, Date startTime, Date endTime);
	
	public List<ObdGatherInfoEntity> getObdByTermidAndGnsstime(String termid,String gnssTime);

	public List<ObdGatherInfoEntity> getObdIdByToday(String string);
}
