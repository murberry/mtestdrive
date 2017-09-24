package org.jeecgframework.web.demo.service.test;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.demo.entity.test.TFinanceEntity;
import org.jeecgframework.web.demo.entity.test.TFinanceFilesEntity;

public interface TFinanceServiceI extends CommonService{

	void deleteFile(TFinanceFilesEntity file);

	void deleteFinance(TFinanceEntity finance);

}
