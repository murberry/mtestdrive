package com.mtestdrive.service;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.mtestdrive.entity.SalesmanInfoEntity;

public interface SalesmanInfoServiceI extends CommonService{
	List<SalesmanInfoEntity> getSalesmanByAgencyId(String agencyId);
}
