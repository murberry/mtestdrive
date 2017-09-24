package com.mtestdrive.service;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.mtestdrive.entity.AgencyInfoEntity;

public interface AgencyInfoServiceI extends CommonService{
	AgencyInfoEntity getAgencyByCode(String code);
	List<AgencyInfoEntity> getAgencysByDearlerGroup(String regionName);
}
