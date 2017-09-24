package com.mtestdrive.service.impl;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.service.TokenServiceI;

@Service("tokenService")
@Transactional
public class TokenServiceImpl extends CommonServiceImpl implements TokenServiceI {
	
}