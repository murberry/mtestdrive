package org.jeecgframework.web.demo.service.impl.test;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.demo.service.test.JpPersonServiceI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jpPersonService")
@Transactional
public class JpPersonServiceImpl extends CommonServiceImpl implements JpPersonServiceI {
	
}