package com.mtestdrive.service;

import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

public interface QuestionnaireInfoServiceI extends CommonService{

	Map<String, Integer> satisfied(String groupBy, String month, String...params);

}
