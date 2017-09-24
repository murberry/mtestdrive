package com.mtestdrive.service;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.mtestdrive.entity.RouteInfoEntity;

public interface RouteInfoServiceI extends CommonService{

	List<RouteInfoEntity> getByAgencyId(String agencyId);

}
