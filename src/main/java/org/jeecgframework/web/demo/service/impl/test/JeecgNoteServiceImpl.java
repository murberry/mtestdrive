package org.jeecgframework.web.demo.service.impl.test;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.demo.service.test.JeecgNoteServiceI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jeecgNoteService")
@Transactional
public class JeecgNoteServiceImpl extends CommonServiceImpl implements JeecgNoteServiceI {
	
}