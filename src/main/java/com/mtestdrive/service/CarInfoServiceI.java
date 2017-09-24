package com.mtestdrive.service;

import java.util.List;

import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.system.pojo.base.TSType;

import com.mtestdrive.dto.CarInfoDto;
import com.mtestdrive.entity.CarInfoEntity;

public interface CarInfoServiceI extends CommonService {
	public List<CarInfoEntity> getCarByType(String agencyId, String type);

	public List<Object[]> getCarTypes();
	public List<TSType> getAllCarTypes();
	public void subTimes(String carId);
	public void datagrid(CarInfoDto carInfo, DataGrid dataGrid);

	public List<CarInfoEntity> getByCarObdId(String termId);
}
