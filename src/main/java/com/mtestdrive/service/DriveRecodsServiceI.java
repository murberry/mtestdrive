package com.mtestdrive.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.vo.DriveRecodsVo;

public interface DriveRecodsServiceI extends CommonService{
	public List<Map<String, String>> getAwitSendRecodsId();

	public DriveRecodsVo attachPic(String id, String picPath);

	public void complete(String id, int i);

	public Map<String, Integer> effective(String groupBy, String month, String...param);
	
	public List<DriveRecodsEntity> getByObdIdAndTime(String carId , Date startDate , Date endDate);

	public List<DriveRecodsEntity> getByObdIdAndTimeOnHalf(String carId, Date startDate, Date endDate);

	public List<DriveRecodsEntity> getByObdIdAndStartTime(String carId, Date startDate);
	
	public List<DriveRecodsEntity> getByObdIdAndEndTime(String carId, Date endDate);
}
