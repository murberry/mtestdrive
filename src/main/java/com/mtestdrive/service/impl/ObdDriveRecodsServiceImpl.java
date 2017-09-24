package com.mtestdrive.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.MaseratiConstants;
import com.mtestdrive.service.ObdDriveRecodsServiceI;

@Service("obdDriveRecodsService")
@Transactional
public class ObdDriveRecodsServiceImpl extends CommonServiceImpl implements ObdDriveRecodsServiceI {

	@Override
	public Map<String, String> illegal(String groupBy, String month, String... param) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuilder sqlB = new StringBuilder("SELECT ").append(groupBy).append(",SUM(obdDrive.mileage) FROM t_obd_drive_recods obdDrive");
		Map<String, String> whereParam = new HashMap<String, String>();
		if (param != null) {
			if (0 < param.length && StringUtil.isNotEmpty(param[0])) {
				param[0] = new StringBuilder(" AND agency.id='").append(param[0]).append("'").toString();//经销商
				whereParam.put("agency", param[0]);
			}
			if (1 < param.length && StringUtil.isNotEmpty(param[1])) {
				param[1] = new StringBuilder(" AND agency.dearler_group='").append(param[1]).append("'").toString();//集团
				whereParam.put("agency", param[1]);
			}
		}
		//join开始
		String alias = groupBy.substring(0, groupBy.indexOf("."));
		if (!whereParam.keySet().contains(alias)) {
			sqlB.append(MaseratiConstants.getObdDriveJoinOn().get(alias));
		}
		for (String key : whereParam.keySet()) {
			sqlB.append(MaseratiConstants.getObdDriveJoinOn().get(key));
		}
		//join结束
		//where开始
		sqlB.append(" WHERE date_format(obdDrive.drive_start_time,'%Y-%c')='").append(month).append("' AND NOT obdDrive.status=1");
		for (String value : whereParam.values()) {
			sqlB.append(value);
		}
		//where结束
		sqlB.append(" GROUP BY ").append(groupBy);
		//带条件分子
		List<Object[]> list = findListbySql(sqlB.toString());
		for (Object[] objects : list) {
			if (null == objects[0]) {
				continue;
			}
			map.put(objects[0].toString(), objects[1].toString());
		}
		//不带条件分母
		list = findListbySql(sqlB.toString().replace(" AND NOT obdDrive.status=1", ""));
		for (int i = 0; i < list.size(); i++) {
			if (null == list.get(i) || null == list.get(i)[0] || "0".equals(list.get(i)[1])) {
				continue;
			}
			if (null == map.get(list.get(i)[0].toString())) {
				map.put(list.get(i)[0].toString(), "0");
			} else {
				BigDecimal value = new BigDecimal(map.get(list.get(i)[0].toString())).divide(new BigDecimal(list.get(i)[1].toString()),2);
				map.put(list.get(i)[0].toString(), value.toString().replace(".", ""));
			}
		}
		return map;
	}

	@Override
	public Map<String, String> testDriveMileage(String groupBy, String month, String... param) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuilder sqlB = new StringBuilder("SELECT ").append(groupBy).append(",AVG(obdDrive.mileage) FROM t_obd_drive_recods obdDrive");
		Map<String, String> whereParam = new HashMap<String, String>();
		if (param != null) {
			if (0 < param.length && StringUtil.isNotEmpty(param[0])) {
				param[0] = new StringBuilder(" AND agency.id='").append(param[0]).append("'").toString();//经销商
				whereParam.put("agency", param[0]);
			}
			if (1 < param.length && StringUtil.isNotEmpty(param[1])) {
				param[1] = new StringBuilder(" AND agency.dearler_group='").append(param[1]).append("'").toString();//集团
				whereParam.put("agency", param[1]);
			}
		}
		//join开始
		String alias = groupBy.substring(0, groupBy.indexOf("."));
		if (!whereParam.keySet().contains(alias)) {
			sqlB.append(MaseratiConstants.getObdDriveJoinOn().get(alias));
		}
		for (String key : whereParam.keySet()) {
			sqlB.append(MaseratiConstants.getObdDriveJoinOn().get(key));
		}
		//join结束
		//where开始
		sqlB.append(" WHERE date_format(obdDrive.drive_start_time,'%Y-%c')='").append(month).append("' AND obdDrive.status=1");
		for (String value : whereParam.values()) {
			sqlB.append(value);
		}
		//where结束
		sqlB.append(" GROUP BY ").append(groupBy);
		//装配
		List<Object[]> list = findListbySql(sqlB.toString());
		if (!ListUtils.isNullOrEmpty(list)) {
			for (Object[] objects : list) {
				if (null == objects[0]) {
					continue;
				}
				map.put(objects[0].toString(), objects[1].toString());
			}
		}
		return map;
	}

	@Override
	public Map<String, String> testDriveTimeLong(String groupBy, String month, String... param) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuilder sqlB = new StringBuilder("SELECT ").append(groupBy).append(",AVG(TIMESTAMPDIFF(MINUTE,obdDrive.drive_start_time,obdDrive.drive_end_time)) FROM t_obd_drive_recods obdDrive");
		Map<String, String> whereParam = new HashMap<String, String>();
		if (param != null) {
			if (0 < param.length && StringUtil.isNotEmpty(param[0])) {
				param[0] = new StringBuilder(" AND agency.id='").append(param[0]).append("'").toString();//经销商
				whereParam.put("agency", param[0]);
			}
			if (1 < param.length && StringUtil.isNotEmpty(param[1])) {
				param[1] = new StringBuilder(" AND agency.dearler_group='").append(param[1]).append("'").toString();//集团
				whereParam.put("agency", param[1]);
			}
		}
		//join开始
		String alias = groupBy.substring(0, groupBy.indexOf("."));
		if (!whereParam.keySet().contains(alias)) {
			sqlB.append(MaseratiConstants.getObdDriveJoinOn().get(alias));
		}
		for (String key : whereParam.keySet()) {
			sqlB.append(MaseratiConstants.getObdDriveJoinOn().get(key));
		}
		//join结束
		//where开始
		sqlB.append(" WHERE date_format(obdDrive.drive_start_time,'%Y-%c')='").append(month).append("' AND obdDrive.status=1");
		for (String value : whereParam.values()) {
			sqlB.append(value);
		}
		//where结束
		sqlB.append(" GROUP BY ").append(groupBy);
		//装配
		List<Object[]> list = findListbySql(sqlB.toString());
		if (!ListUtils.isNullOrEmpty(list)) {
			for (Object[] objects : list) {
				if (null == objects[0]) {
					continue;
				}
				map.put(objects[0].toString(), objects[1].toString());
			}
		}
		return map;
	}
	
}