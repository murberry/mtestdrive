package org.jeecgframework.web.demo.service.impl.test;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.demo.service.test.JeecgDemoCkfinderServiceI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jeecgDemoCkfinderService")
@Transactional
public class JeecgDemoCkfinderServiceImpl extends CommonServiceImpl implements
		JeecgDemoCkfinderServiceI {

}