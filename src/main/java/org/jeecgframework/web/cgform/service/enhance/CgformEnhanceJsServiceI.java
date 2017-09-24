package org.jeecgframework.web.cgform.service.enhance;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.cgform.entity.enhance.CgformEnhanceJsEntity;

public interface CgformEnhanceJsServiceI extends CommonService{

	/**
	 * 根据cgJsType和formId查找数据
	 * @param cgJsType
	 * @param formId
	 * @return
	 */
	public CgformEnhanceJsEntity getCgformEnhanceJsByTypeFormId(String cgJsType,String formId);
}
