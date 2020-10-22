package com.mtestdrive.service;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.system.pojo.base.TSType;

import com.mtestdrive.entity.CustomerInfoEntity;

public interface CustomerInfoServiceI extends CommonService {

	public List<TSType> getAllQuarries();

	public List<TSType> getAllQuarryDetail();

	public CustomerInfoEntity getCustomerByMobileAndSalesManId(String mobile, String salesManId);
}
