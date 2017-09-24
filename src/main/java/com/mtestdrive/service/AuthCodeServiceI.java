package com.mtestdrive.service;

import org.jeecgframework.core.common.service.CommonService;

import com.mtestdrive.entity.AuthCodeEntity;

public interface AuthCodeServiceI extends CommonService{
	
	public AuthCodeEntity getLastCode(String tel);
	
	public boolean checkAuthCode(String tel, String code);
}
