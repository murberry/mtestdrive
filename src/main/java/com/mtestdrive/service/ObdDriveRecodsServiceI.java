package com.mtestdrive.service;

import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

public interface ObdDriveRecodsServiceI extends CommonService {

	Map<String, String> illegal(String groupBy, String month, String...param);

	Map<String, String> testDriveMileage(String groupBy, String month, String...param);

	Map<String, String> testDriveTimeLong(String groupBy, String month, String...param);

}
