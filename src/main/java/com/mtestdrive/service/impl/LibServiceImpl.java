package com.mtestdrive.service.impl;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.service.LibServiceI;

@Service("libService")
@Transactional
public class LibServiceImpl extends CommonServiceImpl implements LibServiceI {
	
}